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

        Connection connectionSQLite = DriverManager.getConnection("jdbc:sqlite:deer.sqlite.db");
        connectionSQLite.setAutoCommit(false);

        HttpServer server = HttpServer.create(new InetSocketAddress(8001), 0);

        server.createContext("/comment", new CommentHttpHandler(connectionSQLite));
        server.createContext("/coordinates", new CoordinatesHttpHandler(connectionSQLite));
        server.createContext("/registration", new RegistrationHttpHandler(connectionSQLite));
//        server.createContext("/warning", new WarningHttpHandler());

        server.setExecutor(null);  // creates a default executor

        server.start();

        System.out.println("Server started!");
    }
}