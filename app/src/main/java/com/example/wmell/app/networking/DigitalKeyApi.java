package com.example.wmell.app.networking;

import com.example.wmell.app.DAO.Gates;
import com.example.wmell.app.DAO.HistoricalUserList;
import com.example.wmell.app.DAO.Permissions;
import com.example.wmell.app.DAO.Response;
import com.example.wmell.app.DAO.ResponseNFC;
import com.example.wmell.app.DAO.ResponseRegister;
import com.example.wmell.app.DAO.ResponseUpdatePermissions;
import com.example.wmell.app.DAO.User;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by wmell on 22/10/2018.
 */

public interface DigitalKeyApi {

    @GET("/gatesRequest")
    Call<Gates> getGates();

    @POST("/newUser")
    @FormUrlEncoded
    Call<ResponseRegister> createNewUser(@Field("username") String username,
                                         @Field("password") String password,
                                         @Field("permissions") String permissions,
                                         @Field("email") String email);

    @POST("/userAccess")
    @FormUrlEncoded
    Call<User> checkUserAccess(@Field("email") String email,
                               @Field("password") String password);


    @POST("/recoverPassword")
    @FormUrlEncoded
    Call<Response> checkIfKeyIDIsValid(@Field("user_id") String user_id,
                                       @Field("email") String email);


    @POST("/recoverPasswordAfterChecked")
    @FormUrlEncoded
    Call<ResponseRegister> updatePassword(@Field("user_id") String user_id,
                                          @Field("password") String password);

    @POST("/getUserHistorical")
    @FormUrlEncoded
    Call<HistoricalUserList> getUserHistorical(@Field("user_id") int user_id);

    @POST("/getOnHoldPermissions")
    @FormUrlEncoded
    Call<Permissions> getUserPermissions(@Field("user_id") int user_id);

    @POST("/updateUserPermissionsList")
    @FormUrlEncoded
    Call<ResponseUpdatePermissions> updateUserPermissions(@Field("user_id") int user_id);

    @POST("/makeRequestForAccess")
    @FormUrlEncoded
    Call<Response> makeRequestAccess(@Field("user_id") int user_id,
                                     @Field("gate_id") int gate_id,
                                     @Field("gate_name") String gate_name);

    @POST("/getNFCRequest")
    @FormUrlEncoded
    Call<ResponseNFC> makeNfcRequest(@Field("user_id") int user_id,
                                     @Field("gate_id") int gate_id,
                                     @Field("nfc_message") String nfcMessage);

}