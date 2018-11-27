package com.example.wmell.app.login;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.wmell.app.DAO.ResponseRegister;
import com.example.wmell.app.R;
import com.example.wmell.app.main.MainActivity;
import com.example.wmell.app.networking.ApiManager;
import com.example.wmell.app.networking.DigitalKeyApi;
import com.example.wmell.app.networking.ServerCallbackRegisterRecover;
import com.example.wmell.app.util.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.wmell.app.util.Constants.EMAIL_PREFERENCE;
import static com.example.wmell.app.util.Constants.IS_USER_LOGIN;
import static com.example.wmell.app.util.Constants.LASTACCESS_PREFERENCE;
import static com.example.wmell.app.util.Constants.USERID_PREFERENCE;
import static com.example.wmell.app.util.Constants.USERNAME_PREFERENCE;
import static com.example.wmell.app.util.Constants.USER_LOGIN_PREFERENCES;
import static com.example.wmell.app.util.Constants.USER_PREFERENCES;

public class RegisterUser extends AppCompatActivity implements ServerCallbackRegisterRecover {

    private EditText mUsername;
    private EditText mPassword;
    private EditText mEmail;
    private Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user);

        mUsername = findViewById(R.id.editTextName);
        mPassword = findViewById(R.id.editTextPassword);
        mEmail = findViewById(R.id.editTextEmail);
        mRegisterButton = findViewById(R.id.register_button);


        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isFilled(mUsername.getText().toString()) && Utils.isFilled(mEmail.getText().toString()) &&
                        Utils.isFilled(mPassword.getText().toString())) {
                    createNewUser(new ServerCallbackRegisterRecover() {
                        @Override
                        public void onSuccess(ResponseRegister response) {
                            if (Integer.parseInt(response.getStatus()) == 409) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(RegisterUser.this, R.style.Theme_AppCompat));
                                builder.setMessage(getString(R.string.email_already_registered));
                                builder.setPositiveButton(getString(R.string.ok_dialog_alert_text), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        startActivity(new Intent(RegisterUser.this, LoginApplication.class));
                                    }
                                });
                                builder.show();
                            } else if (Integer.parseInt(response.getStatus()) == 200) {
                                SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(USERNAME_PREFERENCE, response.getUsername());
                                editor.putString(EMAIL_PREFERENCE, response.getEmail());
                                editor.putInt(USERID_PREFERENCE, response.getUserId());
                                editor.putString(LASTACCESS_PREFERENCE, response.getLastAccess());
                                editor.apply();

                                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(RegisterUser.this, R.style.Theme_AppCompat));
                                builder.setMessage(getString(R.string.show_user_id) + "\nKEYID: " + response.getUserId());
                                builder.setPositiveButton(getString(R.string.ok_dialog_alert_text), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        sharedPreferences = getSharedPreferences(USER_LOGIN_PREFERENCES, Context.MODE_PRIVATE);
                                        editor = sharedPreferences.edit();
                                        editor.putBoolean(IS_USER_LOGIN, true);
                                        editor.commit();
                                        
                                        startActivity(new Intent(RegisterUser.this, MainActivity.class));
                                        Toast.makeText(RegisterUser.this, "Your account was successful registered!\nWelcome to Digital Key!", Toast.LENGTH_LONG).show();
                                    }
                                });
                                builder.show();
                            } else {
                                Toast.makeText(RegisterUser.this, "Connection Error. Try again later!", Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onFail(Throwable throwable) {

                        }
                    });

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(RegisterUser.this, R.style.Theme_AppCompat));
                    builder.setMessage(getString(R.string.fill_empty_fields));
                    builder.setPositiveButton(getString(R.string.ok_dialog_alert_text), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();
                }

            }
        });
    }


    @Override
    public void onSuccess(ResponseRegister response) {
    }

    @Override
    public void onFail(Throwable throwable) {
    }

    public void createNewUser(final ServerCallbackRegisterRecover serverCallback) {
        DigitalKeyApi service = ApiManager.getService();

        Call<ResponseRegister> call = service.createNewUser(mUsername.getText().toString(), mPassword.getText().toString(), "0101", mEmail.getText().toString());

        call.enqueue(new Callback<ResponseRegister>() {

            @Override
            public void onResponse(Call<ResponseRegister> call, Response<ResponseRegister> response) {
                ResponseRegister responseRegister = response.body();
                serverCallback.onSuccess(responseRegister);
            }

            @Override
            public void onFailure(Call<ResponseRegister> call, Throwable t) {
                serverCallback.onFail(t);
            }
        });
    }

}