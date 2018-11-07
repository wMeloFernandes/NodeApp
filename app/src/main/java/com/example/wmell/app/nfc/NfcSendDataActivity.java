package com.example.wmell.app.nfc;

import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import static com.example.wmell.app.util.Constants.SEND_KEY_NFC;
import static com.example.wmell.app.util.Constants.SEND_USER_INFORMATIONS_NFC;
import static com.example.wmell.app.util.Constants.USERID_PREFERENCE;
import static com.example.wmell.app.util.Constants.USER_PREFERENCES;

public class NfcSendDataActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback {

    private int mSendState = SEND_USER_INFORMATIONS_NFC;
    private String mKeyMessage;

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage message = (NdefMessage) rawMessages[0];
            String messageRequest = String.valueOf(message.getRecords()[0].getPayload());
            sendNfcRequesttoServer(new ServerCallbackNFC() {
                @Override
                public void onSuccess(ResponseNFC responseNFC) {
                    if (responseNFC.getResult() == 200) {
                        //TODO SEND ANSWER TO NFC SHIELD
                        mSendState = SEND_KEY_NFC;
                        mKeyMessage = responseNFC.getMessage();
                    }
                }

                @Override
                public void onFail(Throwable throwable) {
                    Toast.makeText(NfcSendDataActivity.this, "Connection problem! Try again later", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(NfcSendDataActivity.this, GateDetails.class));
                }
            }, messageRequest);
        }
        NfcAdapter mAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
        if (mAdapter != null) {
            mAdapter.setNdefPushMessageCallback(this, this);
        }
    }

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

        mCountDownTimer = new CountDownTimer(15000, 1000) {

            public void onTick(long millisUntilFinished) {
                timerTextView.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                onBackPressed();
            }
        }.start();

        NfcAdapter mAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
        if (mAdapter != null) {
            mAdapter.setNdefPushMessageCallback(this, this);
        }

    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        if (mSendState == SEND_KEY_NFC) {
            String message = String.valueOf(SEND_KEY_NFC) + mKeyMessage;
            NdefRecord ndefRecord = NdefRecord.createMime("text/plain", message.getBytes());
            NdefMessage ndefMessage = new NdefMessage(ndefRecord);
            return ndefMessage;
        } else {
            String message = String.valueOf(SEND_USER_INFORMATIONS_NFC) + GateDetails.getGateID() + "/" + getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE).getInt(USERID_PREFERENCE, 0);
            NdefRecord ndefRecord = NdefRecord.createMime("text/plain", message.getBytes());
            NdefMessage ndefMessage = new NdefMessage(ndefRecord);
            return ndefMessage;
        }
    }

    public void sendNfcRequesttoServer(final ServerCallbackNFC serverCallback, String messageRequest) {
        DigitalKeyApi service = ApiManager.getService();

        Call<ResponseNFC> call = service.makeNfcRequest(getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE).getInt(USERID_PREFERENCE, 0), Integer.parseInt(GateDetails.getGateID()), messageRequest);

        call.enqueue(new Callback<ResponseNFC>() {
            @Override
            public void onResponse(Call<ResponseNFC> call, retrofit2.Response<ResponseNFC> response) {
                if (response.isSuccessful()) {
                    ResponseNFC responseNFC = response.body();
                    serverCallback.onSuccess(responseNFC);
                }
            }

            @Override
            public void onFailure(Call<ResponseNFC> call, Throwable t) {
                serverCallback.onFail(t);
            }
        });
    }

}
