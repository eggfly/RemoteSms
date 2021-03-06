package tk.eggfly.remotesms;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import tk.eggfly.remotesms.DemoMessageReceiver.DemoHandler;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

/**
 * 1、为了打开客户端的日志，便于在开发过程中调试，需要自定义一个Application。
 * 并将自定义的application注册在AndroidManifest.xml文件中
 * 2、为了提高push的注册率，您可以在Application的onCreate中初始化push。你也可以根据需要，在其他地方初始化push。
 * 
 * @author wangkuiwei
 * 
 */
public class DemoApplication extends Application {

    // user your appid the key.
    public static final String APP_ID = "2882303761517350904";
    // user your appid the key.
    public static final String APP_KEY = "5161735064904";

    // 此TAG在adb logcat中检索自己所需要的信息， 只需在命令行终端输入 adb logcat | grep
    public static final String TAG = "RemoteSms";

    private static DemoHandler handler = null;

    @Override
    public void onCreate() {
        super.onCreate();

        // 注册push服务，注册成功后会向DemoMessageReceiver发送广播
        // 可以从DemoMessageReceiver的onCommandResult方法中MiPushCommandMessage对象参数中获取注册信息
        if (shouldInit()) {
            MiPushClient.registerPush(this, APP_ID, APP_KEY);

            TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            final String alias = tm.getDeviceId();
            MiPushClient.setAlias(getApplicationContext(), alias, null);
        }

        LoggerInterface newLogger = new LoggerInterface() {

            @Override
            public void setTag(String tag) {
                // ignore
            }

            @Override
            public void log(String content, Throwable t) {
                Log.d(TAG, content, t);
            }

            @Override
            public void log(String content) {
                Log.d(TAG, content);
            }
        };
        Logger.setLogger(this, newLogger);
        if (handler == null)
            handler = new DemoHandler(getApplicationContext());
    }

    private void getAlias() {
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        final String androidId = Secure.getString(getContentResolver(),
                Secure.ANDROID_ID);
        JSONObject deviceInfo = new JSONObject();
        try {
            deviceInfo.put("product", Build.PRODUCT);
            deviceInfo.put("model", Build.MODEL);
            deviceInfo.put("deviceId", tm.getDeviceId());
            deviceInfo.put("androidId", androidId);
            final String alias = deviceInfo.toString();
            MiPushClient.setAlias(getApplicationContext(), alias, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    public static DemoHandler getHandler() {
        return handler;
    }
}