package uo.rocky.httphandler;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;
import uo.rocky.entity.Coordinate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class CoordinatesHttpHandler extends HttpHandlerBase {
    public static final String GET_CONTEXT = "/coordinates";
    private static final String GET_ALLOW = "GET, HEAD, POST";
    private static final String GET_CONTENT_TYPE = "application/json; charset=utf-8";

    @Override
    public void handleGETRequest(HttpExchange httpExchange) throws IOException {
        String uri = httpExchange.getRequestURI().toString();
        Map<String, String> paramsMap = new HashMap<>();
        for (String param : -1 == uri.indexOf('?') ? new String[]{} : uri.substring(uri.indexOf('?') + 1).split("&")) {
            String[] tempStrings = param.split("=");
            if (2 == tempStrings.length) {
                paramsMap.put(tempStrings[0].toUpperCase(), tempStrings[1]);
            } else {
                // TODO
                System.out.println("It's not two strings with one equal sign in between...");
            }
        }

        try {
            String results = Coordinate.selectCoordinateWithCommentsJSONString(paramsMap);
            System.out.println(results);
            httpExchange.getResponseHeaders().add(ResponseHeader.CONTENT_TYPE.call(), GET_CONTENT_TYPE);
            httpExchange.sendResponseHeaders(StatusCode.OK.code(), results.getBytes(UTF_8).length);
            outputResponseBody(httpExchange.getResponseBody(), results);
        } catch (IOException ioException) {
            throw ioException;
        } catch (Exception e) {
            respondInternalServerError(httpExchange);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handleHEADRequest(HttpExchange httpExchange) throws IOException {
        httpExchange.getResponseHeaders().add(ResponseHeader.ALLOW.call(), GET_ALLOW);
        httpExchange.sendResponseHeaders(StatusCode.OK.code(), -1);
    }

    @Override
    public void handlePOSTRequest(HttpExchange httpExchange) throws IOException {
        try {
            Coordinate coordinate = Coordinate.valueOf(new JSONObject(inputRequestBody(httpExchange.getRequestBody())));
            System.out.println(coordinate);
            System.out.println(coordinate.insertSQL() ? "INSERT succeed!" : "INSERT failed!");
            httpExchange.sendResponseHeaders(StatusCode.OK.code(), -1);
        } catch (JSONException | IllegalArgumentException valueOfException) {
            respondBadRequest(httpExchange, valueOfException.getMessage());
        } catch (IOException ioException) {
            throw ioException;
        } catch (Exception e) {
            respondInternalServerError(httpExchange);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}