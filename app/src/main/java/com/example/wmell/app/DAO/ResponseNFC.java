package com.example.wmell.app.DAO;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseNFC {

    @SerializedName("result")
    @Expose
    private Integer result;
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}