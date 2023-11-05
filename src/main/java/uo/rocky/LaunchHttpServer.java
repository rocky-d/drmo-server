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

public class LaunchHttpServer {
    public static void main(String[] args) throws SQLException, IOException {
        System.out.println("Hello world!");

        String SQLiteURL = "jdbc:sqlite:deer.sqlite.db";
        EntitySQLiteConnection.setConnection(DriverManager.getConnection(SQLiteURL));
        EntitySQLiteConnection.getConnection().setAutoCommit(false);
        EntitySQLiteConnection.setConnectionForAllEntities(EntitySQLiteConnection.getConnection());
        System.out.println(SQLiteURL + " connected!");  // TODO: close()

        int portNumber = 8001;
        HttpServer server = HttpServer.create(new InetSocketAddress(portNumber), 0);
        server.createContext(CommentHttpHandler.GET_CONTEXT, new CommentHttpHandler());
        server.createContext(CoordinatesHttpHandler.GET_CONTEXT, new CoordinatesHttpHandler());
        server.createContext(RegistrationHttpHandler.GET_CONTEXT, new RegistrationHttpHandler());
//        server.createContext(WarningHttpHandler.GET_CONTEXT, new WarningHttpHandler());
        server.setExecutor(null);  // creates a default executor
        server.start();
        System.out.println("Server started!");
    }
}