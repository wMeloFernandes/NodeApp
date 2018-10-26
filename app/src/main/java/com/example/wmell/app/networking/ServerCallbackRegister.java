package com.example.wmell.app.networking;

import com.example.wmell.app.DAO.ResponseRegister;


public interface ServerCallbackRegister {
    void onSuccess(ResponseRegister responseRegister);
    void onFail(Throwable throwable);
}
