package com.example.wmell.app.DAO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class GateResponseList {
    @SerializedName("result")
    @Expose
    private List<GateResponse> result = null;

    public List<GateResponse> getResult() {
        return result;
    }

    public void setResult(List<GateResponse> result) {
        this.result = result;
    }
}
