package com.example.wmell.app.networking;


import com.example.wmell.app.DAO.ResponseNFC;

public interface ServerCallbackNFC {

    void onSuccess(ResponseNFC responseNFC);
    void onFail(Throwable throwable);

}
