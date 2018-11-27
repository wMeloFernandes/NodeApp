package com.example.wmell.app.DAO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GateResponse {

    @SerializedName("gate_id")
    @Expose
    private Integer gateId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("gate_key")
    @Expose
    private String gateKey;
    @SerializedName("last_access")
    @Expose
    private String lastAccess;
    @SerializedName("status")
    @Expose
    private Integer status;

    public Integer getGateId() {
        return gateId;
    }

    public void setGateId(Integer gateId) {
        this.gateId = gateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGateKey() {
        return gateKey;
    }

    public void setGateKey(String gateKey) {
        this.gateKey = gateKey;
    }

    public String getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(String lastAccess) {
        this.lastAccess = lastAccess;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
