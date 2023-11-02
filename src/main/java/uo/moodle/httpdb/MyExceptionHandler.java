package uo.moodle.httpdb;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class MyExceptionHandler {

    File logfile;
    String logFileName = "logfile.txt";

    //SQLite error codes
    private static final int SQLITE_CANTOPEN = 14; 
    private static final int SQLITE_AUTH = 23;
    private static MyExceptionHandler MyExceptionHandlerInstance;


    public static MyExceptionHandler getMyExceptionHandler(){

        if(MyExceptionHandlerInstance == null){
            MyExceptionHandlerInstance = new MyExceptionHandler();
        }

        return MyExceptionHandlerInstance;

    }

    private MyExceptionHandler(){

        createLog();

    }

    private void createLog(){

        try{

            this.logfile = new File(logFileName);
            if(this.logfile.createNewFile()){
                System.out.println("Logfile created " +this.logfile.getName() + ".");
            }else{
                System.out.println("Logfile already available.");
            }


        } catch (IOException e){
            iHaveFailed(e);
        }

    }

    private void writeToLog(String errorDesc){


        try{

            OutputStreamWriter logWriter = new OutputStreamWriter(new FileOutputStream(logFileName, true), StandardCharsets.UTF_8); 

            ZonedDateTime time = ZonedDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ssz");
            String timestamp = time.format(formatter);
            //ZonedDateTime parsedtimestamp = ZonedDateTime.parse(timestamp, formatter);

            logWriter.write(timestamp+ " " + errorDesc + "\n");
            logWriter.flush();
            logWriter.close();

            System.out.println(errorDesc);
        } catch (IOException e){

            iHaveFailed(e);

        }
    }

    public void handleException(SQLException e){

        if(e.getErrorCode() == SQLITE_CANTOPEN){

            //NOTE: How this has been implemented (due database already existing) it will be always thrown when db has been already created
            //This is to just simulate flow of error messages, you need to create a check here if the problem is due existing database if you want to make this properly working
            //But that is excercise for your own time

            System.out.println("Cannot open db, check that database has been created correctly");
            writeToLog(e.getLocalizedMessage());

        }else if(e.getErrorCode() == SQLITE_AUTH){

            System.out.println("SQL statement not authorised");
            writeToLog(e.getLocalizedMessage());

        }else{

            System.out.println("Something went wrong with the db, check log for further info");
            writeToLog(e.getLocalizedMessage());
        }

        e.printStackTrace();

    }

    public void handleExceptionException (IOException e){

            System.out.println("IOException thrown, check log for further info");
            writeToLog(e.getLocalizedMessage());

    }
    
    private void iHaveFailed(IOException e){
        System.out.println("Uh oh... Exception handler had an exception... game over man");
        e.printStackTrace();
    }

}
