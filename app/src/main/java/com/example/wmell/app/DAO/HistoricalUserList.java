package com.example.wmell.app.DAO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HistoricalUserList {

    @SerializedName("historical")
    @Expose
    private List<Historical> historical = null;

    public List<Historical> getHistorical() {
        return historical;
    }

    public void setHistorical(List<Historical> historical) {
        this.historical = historical;
    }

}