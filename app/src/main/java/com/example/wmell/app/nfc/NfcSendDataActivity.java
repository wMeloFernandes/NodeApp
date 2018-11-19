package com.example.wmell.app.nfc;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wmell.app.DAO.Response;
import com.example.wmell.app.DAO.ResponseNFC;
import com.example.wmell.app.R;
import com.example.wmell.app.main.GateDetails;
import com.example.wmell.app.main.MainActivity;
import com.example.wmell.app.networking.ApiManager;
import com.example.wmell.app.networking.DigitalKeyApi;
import com.example.wmell.app.networking.ServerCallbackNFC;
import com.example.wmell.app.networking.ServerCallbackStatusUpdate;
import com.skyfishjy.library.RippleBackground;

import retrofit2.Call;
import retrofit2.Callback;

import static com.example.wmell.app.util.Constants.GATE_ID;
import static com.example.wmell.app.util.Constants.GATE_KEY;
import static com.example.wmell.app.util.Constants.NDEF_NFC_CHECK_MESSAGE;
import static com.example.wmell.app.util.Constants.NDEF_NFC_GATE_TOKEN;
import static com.example.wmell.app.util.Constants.NDEF_NFC_TOKEN;
import static com.example.wmell.app.util.Constants.NDEF_NFC_USER_TOKEN;
import static com.example.wmell.app.util.Constants.USERID_PREFERENCE;
import static com.example.wmell.app.util.Constants.USER_PREFERENCES;

public class NfcSendDataActivity extends AppCompatActivity {

    private static String mNFCToken;
    private static Intent mIntent;
    private static String mGateKey;


    @Override
    protected void onStop() {
        super.onStop();
        // mCountDownTimer.cancel();
    }

    private CountDownTimer mCountDownTimer;

    @Override
    public void onBackPressed() {
        mCountDownTimer.cancel();
        startActivity(new Intent(NfcSendDataActivity.this, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_send_data);
        mGateKey = getIntent().getExtras().getString(GATE_KEY);
        Log.v("WILLIAN", mGateKey);


        final RippleBackground rippleBackground = findViewById(R.id.content);
        final TextView timerTextView = findViewById(R.id.tv_timer);
        rippleBackground.startRippleAnimation();

        updateGateLastAccess(new ServerCallbackStatusUpdate() {
            @Override
            public void onSuccess(Response response) {
                if (response.getStatus().compareTo("200") != 0) {
                    Log.v("WILLIAN", "Error updating last gate access: " + response.getStatus());
                }
            }

            @Override
            public void onFail(Throwable throwable) {
                Log.v("WILLIAN", throwable.getMessage());
            }
        }, getIntent().getExtras().getInt(GATE_ID));


        getNFCToken(new ServerCallbackNFC() {
            @Override
            public void onSuccess(ResponseNFC responseNFC) {
                mNFCToken = responseNFC.getMessage();
                mIntent = new Intent(getApplicationContext(), HostApduServiceNfc.class);
                String firstCheck = mGateKey;
                mIntent.putExtra(NDEF_NFC_TOKEN, mNFCToken);
                mIntent.putExtra(NDEF_NFC_CHECK_MESSAGE, firstCheck);
                mIntent.putExtra(NDEF_NFC_USER_TOKEN, getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE).getInt(USERID_PREFERENCE, 0));
                mIntent.putExtra(NDEF_NFC_GATE_TOKEN, getIntent().getExtras().getInt(GATE_ID));
                Log.v("WILLIAN", mNFCToken);
                startService(mIntent);
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

    public void updateGateLastAccess(final ServerCallbackStatusUpdate serverCallbackStatusUpdate, int gate_id) {
        DigitalKeyApi service = ApiManager.getService();

        Call<Response> call = service.updateGateLastAccess(gate_id);

        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response.isSuccessful()) {
                    Response response1 = response.body();
                    serverCallbackStatusUpdate.onSuccess(response1);
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                serverCallbackStatusUpdate.onFail(t);
            }
        });
    }


}
