package uo.moodle.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;

public class TestMinReq {

    private static TestClient testClient = null;
    private static TestSettings testSettings = null;

    TestMinReq() {
        testSettings = new TestSettings();
        TestSettings.readSettingsXML("testconfigw5.xml");
        testClient = new TestClient(testSettings.getServerAddress());

    }

    @Test
    @BeforeAll
    @DisplayName("Setting up the test environment")
    public static void initialize() {
        System.out.println("initialized tests");
    }

    @Test
    @AfterAll
    public static void teardown() {
        System.out.println("Testing finished.");
    }

    @Test
    @Order(1)
    @DisplayName("Testing server connection")
    void testHTTPServerConnection() throws IOException {
        System.out.println("Testing server connection");
        int result = testClient.testConnection();
        assertTrue(result > 1);
    }

    @Test
    @Order(2)
    @DisplayName("Testing sending message to server")
    void testSendMessage() throws IOException {
        System.out.println("Testing sending message to server");

        JSONObject obj = new JSONObject();

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd'T'HH:mm:ss.SSSX");

        String dateText = now.format(formatter);

        double coord1 = ThreadLocalRandom.current().nextDouble(0, 90);
        double coord2 = ThreadLocalRandom.current().nextDouble(0, 90);

        obj.put("nickname", "Seppo");
        obj.put("latitude", coord1);
        obj.put("longitude", coord2);
        obj.put("sent", dateText);
        obj.put("dangertype", "Moose");
        int result = testClient.testJSONMessage(obj);
        System.out.println(result);
        assertTrue(200 <= result && result <= 299, "Test failed, posting message returned code " + result);
        String response = testClient.getMessages();
        JSONArray obj2 = new JSONArray(response);
        System.out.println(response);
        System.out.println("original is " + obj);
        System.out.println("object is " + obj2);


        boolean isSame = false;
        JSONObject obj3 = new JSONObject();
        for (int i = 0; i < obj2.length(); i++) {
            obj3 = obj2.getJSONObject(i);

            //feature compliancy...
            if(obj3.has("id")){
                obj3.remove("id");
            }

            System.out.println(obj3);
            if (obj.similar(obj3)) {
                isSame = true;
                break;
            }
        }

        assertTrue(isSame, "Test failed, Object " + obj.toString() + " could not be found in response");

    }

    /**
     * 
     * @throws IOException
     * 
     * 
     * 
     */
    @Test
    @Order(5)
    @DisplayName("Testing characters")
    void testCharacters() throws IOException {
        System.out.println("Testing characters in message");

        JSONObject obj = new JSONObject();

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd'T'HH:mm:ss.SSSX");

        String dateText = now.format(formatter);

        double coord1 = ThreadLocalRandom.current().nextDouble(0, 90);
        double coord2 = ThreadLocalRandom.current().nextDouble(0, 90);

        obj.put("nickname", "Matti Meikäläinen");
        obj.put("latitude", coord1);
        obj.put("longitude", coord2);
        obj.put("sent", dateText);
        obj.put("dangertype", "Reindeer");
        int result = testClient.testJSONMessage(obj);
        System.out.println(result);
        assertTrue(200 <= result && result <= 299, "Test failed, posting message returned code " + result);
        String response = testClient.getMessages();
        JSONArray obj2 = new JSONArray(response);
        System.out.println(response);
        System.out.println("original is " + obj);
        System.out.println("object is " + obj2);

        boolean isSame = false;
        JSONObject obj3 = new JSONObject();
        for (int i = 0; i < obj2.length(); i++) {
            obj3 = obj2.getJSONObject(i);

                        //feature compliancy...
                        if(obj3.has("id")){
                            obj3.remove("id");
                        }

            System.out.println(obj3);
            if (obj.similar(obj3)) {
                isSame = true;
                break;
            }
        }

        assertTrue(isSame, "Test failed, Object " + obj.toString() + " could not be found in response");
    }


    @Test
    @Order(7)
    @DisplayName("Testing faulty message")
    void testFaultyMessage() throws IOException {
        System.out.println("Testing faulty attributes in message");

        JSONObject obj = new JSONObject();

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd'T'HH:mm:ss.SSSX");

        String dateText = now.format(formatter);

        obj.put("nickname", "Virhe");
        obj.put("latitude", "stuff");
        obj.put("longitude", 1);
        obj.put("sent", dateText);
        obj.put("dangertype", "Reindeer");
        int result = testClient.testJSONMessage(obj);
        System.out.println(result);
        assertFalse(200 <= result && result <= 299,
                "Test failed, latitude and longitude weren't double, make sure that you check the values in your code");
    }

    @Test
    @Order(8)
    @DisplayName("Testing faulty time")
    void testFaultyTime() throws IOException {
        System.out.println("Testing faulty attributes in message");

        JSONObject obj = new JSONObject();

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));

        double coord1 = ThreadLocalRandom.current().nextDouble(0, 90);
        double coord2 = ThreadLocalRandom.current().nextDouble(0, 90);

        obj.put("nickname", "Virhe");
        obj.put("latitude", coord2);
        obj.put("longitude", coord1);
        obj.put("sent", now);
        obj.put("dangertype", "Reindeer");
        int result = testClient.testJSONMessage(obj);
        System.out.println(result);
        assertFalse(200 <= result && result <= 299,
                "Test failed, time format was wrong, make sure that you check that the time is correct in your code");
    }


    @RepeatedTest(200)
    @Execution(ExecutionMode.CONCURRENT)
    @Order(11)
    @DisplayName("Server load test with large amount of messages")
    void testServerLoading() throws IOException {
        System.out.println("Server load test with large amount of messages");

        JSONObject obj = new JSONObject();

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd'T'HH:mm:ss.SSSX");

        String dateText = now.format(formatter);

        double coord1 = ThreadLocalRandom.current().nextDouble(0, 90);
        double coord2 = ThreadLocalRandom.current().nextDouble(0, 90);

        obj.put("nickname", "Seppo");
        obj.put("latitude", coord1);
        obj.put("longitude", coord2);
        obj.put("sent", dateText);
        obj.put("dangertype", "Moose");
        int result = testClient.testJSONMessage(obj);
        assertTrue(200 <= result && result <= 299,
                "Test failed, posting message returned code " + result + " when loaded");
    }

    @Test
    @Order(12)
    @DisplayName("Testing faulty message 2 - not supported animal type")
    void testFaultyMessage2() throws IOException {
        System.out.println("Testing faulty animal type in message");

        JSONObject obj = new JSONObject();

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd'T'HH:mm:ss.SSSX");

        String dateText = now.format(formatter);

        double coord1 = ThreadLocalRandom.current().nextDouble(0, 90);
        double coord2 = ThreadLocalRandom.current().nextDouble(0, 90);

        obj.put("nickname", "Seppo");
        obj.put("latitude", coord1);
        obj.put("longitude", coord2);
        obj.put("sent", dateText);
        obj.put("dangertype", "NotAReindeer");
        int result = testClient.testJSONMessage(obj);
        System.out.println(result);
        assertFalse(200 <= result && result <= 299, "Test failed, a non-supported value was passed for the dangertype");
    }

    @Test
    @Order(13)
    @DisplayName("Testing faulty message 3 - not json")
    void testFaultyMessage3() throws IOException {
        System.out.println("Testing faulty json in message");

        int result = testClient.testFaultyMessage("{I am not a json}");
        System.out.println(result);
        assertFalse(200 <= result && result <= 299, "Test failed, a faulty json was supplied");
    }

}
