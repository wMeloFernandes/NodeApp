package com.example.wmell.app.DAO;

/**
 * Created by wmell on 22/10/2018.
 */

public class UserModel {

    private String username;
    private String password;

    public UserModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
