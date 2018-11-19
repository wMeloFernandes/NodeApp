package com.example.wmell.app.nfc;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefRecord;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.example.wmell.app.util.Utils;

import java.math.BigInteger;
import java.nio.charset.Charset;

import static com.example.wmell.app.util.Constants.NDEF_NFC_CHECK_MESSAGE;
import static com.example.wmell.app.util.Constants.NDEF_NFC_GATE_TOKEN;
import static com.example.wmell.app.util.Constants.NDEF_NFC_TOKEN;
import static com.example.wmell.app.util.Constants.NDEF_NFC_USER_TOKEN;


public class HostApduServiceNfc extends HostApduService {


    private static final String TAG = "JDR HostApduServiceNfc";
    private static int SEND_MODE = 0;

    //
    // We use the default AID from the HCE Android documentation
    // https://developer.android.com/guide/topics/connectivity/nfc/hce.html
    //
    // Ala... <aid-filter android:name="F0394148148100" />
    //
    private static final byte[] APDU_SELECT = {
            (byte) 0x00, // CLA	- Class - Class of instruction
            (byte) 0xA4, // INS	- Instruction - Instruction code
            (byte) 0x04, // P1	- Parameter 1 - Instruction parameter 1
            (byte) 0x00, // P2	- Parameter 2 - Instruction parameter 2
            (byte) 0x07, // Lc field	- Number of bytes present in the data field of the command
            (byte) 0xF0, (byte) 0x39, (byte) 0x41, (byte) 0x48, (byte) 0x14, (byte) 0x81, (byte) 0x00, // NDEF Tag Application name
            (byte) 0x00  // Le field	- Maximum number of bytes expected in the data field of the response to the command
    };

    private static final byte[] CAPABILITY_CONTAINER = {
            (byte) 0x00, // CLA	- Class - Class of instruction
            (byte) 0xa4, // INS	- Instruction - Instruction code
            (byte) 0x00, // P1	- Parameter 1 - Instruction parameter 1
            (byte) 0x0c, // P2	- Parameter 2 - Instruction parameter 2
            (byte) 0x02, // Lc field	- Number of bytes present in the data field of the command
            (byte) 0xe1, (byte) 0x03 // file identifier of the CC file
    };

    private static final byte[] READ_CAPABILITY_CONTAINER = {
            (byte) 0x00, // CLA	- Class - Class of instruction
            (byte) 0xb0, // INS	- Instruction - Instruction code
            (byte) 0x00, // P1	- Parameter 1 - Instruction parameter 1
            (byte) 0x00, // P2	- Parameter 2 - Instruction parameter 2
            (byte) 0x01,  // Lc field	- Number of bytes present in the data field of the command
            (byte) 0xe1   //file identifier of the CC file
    };


    // In the scenario that we have done a CC read, the same byte[] match
    // for ReadBinary would trigger and we don't want that in succession
    private boolean READ_CAPABILITY_CONTAINER_CHECK = false;

    private static final byte[] READ_CAPABILITY_CONTAINER_RESPONSE = {
            (byte) 0x00, (byte) 0x0F, // CCLEN length of the CC file
            (byte) 0x20, // Mapping Version 2.0
            (byte) 0x00, (byte) 0x3B, // MLe maximum 59 bytes R-APDU data size
            (byte) 0x00, (byte) 0x34, // MLc maximum 52 bytes C-APDU data size
            (byte) 0x04, // T field of the NDEF File Control TLV
            (byte) 0x06, // L field of the NDEF File Control TLV
            (byte) 0xE1, (byte) 0x04, // File Identifier of NDEF file
            (byte) 0x00, (byte) 0x32, // Maximum NDEF file size of 50 bytes
            (byte) 0x00, // Read access without any security
            (byte) 0x00, // Write access without any security
            (byte) 0x90, (byte) 0x00 // A_OKAY
    };

