package et.maimob.com.et.function;

import android.content.Context;
import android.net.wifi.WifiManager;

/**
 * Created by HJ on 2015/7/8.
 * <p/>
 * wifi开关类
 */
public class SetWiFI {

    /**
     * 开关wifi，已开启则关闭
     */
    public static void setWifi(Context context) {

        WifiManager wifiManager = (WifiManager) context.getSystemService(
                Context.WIFI_SERVICE);

        //判断WiFi是否开启，若未开启则开启wifi
        wifiManager.setWifiEnabled(!wifiManager.isWifiEnabled());
    }

    /**
     * 获取wifi的状态
     *
     * @return
     */
    public static boolean GetWifiEnable(Context context) {

        WifiManager wifiManager = (WifiManager) context.getSystemService(
                Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }
}
