package com.example.wmell.app.nfc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wmell.app.DAO.User;
import com.example.wmell.app.R;
import com.example.wmell.app.login.LoginApplication;
import com.example.wmell.app.main.GateDetails;
import com.example.wmell.app.networking.ApiManager;
import com.example.wmell.app.networking.DigitalKeyApi;
import com.example.wmell.app.networking.ServerCallbackLogin;
import com.poovam.pinedittextfield.CirclePinField;

import retrofit2.Call;
import retrofit2.Callback;

import static com.example.wmell.app.util.Constants.EMAIL_PREFERENCE;


import static com.example.wmell.app.util.Constants.GATE_DETAILS_INTENT;
import static com.example.wmell.app.util.Constants.GATE_ID;
import static com.example.wmell.app.util.Constants.USER_LOGIN_PREFERENCES;
import static com.example.wmell.app.util.Constants.USER_PREFERENCES;
import static com.example.wmell.app.util.Constants.USER_TRIES;
import static com.example.wmell.app.util.Constants.USER_TRY_PASSWORD;

public class CheckUserPasswordActivity extends AppCompatActivity implements ServerCallbackLogin {

    private TextView mPasswordText;
    private CirclePinField mCirclePinField;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_user_password);
        mPasswordText = findViewById(R.id.tv_password_text);
        mCirclePinField = findViewById(R.id.circleField);
        mProgressBar = findViewById(R.id.pb_checking_password);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        mCirclePinField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 6) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mCirclePinField.getWindowToken(), 0);
                    showProgressBar();

                    isPasswordCorrect(new ServerCallbackLogin() {

                        @Override
                        public void onSuccess(User user) {
                            if (Integer.valueOf(user.getStatus()) == 200) {
                                SharedPreferences sharedPreferences = getSharedPreferences(USER_TRY_PASSWORD, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt(USER_TRIES, 4);
                                editor.commit();
                                Intent intent = new Intent(CheckUserPasswordActivity.this, NfcSendDataActivity.class);
                                intent.putExtra(GATE_ID, getIntent().getExtras().getInt(GATE_ID));
                                startActivityForResult(intent, GATE_DETAILS_INTENT);
                            }
                        }

                        @Override
                        public void onFail(Throwable throwable) {
                            hideProgressBar();
                            mCirclePinField.setText("");
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                            controlUserTry();
                        }
                    }, getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE).getString(EMAIL_PREFERENCE, ""), s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    public void isPasswordCorrect(final ServerCallbackLogin serverCallback, String email, String password) {
        DigitalKeyApi service = ApiManager.getService();

        Call<User> call = service.checkUserAccess(email, password);

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

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
        mPasswordText.setVisibility(View.GONE);
        mCirclePinField.setVisibility(View.GONE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
        mPasswordText.setVisibility(View.VISIBLE);
        mCirclePinField.setVisibility(View.VISIBLE);
    }

    private void controlUserTry() {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_TRY_PASSWORD, Context.MODE_PRIVATE);
        int tryPassword = sharedPreferences.getInt(USER_TRIES, 4);
        --tryPassword;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(USER_TRIES, tryPassword);
        editor.commit();

        if (tryPassword <= 3) {
            if (tryPassword > 0) {
                Toast.makeText(this, "Wrong passoword!\n" + tryPassword + " more attempts!", Toast.LENGTH_SHORT).show();
            } else {
                sharedPreferences = getSharedPreferences(USER_LOGIN_PREFERENCES, Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mCirclePinField.getWindowToken(), 0);
                startActivity(new Intent(CheckUserPasswordActivity.this, LoginApplication.class));
            }
        } else {
            Toast.makeText(CheckUserPasswordActivity.this, "Wrong password!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSuccess(User use) {

    }

    @Override
    public void onFail(Throwable throwable) {

    }
}
