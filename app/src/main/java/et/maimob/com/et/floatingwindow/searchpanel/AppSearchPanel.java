package et.maimob.com.et.floatingwindow.searchpanel;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.net.Uri;
import android.os.Build;
import android.os.RemoteException;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ListView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import et.maimob.com.et.R;
import et.maimob.com.et.floatingwindow.AppStateReceiver;
import et.maimob.com.et.floatingwindow.floatwindow.FloatWindowMgr;

/**
 * Created by jhj_Plus on 2015/7/6.
 * App查询功能
 */
public class AppSearchPanel {
    private static final String TAG = "AppSearchPanel";

    private static final String ACTION_APP_INSTALLED = Intent.ACTION_PACKAGE_ADDED;

    private static final String ACTION_APP_DOWNLOAD_COMPLETE= DownloadManager
            .ACTION_DOWNLOAD_COMPLETE;
    private static final String ACTION_APP_NOTIFICATION_CLICKED =
            DownloadManager.ACTION_NOTIFICATION_CLICKED;
    //public static final String ACTION_APP_DOWNLOADED = "app.intent.action.APP_DOWNLOADED";
    private AppStateReceiver mAppStateReceiver;
    /**
     * 过滤后的App信息列表视图
     */
    private static ListView lv_filtered_app;
    /**
     * 输入几个字符就开始过滤
     */
    private static int mThreshold = 0;
    /**
     * 全局应用上下文
     */
    private Context mAppContext;
    /**
     * 所有App信息列表
     */
    private List<AppInfo> mAppInfos;
    /**
     * 关键字查询输入框
     */
    private SearchEditText et_app_search;
    /**
     * 指定关键字查询按钮
     */
    private Button btn_app_search;

    private InputMethodManager imm;

