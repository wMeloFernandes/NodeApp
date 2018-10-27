package com.example.wmell.app.networking;

import com.example.wmell.app.DAO.Response;
import com.example.wmell.app.DAO.User;

/**
 * Created by wmell on 27/10/2018.
 */

public interface ServerCallbackLogin {

    void onSuccess(User use);

    void onFail(Throwable throwable);
}
