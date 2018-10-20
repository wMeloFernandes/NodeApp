package com.example.wmell.app.login;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.example.wmell.app.R;

public class RegisterUser extends AppCompatActivity {

    private EditText mUser;
    private EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user);

        //mUser = findViewById(R.id.editText_user_register);
        //mPassword = findViewById(R.id.editText_password_register);
        //Button registerButton = findViewById(R.id.button_register);

//        registerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Utils.isFilled(mUser.getText().toString(), mPassword.getText().toString())) {
//                    Toast.makeText(RegisterUser.this, "User registered!", Toast.LENGTH_SHORT).show();
//                } else {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(RegisterUser.this, R.style.Theme_AppCompat));
//                    builder.setMessage(getString(R.string.fill_empty_fields));
//                    builder.setPositiveButton(getString(R.string.ok_dialog_alert_text), new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//
//                        }
//                    });
//                    builder.show();
//                }
//            }
//        });
    }
}
