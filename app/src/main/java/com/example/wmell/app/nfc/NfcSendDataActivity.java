package com.example.wmell.app.nfc;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wmell.app.R;
import com.example.wmell.app.main.GateDetails;
import com.skyfishjy.library.RippleBackground;

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

        mCountDownTimer = new CountDownTimer(15000, 1000) {

            public void onTick(long millisUntilFinished) {
                timerTextView.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                onBackPressed();
            }
        }.start();

    }
}
