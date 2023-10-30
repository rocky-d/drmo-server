package programming3.rocky.httphandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public abstract class HttpHandlerPrinciple implements HttpHandler {
    public abstract void handleGETRequest(HttpExchange httpExchange);

    public abstract void handlePOSTRequest(HttpExchange httpExchange);

    public void handleNonSupportedRequest(HttpExchange httpExchange) {
        // TODO
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
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