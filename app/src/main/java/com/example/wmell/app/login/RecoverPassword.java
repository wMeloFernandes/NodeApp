package com.example.wmell.app.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wmell.app.DAO.Gates;
import com.example.wmell.app.DAO.HistoricalUserList;
import com.example.wmell.app.DAO.Response;
import com.example.wmell.app.DAO.ResponseRegister;
import com.example.wmell.app.R;
import com.example.wmell.app.main.MainActivity;
import com.example.wmell.app.networking.ApiManager;
import com.example.wmell.app.networking.DigitalKeyApi;
import com.example.wmell.app.networking.ServerCallback;
import com.example.wmell.app.networking.ServerCallbackRegisterRecover;
import com.example.wmell.app.util.Utils;

import retrofit2.Call;
import retrofit2.Callback;

import static com.example.wmell.app.util.Constants.EMAIL_PREFERENCE;
import static com.example.wmell.app.util.Constants.LASTACCESS_PREFERENCE;
import static com.example.wmell.app.util.Constants.USERID_PREFERENCE;
import static com.example.wmell.app.util.Constants.USERNAME_PREFERENCE;
import static com.example.wmell.app.util.Constants.USER_PREFERENCES;

public class RecoverPassword extends AppCompatActivity implements ServerCallbackRegisterRecover {


    private String mUserID;
    private String mEmail;
    private String mPassword;
    private EditText editTextEmail;
    private EditText editTextKeyID;
    private EditText editTextPassword;
    private Button buttonRecover;
    private Button buttonNewPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);


        editTextEmail = findViewById(R.id.editTextRecoverEmail);
        editTextKeyID = findViewById(R.id.editTextRecoverKeyID);
        editTextPassword = findViewById(R.id.editTextRecoverPassword);
        buttonRecover = findViewById(R.id.recover_button);
        buttonNewPassword = findViewById(R.id.save_new_password_button);


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
                                enableUpdatePasswordLayout();
                            } else {
                                Toast.makeText(RecoverPassword.this, "There is a problem with your account. Contact the administrator!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onSuccess(HistoricalUserList historicalUserList) {
                        }

                        @Override
                        public void onFail(Throwable throwable) {
                            Toast.makeText(RecoverPassword.this, "There is a problem with your account. Contact the administrator!", Toast.LENGTH_SHORT).show();
                        }

                    });
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(RecoverPassword.this, R.style.Theme_AppCompat));
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

        buttonNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPassword = editTextPassword.getText().toString();
                updateUserPassword(new ServerCallbackRegisterRecover() {
                    @Override
                    public void onSuccess(ResponseRegister response) {
                        if (Integer.valueOf(response.getStatus()) == 200) {
                            SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(USERNAME_PREFERENCE, response.getUsername());
                            editor.putString(EMAIL_PREFERENCE, response.getEmail());
                            editor.putInt(USERID_PREFERENCE, response.getUserId());
                            editor.putString(LASTACCESS_PREFERENCE, response.getLastAccess());
                            editor.apply();

                            startActivity(new Intent(RecoverPassword.this, MainActivity.class));
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
    public void onSuccess(ResponseRegister responseRegister) {
    }

    @Override
    public void onFail(Throwable throwable) {
    }


    private void checkEmailAndKeyID(final ServerCallback serverCallback) {
        DigitalKeyApi service = ApiManager.getService();

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


    private void updateUserPassword(final ServerCallbackRegisterRecover serverCallback) {
        DigitalKeyApi service = ApiManager.getService();

        Call<ResponseRegister> call = service.updatePassword(mUserID, mPassword);

        call.enqueue(new Callback<ResponseRegister>() {
            @Override
            public void onResponse(Call<com.example.wmell.app.DAO.ResponseRegister> call, retrofit2.Response<ResponseRegister> response) {
                if (response.isSuccessful()) {
                    com.example.wmell.app.DAO.ResponseRegister response1 = response.body();
                    serverCallback.onSuccess(response1);
                }
            }

            @Override
            public void onFailure(Call<com.example.wmell.app.DAO.ResponseRegister> call, Throwable t) {
                serverCallback.onFail(t);
            }
        });
    }

    public void enableUpdatePasswordLayout() {
        Toast.makeText(this, "Enter the new password!", Toast.LENGTH_SHORT).show();
        editTextEmail.setFocusable(false);
        editTextKeyID.setFocusable(false);
        editTextPassword.setVisibility(View.VISIBLE);
        buttonNewPassword.setVisibility(View.VISIBLE);
        buttonRecover.setVisibility(View.GONE);
    }
}