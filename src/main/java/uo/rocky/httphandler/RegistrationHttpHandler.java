package uo.rocky.httphandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;
import uo.rocky.entity.User;

import java.io.IOException;

public final class RegistrationHttpHandler extends HttpHandlerBase implements HttpHandler {
    public static final String GET_CONTEXT = "/registration";
    private static final String GET_ALLOW = "HEAD, POST";
    private static final String GET_CONTENT_TYPE = "application/json; charset=utf-8";

    @Override
    public void handleHEADRequest(HttpExchange httpExchange) throws IOException {
        httpExchange.getResponseHeaders().add(ResponseHeader.ALLOW.call(), GET_ALLOW);
        httpExchange.sendResponseHeaders(200, -1);
    }

    @Override
    public void handlePOSTRequest(HttpExchange httpExchange) throws IOException {
        httpExchange.getRequestHeaders();

        User user = User.valueOf(new JSONObject(inputRequestBody(httpExchange.getRequestBody())));
        System.out.println(user);
        try {
            System.out.println(user.insertSQLite() ? "Insert succeed!" : "Insert failed!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        httpExchange.sendResponseHeaders(200, -1);
    }
}