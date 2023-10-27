package programming3.rocky.moodlecodeexample.httpdb;

import java.lang.String;

import org.json.JSONArray;
import org.json.JSONObject;


import com.sun.net.httpserver.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.io.*;

/**
 * Hello world!
 */

class MyHandler implements HttpHandler {

    private static MyExceptionHandler exchand = MyExceptionHandler.getMyExceptionHandler();
    private static SimpleDatabase db = SimpleDatabase.getInstance(exchand);
    
    @Override
    public void handle(HttpExchange t) throws IOException { 


        if("GET".equals(t.getRequestMethod())) {

            handleResponseGET(t); 



        }else if("POST".equals(t.getRequestMethod())){ 
            
            //remember to check if the user provided string is actually a JSON object!
            String jsonString = handleResponsePOST(t);


            JSONObject obj = new JSONObject(jsonString);
            System.out.println(jsonString);
            System.out.println(obj.toString());
            db.setMessage(obj);           

            t.sendResponseHeaders(200, 0);
            
            OutputStream outputStream = t.getResponseBody();

            outputStream.write("OK".getBytes());
    
            outputStream.flush();
    
            outputStream.close();
        
            
        }else{

            handleResponse(t,"Not supported");

        }


    }

    private void handleResponseGET(HttpExchange httpExchange)  throws  IOException {

        OutputStream outputStream = httpExchange.getResponseBody();

        JSONArray response = db.getMessages();

        String stringResponse = response.toString();

        httpExchange.sendResponseHeaders(200, stringResponse.length());

        outputStream.write(stringResponse.getBytes());

        outputStream.flush();

        outputStream.close();        

    }

    private String handleResponsePOST(HttpExchange httpExchange)  throws  IOException {

        InputStream stream = httpExchange.getRequestBody();
        String text = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));

        return text;

    }

    private void handleResponse(HttpExchange httpExchange, String requestParamValue)  throws  IOException {

        OutputStream outputStream = httpExchange.getResponseBody();
        StringBuilder htmlBuilder = new StringBuilder();        
        htmlBuilder.append("<html>")
            .append("<body>")
            .append("<h1>")
            .append("Returning payload ")
            .append(requestParamValue)
            .append("</h1>")
            .append("</body>")
            .append("</html>");

        String htmlResponse = htmlBuilder.toString();

        httpExchange.sendResponseHeaders(200, htmlResponse.length());

        outputStream.write(htmlResponse.getBytes());

        outputStream.flush();

        outputStream.close();        

    }


}
public class App 
{

    //private static String jsonString = "{\"user\": \"seppo\", \"message\": \"viesti on\"}";



    public static void main( String[] args ) throws IOException
    {

        HttpServer server = HttpServer.create(new InetSocketAddress(1024),0);
        server.createContext("/coordinates", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start(); 

    }
}
