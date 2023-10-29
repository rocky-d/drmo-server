package programming3.moodle.template;

import com.sun.net.httpserver.*;

import java.net.InetSocketAddress;


public class Server {


    public static void main(String[] args) throws Exception {
        //create the http server to port 8001 with default logger
        HttpServer server = HttpServer.create(new InetSocketAddress(8001),0);
        
        HttpContext context = server.createContext("/coordinates", new CoordinateHandler());

        // creates a default executor
        server.setExecutor(null); 
        server.start(); 
    }


}
