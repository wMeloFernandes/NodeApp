package com.example.wmell.app.nfc;

import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.wmell.app.DAO.Response;
import com.example.wmell.app.DAO.ResponseNFC;
import com.example.wmell.app.DAO.User;
import com.example.wmell.app.R;
import com.example.wmell.app.main.GateDetails;
import com.example.wmell.app.networking.ApiManager;
import com.example.wmell.app.networking.DigitalKeyApi;
import com.example.wmell.app.networking.ServerCallbackLogin;
import com.example.wmell.app.networking.ServerCallbackNFC;

import retrofit2.Call;
import retrofit2.Callback;

import static com.example.wmell.app.util.Constants.USERID_PREFERENCE;
import static com.example.wmell.app.util.Constants.USER_PREFERENCES;

public class NfcCallbackMessage extends AppCompatActivity implements ServerCallbackNFC {

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage message = (NdefMessage) rawMessages[0];
            String messageRequest = String.valueOf(message.getRecords()[0].getPayload());


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onSuccess(ResponseNFC responseNFC) {

    }

    @Override
    public void onFail(Throwable throwable) {

    }
}
