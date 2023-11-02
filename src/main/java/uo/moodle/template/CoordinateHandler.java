package uo.moodle.template;

import com.sun.net.httpserver.*;


import java.io.IOException;



public class CoordinateHandler implements HttpHandler  {

 
    public CoordinateHandler() {



    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Headers headers = exchange.getRequestHeaders();
        CoordDatabase db = CoordDatabase.getInstance();

        String contentType = headers.get("Content-Type").get(0);
        if(contentType.equalsIgnoreCase("application/json")){

            if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
 
                //Code for handling get requests

            } else if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {

                //Code for handling post requests

            }else{

                //Code for handling other requests


            }


    }

    }
}
