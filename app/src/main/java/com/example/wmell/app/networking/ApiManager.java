package com.example.wmell.app.networking;

import com.example.wmell.app.DAO.UserModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wmell on 22/10/2018.
 */

public class ApiManager {

    private static IUsersApi service;
    private static ApiManager apiManager;

    private ApiManager() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.101:8080/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        service = retrofit.create(IUsersApi.class);
    }

    public static ApiManager getInstance() {
        if (apiManager == null) {
            apiManager = new ApiManager();
        }
        return apiManager;
    }

    public void createUserModel(UserModel userModel, Callback<UserModel> callback) {
        Call<UserModel> userModelCall = service.createUserModel(userModel);
        userModelCall.enqueue(callback);
    }
}
