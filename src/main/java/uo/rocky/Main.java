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

        String SQLiteURL = "jdbc:sqlite:deer.sqlite.db";
        EntitySQLiteConnection.setConnection(DriverManager.getConnection(SQLiteURL));
        EntitySQLiteConnection.getConnection().setAutoCommit(false);
        EntitySQLiteConnection.setConnectionForAllEntities(EntitySQLiteConnection.getConnection());
        System.out.println(SQLiteURL + " connected!");  // TODO: close()

        HttpServer server = HttpServer.create(new InetSocketAddress(8001), 0);
        server.createContext(CommentHttpHandler.CONTEXT, new CommentHttpHandler());
        server.createContext(CoordinatesHttpHandler.CONTEXT, new CoordinatesHttpHandler());
        server.createContext(RegistrationHttpHandler.CONTEXT, new RegistrationHttpHandler());
//        server.createContext(WarningHttpHandler.CONTEXT, new WarningHttpHandler());

        server.setExecutor(null);  // creates a default executor
        server.start();
        System.out.println("Server started!");
    }
}