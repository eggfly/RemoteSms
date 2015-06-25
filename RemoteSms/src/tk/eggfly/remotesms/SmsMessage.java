package tk.eggfly.remotesms;

import org.json.JSONException;
import org.json.JSONObject;

public class SmsMessage {
    private static final String J_DEST = "dest";
    private static final String J_CONTENT = "content";
    private String mDest;
    private String mContent;

    private SmsMessage() {
    }

    public static SmsMessage fromJson(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            final String dest = obj.getString(J_DEST);
            final String content = obj.getString(J_CONTENT);
            SmsMessage message = new SmsMessage();
            message.mContent = content;
            message.mDest = dest;
            return message;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getDest() {
        return mDest;
    }

    public String getContent() {
        return mContent;
    }

}
