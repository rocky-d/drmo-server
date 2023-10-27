package programming3.rocky.moodle;

import com.sun.net.httpserver.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.io.*;

/**
 * Hello world!
 */

class Server implements HttpHandler {

    StringBuilder textDump = new StringBuilder("Dumped text: ");

    @Override
    public void handle(HttpExchange t) throws IOException {


        if("GET".equals(t.getRequestMethod())) {

            //code here


        }else if("POST".equals(t.getRequestMethod())){ 
            
            //code here
            
        }else{

            //code here

        }


    }

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(1024),0);
        server.createContext("/coordinates", new Server());
        server.setExecutor(null); // creates a default executor
        server.start(); 
    }
}