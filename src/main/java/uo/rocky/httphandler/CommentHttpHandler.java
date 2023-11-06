package uo.rocky.httphandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import uo.rocky.entity.Comment;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class CommentHttpHandler extends HttpHandlerBase implements HttpHandler {
    public static final String GET_CONTEXT = "/comment";
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
                System.out.println("煞笔吧，一个参数的位置写两个等号。。。");
            }
        }
        try {
            StringJoiner results = new StringJoiner(",", "[", "]");
            for (Comment comment : Comment.selectSQLite(paramsMap)) {
                results.add(comment.toJSONString());
            }
            System.out.println(results);

            httpExchange.getResponseHeaders().add(ResponseHeader.CONTENT_TYPE.call(), GET_CONTENT_TYPE);
            httpExchange.sendResponseHeaders(200, results.toString().getBytes(UTF_8).length);

            outputResponseBody(httpExchange.getResponseBody(), results.toString());
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
}