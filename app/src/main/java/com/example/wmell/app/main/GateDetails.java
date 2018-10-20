package com.example.wmell.app.main;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyProperties;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wmell.app.R;
import com.example.wmell.app.features.ActivateFeatures;
import com.example.wmell.app.features.FeaturesName;

import java.security.KeyStore;

import javax.crypto.KeyGenerator;


public class GateDetails extends AppCompatActivity {


    private FingerprintManager fingerprintManager;
    private FingerprintManager.CryptoObject cryptoObject;
    private KeyguardManager keyguardManager;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gate_details);

        Button buttonPIN = findViewById(R.id.PIN_button);
        TextView textViewFingerPrint = findViewById(R.id.textView_NFC);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
                fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
                if (fingerprintManager.hasEnrolledFingerprints()) {
                    try {
                        generateKey();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }

        }

        if (ActivateFeatures.isFeatureEnable(FeaturesName.FINGERPRINT_AUTHENTICATION)) {
            textViewFingerPrint.setVisibility(View.VISIBLE);
        } else {
            textViewFingerPrint.setVisibility(View.GONE);
        }

        buttonPIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GateDetails.this, "Working!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateKey() throws Exception {
        keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        keyStore.load(null);

    }
}