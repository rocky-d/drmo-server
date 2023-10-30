package programming3.rocky.httphandler;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class RegistrationHttpHandler extends HttpHandlerPrinciple {
    @Override
    public void handleGETRequest(HttpExchange httpExchange) {

    }

    @Override
    public void handlePOSTRequest(HttpExchange httpExchange) {

    }

    @Override
    public void handleNonSupportedRequest(HttpExchange httpExchange) {
        super.handleNonSupportedRequest(httpExchange);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        super.handle(httpExchange);
    }
}