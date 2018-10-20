package com.example.wmell.app.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wmell.app.R;
import com.example.wmell.app.main.MainScreen;
import com.example.wmell.app.util.Utils;

public class RecoverPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);


        final EditText editTextEmail = findViewById(R.id.editTextRecoverEmail);
        final EditText editTextKeyID = findViewById(R.id.editTextRecoverKeyID);
        final EditText editTextPassword = findViewById(R.id.editTextRecoverPassword);
        final Button buttonRecover = findViewById(R.id.recover_button);
        final Button buttonNewPassword = findViewById(R.id.save_new_password_button);


        buttonRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String KeyID = editTextKeyID.getText().toString();

                //Verifica nos Servidor as informações e ativa o botão de nova senha
                boolean result;
                if (Utils.isFilled(email)) {
                    result = true;
                } else {
                    result = false;
                }

                if (result) {
                    editTextEmail.setFocusable(false);
                    editTextKeyID.setFocusable(false);
                    editTextPassword.setVisibility(View.VISIBLE);
                    buttonNewPassword.setVisibility(View.VISIBLE);
                    buttonRecover.setVisibility(View.GONE);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(RecoverPassword.this, R.style.Theme_AppCompat));
                    builder.setMessage(getString(R.string.try_again_alert));
                    builder.setPositiveButton(getString(R.string.ok_dialog_alert_text), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();
                }
            }
        });

        buttonNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = editTextPassword.getText().toString();
                //Passa para o servidor a nova senha

                startActivity(new Intent(RecoverPassword.this, MainScreen.class));
            }
        });


    }
}
