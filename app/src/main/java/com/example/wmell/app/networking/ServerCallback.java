package com.example.wmell.app.networking;

import com.example.wmell.app.DAO.Gates;
import com.example.wmell.app.DAO.Response;

public interface ServerCallback {

    void onSuccess(Gates gates);
    void onSuccess(Response response);
    void onFail(Throwable throwable);

}