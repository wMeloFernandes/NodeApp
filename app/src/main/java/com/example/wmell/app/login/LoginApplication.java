package com.example.wmell.app.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wmell.app.R;
import com.example.wmell.app.features.ActivateFeatures;
import com.example.wmell.app.main.MainScreen;
import com.example.wmell.app.util.Utils;

import static com.example.wmell.app.features.FeaturesName.FINGERPRINT_AUTHENTICATION;
import static com.example.wmell.app.util.Constants.FEATURE_DISABLE;
import static com.example.wmell.app.util.Constants.FEATURE_ENABLE;

public class LoginApplication extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_application);
        getSupportActionBar().hide();


        final EditText user = findViewById(R.id.editText_email);
        final EditText password = findViewById(R.id.editText_password);
        Button connectButton = findViewById(R.id.login_button);
        TextView signUp = findViewById(R.id.textView_sign);
        TextView lostPassword = findViewById(R.id.textView_lostPassword);

        signUp.setText(Utils.underlineTextView(getString(R.string.no_account_sign_up)));

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginApplication.this, RegisterUser.class));
            }
        });

        lostPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginApplication.this, RecoverPassword.class));
            }
        });

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isFilled(user.getText().toString()) && Utils.isFilled(password.getText().toString())) {
                    //Activate Features
                    ActivateFeatures.addFeature(FINGERPRINT_AUTHENTICATION, FEATURE_ENABLE);

                    startActivity(new Intent(LoginApplication.this, MainScreen.class));
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(LoginApplication.this, R.style.Theme_AppCompat));
                    builder.setMessage(getString(R.string.fill_empty_fields));
                    builder.setPositiveButton(getString(R.string.ok_dialog_alert_text), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();
                }
            }
        });
    }

}