    private static final byte[] NDEF_SELECT = {
            (byte) 0x00, // CLA	- Class - Class of instruction
            (byte) 0xa4, // Instruction byte (INS) for Select command
            (byte) 0x00, // Parameter byte (P1), select by identifier
            (byte) 0x0c, // Parameter byte (P1), select by identifier
            (byte) 0x02, // Lc field	- Number of bytes present in the data field of the command
            (byte) 0xE1, (byte) 0x04 // file identifier of the NDEF file retrieved from the CC file
    };

    private static final byte[] NDEF_READ_BINARY_NLEN = {
            (byte) 0x00, // Class byte (CLA)
            (byte) 0xb0, // Instruction byte (INS) for ReadBinary command
            (byte) 0x00, (byte) 0x00, // Parameter byte (P1, P2), offset inside the CC file
            (byte) 0x02  // Le field
    };

    private static final byte[] NDEF_READ_BINARY_GET_NDEF = {
            (byte) 0x00, // Class byte (CLA)
            (byte) 0xb0, // Instruction byte (INS) for ReadBinary command
            (byte) 0x00, (byte) 0x00, // Parameter byte (P1, P2), offset inside the CC file
            (byte) 0x0f  //  Le field
    };

    private static final byte[] A_OKAY = {
            (byte) 87,  // SW1	Status byte 1 - Command processing status
            (byte) 73,   // SW2	Status byte 2 - Command processing qualifier
            (byte) 76,    //L
            (byte) 76     //L
    };

    private static final byte[] NO_SIGNAL = {
            (byte) 0x4E,
            (byte) 0x4F
    };

    private static final byte[] NDEF_ID = {
            (byte) 0xE1,
            (byte) 0x04
    };

