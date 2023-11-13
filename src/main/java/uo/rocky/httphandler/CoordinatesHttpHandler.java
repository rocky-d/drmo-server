package uo.rocky.httphandler;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;
import uo.rocky.entity.Coordinate;

import java.io.IOException;
import java.sql.SQLException;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class CoordinatesHttpHandler extends HttpHandlerBase {
    public static final String GET_CONTEXT = "/coordinates";
    private static final String GET_ALLOW = "GET, HEAD, POST";
    private static final String GET_CONTENT_TYPE = "application/json; charset=utf-8";

    @Override
    public void handleGETRequest(HttpExchange httpExchange) throws IOException {
        try {
            String results = Coordinate.selectCoordinateWithCommentsJSONString(parseQueryParameters(httpExchange));
            System.out.println(results);
            httpExchange.getResponseHeaders().add(ResponseHeader.CONTENT_TYPE.call(), GET_CONTENT_TYPE);
            httpExchange.sendResponseHeaders(StatusCode.OK.code(), results.getBytes(UTF_8).length);
            outputResponseBody(httpExchange.getResponseBody(), results, UTF_8);
        } catch (RuntimeException runtimeException) {
            respondBadRequest(httpExchange, runtimeException.getClass().getSimpleName() + ": " + runtimeException.getMessage());
        } catch (SQLException sqlException) {
            respondInternalServerError(httpExchange, sqlException);
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
            Coordinate coordinate = Coordinate.valueOf(new JSONObject(inputRequestBody(httpExchange.getRequestBody(), UTF_8)));
            System.out.println(coordinate);
            System.out.println(coordinate.insertSQL() ? "INSERT succeed!" : "INSERT failed!");
            httpExchange.sendResponseHeaders(StatusCode.OK.code(), -1);
        } catch (RuntimeException runtimeException) {
            respondBadRequest(httpExchange, runtimeException.getMessage());
        } catch (SQLException sqlException) {
            respondInternalServerError(httpExchange, sqlException);
        }
    }
}