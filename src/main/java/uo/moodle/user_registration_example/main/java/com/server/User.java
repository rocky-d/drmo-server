package uo.moodle.user_registration_example.main.java.com.server;

public class User {

    private String username;
    private String password;
    private String email;

    public User (){
        //Default constructor

    }

    public User(String name, String pass, String mail){
        this.username = name;
        this.password = pass;
        this.email = mail;
    }

    public String getUserName(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }

    public String getEmail(){
        return this.email;
    }


    
}
