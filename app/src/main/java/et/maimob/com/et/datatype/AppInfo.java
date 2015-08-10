package et.maimob.com.et.datatype;

import android.graphics.drawable.Drawable;

import et.maimob.com.et.util.PingyingUtils;

/**
 * Created by HJ on 2015/7/7.
 * <p/>
 * 程序信息类
 */
public class AppInfo
        implements Comparable<AppInfo> {  // 此接口实现链表排序

    private Drawable icon;
    private String appName;
    private String packageName;
    private boolean isSystemApp;

    public AppInfo(String appName, String packageName, Drawable icon) {
        this.icon = icon;
        this.appName = appName;
        this.packageName = packageName;
    }

    public AppInfo() {
    }

    public AppInfo(String appName, String packageName, Drawable icon, boolean isSystemApp) {
        this.icon = icon;
        this.appName = appName;
        this.packageName = packageName;
        this.isSystemApp = isSystemApp;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isSystemApp() {
        return isSystemApp;
    }

    public void setSystemApp(boolean isSystemApp) {
        this.isSystemApp = isSystemApp;
    }

    @Override
    public int compareTo(AppInfo another) {  //以指定字段进行排序
        return PingyingUtils.cn2FirstSpell(this.appName).compareTo(PingyingUtils.cn2FirstSpell(
                another.getAppName()));
    }
}