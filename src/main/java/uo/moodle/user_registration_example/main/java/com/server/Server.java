package uo.moodle.user_registration_example.main.java.com.server;

import com.sun.net.httpserver.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;


public class Server {


    public static void main(String[] args) throws Exception {
        //create the http server to port 8001 with default logger
        HttpsServer server = HttpsServer.create(new InetSocketAddress(8001),0);
        SSLContext sslContext = coordinatesServerSSLContext(args[0],args[1]);
        server.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
            public void configure(HttpsParameters params) {
                InetSocketAddress remote = params.getClientAddress();

                SSLContext c = getSSLContext();
                SSLParameters sslparams = c.getDefaultSSLParameters();
                params.setSSLParameters(sslparams);
            }
        });

        UserAuthenticatorDB authentication = new UserAuthenticatorDB();

        server.createContext("/registration", new RegistrationHandler(authentication));

        // creates a default executor
        server.setExecutor(null); 
        server.start(); 
    }

private static SSLContext coordinatesServerSSLContext(String file, String password)
throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException, UnrecoverableKeyException,
CertificateException, FileNotFoundException, IOException {
    char[] passphrase = password.toCharArray();
    KeyStore ks = KeyStore.getInstance("JKS");
    ks.load(new FileInputStream(file), passphrase);

    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(ks, passphrase);

    TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
    tmf.init(ks);

    SSLContext ssl = SSLContext.getInstance("TLS");
    ssl.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
    return ssl;

}
}
