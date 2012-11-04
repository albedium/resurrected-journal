package com.mcgath.rj;

/** This class encapsulates the user identity and supports login */
public class User {

    private boolean loggedIn = false;
    private String username;
    private String password;
    
    public User(String user, String pw) {
        username = user;
        password = pw;
    }

    public boolean login () {
        // TODO stub
        return false;
    }
}
