package uo.rocky;

import com.sun.net.httpserver.*;
import org.json.JSONObject;
import uo.rocky.entity.EntitySQLConnection;
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
import static uo.rocky.LogWriter.LogEntryType.INFO;

/**
 * Loads the config, prepares the log, connects the database, and launches the server.
 * <p>
 * All fields and methods are static.
 *
 * @author Rocky Haotian Du
 */
public final class ServerLauncher {

    private static final Path CONFIG_FILE = Paths.get("serverLauncher.config.json");

    private static Path logFile;
    private static String sqliteUrl;
    private static boolean isHttps;
    private static int port;
    private static InetAddress host;
    private static boolean setAuthenticator;
    private static String keystoreType;
    private static Path keystoreFile;
    private static char[] keystorePassword;

    public static void main(String[] args) {
        System.out.println("Hello world!");
        System.out.println("---------");

        try {
            loadConfig();
            System.out.println("Config \"" + CONFIG_FILE.getFileName() + "\" has been loaded.");
            System.out.println("---------");

            LogWriter.setBufferedWriter(logFile);
            System.out.println("Log \"" + logFile.getFileName() + "\" has been prepared.");
            System.out.println("---------");

            EntitySQLConnection.setConnection(DriverManager.getConnection(sqliteUrl));
            System.out.println("SQLite \"" + sqliteUrl + "\" has been connected.");
            System.out.println("---------");

            if (!isHttps) {
                launchHttpServer();
                LogWriter.append(INFO, "HTTP server has started to listen on (port number): " + port, "Access control (IP address of remote hosts): " + host);
            } else {
                launchHttpsServer();
                LogWriter.append(INFO, "HTTPS server has started to listen on (port number): " + port, "Access control (IP address of remote hosts): " + host);
            }

        } catch (Exception exception) {
            System.err.println("Launch failed.");
            System.err.println(exception.getClass().getName() + ": " + exception.getMessage());

            Scanner scanner = new Scanner(System.in);
            String input;
            while (true) {
                System.out.println("Override config file with default configuration?");
                System.out.print("(Yes/No) > ");
                input = scanner.nextLine();
                if ("Y".equalsIgnoreCase(input) || "YES".equalsIgnoreCase(input)) {
                    try {
                        overrideConfig();
                        System.out.println("Config has been overridden.");
                        System.out.println("Please check and modify (if you need to) the config file");
                        System.out.println("\"" + CONFIG_FILE.toAbsolutePath() + "\".");
                        System.out.println("And then rerun the program.");
                    } catch (IOException ioException) {
                        System.err.println("Override failed.");
                        System.err.println(ioException.getClass().getName() + ": " + ioException.getMessage());
                    }
                    break;
                } else if ("N".equalsIgnoreCase(input) || "NO".equalsIgnoreCase(input)) {
                    System.out.println("Please check the problem.");
                    break;
                }
            }

        } finally {
            System.out.println("=========");
        }
    }

    public static void launchHttpServer() throws IOException {
        final HttpServer httpServer = HttpServer.create(new InetSocketAddress(host, port), 0);

        final HttpContext commentContext = httpServer.createContext(CommentHttpHandler.GET_CONTEXT, new CommentHttpHandler());
        final HttpContext coordinatesContext = httpServer.createContext(CoordinatesHttpHandler.GET_CONTEXT, new CoordinatesHttpHandler());
        final HttpContext registrationContext = httpServer.createContext(RegistrationHttpHandler.GET_CONTEXT, new RegistrationHttpHandler());
        if (setAuthenticator) {
            commentContext.setAuthenticator(new UserAuthenticator("'" + CommentHttpHandler.GET_CONTEXT + "' requires authentication"));
            coordinatesContext.setAuthenticator(new UserAuthenticator("'" + CoordinatesHttpHandler.GET_CONTEXT + "' requires authentication"));
//            registrationContext.setAuthenticator(new UserAuthenticator("'" + RegistrationHttpHandler.GET_CONTEXT + "' requires authentication"));
        }

        httpServer.setExecutor(null);
        httpServer.start();
    }

