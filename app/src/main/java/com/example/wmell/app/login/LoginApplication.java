package com.example.wmell.app.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wmell.app.DAO.User;
import com.example.wmell.app.R;
import com.example.wmell.app.features.ActivateFeatures;
import com.example.wmell.app.main.MainScreen;
import com.example.wmell.app.networking.ApiManager;
import com.example.wmell.app.networking.DigitalKeyApi;
import com.example.wmell.app.networking.ServerCallbackLogin;
import com.example.wmell.app.util.Utils;

import retrofit2.Call;
import retrofit2.Callback;


import static com.example.wmell.app.features.FeaturesName.FINGERPRINT_AUTHENTICATION;
import static com.example.wmell.app.util.Constants.EMAIL_PREFERENCE;
import static com.example.wmell.app.util.Constants.FEATURE_ENABLE;
import static com.example.wmell.app.util.Constants.IS_USER_LOGIN;
import static com.example.wmell.app.util.Constants.LASTACCESS_PREFERENCE;
import static com.example.wmell.app.util.Constants.PERMISSIONS_PREFERENCE;
import static com.example.wmell.app.util.Constants.USERID_PREFERENCE;
import static com.example.wmell.app.util.Constants.USERNAME_PREFERENCE;
import static com.example.wmell.app.util.Constants.USER_LOGIN_PREFERENCES;
import static com.example.wmell.app.util.Constants.USER_PREFERENCES;

public class LoginApplication extends AppCompatActivity implements ServerCallbackLogin {

    private EditText email;
    private EditText password;
    private String mEmail;
    private String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_application);
        getSupportActionBar().hide();

        if (getSharedPreferences(USER_LOGIN_PREFERENCES, Context.MODE_PRIVATE).getBoolean(IS_USER_LOGIN, false)) {
            startActivity(new Intent(LoginApplication.this, MainScreen.class));
        }

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

                    checkUserAccess(new ServerCallbackLogin() {

                        @Override
                        public void onSuccess(User user) {
                            if (Integer.valueOf(user.getStatus()) == 200) {

                                SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(USERNAME_PREFERENCE, user.getUsername());
                                editor.putString(EMAIL_PREFERENCE, user.getEmail());
                                editor.putInt(USERID_PREFERENCE, user.getUserId());
                                editor.putString(LASTACCESS_PREFERENCE, user.getLastAccess());
                                editor.putString(PERMISSIONS_PREFERENCE, user.getPermissions());
                                editor.commit();

                                sharedPreferences = getSharedPreferences(USER_LOGIN_PREFERENCES, Context.MODE_PRIVATE);
                                editor = sharedPreferences.edit();
                                editor.putBoolean(IS_USER_LOGIN, true);
                                editor.commit();

                                startActivity(new Intent(LoginApplication.this, MainScreen.class));
                            } else if (Integer.valueOf(user.getStatus()) == 404) {
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
    public void onSuccess(User use) {

    }

    @Override
    public void onFail(Throwable throwable) {
    }

    public void checkUserAccess(final ServerCallbackLogin serverCallback) {
        DigitalKeyApi service = ApiManager.getService();

        Call<User> call = service.checkUserAccess(mEmail, mPassword);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<com.example.wmell.app.DAO.User> call, retrofit2.Response<User> response) {
                if (response.isSuccessful()) {
                    com.example.wmell.app.DAO.User response1 = response.body();
                    serverCallback.onSuccess(response1);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                serverCallback.onFail(t);
            }
        });
    }
}