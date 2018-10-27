package com.example.wmell.app.networking;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiManager {


    public static DigitalKeyApi getService() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(DigitalKeyApi.class);
    }
}
