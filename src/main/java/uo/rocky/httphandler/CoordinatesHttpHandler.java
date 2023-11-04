package uo.rocky.httphandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;
import uo.rocky.entity.Coordinate;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class CoordinatesHttpHandler extends HttpHandlerPrinciple implements HttpHandler {
    public static final String GET_CONTEXT = "/coordinates";
    private static final String GET_ALLOW = "GET, HEAD, POST";
    private static final String GET_CONTENT_TYPE = "application/json; charset=utf-8";

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

            httpExchange.getResponseHeaders().add(ResponseHeader.ALLOW.call(), GET_ALLOW);
            httpExchange.getResponseHeaders().add(ResponseHeader.CONTENT_TYPE.call(), GET_CONTENT_TYPE);
            httpExchange.sendResponseHeaders(200, coordinates.get(0).toJSONString().getBytes(UTF_8).length);

            outputResponseBody(httpExchange.getResponseBody(), coordinates.get(0).toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handleHEADRequest(HttpExchange httpExchange) throws IOException {
        httpExchange.getResponseHeaders().add(ResponseHeader.ALLOW.call(), GET_ALLOW);
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

        httpExchange.getResponseHeaders().add(ResponseHeader.ALLOW.call(), GET_ALLOW);
        httpExchange.getResponseHeaders().add(ResponseHeader.CONTENT_TYPE.call(), GET_CONTENT_TYPE);
        httpExchange.sendResponseHeaders(200, "".getBytes(UTF_8).length);

        outputResponseBody(httpExchange.getResponseBody(), "");
    }
}