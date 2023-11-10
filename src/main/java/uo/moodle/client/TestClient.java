package uo.moodle.client;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Base64;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.json.JSONObject;



public final class TestClient {

    String username;
    String password;
    String auth;
    String fullAddress;
    static Client client;

    private static final int CONNECT_TIMEOUT = 10 * 1000;
	private static final int REQUEST_TIMEOUT = 30 * 1000;

    private static final String USER_AGENT = "Mozilla/5.0";

    public TestClient(String address){

        client = new Client(address);
        System.out.println("Test client created");

    }

    public TestClient(String keystore, String address, String newUser, String newPassword) {

        client = new Client(keystore, address);
        username = newUser;
        password = newPassword;
        auth = username + ":" + password;

    }

    public synchronized int testConnection() throws IOException{

        int responseCode = 200;

        fullAddress = client.getServerAddress();
        fullAddress += client.getMessageContext();

        URL url = new URL(fullAddress);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		responseCode = con.getResponseCode();

        return responseCode;
    }


    public synchronized int testJSONMessage(JSONObject obj) throws IOException {
        int responseCode = 400;
        byte[] msgBytes;

        fullAddress = client.getServerAddress();
        fullAddress += client.getMessageContext();

        URL url = new URL(fullAddress);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
	
        String coordinates = obj.toString();
        msgBytes = coordinates.getBytes("UTF-8");
        con.setRequestProperty("Content-Type", "application/json");

		con.setRequestMethod("POST");
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestProperty("Content-Length", String.valueOf(msgBytes.length));

 
        System.out.println("sending coords");
		OutputStream writer = con.getOutputStream();
		writer.write(msgBytes);
		writer.close();


		responseCode = con.getResponseCode();
        System.out.println("got response code: "+responseCode);

        return responseCode;
    }

        /**
        * proper description.
        * @param name value what function takes.
        * @return A sentence greeting the person.
        */
        public synchronized String getMessages() throws IOException {
        int responseCode = 400;

        fullAddress = client.getServerAddress();
        fullAddress += client.getMessageContext();

        URL url = new URL(fullAddress);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");

        System.out.println("done setting GET");
        responseCode = con.getResponseCode();
        System.out.println(responseCode);

        StringBuilder coordinates = new StringBuilder();
        String input;
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));

        while ((input = in.readLine()) != null) {
            coordinates.append(input);
        }

        String result = coordinates.toString();
        System.out.println(result);

        return result;

    }




    public synchronized int testFaultyMessage(String message) throws IOException {
        int responseCode = 400;
        byte[] msgBytes;

        fullAddress = client.getServerAddress();
        fullAddress += client.getMessageContext();

        URL url = new URL(fullAddress);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
	
        msgBytes = message.getBytes("UTF-8");
        con.setRequestProperty("Content-Type", "application/json");

		con.setRequestMethod("POST");
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestProperty("Content-Length", String.valueOf(msgBytes.length));

		OutputStream writer = con.getOutputStream();
		writer.write(msgBytes);
		writer.close();

		responseCode = con.getResponseCode();

        return responseCode;
    }


    public synchronized String getUserCoordinates(String user) throws IOException, KeyManagementException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
        int responseCode = 400;
        byte[] msgBytes;

        fullAddress = client.getServerAddress();
        fullAddress += client.getMessageContext();

        URL url = new URL(fullAddress);

        HttpURLConnection con = createTrustingConnection(url);

        JSONObject obj = new JSONObject();
        obj.put("query", "user");
        obj.put("nickname", user);
	
        String coordinates = obj.toString();
        msgBytes = coordinates.getBytes("UTF-8");
        con.setRequestProperty("Content-Type", "application/json");

		con.setRequestMethod("POST");
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestProperty("Content-Length", String.valueOf(msgBytes.length));

        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
		String authHeaderValue = "Basic " + new String(encodedAuth);
		con.setRequestProperty("Authorization", authHeaderValue);

		OutputStream writer = con.getOutputStream();
		writer.write(msgBytes);
		writer.close();


		responseCode = con.getResponseCode();
        System.out.println("got response code: "+responseCode);

        StringBuilder coordinatesReturned = new StringBuilder();
        String input;
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));

        while ((input = in.readLine()) != null) {
            coordinatesReturned.append(input);
        }

        
        String result = coordinatesReturned.toString();
        System.out.println(result);

        return result;
    }

    public synchronized String getMessageByArea(Double long1, Double lat1, Double long2, Double lat2) throws IOException, KeyManagementException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
        int responseCode = 400;
        byte[] msgBytes;

        fullAddress = client.getServerAddress();
        fullAddress += client.getMessageContext();

        URL url = new URL(fullAddress);

        HttpURLConnection con = createTrustingConnection(url);

        JSONObject obj = new JSONObject();
        obj.put("query", "location");
        obj.put("uplongitude", long1);
        obj.put("uplatitude", lat1);
        obj.put("downlongitude", long2);
        obj.put("downlatitude", lat2);
	
        String coordinates = obj.toString();
        msgBytes = coordinates.getBytes("UTF-8");
        con.setRequestProperty("Content-Type", "application/json");

		con.setRequestMethod("POST");
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestProperty("Content-Length", String.valueOf(msgBytes.length));

        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
		String authHeaderValue = "Basic " + new String(encodedAuth);
		con.setRequestProperty("Authorization", authHeaderValue);

		OutputStream writer = con.getOutputStream();
		writer.write(msgBytes);
		writer.close();


		responseCode = con.getResponseCode();
        System.out.println("got response code: "+responseCode);

        StringBuilder coordinatesReturned = new StringBuilder();
        String input;
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));

        while ((input = in.readLine()) != null) {
            coordinatesReturned.append(input);
        }

        
        String result = coordinatesReturned.toString();
        System.out.println(result);

        return result;
    }

    public synchronized int testQuery(String command) throws IOException, KeyManagementException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
        int responseCode = 400;
        byte[] msgBytes;

        fullAddress = client.getServerAddress();
        fullAddress += client.getRegisterContext();

        URL url = new URL(fullAddress);

        HttpURLConnection con = createTrustingConnection(url);

        JSONObject obj = new JSONObject();
        obj.put("query", command);
	
        String coordinates = obj.toString();
        msgBytes = coordinates.getBytes("UTF-8");
        con.setRequestProperty("Content-Type", "application/json");

		con.setRequestMethod("POST");
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestProperty("Content-Length", String.valueOf(msgBytes.length));

        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
		String authHeaderValue = "Basic " + new String(encodedAuth);
		con.setRequestProperty("Authorization", authHeaderValue);

		OutputStream writer = con.getOutputStream();
		writer.write(msgBytes);
		writer.close();


		responseCode = con.getResponseCode();
        System.out.println("got response code: "+responseCode);

        return responseCode;
    }


    /**
     * Main
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {

        
        // client.setupClient(args[0], args[1]);



    }

    private HttpURLConnection createTrustingConnection(URL url) throws KeyStoreException, CertificateException,
            NoSuchAlgorithmException, FileNotFoundException, KeyManagementException, IOException {

            Certificate certificate = CertificateFactory.getInstance("X.509").generateCertificate(new FileInputStream(client.getKeystore()));
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(null, null);
            keyStore.setCertificateEntry("localhost", certificate);

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(sslContext.getSocketFactory());
            // All requests use these common timeouts.
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(REQUEST_TIMEOUT);
            return connection;

    }
}
