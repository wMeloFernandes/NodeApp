package com.example.wmell.app.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wmell.app.DAO.Gates;
import com.example.wmell.app.DAO.Response;
import com.example.wmell.app.R;
import com.example.wmell.app.main.MainScreen;
import com.example.wmell.app.networking.DigitalKeyApi;
import com.example.wmell.app.networking.ServerCallback;
import com.example.wmell.app.util.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecoverPassword extends AppCompatActivity implements ServerCallback {


    private String mUserID;
    private String mEmail;
    private String mPassword;
    private boolean mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);


        final EditText editTextEmail = findViewById(R.id.editTextRecoverEmail);
        final EditText editTextKeyID = findViewById(R.id.editTextRecoverKeyID);
        final EditText editTextPassword = findViewById(R.id.editTextRecoverPassword);
        final Button buttonRecover = findViewById(R.id.recover_button);
        final Button buttonNewPassword = findViewById(R.id.save_new_password_button);


        buttonRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmail = editTextEmail.getText().toString();
                mUserID = editTextKeyID.getText().toString();

                //Verifica nos Servidor as informações e ativa o botão de nova senha
                if (Utils.isFilled(mEmail) && Utils.isFilled(mUserID)) {
                    checkEmailAndKeyID(new ServerCallback() {
                        @Override
                        public void onSuccess(Gates gates) {
                        }

                        @Override
                        public void onSuccess(Response response) {
                            if (Integer.valueOf(response.getStatus()) == 200) {
                                mResult = true;
                            } else {
                                Toast.makeText(RecoverPassword.this, "There is a problem with your account. Contact the administrator!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFail(Throwable throwable) {

                        }
                    });
                } else {
                    mResult = false;
                }

                if (mResult) {
                    editTextEmail.setFocusable(false);
                    editTextKeyID.setFocusable(false);
                    editTextPassword.setVisibility(View.VISIBLE);
                    buttonNewPassword.setVisibility(View.VISIBLE);
                    buttonRecover.setVisibility(View.GONE);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(RecoverPassword.this, R.style.Theme_AppCompat));
                    builder.setMessage(getString(R.string.try_again_alert));
                    builder.setPositiveButton(getString(R.string.ok_dialog_alert_text), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();
                }
            }
        });

        buttonNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPassword = editTextPassword.getText().toString();
                updateUserPassword(new ServerCallback() {
                    @Override
                    public void onSuccess(Gates gates) {
                    }

                    @Override
                    public void onSuccess(Response response) {
                        if (Integer.valueOf(response.getStatus()) == 200) {
                            startActivity(new Intent(RecoverPassword.this, MainScreen.class));
                            Toast.makeText(RecoverPassword.this, "Password changed successful", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(RecoverPassword.this, "Connection Error. Try again later!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFail(Throwable throwable) {
                        Toast.makeText(RecoverPassword.this, "Connection Error. Try again later!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    @Override
    public void onSuccess(Gates gates) {
    }

    @Override
    public void onSuccess(Response response) {
    }

    @Override
    public void onFail(Throwable throwable) {
    }


    private void checkEmailAndKeyID(final ServerCallback serverCallback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.101:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DigitalKeyApi service = retrofit.create(DigitalKeyApi.class);

        Call<Response> call = service.checkIfKeyIDIsValid(mUserID, mEmail);

        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<com.example.wmell.app.DAO.Response> call, retrofit2.Response<Response> response) {
                if (response.isSuccessful()) {
                    com.example.wmell.app.DAO.Response response1 = response.body();
                    serverCallback.onSuccess(response1);
                }
            }

            @Override
            public void onFailure(Call<com.example.wmell.app.DAO.Response> call, Throwable t) {
                serverCallback.onFail(t);
            }
        });
    }


    private void updateUserPassword(final ServerCallback serverCallback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.101:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DigitalKeyApi service = retrofit.create(DigitalKeyApi.class);

        Call<Response> call = service.updatePassword(mUserID, mPassword);

        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<com.example.wmell.app.DAO.Response> call, retrofit2.Response<Response> response) {
                if (response.isSuccessful()) {
                    com.example.wmell.app.DAO.Response response1 = response.body();
                    serverCallback.onSuccess(response1);
                }
            }

            @Override
            public void onFailure(Call<com.example.wmell.app.DAO.Response> call, Throwable t) {
                serverCallback.onFail(t);
            }
        });
    }
}