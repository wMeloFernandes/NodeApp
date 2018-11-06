package com.example.wmell.app.nfc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.wmell.app.R;
import com.poovam.pinedittextfield.CirclePinField;

public class CheckUserPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_user_password);

        CirclePinField circlePinField = findViewById(R.id.circleField);
        circlePinField.requestFocus();

        Button button = findViewById(R.id.bt_check_password);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CheckUserPasswordActivity.this, NfcSendDataActivity.class));
            }
        });
    }
}
