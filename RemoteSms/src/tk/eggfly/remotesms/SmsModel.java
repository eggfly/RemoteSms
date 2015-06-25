package tk.eggfly.remotesms;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

public class SmsModel {
    static void sendSms(Context context, String dest, String message) {
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
                new Intent(), 0);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(dest, null, message, sentPI, null);
    }
}
