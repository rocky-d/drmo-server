package uo.rocky.httphandler;

import com.sun.net.httpserver.HttpHandler;

import java.sql.Connection;

public final class CommentHttpHandler extends HttpHandlerPrinciple implements HttpHandler {
    public CommentHttpHandler(Connection connectionSQLite) {
        super(connectionSQLite);
    }
}