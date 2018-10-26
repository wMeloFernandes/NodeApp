package com.example.wmell.app.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wmell.app.DAO.Gates;
import com.example.wmell.app.DAO.Response;
import com.example.wmell.app.R;
import com.example.wmell.app.features.ActivateFeatures;
import com.example.wmell.app.main.MainScreen;
import com.example.wmell.app.networking.DigitalKeyApi;
import com.example.wmell.app.networking.ServerCallback;
import com.example.wmell.app.util.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.wmell.app.features.FeaturesName.FINGERPRINT_AUTHENTICATION;
import static com.example.wmell.app.util.Constants.FEATURE_ENABLE;

public class LoginApplication extends AppCompatActivity implements ServerCallback {

    private EditText email;
    private EditText password;
    private String mEmail;
    private String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_application);
        getSupportActionBar().hide();


        email = findViewById(R.id.editText_email);
        password = findViewById(R.id.editText_password);
        Button connectButton = findViewById(R.id.login_button);
        TextView signUp = findViewById(R.id.textView_sign);
        TextView lostPassword = findViewById(R.id.textView_lostPassword);

        signUp.setText(Utils.underlineTextView(getString(R.string.no_account_sign_up)));

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginApplication.this, RegisterUser.class));
            }
        });

        lostPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginApplication.this, RecoverPassword.class));
            }
        });

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isFilled(email.getText().toString()) && Utils.isFilled(password.getText().toString())) {
                    //Activate Features
                    ActivateFeatures.addFeature(FINGERPRINT_AUTHENTICATION, FEATURE_ENABLE);
                    mEmail = email.getText().toString();
                    mPassword = password.getText().toString();

                    checkUserAccess(new ServerCallback() {
                        @Override
                        public void onSuccess(Gates gates) {
                        }

                        @Override
                        public void onSuccess(Response response) {
                            if (Integer.valueOf(response.getStatus()) == 200) {
                                startActivity(new Intent(LoginApplication.this, MainScreen.class));
                            } else if (Integer.valueOf(response.getStatus()) == 404) {
                                Toast.makeText(LoginApplication.this, "E-mail doesn't exist!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginApplication.this, "There is a problem with your account. Contact the administrator!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFail(Throwable throwable) {
                            Toast.makeText(LoginApplication.this, "Connection Error. Try again later!", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(LoginApplication.this, R.style.Theme_AppCompat));
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
    public void onSuccess(Gates gates) {
    }

    @Override
    public void onSuccess(Response response) {
    }

    @Override
    public void onFail(Throwable throwable) {
    }

    public void checkUserAccess(final ServerCallback serverCallback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.101:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DigitalKeyApi service = retrofit.create(DigitalKeyApi.class);

        Call<Response> call = service.checkUserAccess(mEmail, mPassword);

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