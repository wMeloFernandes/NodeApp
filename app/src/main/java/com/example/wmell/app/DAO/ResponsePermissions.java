package com.example.wmell.app.DAO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponsePermissions {

    @SerializedName("requisition_id")
    @Expose
    private Integer requisitionId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("gate_id")
    @Expose
    private Integer gateId;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("status")
    @Expose
    private Integer status;

    public Integer getRequisitionId() {
        return requisitionId;
    }

    public void setRequisitionId(Integer requisitionId) {
        this.requisitionId = requisitionId;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}