    public static void launchHttpsServer() throws Exception {
        final HttpsServer httpsServer = HttpsServer.create(new InetSocketAddress(host, port), 0);

        final HttpContext commentContext = httpsServer.createContext(CommentHttpHandler.GET_CONTEXT, new CommentHttpHandler());
        final HttpContext coordinatesContext = httpsServer.createContext(CoordinatesHttpHandler.GET_CONTEXT, new CoordinatesHttpHandler());
        final HttpContext registrationContext = httpsServer.createContext(RegistrationHttpHandler.GET_CONTEXT, new RegistrationHttpHandler());
        if (setAuthenticator) {
            commentContext.setAuthenticator(new UserAuthenticator("'" + CommentHttpHandler.GET_CONTEXT + "' requires authentication"));
            coordinatesContext.setAuthenticator(new UserAuthenticator("'" + CoordinatesHttpHandler.GET_CONTEXT + "' requires authentication"));
//            registrationContext.setAuthenticator(new UserAuthenticator("'" + RegistrationHttpHandler.GET_CONTEXT + "' requires authentication"));
        }

        KeyStore keyStore = KeyStore.getInstance(keystoreType);
        keyStore.load(Files.newInputStream(keystoreFile), keystorePassword);
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore, keystorePassword);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        trustManagerFactory.init(keyStore);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
        httpsServer.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
            @Override
            public void configure(HttpsParameters httpsParameters) {
                LogWriter.append(INFO, "Got remote connection: " + httpsParameters.getClientAddress());
                httpsParameters.setSSLParameters(getSSLContext().getDefaultSSLParameters());
            }
        });

        httpsServer.setExecutor(null);
        httpsServer.start();
    }

    public static void loadConfig() throws IOException {
        Stream<String> lines = Files.lines(CONFIG_FILE, UTF_8);
        String content = lines.collect(Collectors.joining("\n"));
        lines.close();


        JSONObject config = new JSONObject(content);

        JSONObject logConfig = config.getJSONObject("LOG");
        logFile = Paths.get(logConfig.getString("PATH"));

        JSONObject dbConfig = config.getJSONObject("SQL");
        sqliteUrl = "jdbc:sqlite:" + dbConfig.getString("PATH");

        JSONObject serverConfig = config.getJSONObject("SERVER");
        String serverMode = serverConfig.getString("MODE");
        if ("HTTP".equalsIgnoreCase(serverMode)) {
            isHttps = false;

            JSONObject httpConfig = serverConfig.getJSONObject("HTTP");
            port = httpConfig.getInt("PORT");
            host = InetAddress.getByName(httpConfig.getString("HOST"));
            setAuthenticator = httpConfig.getBoolean("AUTHENTICATION");

        } else if ("HTTPS".equalsIgnoreCase(serverMode)) {
            isHttps = true;

            JSONObject httpsConfig = serverConfig.getJSONObject("HTTPS");
            port = httpsConfig.getInt("PORT");
            host = InetAddress.getByName(httpsConfig.getString("HOST"));
            setAuthenticator = httpsConfig.getBoolean("AUTHENTICATION");

            JSONObject keystoreConfig = httpsConfig.getJSONObject("KEYSTORE");
            keystoreType = keystoreConfig.getString("TYPE");
            keystoreFile = Paths.get(keystoreConfig.getString("PATH"));
            keystorePassword = keystoreConfig.getString("PASSWORD").toCharArray();
        }
    }

    public static void overrideConfig() throws IOException {
        Files.deleteIfExists(CONFIG_FILE);
        Files.write(CONFIG_FILE, ("\n" +
                "{\n" +
                "  \"LOG\": {\n" +
                "    \"PATH\": \"drmo.server.log\"\n" +
                "  },\n" +
                "  \"SQL\": {\n" +
                "    \"PATH\": \"drmo.sqlite.db\"\n" +
                "  },\n" +
                "  \"SERVER\": {\n" +
                "    \"MODE\": \"HTTP\",\n" +
                "    \"HTTP\": {\n" +
                "      \"PORT\": 8001,\n" +
                "      \"HOST\": \"0.0.0.0\",\n" +
                "      \"AUTHENTICATION\": false\n" +
                "    },\n" +
                "    \"HTTPS\": {\n" +
                "      \"PORT\": 8001,\n" +
                "      \"HOST\": \"0.0.0.0\",\n" +
                "      \"AUTHENTICATION\": true,\n" +
                "      \"KEYSTORE\": {\n" +
                "        \"TYPE\": \"JKS\",\n" +
                "        \"PATH\": \"<path>\",\n" +
                "        \"PASSWORD\": \"<password>\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}\n"
        ).getBytes(UTF_8));
    }
}