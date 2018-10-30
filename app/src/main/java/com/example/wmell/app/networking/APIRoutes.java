package com.example.wmell.app.networking;

import android.os.CountDownTimer;

import static com.example.wmell.app.main.MainActivity.updateUIFetchingData;

/**
 * Created by wmell on 20/10/2018.
 */

public class APIRoutes {

    public static void waitServerResponse() {
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                updateUIFetchingData();
            }
        }.start();
    }
}
