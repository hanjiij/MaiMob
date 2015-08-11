package et.maimob.com.et.floatingwindow.floatwindow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

/**
 * Created by jhj_Plus on 2015/8/11.
 */
public class BackToSFWActionReceiver extends BroadcastReceiver {
    private static final String TAG = "BackToSFWActionReceiver";
    public static final String ACTION_BACKTOSFWINTENT="intent.action.backToSFWIntentAction";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (TextUtils.equals(ACTION_BACKTOSFWINTENT, intent.getAction())) {
            FloatWindowMgr.getInstance(context).backToSmallFloatWindow();
        }

    }
}
