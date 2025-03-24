import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;

public class SubTaskHandler extends BaseHttpHandler implements HttpHandler {
    public SubTaskHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes());

        String[] pathParts = exchange.getRequestURI().getPath().split("/");

        switch (endpoint) {
            case GET_SUB_TASKS:
                sendText(exchange, gson.toJson(manager.getSubTasks()));
                break;
            case GET_SUB_TASK:
                try{
                    SubTask subTask = manager.getSubTask(Integer.parseInt(pathParts[2]));
                    sendText(exchange, gson.toJson(subTask));
                } catch (NullPointerException e) {
                    sendNotFound(exchange);
                }
                break;
            case POST_SUB_TASK:
                try {
                    SubTask subTask = gson.fromJson(body, SubTask.class);
                    manager.addSubTask(subTask);
                    sendSuccessCreateOrUpdate(exchange);
                } catch (TaskIntersectedException e) {
                    sendHasInteractions(exchange);
                }
                break;
            case POST_SUB_TASK_BY_ID:
                try {
                    SubTask subTask = gson.fromJson(body, SubTask.class);
                    manager.updateSubTask(subTask);
                    sendSuccessCreateOrUpdate(exchange);
                } catch (TaskIntersectedException e) {
                    sendHasInteractions(exchange);
                }
                break;
            case DELETE_SUB_TASK:
                try {
                    manager.deleteSubTask(Integer.parseInt(pathParts[2]));
                    sendText(exchange, "Удален сабтаск - " + pathParts[2]);
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
                if (pathParts.length == 2) {
                    return Endpoint.GET_SUB_TASKS;
                }
                if (pathParts.length == 3) {
                    return Endpoint.GET_SUB_TASK;
                }
            case ("POST"):
                if (pathParts.length == 3) {
                    return Endpoint.POST_SUB_TASK_BY_ID;
                }
                if (pathParts.length == 2) {
                    return Endpoint.POST_SUB_TASK;
                }
            case ("DELETE"):
                if (pathParts.length == 3) {
                    return Endpoint.DELETE_SUB_TASK;
                }
            default:
                return Endpoint.NOT_FOUND;
        }
    }
}
