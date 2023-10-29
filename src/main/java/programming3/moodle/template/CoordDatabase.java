package programming3.moodle.template;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


import org.json.JSONArray;


public class CoordDatabase{

    private Connection dbConnection = null;
    private static CoordDatabase dbInstance = null;


	public static synchronized CoordDatabase getInstance() {
		if (null == dbInstance) {
			dbInstance = new CoordDatabase();
		}
        return dbInstance;
    }

    private CoordDatabase(){

        try {
            init();
        } catch (SQLException e) {
            System.out.println("Log - SQLexception");
        }

    }

    private boolean init() throws SQLException{

        String dbName = "MyDatabase";

        String database = "jdbc:sqlite:" + dbName;
        dbConnection = DriverManager.getConnection(database);

        if (null == dbConnection) {
          
            //Create database if no available

            return true;
        }

        System.out.println("DB creation failed");
        return false;

    }

    public void closeDB() throws SQLException {
		if (null != dbConnection) {
			dbConnection.close();
            System.out.println("closing db connection");
			dbConnection = null;
		}
    }

    public void storeCoordinates(Coordinate coord) {
 
        //Code to store coordinates to database
        
    }

    public JSONArray getCoordinates(){

       //Code to get coordinates from the database

       JSONArray coordinatesToBeReturned = null;
       return coordinatesToBeReturned;

    }



}