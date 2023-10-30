package programming3.rocky.httphandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class WarningHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        switch (httpExchange.getRequestMethod().toUpperCase()) {
            case "GET":

                break;
            case "POST":

                break;
            default:

                break;
        }
    }
}