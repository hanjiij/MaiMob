package et.maimob.com.et.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import et.maimob.com.et.database.data.DateUtils;
import et.maimob.com.et.datatype.AppInfo;

/**
 * Created by HJ on 2015/7/7.
 * <p/>
 * 获取程序信息列表类
 */
public class GetAppInfo {

    private PackageManager packageManager;
    private Context context;
    private String packs = "";  // 用于判断程序列表中是否已存在于数据库中

    public GetAppInfo(Context context) {

        this.context = context;
        Init();
    }

    /**
     * 初始化变量
     */
    private void Init() {

        packageManager = context.getPackageManager();
    }

    /**
     * 获取系统中所有应用信息，
     * 并将应用软件信息保存到list列表中。
     *
     * @return 返回系统安装的程序信息
     */
    public List<AppInfo> getAllApps() {

        List<AppInfo> list = new ArrayList<>();

        List<AppInfo> recentlyInfoList = Get_Recently_App_Info(context);  // 获取最近使用过的app

        AppInfo myAppInfo;

        //获取到所有安装了的应用程序的信息，包括那些卸载了的，但没有清除数据的应用程序
        List<PackageInfo> packageInfos =
                packageManager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);

        //获取数据库中所有项的包名，用于匹配
        List<AppInfo> list1= DateUtils.getApps(context);

        for (int i = 0; i < list1.size(); i++) {
            packs=packs+list1.get(i).getPackageName();
        }

        ApplicationInfo appInfo; // 应用程序的信息

        for (PackageInfo info : packageInfos) {
            myAppInfo = new AppInfo();

            //拿到应用程序的信息
            appInfo = info.applicationInfo;
            String packageName = info.packageName;

            if (!filterApp(appInfo) && !packageName.equals(context.getPackageName())) {
                if (!packs.contains(packageName)) {

                    myAppInfo.setPackageName(packageName);
                    myAppInfo.setAppName(appInfo.loadLabel(packageManager).toString());
                    myAppInfo.setIcon(appInfo.loadIcon(packageManager)); //拿到应用程序的图标，并添加
                    list.add(myAppInfo);
                }
            }
        }

        Collections.sort(list);  // 对链表进行按程序首字母排序，汉子变为首字母，英文不变

        int j;
        for (int i = 0; i < recentlyInfoList.size(); i++) {  // 查看最近的app使用，添加到链表的开头
            for (j = 0; j < list.size(); j++) {
                if (recentlyInfoList.get(i).getPackageName().equals(list.get(j).getPackageName())) {
                    break;
                }
            }

            if (j != list.size()) {  // for循环未执行完则表示已存在，移除当前位置并添加到开头

                list.remove(j);
                list.add(i, recentlyInfoList.get(i));
            }
        }

        return list;
    }


    /**
     * 获取最近使用的app信息
     *
     * @return 最近使用的app信息
     */
    public static List<AppInfo> Get_Recently_App_Info(Context context) {

//        editor = sharedPreferences.edit();

        ActivityManager activityManager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);

        PackageManager packageManager = context.getPackageManager();

        List<ActivityManager.RecentTaskInfo> list = new ArrayList<>();
        List<AppInfo> listAppInfo = new ArrayList<>();
        list = activityManager.getRecentTasks(100, 0);

//        int count = 0; // 判断是否最近使用 是否超过8个

        AppInfo appInfo;
        Intent intent;
        ResolveInfo resolveInfo;
        String package_name;
        String app_name;
        for (int i = 0; i < list.size(); i++) {

            appInfo = new AppInfo();

            intent = list.get(i).baseIntent;
            resolveInfo = packageManager.resolveActivity(intent, 0);

            package_name = intent.getComponent().getPackageName();
            app_name = resolveInfo.loadLabel(packageManager).toString();

            intent = packageManager.getLaunchIntentForPackage(package_name);

            if (intent != null &&
                    (app_name.length() < 4 || !app_name.substring(0, 3).equals("com"))/* &&
                    count < 8*/ && !package_name.equals(context.getPackageName())) {

                // 设置appinfo信息
                appInfo.setIcon(resolveInfo.loadIcon(packageManager));
                appInfo.setAppName(app_name);
                appInfo.setPackageName(package_name);

                listAppInfo.add(appInfo);
            }
        }
        return listAppInfo;
    }


    /**
     * 判断某一个应用程序是不是系统的应用程序
     * 如果是返回true，否则返回false。
     */
    public boolean filterApp(ApplicationInfo info) {

        //有些系统应用是可以更新的，如果用户自己下载了一个系统的应用来更新了原来的，它还是系统应用，这个就是判断这种情况的
        if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            return false;
        } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {//判断是不是系统应用
            return false;
        }
        return true;
    }
}
