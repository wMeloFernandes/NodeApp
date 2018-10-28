package com.example.wmell.app.DAO;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdatePermission {

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
