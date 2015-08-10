package et.maimob.com.et.function;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.provider.Settings;

import java.lang.reflect.Method;

/**
 * Created by HJ on 2015/7/6.
 * <p>
 * 设置数据流量开关类
 */
public class SetGprs {

    /**
     * 开启或关闭数据流量
     *已开启则关闭
     * @param context
     */
    public static void Open_And_Close_Gprs(Context context) {

        boolean isGprs=gprsIsOpenMethod(context);

        if (getSDKVersionNumber() < 20) {

            System.out.println(isGprs);

            if (isGprs) {

                toggleMobileData(context, !isGprs);

            } else {

                toggleMobileData(context, !isGprs);

            }
        } else {
            context.startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
        }
    }


    /**
     * 判断数据流量是否开启
     *
     * @return
     */
    public static boolean gprsIsOpenMethod(Context context) {

        ConnectivityManager mCM =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        Class cmClass = mCM.getClass();
        Class[] argClasses = null;
        Object[] argObject = null;

        Boolean isOpen = false;
        try {
            Method method = cmClass.getMethod("getMobileDataEnabled", argClasses);

            isOpen = (Boolean) method.invoke(mCM, argObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isOpen;
    }

    /**
     * 数据流量的开启关闭（仅适用于5.0以下）
     *
     * @param context
     * @param enabled
     */
    private static void toggleMobileData(Context context, boolean enabled) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Method setMobileDataEnabl;
        try {
            setMobileDataEnabl =
                    connectivityManager.getClass().getDeclaredMethod("setMobileDataEnabled",
                            boolean.class);
            setMobileDataEnabl.invoke(connectivityManager, enabled);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取系统的版本号
     *
     * @return
     */
    private static int getSDKVersionNumber() {

        int sdkVersion;
        try {
            sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            sdkVersion = 0;
        }
        return sdkVersion;
    }
}
