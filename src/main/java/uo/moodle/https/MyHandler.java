package uo.moodle.https;

import com.sun.net.httpserver.*;

import java.io.IOException;
import java.io.OutputStream;

class MyHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange t) throws IOException {

        if("GET".equals(t.getRequestMethod())) {

            handleResponseGET(t); 


        }else if("POST".equals(t.getRequestMethod())){ 
            
            handleResponsePOST(t); 
            
        }else{

             // Do nothing

        }


    }


    private void handleResponsePOST(HttpExchange httpExchange)  throws  IOException {

        OutputStream outputStream = httpExchange.getResponseBody();
        StringBuilder response = new StringBuilder();

        response.append("Recieved POST - auth OK");

        httpExchange.sendResponseHeaders(200, response.toString().length());

        outputStream.write(response.toString().getBytes());

        outputStream.flush();

        outputStream.close();

    }

    private void handleResponseGET(HttpExchange httpExchange)  throws  IOException {

        OutputStream outputStream = httpExchange.getResponseBody();
        StringBuilder response = new StringBuilder();

        response.append("Recieved GET - auth OK");

        httpExchange.sendResponseHeaders(200, response.toString().length());

        outputStream.write(response.toString().getBytes());

        outputStream.flush();

        outputStream.close();

    }

}
