package et.maimob.com.et.floatingwindow.floatwindow;

import android.content.Context;
import android.graphics.PixelFormat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import et.maimob.com.et.R;
import et.maimob.com.et.floatingwindow.mainpanel.MainPanel;
import et.maimob.com.et.floatingwindow.searchpanel.AppSearchPanel;


/**
 * Created by jhj_Plus on 2015/7/9.
 * 大悬浮窗
 * 显示桌面操作的大悬浮窗
 */
public class BigFloatWindow implements View.OnTouchListener {
    private static final String TAG = "BigFloatWindow";
    View[] bigFloatWindowViews;
    private boolean isShowing;
    private Context mAppContext;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mBigFloatWindowParams;
    private ViewPager mBigFloatView;
    private ViewGroup rootView;
    private ImageView imgView_page_indicator;
    private ImageView imgView_page_indicator_2;
    //    private ViewGroup rootView;
    //    private ViewGroup mBigFloatView;
    //    private boolean isChangeLayout;
    //    private Button btn_test;
    //    private List<AppInfo> mAppInfos;
    //    private GridView gv_app_info;
    //    private ImageView imgView_mem_acc;
    //    private GridView gv_shortcut_sys_func;
    //    private Integer[] items =
    //            {R.drawable.selector_imgbtn_flashlight, R.drawable.selector_imgbtn_wifi,
    //             R.drawable.selector_imgbtn_flow, R.drawable.floating_main_setting};

    public BigFloatWindow(Context context) {
        initData(context);
    }

    /**
     * 初始化窗口显示数据
     *
     * @param context 全局应用上下文
     */
    private void initData(Context context) {
        mAppContext = context.getApplicationContext();
        mWindowManager = (WindowManager) mAppContext.getSystemService(Context.WINDOW_SERVICE);
        mBigFloatWindowParams = new WindowManager.LayoutParams();

        bigFloatWindowViews = new View[]{new MainPanel(mAppContext).getView(),
                                         new AppSearchPanel(mAppContext).getView()};

        //        mAppInfos = AppInfoLab.getInstance(mAppContext).getAppInfos();
        //
        //        rootView = (ViewGroup) LayoutInflater.from(mAppContext)
        //                                             .inflate(R.layout.fragment_float_window_big_main_panel, null);
        //        mBigFloatView = (ViewGroup) rootView.findViewById(R.id.layout_float_window_big);
        //        gv_app_info = (GridView) rootView.findViewById(R.id.gv_app);
        //        gv_app_info.setAdapter(new AppInfoAdapter(mAppContext, mAppInfos));
        //
        //        imgView_mem_acc= (ImageView) rootView.findViewById(R.id.imgView_mem_acc);
        //
        //        gv_shortcut_sys_func = (GridView) rootView.findViewById(R.id.gv_shortcut_sys_func);
        //        gv_shortcut_sys_func.setAdapter(new SysShortcutFuncGVAdapter(mAppContext, items));
        //
        //        rootView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
        //                         View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        //        rootView.setOnTouchListener(this);


        rootView = (ViewGroup) LayoutInflater.from(mAppContext)
                                             .inflate(R.layout.float_window_big, null);
        rootView.setOnTouchListener(this);
        rootView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                         View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        mBigFloatView = (ViewPager) rootView.findViewById(R.id.vp_float_window_big);
        mBigFloatView.setAdapter(new BigFloatViewPagerAdapter());
        mBigFloatView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position) {
                imgView_page_indicator.setImageResource(
                        position == 0 ? R.mipmap.icon_point_gray : R.mipmap.icon_point_white);
                imgView_page_indicator_2.setImageResource(position==1?R.mipmap.icon_point_gray:R
                        .mipmap.icon_point_white);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        imgView_page_indicator= (ImageView) rootView.findViewById(R.id.imgView_page_indicator);
        imgView_page_indicator_2=(ImageView) rootView.findViewById(R.id.imgView_page_indicator_2);

        int screenWidth = mWindowManager.getDefaultDisplay().getWidth();
        int screenHeight = mWindowManager.getDefaultDisplay().getHeight();
        int bigFloatWindowWidth = mBigFloatView.getMeasuredWidth();
        int bigFloatWindowHeight = mBigFloatView.getMeasuredHeight();

        //TODO 设置可选择的窗口类型，显示在任何窗口之上或只在桌面显示
        mBigFloatWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //TODO 设置按返回键返回小悬浮窗和点击大悬浮窗范围之外时拦截后面的窗口的触摸事件
        mBigFloatWindowParams.flags = WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                                      WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        mBigFloatWindowParams.softInputMode=WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING;
        mBigFloatWindowParams.format = PixelFormat.RGBA_8888;
        mBigFloatWindowParams.alpha = 1.0f;
        mBigFloatWindowParams.gravity = Gravity.START | Gravity.TOP;
        mBigFloatWindowParams.x = screenWidth - bigFloatWindowWidth >> 1;
        mBigFloatWindowParams.y = screenHeight - bigFloatWindowHeight >> 1;
        mBigFloatWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mBigFloatWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Log.i(TAG, "mBigFloatWindow Measured _width------------->" + bigFloatWindowWidth);
        Log.i(TAG, "mBigFloatWindow Measured _height------------->" + bigFloatWindowHeight);

        mBigFloatView.post(new Runnable() {
            @Override
            public void run() {

                Log.i(TAG, "mBigFloatWindow post _width------------->" + mBigFloatView.getWidth());
                Log.i(TAG,
                      "mBigFloatWindow post _height------------->" + mBigFloatView.getHeight());
            }
        });

        //        btn_test = (Button) rootView.findViewById(R.id.btn_test);
        //        btn_test.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                changeLayout();
        //            }
        //        });

    }

