package programming3.rocky;

import com.sun.net.httpserver.HttpServer;
import programming3.rocky.httphandler.CommentHttpHandler;
import programming3.rocky.httphandler.CoordinatesHttpHandler;
import programming3.rocky.httphandler.RegistrationHttpHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello world!");

        HttpServer server = HttpServer.create(new InetSocketAddress(1989), 0);

        server.createContext("/comment", new CommentHttpHandler());
        server.createContext("/coordinates", new CoordinatesHttpHandler());
        server.createContext("/registration", new RegistrationHttpHandler());
//        server.createContext("/warning", new WarningHttpHandler());

        server.setExecutor(null);  // creates a default executor

        server.start();

        System.out.println("Server started!");
    }
}