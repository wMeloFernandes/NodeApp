package com.example.wmell.app.networking;


import com.example.wmell.app.DAO.GateResponseList;

public interface ServerCallbackGatesByStatus {

    void onSuccess(GateResponseList gateResponseList);
    void onFail(Throwable throwable);
}
