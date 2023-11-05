package uo.rocky;

import com.sun.net.httpserver.*;
import uo.rocky.entity.EntitySQLiteConnection;
import uo.rocky.httphandler.CoordinatesHttpHandler;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.sql.DriverManager;

public class LaunchHttpsServer {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello world!");

        String SQLiteURL = "jdbc:sqlite:deer.sqlite.db";
        EntitySQLiteConnection.setConnection(DriverManager.getConnection(SQLiteURL));
        EntitySQLiteConnection.getConnection().setAutoCommit(false);
        EntitySQLiteConnection.setConnectionForAllEntities(EntitySQLiteConnection.getConnection());
        System.out.println(SQLiteURL + " connected!");  // TODO: close()

        String username = "rocky", password = "891213";
        char[] passwordChars = password.toCharArray();
        int portNumber = 8001;

        HttpsServer httpsServer = HttpsServer.create(new InetSocketAddress(portNumber), 0);

        HttpContext httpContext = httpsServer.createContext(CoordinatesHttpHandler.GET_CONTEXT, new CoordinatesHttpHandler());
        httpContext.setAuthenticator(new BasicAuthenticator("This is a realm.") {
            @Override
            public boolean checkCredentials(String var1, String var2) {
                return username.equals(var1) && password.equals(var2);
            }
        });

        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(Files.newInputStream(Paths.get("keystore00.jks")), passwordChars);

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore, passwordChars);

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        trustManagerFactory.init(keyStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

        httpsServer.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
            @Override
            public void configure(HttpsParameters params) {
                InetSocketAddress remote = params.getClientAddress();
                params.setSSLParameters(getSSLContext().getDefaultSSLParameters());
            }
        });

        httpsServer.setExecutor(null);
        httpsServer.start();
        System.out.println("Server started!");
    }
}