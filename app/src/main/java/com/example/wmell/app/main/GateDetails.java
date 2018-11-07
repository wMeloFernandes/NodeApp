package com.example.wmell.app.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wmell.app.R;
import com.example.wmell.app.nfc.CheckUserPasswordActivity;
import com.example.wmell.app.util.Utils;

import static com.example.wmell.app.util.Constants.GATE_ID;
import static com.example.wmell.app.util.Constants.GATE_LAST_ACCESS;
import static com.example.wmell.app.util.Constants.GATE_NAME;


public class GateDetails extends AppCompatActivity {

    private TextView mGateDetailsName;
    private TextView mGateDetailsLastAccess;
    private String mGateName;
    private String mgateLastAccess;
    private static int mGateID;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(GateDetails.this, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gate_details);

        mGateDetailsName = findViewById(R.id.textView_name_gate_details);
        mGateDetailsLastAccess = findViewById(R.id.textView_last_access);
        Button buttonPIN = findViewById(R.id.PIN_button);
        mGateDetailsName = findViewById(R.id.textView_name_gate_details);
        mGateDetailsLastAccess = findViewById(R.id.textView_last_access);

        if (getIntent().getExtras() != null) {
            mGateName = getIntent().getExtras().getString(GATE_NAME);
            mgateLastAccess = Utils.parseTimestampSQLToDate(getIntent().getExtras().getString(GATE_LAST_ACCESS));
            mGateID = getIntent().getExtras().getInt(GATE_ID);
        } else {
            mGateName = "";
            mgateLastAccess = "";
        }
        mGateDetailsName.setText(mGateName);
        mGateDetailsLastAccess.setText("Last access to gate: " + mgateLastAccess);
        setResult(RESULT_OK);

        buttonPIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GateDetails.this, CheckUserPasswordActivity.class));
            }
        });
    }

    public static String getGateID() {
        return String.valueOf(mGateID);
    }

}