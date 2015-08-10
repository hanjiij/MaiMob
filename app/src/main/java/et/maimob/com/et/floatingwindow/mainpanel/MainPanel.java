package et.maimob.com.et.floatingwindow.mainpanel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import et.maimob.com.et.Config;
import et.maimob.com.et.IMainPanelDataChange;
import et.maimob.com.et.R;
import et.maimob.com.et.database.data.DateUtils;
import et.maimob.com.et.datatype.AppInfo;
import et.maimob.com.et.datatype.FunctionInfo;
import et.maimob.com.et.floatingwindow.floatwindow.FloatWindowMgr;
import et.maimob.com.et.function.SetBlueTooth;
import et.maimob.com.et.function.SetFlashLight;
import et.maimob.com.et.function.SetFlightMode;
import et.maimob.com.et.function.SetGprs;
import et.maimob.com.et.function.SetHotSpot;
import et.maimob.com.et.function.SetScreenShot;
import et.maimob.com.et.function.SetSilent;
import et.maimob.com.et.function.SetWiFI;

/**
 * Created by jhj_Plus on 2015/7/14.
 */
public class MainPanel implements IMainPanelDataChange.OnMainPanelDataChangeListener{
    private static final String TAG = "MainPanelFragment";
    private static final String[] PKGNAMES =
            {"game", "music", "adobe", "avast", "earth", "fq", "templerun", "wifi"};
    private static final String[] NAMES =
            {"game", "music", "adobe", "avast", "earth", "fq", "templerun", "wifi"};
    private static final int[] ICONIDS =
            {R.mipmap.ic_game, R.mipmap.ic_music, R.mipmap.icon_adobe_air, R.mipmap.icon_avast,
             R.mipmap.icon_earth, R.mipmap.icon_fq, R.mipmap.icon_templerun, R.mipmap.icon_wifi};
    //    private ViewGroup rootView;
    //    private ViewGroup mBigFloatView;
    private boolean isChangeLayout;
    // private Button btn_test;
    //private List<AppInfo> mAppInfos;
    private GridView gv_app_info;
    private ImageView imgView_mem_acc;
    private GridView gv_shortcut_sys_func;
    private Integer[] items =
            {R.drawable.selector_imgbtn_flashlight, R.drawable.selector_imgbtn_wifi,
             R.drawable.selector_imgbtn_flow, R.drawable.floating_main_setting};
    private Integer[] items2 =
            {R.drawable.selector_imgbtn_flashlight, R.drawable.selector_imgbtn_wifi,
             R.drawable.selector_imgbtn_flow};
    private int[] ids = {0, 1, 2, 3};
    private List<Integer> mIntegers;
    private Context mAppContext;
    private List<FunctionInfo> mFunctionInfos;//######
    private List<AppInfo> mAppInfos;//####

    //private int style=0;

    private SharedPreferences p;

    private AdapterView.OnItemClickListener reguseAppItemClickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(mAppContext, "position=" + position, Toast.LENGTH_SHORT).show();

