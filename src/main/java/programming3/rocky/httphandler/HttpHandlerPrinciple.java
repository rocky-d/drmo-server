package programming3.rocky.httphandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public interface HttpHandlerPrinciple extends HttpHandler {
    void handleGETRequest(HttpExchange httpExchange);

    void handlePOSTRequest(HttpExchange httpExchange);

    void handleNonSupportedRequest(HttpExchange httpExchange);

    @Override
    default void handle(HttpExchange httpExchange) throws IOException {
        switch (httpExchange.getRequestMethod().toUpperCase()) {
            case "GET":
                handleGETRequest(httpExchange);
                break;
            case "POST":
                handlePOSTRequest(httpExchange);
                break;
            default:
                handleNonSupportedRequest(httpExchange);
                break;
        }
    }
}