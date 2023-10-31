package com.example;

import com.sun.net.httpserver.*;


class authMe extends BasicAuthenticator{

    public authMe (String realm){
        super(realm);
    }

    public boolean checkCredentials(String username, String password){

        if (username.equals("dummy") && password.equals("passw"))
        return true;
        else
        return false;

    }

}