                    AppInfo appInfo=mAppInfos.get(position);
                    Intent intent=mAppContext.getPackageManager().getLaunchIntentForPackage
                            (appInfo.getPackageName());
                    if (intent!=null) {
                        mAppContext.startActivity(intent);
                        FloatWindowMgr.getInstance(mAppContext).backToSmallFloatWindow();
                    }else {
                        Toast.makeText(mAppContext, "error!!!!", Toast.LENGTH_SHORT).show();
                    }
                }
            };

    private CompoundButton.OnCheckedChangeListener shortcutFuncClickListener =
            new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //fix me id!
                    int id = buttonView.getId();
                    String settingState="";
                    switch (id) {
                        case 1:
                            SetWiFI.setWifi(mAppContext);
                            break;
                        case 2:
                            SetGprs.Open_And_Close_Gprs(mAppContext);
                            break;
                        case 3:
                            int state= SetBlueTooth.setBlueToothCloseAOpen();
                            settingState=state==0?"未知错误":state==2?"已开启":"已关闭";
                            break;
                        case 4:
                            SetScreenShot setScreenShot=SetScreenShot.getInstance(mAppContext);
                            setScreenShot.setScreenShotListener(
                                    new SetScreenShot.ScreenShotListener() {
                                        @Override
                                        public void onScreenShotListener(int status) {
                                            Toast.makeText(mAppContext,
                                                    status == 1 ? "截图成功" : "截图失败",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            setScreenShot.setScreenShot(
                                    Environment.DIRECTORY_DOWNLOADS + System.currentTimeMillis() +
                                            ".jpg");
                            break;
                        case 5:
                            SetFlashLight.setLight();
                            break;
                        case 6:
                            Intent intent=new Intent(Settings.ACTION_SETTINGS);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mAppContext.startActivity(intent);
                            break;
                        case 7:
                            //FIXME
                            break;
                        case 8:
                            int state2= SetHotSpot.setHot(mAppContext);
                            settingState=state2==-1?"未知错误":state2==1?"已开启":"已关闭";
                            break;
                        case 9:
                            SetFlightMode setFlightMode=SetFlightMode.getIntence(mAppContext);
                            setFlightMode.onStatusListener(new SetFlightMode.FlightModeStatus() {
                                @Override
                                public void onStatus(int status) {
                                    Toast.makeText(mAppContext,
                                            status == -1 ?"未知错误": status==1?"已开启":"已关闭",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                            setFlightMode.setAirplaneModeOn();
                            break;
                        case 10:
                            SetSilent.setRingMode(mAppContext);
                            break;
                        case 11:
                            Intent intent2=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mAppContext.startActivity(intent2);
                            break;
                        default:
                            break;
                    }

                    Toast.makeText(mAppContext, mFunctionInfos.get(gv_shortcut_sys_func.getPositionForView(buttonView))
                            .getName()+settingState, Toast.LENGTH_SHORT)
                            .show();
                }
            };

    public MainPanel(Context context) {
        IMainPanelDataChange.getInstance().setOnMainPanelDataChangeListener(this);
        mAppContext = context.getApplicationContext();
        mIntegers = new ArrayList<Integer>();
        mIntegers.addAll(Arrays.asList(items));
        mAppInfos = new ArrayList<AppInfo>();

        p=mAppContext.getSharedPreferences(Config.THEME_LIST_INFO,
                Activity.MODE_PRIVATE);
        //MAPP.setStyleInfo(mAppContext, 1);
        //######
        testReguseAppData();
        //mFunctionInfos = new ArrayList<FunctionInfo>();
        //#####
        testShortcutSettingsData();
    }

    //#####
    private void testReguseAppData() {

//        for (int i = 0; i < 8; i++) {
//            AppInfo appInfo =
//                    new AppInfo(NAMES[i], PKGNAMES[i],
//                                                           mAppContext.getResources()
//                                                                      .getDrawable(ICONIDS[i]));
//            DateUtils.insertApp(mAppContext, i + 1, appInfo);
//        }
        mAppInfos.addAll(DateUtils.getApps(mAppContext));
    }

    //######
    private void testShortcutSettingsData() {

//        MAPP.setStyleInfo(mAppContext, 1);

//        //插入快捷功能数据 4个
//        for (int i = 0; i < 4; i++) {
//            DateUtils.setFunction(mAppContext, i + 1, i);//id bug
//        }
        //获取之前插入的快捷数据
        mFunctionInfos = DateUtils.getFunctions(mAppContext);

        for (FunctionInfo functionInfo : mFunctionInfos) {
            Log.e(TAG,"set_id="+functionInfo.getId()+",set_name="+functionInfo.getName()
                 );
        }
    }

    public View getView() {
        //mAppInfos = AppInfoLab.getInstance(mAppContext).getAppInfos();
        View rootView = LayoutInflater.from(mAppContext)
                                      .inflate(R.layout.fragment_float_window_big_main_panel, null);

        gv_app_info = (GridView) rootView.findViewById(R.id.gv_app);
        gv_app_info.setAdapter(new AppInfoAdapter(mAppContext, mAppInfos));
        gv_app_info.setOnItemClickListener(reguseAppItemClickListener);

        imgView_mem_acc = (ImageView) rootView.findViewById(R.id.imgView_mem_acc);

        gv_shortcut_sys_func = (GridView) rootView.findViewById(R.id.gv_shortcut_sys_func);
        gv_shortcut_sys_func.setAdapter(new SysShortcutFuncGVAdapter(mAppContext, mFunctionInfos));


        Button btn_test = (Button) rootView.findViewById(R.id.btn_test);
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLayout();
            }
        });

        Button button_clear = (Button) rootView.findViewById(R.id.btn_clear);
        button_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntegers.clear();
                ((SysShortcutFuncGVAdapter) gv_shortcut_sys_func.getAdapter())
                        .notifyDataSetChanged();
            }
        });
        Button button_appear = (Button) rootView.findViewById(R.id.btn_appear);
        button_appear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntegers.addAll(Arrays.asList(items2));
                gv_shortcut_sys_func.setNumColumns(3);
                ((SysShortcutFuncGVAdapter) gv_shortcut_sys_func.getAdapter())
                        .notifyDataSetChanged();
            }
        });


        /**
         *  boolean is_style=sharedPreferences.getString(Config.IS_STYLE,"two").equals("two");  // 判断选择的主题
         boolean is_item_two=sharedPreferences.getBoolean(Config.IS_ITEM_TWO, false);   // 判断主题一是否勾选
         boolean is_item_nine=sharedPreferences.getBoolean(Config.IS_ITEM_NINE,false);
         */

        changeReguseAppDisplayStyle(0);
        return rootView;
    }

    /**
     * 更改大悬浮窗面板的布局(两行和九宫格显示)
     */
    private void changeLayout() {

        isChangeLayout = true;
        mAppInfos.add(4, new AppInfo("head", "head", mAppContext.getResources().getDrawable(
                R.drawable.detail_comment_portrait_manual)));
        //        List<AppInfo> appsInfo = DbHelper.getAppsInfo(mAppContext);
        //        mAppInfos.clear();
        //        mAppInfos.addAll(appsInfo);

        //        mAppInfos.add(4, new AppInfo("head", "head", BitmapFactory
        //                .decodeResource(mAppContext.getResources(),
        //                                R.drawable.detail_comment_portrait_manual)));

        gv_app_info.setNumColumns(3);
        ((AppInfoAdapter) gv_app_info.getAdapter()).notifyDataSetChanged();
        RelativeLayout.LayoutParams lp =
                (RelativeLayout.LayoutParams) gv_app_info.getLayoutParams();
        lp.topMargin = 100;
        imgView_mem_acc.setVisibility(View.GONE);
    }

    @Override
    public void onMainPanelStyleChanged(int style, boolean isShowShortcutSettings) {
        changeReguseAppDisplayStyle(style);
        gv_shortcut_sys_func.setVisibility(isShowShortcutSettings?View.VISIBLE:View.GONE);
    }

    @Override
    public void onMainPanelReguseAppChanged(List<AppInfo> newAppInfos) {
        mAppInfos.clear();
        mAppInfos.addAll(newAppInfos);
        ((AppInfoAdapter) gv_app_info.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onMainPanelShortcutSettingsChanged(List<FunctionInfo> newFunctionInfos) {
        mFunctionInfos.clear();
        mFunctionInfos.addAll(newFunctionInfos);
        ((SysShortcutFuncGVAdapter)gv_shortcut_sys_func.getAdapter()).notifyDataSetChanged();
    }
    /**
     * App信息显示的适配器
     * 现只包含App图标
     */
    private class AppInfoAdapter extends ArrayAdapter<AppInfo> {
        public AppInfoAdapter(Context context, List<AppInfo> objects) {
            super(context, 0, objects);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mAppContext)
                                            .inflate(R.layout.item_app_info, parent, false);
            }

            AppInfo appInfo = mAppInfos.get(position);
            ImageView imgView_app_icon =
                    (ImageView) convertView.findViewById(R.id.imgView_app_icon);
            Drawable icon = appInfo.getIcon();

            if (position == 4 && isChangeLayout) {
                ViewGroup.LayoutParams lp = imgView_app_icon.getLayoutParams();
                lp.width = 150;
                lp.height = 150;
                imgView_app_icon.setPadding(0, 0, 0, 0);
                //                Log.i(TAG, "width------->" + icon.getIntrinsicWidth() + "," +
                //                           "height---------->" + icon.getIntrinsicHeight());
                imgView_app_icon.setLayoutParams(lp);
            }
            imgView_app_icon.setImageDrawable(icon);

            TextView tv_app_name = (TextView) convertView.findViewById(R.id.txtView_app_name);
            tv_app_name.setText(position == 4 && isChangeLayout?"":appInfo.getAppName());

            return convertView;
        }


    }

    /**
     * 更改大悬浮窗面板的布局(两行和九宫格显示)
     */
    private void changeReguseAppDisplayStyle(int style) {
        switch (style) {
            case 0:
                if (isChangeLayout) {
                    mAppInfos.remove(4);
                }
                break;
            case 1:
                mAppInfos.add(4, new AppInfo("head", "head", mAppContext.getResources().getDrawable(
                        R.drawable.detail_comment_portrait_manual)));
                break;
        }
        gv_app_info.setNumColumns(style == 0 ? 4 : 3);
        RelativeLayout.LayoutParams lp =
                (RelativeLayout.LayoutParams) gv_app_info.getLayoutParams();
        lp.topMargin = style == 0 ? 120 : 50;
        imgView_mem_acc.setVisibility(style == 0 ? View.VISIBLE : View.GONE);
        isChangeLayout = style == 1;
        ((AppInfoAdapter) gv_app_info.getAdapter()).notifyDataSetChanged();
    }

    /**
     * 系统功能快捷方式适配器
     */
    private class SysShortcutFuncGVAdapter extends ArrayAdapter<FunctionInfo> {

        public SysShortcutFuncGVAdapter(Context context, List<FunctionInfo> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //FIXME
            View root = LayoutInflater.from(mAppContext)
                                      .inflate(R.layout.item_shortcut_system_function, parent,
                                               false);

            FunctionInfo info = mFunctionInfos.get(position);
            ToggleButton toggleButton = (ToggleButton) root.findViewById(R.id.togBtn);
            toggleButton.setId(info.getId()+1);
            initShortcutSettingsState(info.getId(),toggleButton);
            toggleButton.setOnCheckedChangeListener(shortcutFuncClickListener);


            StateListDrawable stateListDrawable=new StateListDrawable();
            Drawable drawable=mAppContext.getResources().getDrawable(R.drawable.earth);
            stateListDrawable.addState(new int[]{android.R.attr.state_checked},drawable);
            stateListDrawable.addState(new int[]{},info.getIco());

            toggleButton.setBackgroundDrawable(stateListDrawable);
            return root;
        }
    }
    private void initShortcutSettingsState(int id,ToggleButton btn) {
        boolean isOpen=false;
        switch (id) {
            case 1:
                isOpen=SetWiFI.GetWifiEnable(mAppContext);
                break;
            case 2:
                isOpen=SetGprs.gprsIsOpenMethod(mAppContext);
                break;
            case 3:
                isOpen=SetBlueTooth.getBlueToothStatu()==1;
                break;
            case 4:

                break;
            case 5:

                break;
            case 6:

                break;
            case 7:
                //FIXME
                break;
            case 8:
                isOpen=SetHotSpot.getWifiApState(mAppContext)==13;
                break;
            case 9:
                isOpen=SetFlightMode.getIntence(mAppContext).getAirplaneMode();
                break;
            case 10:
                isOpen=SetSilent.getRingMode(mAppContext)==0;
                break;
            case 11:

                break;
        }
        btn.setChecked(isOpen);
    }
}
