package com.example.wmell.app.DAO;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Permissions {

    @SerializedName("permissions")
    @Expose
    private List<ResponsePermissionsUpdate> permissions = null;

    public List<ResponsePermissionsUpdate> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<ResponsePermissionsUpdate> permissions) {
        this.permissions = permissions;
    }

}
