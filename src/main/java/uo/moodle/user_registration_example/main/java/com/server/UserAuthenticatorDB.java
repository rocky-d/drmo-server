package uo.moodle.user_registration_example.main.java.com.server;

import java.sql.SQLException;

import com.sun.net.httpserver.BasicAuthenticator;

import org.json.JSONException;
import org.json.JSONObject;

public class UserAuthenticatorDB extends BasicAuthenticator {

    private CoordDatabase db = null;


    public UserAuthenticatorDB() {
        super("coordinates");
        db = CoordDatabase.getInstance();
    }

    @Override
    public boolean checkCredentials(String username, String password) {

        System.out.println("checking user: " + username + " " + password + "\n");

        boolean isValidUser;
        try {
            isValidUser = db.authenticateUser(username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return isValidUser;
    }

    public boolean addUser(String userName, String password, String email) throws JSONException, SQLException {
        
        boolean result = db.setUser(new JSONObject().put("username", userName).put("password", password).put("email", email));
        if(!result){
            System.out.println("cannot register user");
            return false;
        }
       System.out.println(userName + " registered");

       return true;

    }

}
