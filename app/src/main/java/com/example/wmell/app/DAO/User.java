package com.example.wmell.app.DAO;


public class User {

    private String mUsername;
    private String mEmail;

    public User(String username, String email) {
        this.mUsername = username;
        this.mEmail = email;
    }


    public String getUsername() {
        return this.mUsername;
    }

    public String getEmail() {
        return mEmail;
    }

}
