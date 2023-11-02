package uo.rocky.httphandler;

import com.sun.net.httpserver.HttpHandler;

import java.sql.Connection;

public final class RegistrationHttpHandler extends HttpHandlerPrinciple implements HttpHandler {
    public RegistrationHttpHandler(Connection connection) {
        super(connection);
    }
}