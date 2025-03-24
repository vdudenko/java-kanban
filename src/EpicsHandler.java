import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {
    public EpicsHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes());

        String[] pathParts = exchange.getRequestURI().getPath().split("/");

        switch (endpoint) {
            case GET_EPICS:
                sendText(exchange, gson.toJson(manager.getEpics()));
                break;
            case GET_EPIC:
                try{
                    Epic epic = manager.getEpic(Integer.parseInt(pathParts[2]));
                    sendText(exchange, gson.toJson(epic));
                } catch (NullPointerException e) {
                    sendNotFound(exchange);
                }
                break;
            case GET_EPIC_SUB_TASKS:
                try {
                    Epic epic = manager.getEpic(Integer.parseInt(pathParts[2]));
                    sendText(exchange, gson.toJson(epic.subTaskIds));
                } catch (TaskIntersectedException e) {
                    sendNotFound(exchange);
                }
                break;
            case POST_EPIC:
                try {
                    Epic epic = gson.fromJson(body, Epic.class);
                    manager.addEpic(epic);
                    sendSuccessCreateOrUpdate(exchange);
                } catch (TaskIntersectedException e) {
                    sendHasInteractions(exchange);
                }
                break;
            case DELETE_EPIC:
                try {
                    manager.deleteEpic(Integer.parseInt(pathParts[2]));
                    sendText(exchange, "Удален эпик' - " + pathParts[2]);
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
                    return Endpoint.GET_EPICS;
                }
                if (pathParts.length == 3) {
                    return Endpoint.GET_EPIC;
                }
                if (pathParts.length == 4) {
                    return Endpoint.GET_EPIC_SUB_TASKS;
                }
            case ("POST"):
                if (pathParts.length == 2) {
                    return Endpoint.POST_EPIC;
                }
            case ("DELETE"):
                if (pathParts.length == 3) {
                    return Endpoint.DELETE_EPIC;
                }
            default:
                return Endpoint.NOT_FOUND;
        }
    }
}
