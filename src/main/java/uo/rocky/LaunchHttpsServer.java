package uo.rocky;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import uo.rocky.entity.EntitySQLiteConnection;
import uo.rocky.httphandler.CommentHttpHandler;
import uo.rocky.httphandler.CoordinatesHttpHandler;
import uo.rocky.httphandler.RegistrationHttpHandler;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.sql.DriverManager;

public class LaunchHttpsServer {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello world!");


        String SQLiteURL = "jdbc:sqlite:deer.sqlite.db";
        EntitySQLiteConnection.setConnection(DriverManager.getConnection(SQLiteURL), false);
        System.out.println(SQLiteURL + " connected!");  // TODO: close()


        final String USERNAME = "rocky", PASSWORD = "891213";
        final String JKS_PATH = "keystore00.jks";
        final char[] JKS_PASSWORD = "891213".toCharArray();
        final int PORT = 8001;
        final String HOST = "0.0.0.0";

        final HttpsServer httpsServer = HttpsServer.create(new InetSocketAddress(InetAddress.getByName(HOST), PORT), 0);

        final HttpContext commentContext = httpsServer.createContext(CommentHttpHandler.GET_CONTEXT, new CommentHttpHandler());
        commentContext.setAuthenticator(new UserAuthenticator("'" + CommentHttpHandler.GET_CONTEXT + "' requires authentication."));
        final HttpContext coordinatesContext = httpsServer.createContext(CoordinatesHttpHandler.GET_CONTEXT, new CoordinatesHttpHandler());
        coordinatesContext.setAuthenticator(new UserAuthenticator("'" + CoordinatesHttpHandler.GET_CONTEXT + "' requires authentication."));
        final HttpContext registrationContext = httpsServer.createContext(RegistrationHttpHandler.GET_CONTEXT, new RegistrationHttpHandler());
//        registrationContext.setAuthenticator(new UserAuthenticator("'" + RegistrationHttpHandler.GET_CONTEXT + "' requires authentication."));
//        final HttpContext warningContext = httpsServer.createContext(WarningHttpHandler.GET_CONTEXT, new WarningHttpHandler());
//        warningContext.setAuthenticator(new UserAuthenticator("'" + WarningHttpHandler.GET_CONTEXT + "' requires authentication."));

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
                httpsParameters.setSSLParameters(getSSLContext().getDefaultSSLParameters());
            }
        });

        httpsServer.setExecutor(null);
        httpsServer.start();
        System.out.println("Server started!");
    }
}