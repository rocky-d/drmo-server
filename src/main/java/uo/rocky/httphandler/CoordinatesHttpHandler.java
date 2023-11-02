package uo.rocky.httphandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;
import uo.rocky.entity.Coordinate;

import java.io.IOException;

public final class CoordinatesHttpHandler extends HttpHandlerPrinciple implements HttpHandler {
    @Override
    public void handlePOSTRequest(HttpExchange httpExchange) throws IOException {
        httpExchange.getRequestHeaders();

        Coordinate coordinate = new Coordinate(new JSONObject(inputRequestBody(httpExchange.getRequestBody())));
        System.out.println(coordinate);
        try {
            coordinate.insertSQLite();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        httpExchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");

        httpExchange.sendResponseHeaders(200, 0);

        outputResponseBody(httpExchange.getResponseBody(), "");
    }
}