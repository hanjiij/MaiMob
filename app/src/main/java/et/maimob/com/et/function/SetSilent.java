package et.maimob.com.et.function;

import android.content.Context;
import android.media.AudioManager;

/**
 * Created by HJ on 2015/7/15.
 * 设置静音开关
 */
public class SetSilent {

    /**
     * 获取当前情景模式
     * int RINGER_MODE_SILENT = 0; 静音
     * int RINGER_MODE_VIBRATE = 1; 震动
     * int RINGER_MODE_NORMAL = 2; 普通
     * 只需看静音还是非静音
     *
     * @return
     */
    public static int getRingMode(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getRingerMode();
    }

    /**
     * 设置静音开关
     * 非静音改为静音
     */
    public static void setRingMode(Context context) {

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        if (getRingMode(context) == AudioManager.RINGER_MODE_NORMAL) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        } else {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            audioManager.setStreamVolume(AudioManager.RINGER_MODE_NORMAL, 4, 0);
        }
    }
}