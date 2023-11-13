package uo.rocky;

import com.sun.net.httpserver.*;
import uo.rocky.entity.EntityDBConnection;
import uo.rocky.httphandler.CommentHttpHandler;
import uo.rocky.httphandler.CoordinatesHttpHandler;
import uo.rocky.httphandler.RegistrationHttpHandler;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.sql.DriverManager;

public final class ServerLauncher {
    public static final String SQLITE_URL = "jdbc:sqlite:deer.sqlite.db";

    public static final int PORT = 8001;
    public static final String HOST = "0.0.0.0";

    public static void launchHttpServer() throws IOException {
        final HttpServer httpServer = HttpServer.create(new InetSocketAddress(InetAddress.getByName(HOST), PORT), 0);

        final HttpContext commentContext = httpServer.createContext(CommentHttpHandler.GET_CONTEXT, new CommentHttpHandler());
        final HttpContext coordinatesContext = httpServer.createContext(CoordinatesHttpHandler.GET_CONTEXT, new CoordinatesHttpHandler());
        final HttpContext registrationContext = httpServer.createContext(RegistrationHttpHandler.GET_CONTEXT, new RegistrationHttpHandler());
//        final HttpContext warningContext = httpServer.createContext(WarningHttpHandler.GET_CONTEXT, new WarningHttpHandler());

        httpServer.setExecutor(null);
        httpServer.start();
    }

    public static void launchHttpsServer() throws Exception {
        final String JKS_PATH = "keystore00.jks";
        final char[] JKS_PASSWORD = "891213".toCharArray();


        final HttpsServer httpsServer = HttpsServer.create(new InetSocketAddress(InetAddress.getByName(HOST), PORT), 0);

        final HttpContext commentContext = httpsServer.createContext(CommentHttpHandler.GET_CONTEXT, new CommentHttpHandler());
        commentContext.setAuthenticator(new UserAuthenticator("'" + CommentHttpHandler.GET_CONTEXT + "' requires authentication"));
        final HttpContext coordinatesContext = httpsServer.createContext(CoordinatesHttpHandler.GET_CONTEXT, new CoordinatesHttpHandler());
        coordinatesContext.setAuthenticator(new UserAuthenticator("'" + CoordinatesHttpHandler.GET_CONTEXT + "' requires authentication"));
        final HttpContext registrationContext = httpsServer.createContext(RegistrationHttpHandler.GET_CONTEXT, new RegistrationHttpHandler());
//        registrationContext.setAuthenticator(new UserAuthenticator("'" + RegistrationHttpHandler.GET_CONTEXT + "' requires authentication"));
//        final HttpContext warningContext = httpsServer.createContext(WarningHttpHandler.GET_CONTEXT, new WarningHttpHandler());
//        warningContext.setAuthenticator(new UserAuthenticator("'" + WarningHttpHandler.GET_CONTEXT + "' requires authentication"));

        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(Files.newInputStream(Paths.get(JKS_PATH)), JKS_PASSWORD);
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore, JKS_PASSWORD);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        trustManagerFactory.init(keyStore);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
        httpsServer.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
            @Override
            public void configure(HttpsParameters httpsParameters) {
                InetSocketAddress inetSocketAddress = httpsParameters.getClientAddress();
                System.out.println("Remote: " + inetSocketAddress);
                httpsParameters.setSSLParameters(getSSLContext().getDefaultSSLParameters());
            }
        });

        httpsServer.setExecutor(null);
        httpsServer.start();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Hello world!");

        EntityDBConnection.setConnection(DriverManager.getConnection(SQLITE_URL));
        System.out.println(SQLITE_URL + " connected");

        launchHttpServer();
//        launchHttpsServer();
        System.out.println("Server started on port " + PORT);
    }
}