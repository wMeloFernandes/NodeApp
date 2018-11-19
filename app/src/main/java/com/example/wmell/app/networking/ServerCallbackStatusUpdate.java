package com.example.wmell.app.networking;

import com.example.wmell.app.DAO.Response;

public interface ServerCallbackStatusUpdate {
    void onSuccess(Response response);

    void onFail(Throwable throwable);
}