    /**
     * 显示大悬浮窗
     */
    public void show() {
        if (!isShowing && mWindowManager != null && rootView != null &&
            mBigFloatWindowParams != null)
        {
            mWindowManager.addView(rootView, mBigFloatWindowParams);
            isShowing = true;
        }
    }

    /**
     * 移除大悬浮窗
     */
    public void removeBigFloatWindow() {
        if (isShowing && mWindowManager != null && rootView != null) {
            mWindowManager.removeViewImmediate(rootView);
            //           sBigFloatWindow = null;
            isShowing = false;
            //isChangeLayout = false;
        }
        //isShowing = true;
    }

    /**
     * 大悬浮窗的触摸事件监听器
     * 监听用户点击大悬浮窗范围之外的触摸事件
     * 点击窗口之外就返回小的悬浮窗
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        FloatWindowMgr floatWindowMgr = FloatWindowMgr
                .getInstance(mAppContext);
        Log.i(TAG, "rootView----Touched-->");
        int eventType = event.getAction();
        switch (eventType) {
            case MotionEvent.ACTION_OUTSIDE:
                Log.i(TAG, "rootView----ACTION_OUTSIDE-->");
                floatWindowMgr.backToSmallFloatWindow();
                break;
            default:
                break;
        }
        return true;
    }
    //    /**
    //     * 更改大悬浮窗面板的布局(两行和九宫格显示)
    //     */
    //    private void changeLayout() {
    //        isChangeLayout = true;
    //        mAppInfos.clear();
    //        for (int i = 0; i < 9; i++) {
    //            if (i == 4) {
    //                AppInfo appInfo = new AppInfo(mAppContext.getResources().getDrawable(
    //                        R.drawable.detail_comment_portrait_manual));
    //                mAppInfos.add(appInfo);
    //                continue;
    //            }
    //            AppInfo appInfo =
    //                    new AppInfo(mAppContext.getResources().getDrawable(R.mipmap.ic_launcher));
    //            mAppInfos.add(appInfo);
    //        }
    //        gv_app_info.setNumColumns(3);
    //        ((AppInfoAdapter) gv_app_info.getAdapter()).notifyDataSetChanged();
    //
    //        RelativeLayout.LayoutParams lp= (RelativeLayout.LayoutParams) gv_app_info.getLayoutParams();
    //        lp.topMargin=100;
    //        imgView_mem_acc.setVisibility(View.GONE);
    //    }

    private class BigFloatViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return bigFloatWindowViews.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView=bigFloatWindowViews[position];
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }


    //    /**
    //     * App信息显示的适配器
    //     * 现只包含App图标
    //     */
    //    private class AppInfoAdapter extends ArrayAdapter<AppInfo> {
    //        public AppInfoAdapter(Context context, List<AppInfo> objects) {
    //            super(context, 0, objects);
    //        }
    //
    //        @Override
    //        public View getView(int position, View convertView, ViewGroup parent) {
    //            if (convertView == null) {
    //                convertView = LayoutInflater.from(mAppContext)
    //                                            .inflate(R.layout.item_app_info, parent, false);
    //            }
    //            ImageView imgView_app_icon =
    //                    (ImageView) convertView.findViewById(R.id.imgView_app_icon);
    //            Drawable drawable = mAppInfos.get(position).getAppIcon();
    //
    //            if (position == 4 && isChangeLayout) {
    //                ViewGroup.LayoutParams lp = imgView_app_icon.getLayoutParams();
    //                lp.width = 150;
    //                lp.height = 150;
    //                imgView_app_icon.setPadding(0, 0, 0, 0);
    //                Log.i(TAG, "width------->" + drawable.getIntrinsicWidth() + "," +
    //                           "height---------->" + drawable.getIntrinsicHeight());
    //                imgView_app_icon.setLayoutParams(lp);
    //            }
    //            imgView_app_icon.setImageDrawable(drawable);
    //            return convertView;
    //        }
    //
    //
    //    }

    //    /**
    //     * 系统功能快捷方式适配器
    //     */
    //    private class SysShortcutFuncGVAdapter extends ArrayAdapter<Integer> {
    //
    //        public SysShortcutFuncGVAdapter(Context context, Integer[] objects) {
    //            super(context, 0, objects);
    //        }
    //
    //        @Override
    //        public View getView(int position, View convertView, ViewGroup parent) {
    //            if (convertView == null) {
    //                convertView = LayoutInflater.from(mAppContext)
    //                                            .inflate(R.layout.item_shortcut_system_function, parent,
    //                                                     false);
    //            }
    //            ToggleButton toggleButton = (ToggleButton) convertView.findViewById(R.id.togBtn);
    //            toggleButton.setBackgroundResource(items[position]);
    //
    //            return convertView;
    //        }
    //    }
}
