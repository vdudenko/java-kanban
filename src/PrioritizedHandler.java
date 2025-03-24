import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {

    public PrioritizedHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        switch (endpoint) {
            case GET_PRIORITIZED:
                sendText(exchange, gson.toJson(manager.getPrioritizedTasks()));
                break;
            case NOT_FOUND:
                sendNotFound(exchange);
                break;
        }
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");
        if (pathParts.length == 2) {
            return Endpoint.GET_PRIORITIZED;
        }
        return Endpoint.NOT_FOUND;
    }
}
