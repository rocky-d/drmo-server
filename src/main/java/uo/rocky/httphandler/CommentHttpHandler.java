package uo.rocky.httphandler;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;
import uo.rocky.entity.Comment;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class CommentHttpHandler extends HttpHandlerBase {
    public static final String GET_CONTEXT = "/comment";
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

        String results;
        try {
            results = Comment.selectCommentOrderedString(paramsMap);
            System.out.println(results);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        httpExchange.getResponseHeaders().add(ResponseHeader.CONTENT_TYPE.call(), GET_CONTENT_TYPE);
        httpExchange.sendResponseHeaders(StatusCode.OK.code(), results.getBytes(UTF_8).length);
        outputResponseBody(httpExchange.getResponseBody(), results);
    }

    @Override
    public void handleHEADRequest(HttpExchange httpExchange) throws IOException {
        httpExchange.getResponseHeaders().add(ResponseHeader.ALLOW.call(), GET_ALLOW);
        httpExchange.sendResponseHeaders(StatusCode.OK.code(), -1);
    }

    @Override
    public void handlePOSTRequest(HttpExchange httpExchange) throws IOException {
        httpExchange.getRequestHeaders();

        Comment comment = Comment.valueOf(new JSONObject(inputRequestBody(httpExchange.getRequestBody())));
        System.out.println(comment);
        try {
            System.out.println(comment.insertSQL() ? "Insert succeed!" : "Insert failed!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        httpExchange.sendResponseHeaders(StatusCode.OK.code(), -1);
    }
}