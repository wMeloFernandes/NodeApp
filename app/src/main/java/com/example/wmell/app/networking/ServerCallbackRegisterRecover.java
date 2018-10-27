package com.example.wmell.app.networking;

import com.example.wmell.app.DAO.ResponseRegister;


public interface ServerCallbackRegisterRecover {
    void onSuccess(ResponseRegister responseRegister);
    void onFail(Throwable throwable);
}
