import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {

    public TasksHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes());

        String[] pathParts = exchange.getRequestURI().getPath().split("/");

        switch (endpoint) {
            case GET_TASKS:
                sendText(exchange, gson.toJson(manager.getTasks()));
                break;
            case GET_TASK:
                try {
                    Task task = manager.getTask(Integer.parseInt(pathParts[2]));
                    sendText(exchange, gson.toJson(task));
                } catch (NullPointerException e) {
                    sendNotFound(exchange);
                }
                break;
            case POST_TASK:
                try {
                    Task task = gson.fromJson(body, Task.class);
                    manager.addTask(task);
                    sendSuccessCreateOrUpdate(exchange);
                } catch (TaskIntersectedException e) {
                    sendHasInteractions(exchange);
                }
                break;
            case POST_TASK_BY_ID:
                try {
                    Task task = gson.fromJson(body, Task.class);
                    manager.updateTask(task);
                    sendSuccessCreateOrUpdate(exchange);
                } catch (TaskIntersectedException e) {
                    sendHasInteractions(exchange);
                }
                break;
            case DELETE_TASK:
                try {
                    manager.deleteTask(Integer.parseInt(pathParts[2]));
                    sendText(exchange, "Удален таск - " + pathParts[2]);
                } catch (RuntimeException e) {
                    sendNotFound(exchange);
                }
                break;
            case NOT_FOUND:
                sendNotFound(exchange);
                break;
        }
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");
        switch (requestMethod) {
            case ("GET"):
                if (pathParts.length == 3) {
                    return Endpoint.GET_TASK;
                }
                if (pathParts.length == 2) {
                    return Endpoint.GET_TASKS;
                }
            case ("POST"):
                if (pathParts.length == 3) {
                    return Endpoint.POST_TASK_BY_ID;
                }
                if (pathParts.length == 2) {
                    return Endpoint.POST_TASK;
                }
            case ("DELETE"):
                if (pathParts.length == 3) {
                    return Endpoint.DELETE_TASK;
                }
            default:
                return Endpoint.NOT_FOUND;
        }
    }
}
