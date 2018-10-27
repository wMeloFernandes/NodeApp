package com.example.wmell.app.networking;

import com.example.wmell.app.DAO.HistoricalUserList;


public interface ServerCallbackHistorical {
    void onSuccess(HistoricalUserList historical);

    void onFail(Throwable throwable);
}