    /**
     * 过滤后的App点击启动监听器
     */
    private AdapterView.OnItemClickListener filteredAppsListItemClickListener =
            new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent launchIntent = mAppInfos.get(position).getAppLaunchIntent();
                    if (launchIntent != null) {
                        mAppContext.startActivity(launchIntent);
                        FloatWindowMgr.getInstance(mAppContext).backToSmallFloatWindow();
                    }
                }
            };


    public AppSearchPanel(Context context) {
        mAppContext = context.getApplicationContext();
        init();
    }

    /**
     * 判断输入的关键字符是否符合指定查询条件
     *
     * @param text 查询关键字
     * @return true符合反之不符合
     */
    private static boolean enoughToFilter(CharSequence text) {
        return text.length() >= mThreshold;
    }

    /**
     * 插叙指定的关键字App信息
     *
     * @param specifiedConstraint 指定的搜索关键字
     */
    public static void searchSpecifiedConstraint(String specifiedConstraint) {
        Filter specifiedFilter = ((AppFilterAdapter) lv_filtered_app.getAdapter()).obtainFilter();
        if (enoughToFilter(specifiedConstraint) && specifiedFilter != null) {
            specifiedFilter.filter(specifiedConstraint);
        }
    }

    /**
     * 开始查询
     *
     * @param text 搜索关键字
     */
    public static void performFiltering(CharSequence text) {
        Filter filter = ((AppFilterAdapter) lv_filtered_app.getAdapter()).getFilter();
        if (enoughToFilter(text) && filter != null) {
            filter.filter(text);
        }
    }

    //{"zhi fu bao", "bai du di tu", "jin ri tou tiao", "bai du shu ru fa"}

    private void init() {

        IntentFilter appStateIF = new IntentFilter();
        appStateIF.addAction(ACTION_APP_INSTALLED);
        //appStateIF.addAction(ACTION_APP_DOWNLOADED);
        appStateIF.addAction(ACTION_APP_DOWNLOAD_COMPLETE);
        appStateIF.addAction(ACTION_APP_NOTIFICATION_CLICKED);
        //appStateIF.addDataScheme("package");
        mAppStateReceiver = AppStateReceiver.getInstance(mAppContext);
        //注册App状态广播接收器
        mAppContext.registerReceiver(mAppStateReceiver, appStateIF);

        PackageManager mPm = mAppContext.getPackageManager();
        imm = (InputMethodManager) mAppContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        mAppInfos = new ArrayList<AppInfo>();

        List<PackageInfo> packageInfos = mAppContext.getPackageManager().getInstalledPackages(
                PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        mAppInfos.clear();
        for (PackageInfo packageInfo : packageInfos) {
            
            AppInfo appInfo = new AppInfo();
            appInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(mPm));
            appInfo.setAppName(packageInfo.applicationInfo.loadLabel(mPm).toString());
            appInfo.setAppVersionName(packageInfo.versionName);
            appInfo.setAppVersionCode(packageInfo.versionCode);
            appInfo.setAppPkgName(packageInfo.packageName);
            appInfo.setAppLaunchIntent(mPm.getLaunchIntentForPackage(packageInfo.packageName));
            setAppPkgSizeInfo(packageInfo.packageName, appInfo);
            mAppInfos.add(appInfo);
        }
        //new GetAppPkgStatsTask().execute(mAppInfos);
    }

    public View getView() {

        View root = LayoutInflater.from(mAppContext).inflate(R.layout.fragment_app_search, null);

        et_app_search = (SearchEditText) root.findViewById(R.id.et_app_search);
        et_app_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                Log.i(TAG, "beforeTextChanged------------>" + s.toString().trim() + " ," +
                           "length----->" + s.toString().trim().length());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                Log.i(TAG, "onTextChanged------------>" + s.toString().trim() + " ," +
                           "length----->" + s.toString().trim().length());
            }

            @Override
            public void afterTextChanged(Editable s) {
                performFiltering(s.toString().trim());
                Log.i(TAG, "afterTextChanged------------>" + s.toString().trim() + " ," +
                           "length----->" + s.toString().trim().length());
            }
        });

        lv_filtered_app = (ListView) root.findViewById(R.id.lv_filtered_app);
        lv_filtered_app.setEmptyView(root.findViewById(R.id.tv_empty));
        AppFilterAdapter adapter = new AppFilterAdapter(mAppContext, mAppInfos,lv_filtered_app);
        lv_filtered_app.setAdapter(adapter);
        performFiltering(" ");//FIXME 一开始显示空列表
        lv_filtered_app.setOnItemClickListener(filteredAppsListItemClickListener);
        lv_filtered_app.setOnScrollListener(new AbsListView.OnScrollListener() {

            AppFilterAdapter filterAdapter= (AppFilterAdapter) lv_filtered_app.getAdapter();
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount)
            {
                Log.e(TAG,"*************onScroll*******************");

                filterAdapter.setVisibleItemIndex(firstVisibleItem,firstVisibleItem+visibleItemCount-1);

                if (filterAdapter.isNeedRefreshItemVisible(firstVisibleItem,
                                                           firstVisibleItem+visibleItemCount-1)) {
                    Log.e(TAG,"onScroll************NeedRefreshItem**********");
                    filterAdapter.refreshSpecifiedItem();
                }
//                 Log.e(TAG,"firstVisibleItem---->"+firstVisibleItem+
//                      " , visibleItemCount--------->"+visibleItemCount
//                      +" , totalItemCount------>"+totalItemCount);
            }
        });

        btn_app_search = (Button) root.findViewById(R.id.btn_app_search);
        btn_app_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                searchSpecifiedConstraint(et_app_search.getText().toString().trim());
                //                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri
                        .parse("http://www.baidu.com/s?wd=" +
                               Uri.encode(et_app_search.getText().toString())));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mAppContext.startActivity(intent);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                FloatWindowMgr.getInstance(mAppContext).backToSmallFloatWindow();
            }
        });
        return root;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setAppPkgSizeInfo(String pkgName, AppInfo appInfo) {
        if (!TextUtils.isEmpty(pkgName)) {
            //使用反射机制得到PackageManager类的隐藏函数getPackageSizeInfo
            PackageManager pm = mAppContext.getPackageManager();
            try {
                Method getAppPkgSizeInfoMethod = pm.getClass()
                                                   .getMethod("getPackageSizeInfo", String.class,
                                                              IPackageStatsObserver.class);
                getAppPkgSizeInfoMethod.setAccessible(true);
                getAppPkgSizeInfoMethod.invoke(pm, pkgName, new AppPkgSizeObserver(appInfo));
            } catch (Exception e) {
                Log.e(TAG, "**************error!!! NoSuchMethodException***************");
                Log.e(TAG, e.getMessage());
            }
        }
    }

    /**
     * 监听App包大小，调用安卓远程服务获取
     */
    private class AppPkgSizeObserver extends IPackageStatsObserver.Stub {

        private AppInfo appInfo;

        public AppPkgSizeObserver(AppInfo appInfo) {
            this.appInfo = appInfo;
        }

        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
                throws RemoteException
        {
            appInfo.setAppCodeSize(pStats.codeSize);
            appInfo.setAppDataSize(pStats.dataSize);
            appInfo.setAppCacheSize(pStats.cacheSize);
            appInfo.setAppTotalSize(pStats.codeSize + pStats.dataSize + pStats.cacheSize);
            appInfo.setAppPkgSize(pStats.codeSize + pStats.dataSize + pStats.cacheSize);

            Log.i(TAG, "codeSize---->" +
                       Formatter.formatFileSize(mAppContext, pStats.codeSize) +
                       "  , dataSize---->" +
                       Formatter.formatFileSize(mAppContext, pStats.dataSize)

                       + "  ,  cacheSize---->" +
                       Formatter.formatFileSize(mAppContext, pStats.cacheSize));
        }
    }

    //    private class GetAppPkgStatsTask extends AsyncTask<List<AppInfo>,Void,Void>{
    //
    //        @Override
    //        protected Void doInBackground(List<AppInfo>... params) {
    //            for (AppInfo appInfo : params[0]) {
    //                setAppPkgSizeInfo(appInfo.getAppPkgName(),appInfo);
    //            }
    //            return null;
    //        }
    //
    //        @Override
    //        protected void onPostExecute(Void aVoid) {
    //            super.onPostExecute(aVoid);
    //        }
    //    }
}