package programming3.rocky.httphandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class WarningHttpHandler implements HttpHandlerPrinciple {
    @Override
    public void handleGETRequest(HttpExchange httpExchange) {

    }

    @Override
    public void handlePOSTRequest(HttpExchange httpExchange) {

    }

    @Override
    public void handleNonSupportedRequest(HttpExchange httpExchange) {

    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        HttpHandlerPrinciple.super.handle(httpExchange);
    }
}