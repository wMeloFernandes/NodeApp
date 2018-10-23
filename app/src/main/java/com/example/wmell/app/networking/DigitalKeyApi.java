package com.example.wmell.app.networking;

import com.example.wmell.app.DAO.Gate;
import com.example.wmell.app.DAO.Gates;
import com.example.wmell.app.DAO.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by wmell on 22/10/2018.
 */

public interface DigitalKeyApi {

    @GET("/gatesRequest")
    Call<Gates> getGates();
}
