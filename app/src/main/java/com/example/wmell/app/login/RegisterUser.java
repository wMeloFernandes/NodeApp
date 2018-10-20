package com.example.wmell.app.login;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wmell.app.R;
import com.example.wmell.app.main.MainScreen;
import com.example.wmell.app.util.Utils;

public class RegisterUser extends AppCompatActivity {

    private EditText mUsername;
    private EditText mPassword;
    private EditText mEmail;
    private Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user);

        mUsername = findViewById(R.id.editTextName);
        mPassword = findViewById(R.id.editTextPassword);
        mEmail = findViewById(R.id.editTextEmail);
        mRegisterButton = findViewById(R.id.register_button);



        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isFilled(mUsername.getText().toString()) && Utils.isFilled(mEmail.getText().toString()) &&
                        Utils.isFilled(mPassword.getText().toString())) {
                    startActivity(new Intent(RegisterUser.this, MainScreen.class));
                } else {
                    Toast.makeText(RegisterUser.this, "Fill all the fields", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
