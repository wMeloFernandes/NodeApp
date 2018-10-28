package com.example.wmell.app.networking;

import com.example.wmell.app.DAO.Permissions;


public interface ServerCallbackPermissionsStatus {
    void onSuccess(Permissions permissions);

    void onFail(Throwable throwable);
}
