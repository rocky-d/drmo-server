package programming3.rocky.httphandler;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class CommentHttpHandler extends HttpHandlerPrinciple {
    @Override
    public void handleGETRequest(HttpExchange httpExchange) {
        // TODO
    }

    @Override
    public void handlePOSTRequest(HttpExchange httpExchange) {
        // TODO
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