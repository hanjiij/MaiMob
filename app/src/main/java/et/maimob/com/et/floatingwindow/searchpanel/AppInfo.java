package et.maimob.com.et.floatingwindow.searchpanel;

import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 * Created by jhj_Plus on 2015/7/15.
 * App信息保存类
 * 保存App的图标，名称，版本号，版本名，包的大小，数据大小，缓存大小，总大小，用于启动该App的Intent
 */
public class AppInfo {
    private static final String TAG = "AppInfo";
    /**
     * 图标
     */
    private Drawable mAppIcon;
    /**
     * 名称
     */
    private String mAppName;
    /**
     * 版本名
     */
    private String mAppVersionName;
    /**
     * 包大小
     */
    private long mAppPkgSize;
    /**
     * 应用大小(代码)
     */
    private long mAppCodeSize;
    /**
     * 数据大小
     */
    private long mAppDataSize;
    /**
     * 缓存大小
     */
    private long mAppCacheSize;
    /**
     * 总大小
     */
    private long mAppTotalSize;
    /**
     * 启动该App的Intent
     */
    private Intent mAppLaunchIntent;

    /**
     * 版本号
     */
    private int mAppVersionCode;
    /**
     * 包名
     */
    private String mAppPkgName;


    public Drawable getAppIcon() {
        return mAppIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        mAppIcon = appIcon;
    }

    public String getAppName() {
        return mAppName;
    }

    public void setAppName(String appName) {
        mAppName = appName;
    }

    public String getAppVersionName() {
        return mAppVersionName;
    }

    public void setAppVersionName(String appVersionName) {
        mAppVersionName = appVersionName;
    }

    public long getAppPkgSize() {
        return mAppPkgSize;
    }

    public void setAppPkgSize(long appPkgSize) {
        mAppPkgSize = appPkgSize;
    }

    public long getAppCodeSize() {
        return mAppCodeSize;
    }

    public void setAppCodeSize(long appCodeSize) {
        mAppCodeSize = appCodeSize;
    }

    public long getAppDataSize() {
        return mAppDataSize;
    }

    public void setAppDataSize(long appDataSize) {
        mAppDataSize = appDataSize;
    }

    public long getAppCacheSize() {
        return mAppCacheSize;
    }

    public void setAppCacheSize(long appCacheSize) {
        mAppCacheSize = appCacheSize;
    }

    public long getAppTotalSize() {
        return mAppTotalSize;
    }

    public void setAppTotalSize(long appTotalSize) {
        mAppTotalSize = appTotalSize;
    }

    public int getAppVersionCode() {
        return mAppVersionCode;
    }

    public void setAppVersionCode(int appVersionCode) {
        mAppVersionCode = appVersionCode;
    }

    public String getAppPkgName() {
        return mAppPkgName;
    }

    public void setAppPkgName(String appPkgName) {
        mAppPkgName = appPkgName;
    }

    public Intent getAppLaunchIntent() {
        return mAppLaunchIntent;
    }

    public void setAppLaunchIntent(Intent appLaunchIntent) {
        mAppLaunchIntent = appLaunchIntent;
    }
}
