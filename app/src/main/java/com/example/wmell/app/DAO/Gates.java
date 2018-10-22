package com.example.wmell.app.DAO;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Gates {
    @SerializedName("gates")
    @Expose
    private List<Gate> gates = null;

    public List<Gate> getGates() {
        return gates;
    }

    public void setGates(List<Gate> gates) {
        this.gates = gates;
    }
}
