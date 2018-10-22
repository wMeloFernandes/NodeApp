package com.example.wmell.app.networking;

import com.example.wmell.app.DAO.Gate;
import com.example.wmell.app.DAO.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by wmell on 22/10/2018.
 */

public interface IUsersApi {

    @FormUrlEncoded
    @POST("/userAccess")
    Call<UserModel> createUserModel(@Body UserModel user);

    @GET("/gatesRequest")
    Call<Gate> getGates();
}