    private NdefRecord NDEF_URI = new NdefRecord(
            NdefRecord.TNF_WELL_KNOWN,
            NdefRecord.RTD_TEXT,
            NDEF_ID,
            "Hello world!".getBytes(Charset.forName("UTF-8"))
    );
    private byte[] NDEF_URI_BYTES = NDEF_URI.toByteArray();
    private byte[] NDEF_URI_LEN = BigInteger.valueOf(NDEF_URI_BYTES.length).toByteArray();
    private static byte[] NFC_TOKEN_BYTES;
    private static byte[] NFC_FINAL_TOKEN_BYTES;
    private static byte[] NFC_PRIMARY_TOKEN;
    private static byte[] NFC_GATE_ID_TOKEN;
    private String nfcToken;
    private String nfcCheckMessage;
    private String nfcUserToken;
    private String nfcGateToken;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            if (intent.hasExtra(NDEF_NFC_TOKEN)) {
                nfcToken = intent.getExtras().getString(NDEF_NFC_TOKEN);
                nfcCheckMessage = intent.getExtras().getString(NDEF_NFC_CHECK_MESSAGE);
                nfcUserToken = String.valueOf(intent.getExtras().getInt(NDEF_NFC_USER_TOKEN));
                nfcGateToken = String.valueOf(intent.getExtras().getInt(NDEF_NFC_GATE_TOKEN));
            }
            Log.v("WILLIAN", nfcToken);
            SEND_MODE = 1;
        }
        return Service.START_STICKY_COMPATIBILITY;
    }

    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {

        //
        // The following flow is based on Appendix E "Example of Mapping Version 2.0 Command Flow"
        // in the NFC Forum specification
        //
        Log.i(TAG, "processCommandApdu() | incoming commandApdu: " + Utils.bytesToHex(commandApdu));

        //
        // First command: NDEF Tag Application select (Section 5.5.2 in NFC Forum spec)
        //
        if (Utils.isEqual(APDU_SELECT, commandApdu)) {
            if (SEND_MODE == 1) {
                Log.i(TAG, "APDU_SELECT triggered. Our Response: " + Utils.bytesToHex(A_OKAY));
                SEND_MODE = 0;
                if (nfcCheckMessage != null) {
                    NFC_TOKEN_BYTES = nfcCheckMessage.getBytes();
                }
                return NFC_TOKEN_BYTES;
            } else {
                return NO_SIGNAL;
            }
        }

        //
        // Second command: Capability Container select (Section 5.5.3 in NFC Forum spec)
        //
        if (Utils.isEqual(CAPABILITY_CONTAINER, commandApdu)) {
            Log.i(TAG, "CAPABILITY_CONTAINER triggered. Our Response: " + Utils.bytesToHex(A_OKAY));
            if (nfcToken != null) {
                NFC_FINAL_TOKEN_BYTES = nfcToken.getBytes();
                return NFC_FINAL_TOKEN_BYTES;
            } else {
                return NO_SIGNAL;
            }
        }

        //
        // Third command: ReadBinary data from CC file (Section 5.5.4 in NFC Forum spec)
        //
        if (Utils.isEqual(READ_CAPABILITY_CONTAINER, commandApdu)) {
            Log.i(TAG, "READ_CAPABILITY_CONTAINER triggered. Our Response: " + Utils.bytesToHex(READ_CAPABILITY_CONTAINER_RESPONSE));
            NFC_PRIMARY_TOKEN = (nfcUserToken + nfcGateToken).getBytes();
            return NFC_PRIMARY_TOKEN;
        }

        //
        // Fourth command: NDEF Select command (Section 5.5.5 in NFC Forum spec)
        //
        if (Utils.isEqual(NDEF_SELECT, commandApdu)) {
            Log.i(TAG, "NDEF_SELECT triggered. Our Response: " + Utils.bytesToHex(A_OKAY));
            return A_OKAY;
        }

        //
        // Fifth command:  ReadBinary, read NLEN field
        //
        if (Utils.isEqual(NDEF_READ_BINARY_NLEN, commandApdu)) {

            byte[] start = {
                    (byte) 0x00
            };

            // Build our response
            byte[] response = new byte[start.length + NDEF_URI_LEN.length + A_OKAY.length];

            System.arraycopy(start, 0, response, 0, start.length);
            System.arraycopy(NDEF_URI_LEN, 0, response, start.length, NDEF_URI_LEN.length);
            System.arraycopy(A_OKAY, 0, response, start.length + NDEF_URI_LEN.length, A_OKAY.length);

            Log.i(TAG, response.toString());
            Log.i(TAG, "NDEF_READ_BINARY_NLEN triggered. Our Response: " + Utils.bytesToHex(response));

            return response;
        }

        //
        // Sixth command: ReadBinary, get NDEF data
        //
        if (Utils.isEqual(NDEF_READ_BINARY_GET_NDEF, commandApdu)) {
            Log.i(TAG, "processCommandApdu() | NDEF_READ_BINARY_GET_NDEF triggered");

            byte[] start = {
                    (byte) 0x00
            };

            // Build our response
            byte[] response = new byte[start.length + NDEF_URI_LEN.length + NDEF_URI_BYTES.length + A_OKAY.length];

            System.arraycopy(start, 0, response, 0, start.length);
            System.arraycopy(NDEF_URI_LEN, 0, response, start.length, NDEF_URI_LEN.length);
            System.arraycopy(NDEF_URI_BYTES, 0, response, start.length + NDEF_URI_LEN.length, NDEF_URI_BYTES.length);
            System.arraycopy(A_OKAY, 0, response, start.length + NDEF_URI_LEN.length + NDEF_URI_BYTES.length, A_OKAY.length);

            Log.i(TAG, NDEF_URI.toString());
            Log.i(TAG, "NDEF_READ_BINARY_GET_NDEF triggered. Our Response: " + Utils.bytesToHex(response));

            Context context = getApplicationContext();
            CharSequence text = "NDEF text has been sent to the reader!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            READ_CAPABILITY_CONTAINER_CHECK = false;
            return response;
        }

        //
        // We're doing something outside our scope
        //
        Log.wtf(TAG, "processCommandApdu() | I don't know what's going on!!!.");
        return "Can I help you?".getBytes();
    }

    @Override
    public void onDeactivated(int reason) {
        Log.i(TAG, "onDeactivated() Fired! Reason: " + reason);
    }
}
