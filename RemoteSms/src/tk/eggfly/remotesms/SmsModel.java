package tk.eggfly.remotesms;

import java.util.ArrayList;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

public class SmsModel {
    private final static int MAX_LENGTH = 500;

    static void sendSms(Context context, String dest, String text) {
        if (text != null && dest != null) {
            if (text.length() <= MAX_LENGTH) {
//                PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
//                        new Intent(), 0);
                SmsManager smsManager = SmsManager.getDefault();
                ArrayList<String> messageParts = smsManager.divideMessage(text);
                smsManager.sendMultipartTextMessage(dest, null, messageParts,
                        null, null);
                // smsManager.sendTextMessage(dest, null, text, sentPI, null);
            } else {
                Log.e(DemoApplication.TAG,
                        "too long msg, length: " + text.length());
            }
        }
    }
}
