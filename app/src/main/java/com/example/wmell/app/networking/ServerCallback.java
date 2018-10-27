package com.example.wmell.app.networking;

import com.example.wmell.app.DAO.Gates;
import com.example.wmell.app.DAO.HistoricalUserList;
import com.example.wmell.app.DAO.Response;
import com.example.wmell.app.DAO.ResponsePermissionsUpdate;

public interface ServerCallback {

    void onSuccess(Gates gates);
    void onSuccess(Response response);
    void onSuccess(HistoricalUserList historicalUserList);
    void onFail(Throwable throwable);

}