package com.example.wmell.app.networking;


import com.example.wmell.app.DAO.ResponseUpdatePermissions;

public interface ServerCallbackUpdatePermissions {

    void onSuccess(ResponseUpdatePermissions response);

    void onFail(Throwable throwable);
}
