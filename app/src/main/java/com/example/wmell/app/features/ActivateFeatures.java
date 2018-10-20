package com.example.wmell.app.features;

import java.util.HashMap;

import static com.example.wmell.app.util.Constants.FEATURE_DISABLE;
import static com.example.wmell.app.util.Constants.FEATURE_ENABLE;

public class ActivateFeatures {

    //ActivateFeatures names
    public static HashMap<String, Integer> mFeaturesList = new HashMap<>();


    public static void addFeature(String featureName, int value) {
        mFeaturesList.put(featureName, value);
    }

    public static boolean isFeatureEnable(String featureName) {

        switch (mFeaturesList.get(featureName)) {
            case FEATURE_ENABLE:
                return true;
            case FEATURE_DISABLE:
                return false;
            default:
                return false;
        }


    }

}
