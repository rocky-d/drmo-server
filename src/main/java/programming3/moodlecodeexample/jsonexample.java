package programming3.moodlecodeexample;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;
import org.json.JSONWriter;

/**
 * Hello world!
 *
 */
public class jsonexample {
    public static void main(String[] args) throws IOException
    {



        JSONObject obj = new JSONObject(); // create an object

        JSONObject obj2 = new JSONObject(); //create an object

        //System.out.println(obj);

        //System.out.println(obj2);

        obj.put("name", "Seppo")
            .put("age", 24)
            .put("phone number", 84019)
            .put("address", "sepontie 3");


       //System.out.println(obj);


        String jsonstring = obj.get("phone number").toString();

        System.out.println("Seppo's phone number is: " + jsonstring);
 

        obj2.put("name", "Teppo")
        .put("age", 34)
        .put("phone number", 222222)
        .put("address", "tepontie 4");

        JSONArray array= new JSONArray();
        array.put(obj2);
        array.put(obj);
        System.out.println(array);


        JSONObject obj3 = array.getJSONObject(0);

        System.out.println("Let's print Teppo again: " + obj3);



/* 
        BufferedWriter writer2 = new BufferedWriter(new FileWriter("myjson.json"));

        new JSONWriter(writer2).object().key("Message").value("First Message").endObject();
        writer2.flush();
        writer2.close();
*/

/*

        JSONTokener tokener = new JSONTokener(new FileReader("myjson.json"));
        System.out.println("Tokener has more data: " + tokener.more());

        JSONObject obj3 = new JSONObject(tokener);
        System.out.println(obj3);


        System.out.println("Tokener has more data: " + tokener.more());


        try {
            JSONObject obj4 = new JSONObject(tokener);
            System.out.println(obj4);
        } catch(JSONException e) {
            System.out.println("Got exception: " + e);
        }
  
        
        */

       // timeUsage();
       

    }



    private static void timeUsage() {

        ZonedDateTime now =ZonedDateTime.now(ZoneId.of("UTC"));

        System.out.println("ZonedDateTime: " + now);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd'T'HH:mm:ss.SSSX");

        String dateText = now.format(formatter);

        System.out.println("Formatted ZDT: " +dateText);

        OffsetDateTime odt = OffsetDateTime.parse(dateText);

        System.out.println("OffsetDateTime: " +odt);

        LocalDateTime ldt = odt.toLocalDateTime();

        System.out.println("LocalDateTIme: " +ldt);

        long sent = ldt.toInstant(ZoneOffset.UTC).toEpochMilli();

        System.out.println("Epoch: : " +sent);

        Instant i = Instant.ofEpochMilli(sent);

        System.out.println("Instant: " + i.toString());

        //DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyy-MM-dd'T'HH:mm:ss.SSSX");
        ZonedDateTime zdt = ZonedDateTime.ofInstant(i, ZoneOffset.UTC);

        System.out.println("ZonedDateTime: " +zdt);

        //String dateNew = zdt.format(formatter2);

        //System.out.println(dateNew);

    
        
    }
}

