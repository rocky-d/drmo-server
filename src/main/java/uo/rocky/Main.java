package uo.rocky;

import com.sun.net.httpserver.HttpServer;
import uo.rocky.httphandler.CommentHttpHandler;
import uo.rocky.httphandler.CoordinatesHttpHandler;
import uo.rocky.httphandler.RegistrationHttpHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public final class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello world!");

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