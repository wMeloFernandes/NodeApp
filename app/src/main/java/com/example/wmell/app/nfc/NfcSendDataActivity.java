package com.example.wmell.app.nfc;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wmell.app.DAO.ResponseNFC;
import com.example.wmell.app.R;
import com.example.wmell.app.main.GateDetails;
import com.example.wmell.app.networking.ApiManager;
import com.example.wmell.app.networking.DigitalKeyApi;
import com.example.wmell.app.networking.ServerCallbackNFC;
import com.skyfishjy.library.RippleBackground;

import retrofit2.Call;
import retrofit2.Callback;

import static com.example.wmell.app.util.Constants.GATE_ID;
import static com.example.wmell.app.util.Constants.USERID_PREFERENCE;
import static com.example.wmell.app.util.Constants.USER_PREFERENCES;

public class NfcSendDataActivity extends AppCompatActivity {


    @Override
    protected void onStop() {
        super.onStop();
        mCountDownTimer.cancel();
    }

    private CountDownTimer mCountDownTimer;

    @Override
    public void onBackPressed() {
        mCountDownTimer.cancel();
        startActivity(new Intent(NfcSendDataActivity.this, GateDetails.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_send_data);


        final RippleBackground rippleBackground = findViewById(R.id.content);
        final TextView timerTextView = findViewById(R.id.tv_timer);
        rippleBackground.startRippleAnimation();

        getNFCToken(new ServerCallbackNFC() {
            @Override
            public void onSuccess(ResponseNFC responseNFC) {
                Log.v("WILLIAN", responseNFC.getMessage());
                Log.v("WILLIAN", String.valueOf(getIntent().getExtras().getInt(GATE_ID)));
            }

            @Override
            public void onFail(Throwable throwable) {
                Log.v("WILLIAN", throwable.getMessage());
            }
        }, getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE).getInt(USERID_PREFERENCE, 0), getIntent().getExtras().getInt(GATE_ID));


        mCountDownTimer = new CountDownTimer(15000, 1000) {

            public void onTick(long millisUntilFinished) {
                timerTextView.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                onBackPressed();
            }
        }.start();


    }

    public void getNFCToken(final ServerCallbackNFC serverCallbackNFC, int user_id, int gate_id) {
        DigitalKeyApi service = ApiManager.getService();

        //Call<ResponseNFC> call = service.makeNfcRequest(getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE).getInt(USERID_PREFERENCE, 0), getIntent().getExtras().getInt(GATE_ID));
        Call<ResponseNFC> call = service.makeNfcRequest(user_id, gate_id);


        call.enqueue(new Callback<ResponseNFC>() {
            @Override
            public void onResponse(Call<com.example.wmell.app.DAO.ResponseNFC> call, retrofit2.Response<ResponseNFC> response) {
                if (response.isSuccessful()) {
                    com.example.wmell.app.DAO.ResponseNFC response1 = response.body();
                    serverCallbackNFC.onSuccess(response1);
                }
            }

            @Override
            public void onFailure(Call<ResponseNFC> call, Throwable t) {
                serverCallbackNFC.onFail(t);
            }
        });
    }


}
