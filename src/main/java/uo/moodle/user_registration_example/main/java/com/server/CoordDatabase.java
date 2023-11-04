package uo.moodle.user_registration_example.main.java.com.server;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONObject;

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

        }

    }

    private void init() throws SQLException{

        String dbName = "DB";

            String database = "jdbc:sqlite:" + dbName;
            dbConnection = DriverManager.getConnection(database);

            if (null != dbConnection) {
                String createUserTable = "create table users (username varchar(50) NOT NULL, password varchar(50) NOT NULL, email varchar(50), primary key(username))";
                Statement createStatement = dbConnection.createStatement();
                createStatement.executeUpdate(createUserTable);
                createStatement.close();

            }
    }

    public void closeDB() throws SQLException {
		if (null != dbConnection) {
			dbConnection.close();
            System.out.println("closing db connection");
			dbConnection = null;
		}
    }

    public boolean setUser(JSONObject user) throws SQLException {

        //your code here
        return false;
    }

    public boolean checkIfUserExists(String givenUserName) throws SQLException{

          //your code here
        return false;
    }
    
    public boolean authenticateUser(String givenUserName, String givenPassword) throws SQLException {

            //your code here
        return false;
    }

}