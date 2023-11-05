package uo.rocky;

import com.sun.net.httpserver.HttpContext;
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


        final String SQLITE_URL = "jdbc:sqlite:deer.sqlite.db";
        EntitySQLiteConnection.setConnection(DriverManager.getConnection(SQLITE_URL));
        EntitySQLiteConnection.getConnection().setAutoCommit(false);
        EntitySQLiteConnection.setConnectionForAllEntities(EntitySQLiteConnection.getConnection());
        System.out.println(SQLITE_URL + " connected!");  // TODO: close()


        final int PORT = 8001;

        final HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);

        final HttpContext commentContext = httpServer.createContext(CommentHttpHandler.GET_CONTEXT, new CommentHttpHandler());
        final HttpContext coordinatesContext = httpServer.createContext(CoordinatesHttpHandler.GET_CONTEXT, new CoordinatesHttpHandler());
        final HttpContext registrationContext = httpServer.createContext(RegistrationHttpHandler.GET_CONTEXT, new RegistrationHttpHandler());
//        final HttpContext warningContext = httpServer.createContext(WarningHttpHandler.GET_CONTEXT, new WarningHttpHandler());

        httpServer.setExecutor(null);  // creates a default executor
        httpServer.start();
        System.out.println("Server started!");
    }
}