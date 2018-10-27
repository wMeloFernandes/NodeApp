package com.example.wmell.app.DAO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Historical {

    @SerializedName("historical_id")
    @Expose
    private Integer historicalId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("gate_id")
    @Expose
    private Integer gateId;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("gate_name")
    @Expose
    private String gateName;

    public Integer getHistoricalId() {
        return historicalId;
    }

    public void setHistoricalId(Integer historicalId) {
        this.historicalId = historicalId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getGateId() {
        return gateId;
    }

    public void setGateId(Integer gateId) {
        this.gateId = gateId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGateName() {
        return gateName;
    }

    public void setGateName(String gateName) {
        this.gateName = gateName;
    }

}