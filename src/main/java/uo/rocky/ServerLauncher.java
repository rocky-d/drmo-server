package uo.rocky;

import com.sun.net.httpserver.*;
import org.json.JSONObject;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.sql.DriverManager;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class ServerLauncher {
    public static final Path CONFIG_FILE = Paths.get("serverLauncher.json");

    private static String sqliteUrl;
    private static boolean isHttps;
    private static int port;
    private static String host;
    private static String jksPath;
    private static char[] jksPassword;

    public static void launchHttpServer() throws IOException {
        final HttpServer httpServer = HttpServer.create(new InetSocketAddress(InetAddress.getByName(host), port), 0);

        final HttpContext commentContext = httpServer.createContext(CommentHttpHandler.GET_CONTEXT, new CommentHttpHandler());
        final HttpContext coordinatesContext = httpServer.createContext(CoordinatesHttpHandler.GET_CONTEXT, new CoordinatesHttpHandler());
        final HttpContext registrationContext = httpServer.createContext(RegistrationHttpHandler.GET_CONTEXT, new RegistrationHttpHandler());
//        final HttpContext warningContext = httpServer.createContext(WarningHttpHandler.GET_CONTEXT, new WarningHttpHandler());

        httpServer.setExecutor(null);
        httpServer.start();
    }

    public static void launchHttpsServer() throws Exception {
        final HttpsServer httpsServer = HttpsServer.create(new InetSocketAddress(InetAddress.getByName(host), port), 0);

        final HttpContext commentContext = httpsServer.createContext(CommentHttpHandler.GET_CONTEXT, new CommentHttpHandler());
        commentContext.setAuthenticator(new UserAuthenticator("'" + CommentHttpHandler.GET_CONTEXT + "' requires authentication"));
        final HttpContext coordinatesContext = httpsServer.createContext(CoordinatesHttpHandler.GET_CONTEXT, new CoordinatesHttpHandler());
        coordinatesContext.setAuthenticator(new UserAuthenticator("'" + CoordinatesHttpHandler.GET_CONTEXT + "' requires authentication"));
        final HttpContext registrationContext = httpsServer.createContext(RegistrationHttpHandler.GET_CONTEXT, new RegistrationHttpHandler());
//        registrationContext.setAuthenticator(new UserAuthenticator("'" + RegistrationHttpHandler.GET_CONTEXT + "' requires authentication"));
//        final HttpContext warningContext = httpsServer.createContext(WarningHttpHandler.GET_CONTEXT, new WarningHttpHandler());
//        warningContext.setAuthenticator(new UserAuthenticator("'" + WarningHttpHandler.GET_CONTEXT + "' requires authentication"));

        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(Files.newInputStream(Paths.get(jksPath)), jksPassword);
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore, jksPassword);
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

    public static void main(String[] args) throws IOException {
        System.out.println("Hello world!");
        System.out.println("---------");

        try {
            loadConfig();
            System.out.println("Config \"" + CONFIG_FILE.getFileName() + "\" was loaded.");
            System.out.println("---------");

            EntityDBConnection.setConnection(DriverManager.getConnection(sqliteUrl));
            System.out.println("SQLite \"" + sqliteUrl + "\" was connected.");
            System.out.println("---------");

            if (!isHttps) {
                launchHttpServer();
                System.out.println("HTTP server started listening on (port number): " + port);
                System.out.println("Access control (IP address of remote hosts): " + host);
            } else {
                launchHttpsServer();
                System.out.println("HTTPS server started listening on (port number): " + port);
                System.out.println("Access control (IP address of remote hosts): " + host);
            }
        } catch (Exception exception) {
            System.out.println("Launch failed.");
            System.out.println(exception.getClass().getSimpleName() + ": " + exception.getMessage());
            System.out.println();

            Scanner scanner = new Scanner(System.in);
            String input;
            while (true) {
                System.out.println("Override config file with default configuration?");
                System.out.print("(Yes/No) > ");
                input = scanner.nextLine();
                System.out.println();
                if (input.equalsIgnoreCase("Y") || input.equalsIgnoreCase("YES")) {
                    overrideConfig();
                    System.out.println("Config was overridden.");
                    System.out.println("Please check and modify (if you need to) the config file");
                    System.out.println("\"" + CONFIG_FILE.toAbsolutePath() + "\".");
                    System.out.println("And then restart the program.");
                    break;
                } else if (input.equalsIgnoreCase("N") || input.equalsIgnoreCase("NO")) {
                    System.out.println("Please check the problem.");
                    break;
                }
            }
        } finally {
            System.out.println("=========");
        }
    }

    public static void loadConfig() throws IOException {
        Stream<String> lines = Files.lines(CONFIG_FILE, UTF_8);
        String content = lines.collect(Collectors.joining("\n"));
        lines.close();

        JSONObject config = new JSONObject(content);
        JSONObject sqliteConfig = config.getJSONObject("SQLITE");
        sqliteUrl = "jdbc:sqlite:" + sqliteConfig.getString("PATH");
        JSONObject serverConfig = config.getJSONObject("SERVER");
        if ("HTTP".equalsIgnoreCase(serverConfig.getString("PROTOCOL"))) {
            isHttps = false;
            JSONObject httpConfig = serverConfig.getJSONObject("HTTP");
            port = httpConfig.getInt("PORT");
            host = httpConfig.getString("HOST");
        } else if ("HTTPS".equalsIgnoreCase(serverConfig.getString("PROTOCOL"))) {
            isHttps = true;
            JSONObject httpsConfig = serverConfig.getJSONObject("HTTPS");
            port = httpsConfig.getInt("PORT");
            host = httpsConfig.getString("HOST");
            JSONObject jksConfig = httpsConfig.getJSONObject("JKS");
            jksPath = jksConfig.getString("PATH");
            jksPassword = jksConfig.getString("PASSWORD").toCharArray();
        }
    }

    public static void overrideConfig() throws IOException {
        Files.deleteIfExists(CONFIG_FILE);
        Files.write(CONFIG_FILE, (
                "{\n" +
                        "  \"SQLITE\": {\n" +
                        "    \"PATH\": \"default.sqlite.db\"\n" +
                        "  },\n" +
                        "  \"SERVER\": {\n" +
                        "    \"PROTOCOL\": \"HTTP\",\n" +
                        "    \"HTTP\": {\n" +
                        "      \"PORT\": 8001,\n" +
                        "      \"HOST\": \"0.0.0.0\"\n" +
                        "    },\n" +
                        "    \"HTTPS\": {\n" +
                        "      \"PORT\": 8001,\n" +
                        "      \"HOST\": \"0.0.0.0\",\n" +
                        "      \"JKS\": {\n" +
                        "        \"PATH\": \"<file path>\",\n" +
                        "        \"PASSWORD\": \"<password>\"\n" +
                        "      }\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"LOG\": {\n" +
                        "    \"PATH\": \"default.server.log\"\n" +
                        "  }\n" +
                        "}"
        ).getBytes(UTF_8));
    }
}