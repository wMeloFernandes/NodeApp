package com.example.wmell.app.util;


import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;

import com.example.wmell.app.R;

public class Utils {

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

    public static void getServerPermissionStatus(int permission, Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("Access Request");
        builder.setStyle(new NotificationCompat.BigTextStyle());
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setAutoCancel(true);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);

        if (Constants.SERVER_PERMISSION_GRANTED == permission) {
            builder.setContentText("Your request was approved!");
            builder.setSmallIcon(R.drawable.green_padlock);
        } else if (Constants.SERVER_PERMISSION_DENIED == permission) {
            builder.setContentText("Your request was rejected!");
            builder.setSmallIcon(R.drawable.red_padlocl);
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }
}
