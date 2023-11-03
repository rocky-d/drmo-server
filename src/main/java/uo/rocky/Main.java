package uo.rocky;

import com.sun.net.httpserver.HttpServer;
import uo.rocky.httphandler.CommentHttpHandler;
import uo.rocky.httphandler.CoordinatesHttpHandler;
import uo.rocky.httphandler.RegistrationHttpHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Main {
    public static void main(String[] args) throws IOException, SQLException {
        System.out.println("Hello world!");

        Connection connection = DriverManager.getConnection("jdbc:sqlite:deer.sqlite.db");  // TODO: connection.close()
        connection.setAutoCommit(false);
        System.out.println("jdbc:sqlite:coursework.sqlite.db opened");

        HttpServer server = HttpServer.create(new InetSocketAddress(8001), 0);

        server.createContext("/comment", new CommentHttpHandler(connection));
        server.createContext("/coordinates", new CoordinatesHttpHandler(connection));
        server.createContext("/registration", new RegistrationHttpHandler(connection));
//        server.createContext("/warning", new WarningHttpHandler());

        server.setExecutor(null);  // creates a default executor

        server.start();

        System.out.println("Server started!");
    }
}