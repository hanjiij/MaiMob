package et.maimob.com.et;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import et.maimob.com.et.activitys.About_App_aty;
import et.maimob.com.et.activitys.My_Theme_aty;
import et.maimob.com.et.activitys.Set_App_aty;
import et.maimob.com.et.activitys.Theme_aty;
import et.maimob.com.et.activitys.appset.Set_Usual_App_aty;
import et.maimob.com.et.database.data.DateUtils;
import et.maimob.com.et.database.data.MAPP;
import et.maimob.com.et.datatype.AppInfo;
import et.maimob.com.et.floatingwindow.floatwindow.FloatWindowMgr;
import et.maimob.com.et.util.GetAppInfo;


public class MainActivity
        extends ActionBarActivity {

    // 测试
    private LinearLayout theme_set_bt, app_set_bt, my_theme_set_bt, about_app_bt;
    private Handler handler;  // 延迟启动悬浮窗，防止开启软解等待时间长

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        InitDataBase();

        init();

        setListener();

        if (gotoPermissionSettings(MainActivity.this)) {
            Toast.makeText(MainActivity.this, "请打开悬浮窗权限", Toast.LENGTH_SHORT).show();
        }

        inithandler();
    }

    private void inithandler() {

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (msg.what == 1) {

                    if (DateUtils
                            .getSharedPreference(MainActivity.this, Config.IS_FLOATING_WINDOW) !=
                            0) {
                        FloatWindowMgr.getInstance(MainActivity.this).startFloatWindowService();
                    } else {
                        FloatWindowMgr.getInstance(MainActivity.this).stopFloatWindowService();
                    }
                }

            }
        };
    }


    private void InitDataBase() {

        // 判断是否为第一次启动应用
        boolean is_first =
                DateUtils.getSharedPreference(MainActivity.this, Config.IS_FIRST_START) == 0;

        if (is_first) {

            DateUtils.setSharedPreference(MainActivity.this, Config.IS_FLOATING_WINDOW, 1);

            MAPP.setStyleInfo(MainActivity.this, 1);

            DateUtils.setSharedPreference(MainActivity.this, Config.IS_TWO_OPEN, 1);

            DateUtils.setallfun(MainActivity.this);


            // 判断是否为第一次打开软件，若是则向表中添加最近使用的app
            List<AppInfo> recentlyListInfo = GetAppInfo.Get_Recently_App_Info(MainActivity.this);

            if (recentlyListInfo.size() == 0) {

                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

                alert.setMessage("还未设置常用App，点击设置");

                alert.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        startActivity(new Intent(MainActivity.this, Set_Usual_App_aty.class));
                    }
                });
                alert.show();
            }

            // 向数据库中加入最近使用过的app，最大添加8个
            for (int i = 0; i < (recentlyListInfo.size() > 8 ? 8 : recentlyListInfo.size());
                 i++) {
                AppInfo appInfo = recentlyListInfo.get(i);

//                Util_MyDataBase.InsertDataBase(Set_Usual_App_aty.this, appInfo);// 向数据库中插入数据

                DateUtils.insertApp(MainActivity.this, i + 1, appInfo);
            }

            // 初始化常用功能，默认添加前四个
            for (int i = 0; i < 4; i++) {
                DateUtils.upDateFunc(MainActivity.this, i + 1, i);
            }

            DateUtils.setSharedPreference(MainActivity.this, Config.IS_FIRST_START, 1);
        }
    }


    private void init() {

        theme_set_bt = (LinearLayout) findViewById(R.id.theme_set_bt);
        app_set_bt = (LinearLayout) findViewById(R.id.app_set_bt);
        my_theme_set_bt = (LinearLayout) findViewById(R.id.My_theme_set_bt);
        about_app_bt = (LinearLayout) findViewById(R.id.about_app);

        /*if (DateUtils.getSharedPreference(MainActivity.this, Config.IS_FLOATING_WINDOW) != 0) {

//            FloatWindowMgr.getInstance(MainActivity.this)
//                    .stopFloatWindowService();
            FloatWindowMgr.getInstance(MainActivity.this)
                    .startFloatWindowService();
        }*//*else {

            FloatWindowMgr.getInstance(MainActivity.this)
                    .startFloatWindowService();
        }*/


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void setListener() {

        theme_set_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Theme_aty.class));
            }
        });

        my_theme_set_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, My_Theme_aty.class));
            }
        });

        app_set_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, Set_App_aty.class));
            }
        });


        about_app_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, About_App_aty.class));
            }
        });
    }


    /**
     * @param context 传入app 或者 activity context，通过context获取应用packegename，之后通过packegename跳转制定应用
     * @return 是否是miui
     */
    public static boolean gotoPermissionSettings(Context context) {
        String miui = getSystemProperty();
        boolean isMiui = false;
        if (miui.equals("V6")) {
            Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            try {
                intent.setClassName("com.miui.securitycenter",
                        "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                intent.putExtra("extra_pkgname", context.getPackageName());
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
            }
            isMiui = true;
        } else if (miui.equals("V5")) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
            intent.setData(uri);
            context.startActivity(intent);
            isMiui = true;
        } else {
            isMiui = false;
        }
        return isMiui;
    }

    public static String getSystemProperty() {
        String line = null;
        BufferedReader reader = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop ro.miui.ui.version.name");
            reader = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = reader.readLine();
            return line;
        } catch (IOException e) {
        }
        return "UNKNOWN";
    }
}
