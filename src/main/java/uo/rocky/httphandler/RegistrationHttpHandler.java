package uo.rocky.httphandler;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;
import uo.rocky.entity.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class RegistrationHttpHandler extends HttpHandlerBase {
    public static final String GET_CONTEXT = "/registration";
    private static final String GET_ALLOW = "GET, HEAD, POST";
    private static final String GET_CONTENT_TYPE = "application/json; charset=utf-8";

    @Override
    public void handleGETRequest(HttpExchange httpExchange) throws IOException {
        try {
            String results = User.selectUserJSONString(parseParameters(httpExchange));
            System.out.println(results);
            httpExchange.getResponseHeaders().add(ResponseHeader.CONTENT_TYPE.call(), GET_CONTENT_TYPE);
            httpExchange.sendResponseHeaders(StatusCode.OK.code(), results.getBytes(UTF_8).length);
            outputResponseBody(httpExchange.getResponseBody(), results, UTF_8);
        } catch (IOException ioException) {
            throw ioException;
        } catch (Exception exception) {
            respondInternalServerError(httpExchange, exception);
            exception.printStackTrace();
            throw new RuntimeException(exception);
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
            User user = User.valueOf(new JSONObject(inputRequestBody(httpExchange.getRequestBody(), UTF_8)));
            System.out.println(user);
            System.out.println(user.insertSQL() ? "INSERT succeed!" : "INSERT failed!");
            httpExchange.sendResponseHeaders(StatusCode.OK.code(), -1);
        } catch (IOException ioException) {
            throw ioException;
        } catch (JSONException valueOfException) {
            respondBadRequest(httpExchange, valueOfException.getMessage());
        } catch (Exception exception) {
            respondInternalServerError(httpExchange, exception);
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
    }
}