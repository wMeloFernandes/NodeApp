package com.example.wmell.app.main;

import com.example.wmell.app.R;

import static com.example.wmell.app.util.Constants.GATE_DOOR;
import static com.example.wmell.app.util.Constants.GATE_LAMP;
import static com.example.wmell.app.util.Constants.PERMISSION_DENIED;
import static com.example.wmell.app.util.Constants.PERMISSION_GRANTED;
import static com.example.wmell.app.util.Constants.PERMISSION_REQUESTED;

public class Gate {
    private int mGateAuthorization;
    private String mGateName;
    private int mTypeGate;

    public Gate(int authorization, String gateName, int typeGate) {
        mGateAuthorization = authorization;
        mGateName = gateName;
        mTypeGate = typeGate;
    }

    public void setGateAuthorization(int authorization) {
        mGateAuthorization = authorization;
    }

    public int getAccessStatus() {
        return mGateAuthorization;
    }

    public int getGateAuthorization() {
        return mGateAuthorization;
    }

    public int getTypeGateImage() {
        switch (mTypeGate) {
            case GATE_DOOR:
                return R.drawable.door;
            case GATE_LAMP:
                return 22;
            default:
                return 22;
        }
    }

    public int setIconStatus() {
        int result;
        switch (mGateAuthorization) {
            case PERMISSION_GRANTED:
                result = R.drawable.green_padlock;
                break;
            case PERMISSION_DENIED:
                result = R.drawable.red_padlocl;
                break;
            case PERMISSION_REQUESTED:
                result = R.drawable.yellow_padlock;
                break;
            default:
                result = PERMISSION_DENIED;
        }
        return result;
    }

    public String getGateName() {
        return mGateName;
    }
}
