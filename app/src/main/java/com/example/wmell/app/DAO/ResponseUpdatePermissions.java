package com.example.wmell.app.DAO;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseUpdatePermissions {

    @SerializedName("permissions")
    @Expose
    private String permissions;

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }
}