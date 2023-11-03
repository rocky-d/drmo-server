package uo.rocky;

import com.sun.net.httpserver.HttpServer;
import uo.rocky.entity.EntitySQLiteConnection;
import uo.rocky.httphandler.CommentHttpHandler;
import uo.rocky.httphandler.CoordinatesHttpHandler;
import uo.rocky.httphandler.RegistrationHttpHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Main {
    public static void main(String[] args) throws IOException, SQLException {
        System.out.println("Hello world!");

        EntitySQLiteConnection.setConnection(DriverManager.getConnection("jdbc:sqlite:deer.sqlite.db"));
        EntitySQLiteConnection.getConnection().setAutoCommit(false);

        System.out.println("jdbc:sqlite:coursework.sqlite.db opened");

        HttpServer server = HttpServer.create(new InetSocketAddress(8001), 0);

        server.createContext("/comment", new CommentHttpHandler());
        server.createContext("/coordinates", new CoordinatesHttpHandler());
        server.createContext("/registration", new RegistrationHttpHandler());
//        server.createContext("/warning", new WarningHttpHandler());

        server.setExecutor(null);  // creates a default executor

        server.start();

        System.out.println("Server started!");
    }
}