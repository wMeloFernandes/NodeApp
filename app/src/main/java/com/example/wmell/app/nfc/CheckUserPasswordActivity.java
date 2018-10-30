package com.example.wmell.app.nfc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;

import com.example.wmell.app.R;
import com.infideap.blockedittext.BlockEditText;

public class CheckUserPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_user_password);

        BlockEditText editText = findViewById(R.id.blockEditText_tac);
        editText.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        Button button = findViewById(R.id.bt_check_password);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CheckUserPasswordActivity.this, NfcSendDataActivity.class));
            }
        });
    }
}
