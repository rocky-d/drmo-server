package programming3.rocky.moodlecodeexample.httpdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONObject;

public class SimpleDatabase {

    private Connection dbConnection = null;
    private static SimpleDatabase dbInstance = null;
	private static MyExceptionHandler exchand;

    //this class uses singleton design pattern
	public static synchronized SimpleDatabase getInstance(MyExceptionHandler exc) {

		exchand = exc;

		if (null == dbInstance) {
			dbInstance = new SimpleDatabase();
		}
        return dbInstance;
    }
    
    private SimpleDatabase(){

            init();

    }

	private boolean init() {

        String dbName = "MyDatabase";

		String database = "jdbc:sqlite:" + dbName;

		try{
		dbConnection = DriverManager.getConnection(database);

		if (null == dbConnection) {
			String createBasicDB = "create table data (user varchar(500) NOT NULL, usermessage varchar(500) NOT NULL)";
			Statement createStatement = dbConnection.createStatement();
			createStatement.executeUpdate(createBasicDB);
			createStatement.close();

			return true;
		}

		} catch (SQLException e) {
			exchand.handleException(e);
		}
		return false;
	}

	public void closeDB()  {

		try{
		if (null != dbConnection) {
				dbConnection.close();
				dbConnection = null;
			}

		} catch (SQLException e) {
			exchand.handleException(e);
		}
    }
    

	public void setMessage(JSONObject message)  {


		String setMessageString = "insert into data " +
					"VALUES('" + message.getString("user") + "','" + message.getString("message") + "')"; 

		try{
		Statement createStatement;
		createStatement = dbConnection.createStatement();
		createStatement.executeUpdate(setMessageString);
		createStatement.close();
		} catch (SQLException e) {
			exchand.handleException(e);
		}
    }
    
    public JSONArray getMessages() {

        Statement queryStatement = null;
        
		JSONArray jsonarray = new JSONArray();

        String getMessagesString = "select rowid, user, usermessage from data ";

		try{

			queryStatement = dbConnection.createStatement();
			ResultSet rs = queryStatement.executeQuery(getMessagesString);

					
			while (rs.next()) {
				JSONObject obj = new JSONObject(); 
	
				obj.put("id", rs.getInt("rowid"));
				obj.put("user", rs.getString("user"));
				obj.put("usermessage", rs.getString("usermessage"));
	
				jsonarray.put(obj);
			}

		} catch (SQLException e) {
			exchand.handleException(e);
		}

        return jsonarray;

    }


}