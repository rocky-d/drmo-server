package uo.moodle.https;

import com.sun.net.httpserver.*;
import java.net.InetSocketAddress;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;

import java.io.*;


class Server {


    public static void main(String[] args) throws Exception {

        authMe authchecker = new authMe("coordinates");

        HttpsServer server = HttpsServer.create(new InetSocketAddress(8000),0);
        final HttpContext finalContext = server.createContext("/coordinates", new MyHandler());

        finalContext.setAuthenticator(authchecker);

        char[] passphrase = "salasana".toCharArray();
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("keystore.jks"), passphrase);
     
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, passphrase);
     
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);
     
        SSLContext ssl = SSLContext.getInstance("TLS");
        ssl.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        server.setHttpsConfigurator (new HttpsConfigurator(ssl) {
            public void configure (HttpsParameters params) {
    
            // get the remote address if needed
            InetSocketAddress remote = params.getClientAddress();
    
            SSLContext c = getSSLContext();
    
            // get the default parameters
            SSLParameters sslparams = c.getDefaultSSLParameters();

            params.setSSLParameters(sslparams);
            // statement above could throw IAE if any params invalid.
            // eg. if app has a UI and parameters supplied by a user.
    
            }
        });

        server.setExecutor(null); // creates a default executor
        server.start(); 
    }
}
