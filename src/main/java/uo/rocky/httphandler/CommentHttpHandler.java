package uo.rocky.httphandler;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;
import uo.rocky.entity.Comment;

import java.io.IOException;
import java.sql.SQLException;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class CommentHttpHandler extends HttpHandlerBase {
    public static final String GET_CONTEXT = "/comment";
    private static final String GET_ALLOW = "GET, HEAD, POST";
    private static final String GET_CONTENT_TYPE = "application/json; charset=utf-8";

    @Override
    public void handleGETRequest(HttpExchange httpExchange) throws IOException {
        try {
            String results = Comment.selectCommentJSONString(parseQueryParameters(httpExchange));
            System.out.println(results);
            httpExchange.getResponseHeaders().add(ResponseHeader.CONTENT_TYPE.call(), GET_CONTENT_TYPE);
            httpExchange.sendResponseHeaders(StatusCode.OK.code(), results.getBytes(UTF_8).length);
            outputResponseBody(httpExchange.getResponseBody(), results, UTF_8);
        } catch (NullPointerException nullPointerException) {
            respondBadRequest(httpExchange, nullPointerException.getMessage());
        } catch (SQLException sqlException) {
            respondInternalServerError(httpExchange, sqlException);
            sqlException.printStackTrace();
            throw new RuntimeException(sqlException);
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
            Comment comment = Comment.valueOf(new JSONObject(inputRequestBody(httpExchange.getRequestBody(), UTF_8)));
            System.out.println(comment);
            System.out.println(comment.insertSQL() ? "INSERT succeed!" : "INSERT failed!");
            httpExchange.sendResponseHeaders(StatusCode.OK.code(), -1);
        } catch (JSONException valueOfException) {
            respondBadRequest(httpExchange, valueOfException.getMessage());
        } catch (SQLException sqlException) {
            respondInternalServerError(httpExchange, sqlException);
            sqlException.printStackTrace();
            throw new RuntimeException(sqlException);
        }
    }
}