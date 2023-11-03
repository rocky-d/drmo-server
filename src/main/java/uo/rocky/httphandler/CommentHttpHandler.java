package uo.rocky.httphandler;

import com.sun.net.httpserver.HttpHandler;

public final class CommentHttpHandler extends HttpHandlerPrinciple implements HttpHandler {
    public static final String CONTEXT = "/comment";
    private static final String ALLOW = "HEAD";
    private static final String CONTENT_TYPE = "application/json; charset=utf-8";
}