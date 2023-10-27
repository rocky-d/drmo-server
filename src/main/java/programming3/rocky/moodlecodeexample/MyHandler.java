package programming3.rocky.moodle;

import com.sun.net.httpserver.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.io.*;

/**
 * Hello world!
 */

class MyHandler implements HttpHandler {

    StringBuilder textDump = new StringBuilder("Dumped text: ");

    @Override
    public void handle(HttpExchange t) throws IOException {

        String requestParamValue=null;

        if("GET".equals(t.getRequestMethod())) {

            requestParamValue = handleGetRequest(t);
            handleResponseGET(t,requestParamValue);


        }else if("POST".equals(t.getRequestMethod())){
           
            handlePOSTRequest(t);
            handleResponsePOST(t);
           
        }else{

            handleResponse(t," only get and post supported, don't post rubbish");

        }


    }

    private void handleResponseGET(HttpExchange httpExchange, String requestParamValue)  throws  IOException {

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

    private void handleResponsePOST(HttpExchange httpExchange)  throws  IOException {

        OutputStream outputStream = httpExchange.getResponseBody();
        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append("<html>")
            .append("<body>")
            .append("<h1>")
            .append(textDump)
            .append("</h1>")
            .append("</body>")
            .append("</html>");

        String htmlResponse = htmlBuilder.toString();

        httpExchange.sendResponseHeaders(200, htmlResponse.length());

        outputStream.write(htmlResponse.getBytes());

        outputStream.flush();

        outputStream.close();

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


    private String handleGetRequest(HttpExchange httpExchange){

        return httpExchange.getRequestURI().toString().split("\\?")[1].split("=")[1];

    }

    private void handlePOSTRequest(HttpExchange httpExchange) {

        String text = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody(),StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));

        textDump.append(text);

    }

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(1024),0);
        server.createContext("/coordinates", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }
}