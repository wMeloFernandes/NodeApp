package com.example.wmell.app.util;

import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();


    public static SpannableString underlineTextView(String text) {
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        return content;
    }

    public static boolean isFilled(String str1) {
        if (TextUtils.isEmpty(str1)) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean hasMinLenght(String str1, int size) {
        if (str1.length() < size) {
            return false;
        } else {
            return true;
        }
    }


    public static List<Integer> parsePermissions(String permissions) {
        List<String> result = new ArrayList<>();
        if (permissions.isEmpty()) {
            return null;
        } else {
            String[] testando = permissions.split(",");
            result = Arrays.asList(testando);
            ArrayList<Integer> list = new ArrayList<>();
            for (int i = 0; i < testando.length; i++) {
                list.add(Integer.valueOf(testando[i]));
            }
            return list;
        }
    }

    public static String parseTimestampSQLToDate(String timestampSQL) {

        String day = timestampSQL.substring(8, 10);
        String month = timestampSQL.substring(5, 7);
        String year = timestampSQL.substring(0, 4);

        String hour = timestampSQL.substring(11, 16);
        String time = day + "-" + month + "-" + year + " " + hour;
        return time;
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static boolean isEqual(byte[] a, byte[] b) {
        if (a.length != b.length) {
            return false;
        }

        int result = 0;
        for (int i = 0; i < a.length; i++) {
            result |= a[i] ^ b[i];
        }
        return result == 0;
    }
}

