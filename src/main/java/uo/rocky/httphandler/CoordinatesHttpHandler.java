package uo.rocky.httphandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;
import uo.rocky.entity.Coordinate;

import java.io.IOException;
import java.util.*;

public final class CoordinatesHttpHandler extends HttpHandlerPrinciple implements HttpHandler {
    public static final String CONTEXT = "/coordinates";
    private static final String ALLOW = "HEAD, POST";
    private static final String CONTENT_TYPE = "application/json; charset=utf-8";

    @Override
    public void handleGETRequest(HttpExchange httpExchange) throws IOException {
        String uri = httpExchange.getRequestURI().toString();
        String[] params = uri.substring(uri.indexOf('?') + 1).split("&");
        System.out.println(Arrays.toString(params));
        Map<String, String> paramsMap = new HashMap<>();

        for (String param : params) {
            String[] tempStrings = param.split("=");
            if (2 == tempStrings.length) {
                paramsMap.put(tempStrings[0].toUpperCase(), tempStrings[1]);
            } else {
                // TODO
            }
        }
        try {
            List<Coordinate> coordinates = Coordinate.selectSQLite(paramsMap);
            System.out.println(coordinates);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        super.handleGETRequest(httpExchange);
    }

    @Override
    public void handleHEADRequest(HttpExchange httpExchange) throws IOException {
        httpExchange.getResponseHeaders().add("Allow", ALLOW);
        httpExchange.sendResponseHeaders(200, -1);
    }

    @Override
    public void handlePOSTRequest(HttpExchange httpExchange) throws IOException {
        httpExchange.getRequestHeaders();

        Coordinate coordinate = Coordinate.valueOf(new JSONObject(inputRequestBody(httpExchange.getRequestBody())));
        System.out.println(coordinate);
        try {
            System.out.println(coordinate.insertSQLite() ? "Insert succeed!" : "Insert failed!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        httpExchange.getResponseHeaders().add("Allow", ALLOW);
        httpExchange.getResponseHeaders().add("Content-Type", CONTENT_TYPE);

        httpExchange.sendResponseHeaders(200, 0);

        outputResponseBody(httpExchange.getResponseBody(), "");
    }
}