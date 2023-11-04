package uo.rocky.httphandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public final class CommentHttpHandler extends HttpHandlerPrinciple implements HttpHandler {
    public static final String CONTEXT = "/comment";
    private static final String ALLOW = "HEAD";
    private static final String CONTENT_TYPE = "application/json; charset=utf-8";

    @Override
    public void handleHEADRequest(HttpExchange httpExchange) throws IOException {
        httpExchange.getResponseHeaders().add("Allow", ALLOW);
        httpExchange.sendResponseHeaders(200, -1);
    }
}