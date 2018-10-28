package com.example.wmell.app.DAO;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseUpdatePermissions {

    @SerializedName("permissions")
    @Expose
    private List<UpdatePermission> permissions = null;

    public List<UpdatePermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<UpdatePermission> permissions) {
        this.permissions = permissions;
    }

}