import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {

    public HistoryHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath());
        if (exchange.getRequestMethod().equals("GET")) {
            switch (endpoint) {
                case GET_HISTORY:
                    sendText(exchange, gson.toJson(manager.getHistory()));
                    break;
                case NOT_FOUND:
                    sendNotFound(exchange);
                    break;
            }
        }
        sendNotFound(exchange);
    }

    private Endpoint getEndpoint(String requestPath) {
        String[] pathParts = requestPath.split("/");
        if (pathParts.length == 2) {
            return Endpoint.GET_HISTORY;
        }
        return Endpoint.NOT_FOUND;
    }
}
