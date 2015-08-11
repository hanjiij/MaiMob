package et.maimob.com.et.floatingwindow.searchpanel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import et.maimob.com.et.R;
import et.maimob.com.et.floatingwindow.APKDownloader;


/**
 * Created by jhj_Plus on 2015/7/7.
 * 过滤App适配器
 * 根据一些过滤规则来实现过滤App
 */
//TODO--------->应用退出后过滤功能不刷新列表!!!!(待解决) bug
public class AppFilterAdapter extends ArrayAdapter<AppInfo>
        implements APKDownloader.OnProgressChangeListener
{
    public static final String APKDIR =
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/downloadedAPK";
    private static final String TAG = "AppFilterAdapter";
    private static final String[] APKDOWNLOADURLS =
            {"http://apk.hiapk.com/appdown/com.farproc.wifi.analyzer",
             "http://apk.hiapk.com/appdown/com.estrongs.android.pop?webparams=sviptodoc291cmNlPTkz",
             "http://apk.hiapk.com/appdown/com.sdu.didi.psnger?planid=1125795&seid=c6b22ae9-0070-0001-d02b-1b8018201957",
             "http://apk.hiapk.com/appdown/cn.etouch.ecalendar?planid=1125794&seid=c6b22a95-ae60-0001-7e6e-e0e016a0143a",
             "http://apk.hiapk.com/web/api.do?qt=8051&id=608"};

    private static final String[] APKNAMES =
            {"WiFi分析仪.apk", "ES文件浏览器.apk", "滴滴打车.apk", "中华万年历.apk", "安卓市场.apk"};

    private static final int RERUEST_INSTALL = 1;

    /**
     * 模糊过滤器
     */
    private static Filter mFilter;
    /**
     * 应用上下文
     */
    private Context mAppContext;
    /**
     * 过滤后的App列表信息
     */
    private List<AppInfo> mFilteredApps;

    /**
     * 指定的过滤条件过滤器
     */
    private Filter mSpecifiedFilter;

    private int MSG_REFRESH_DOWNLOAD_PROGRESS = 1;
    //private ImageView imgView_app_state;

    //private ProgressBar pb_download;

    //private List<Integer> mPositions;
    private int firstVisibleItem;
    private int lastVisibleItem;
    private int count;
    /**
     * 保存点击下载项的 在ListView里的所有数据的位置 和与之对应的当前进度
     */
    private Map<Integer, Integer> mProgressMap;
    // private Map<Integer, ProgressBar> mProgressBarMap;
    private APKDownloader mAPKDownloader;
    /**
     * 保存当前需要刷新的列表项的 在ListView里的所有数据的位置
     */
    private List<Integer> mNeedRefreshItems;

    private ListView mListView;
    //    private Handler mHandler = new Handler() {
    //        @Override
    //        public void handleMessage(Message msg) {
    //            if (msg.what == MSG_REFRESH_DOWNLOAD_PROGRESS) {
    //                int progress = msg.arg1;
    //                int position=msg.arg2;
    //                mProgressBarMap.get(position).setProgress(progress);
    //            }
    //        }
    //    };

    @SuppressLint("UseSparseArrays")
    public AppFilterAdapter(Context context, List<AppInfo> appInfos, ListView lv) {
        super(context, 0, appInfos);
        mAppContext = context;
        mFilteredApps = appInfos;
        mListView = lv;

        mAPKDownloader = APKDownloader.getInstance(mAppContext);
        mAPKDownloader.setOnProgressChangeListener(this);
        // mPositions=new ArrayList<Integer>();
        mProgressMap = new HashMap<Integer, Integer>();
        // mProgressBarMap = new HashMap<Integer, ProgressBar>();
        mNeedRefreshItems = new LinkedList<Integer>();
        Log.i(TAG, "-----------------new  AppFilterAdapter-----------------");
    }

    //TODO----------》测试
    @Override
    public int getCount() {

        Log.i(TAG, "getCount------------------>" + super.getCount());
        return super.getCount();

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            Log.e(TAG, "*************New Layout************");
            convertView = LayoutInflater.from(mAppContext)
                                        .inflate(R.layout.item_app_filter, parent, false);
        }
        AppInfo appInfo = mFilteredApps.get(position);
        ImageView imgView_appIcon = (ImageView) convertView.findViewById(R.id.imgView_app_icon);
        imgView_appIcon.setImageDrawable(appInfo.getAppIcon());

        TextView tv_appName = (TextView) convertView.findViewById(R.id.tv_app_name);
        tv_appName.setText(appInfo.getAppName());

        TextView tv_app_versionCode = (TextView) convertView.findViewById(R.id.tv_app_versionCode);
        tv_app_versionCode.setText(
                mAppContext.getString(R.string.app_versionName) + appInfo.getAppVersionName());

        TextView tv_app_size = (TextView) convertView.findViewById(R.id.tv_app_size);
        //setAppPkgSizeInfo(appInfo.getAppPkgName(),appInfo);
        tv_app_size.setText(Formatter.formatFileSize(mAppContext, appInfo.getAppPkgSize()));

        ImageView imgView_app_state = (ImageView) convertView.findViewById(R.id.imgView_app_state);
        imgView_app_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                doAppTransaction(v, position);
            }
        });

        ProgressBar pb_download = (ProgressBar) convertView.findViewById(R.id.pb_app_install);

        imgView_app_state
                .setVisibility(mProgressMap.containsKey(position) ? View.GONE : View.VISIBLE);
        pb_download.setVisibility(mProgressMap.containsKey(position) ? View.VISIBLE : View.GONE);

        //        if (pb_download.getVisibility() == View.VISIBLE) {
        //            pb_download.setProgress(mProgressMap.get(position));
        //        }

        //        Log.e(TAG,
        //              "Position--------->" + position + " , ProgressBarId--------->" + pb_download.getId());
        return convertView;
    }

//    private void setAppPkgSizeInfo(String pkgName, AppInfo appInfo) {
//        if (!TextUtils.isEmpty(pkgName)) {
//            //使用反射机制得到PackageManager类的隐藏函数getPackageSizeInfo
//            PackageManager pm = mAppContext.getPackageManager();
//            try {
//                Method getAppPkgSizeInfoMethod = pm.getClass()
//                                                   .getMethod("getPackageSizeInfo", String.class,
//                                                              IPackageStatsObserver.class);
//                getAppPkgSizeInfoMethod.setAccessible(true);
//                getAppPkgSizeInfoMethod.invoke(pm, pkgName, new AppPkgSizeObserver(appInfo));
//            } catch (Exception e) {
//                Log.e(TAG, "**************error!!! NoSuchMethodException***************");
//                Log.e(TAG, e.getMessage());
//            }
//        }
//    }
//
//    /**
//     * 监听App包大小，调用安卓远程服务获取
//     */
//    private class AppPkgSizeObserver extends IPackageStatsObserver.Stub {
//
//        private AppInfo appInfo;
//
//        public AppPkgSizeObserver(AppInfo appInfo) {
//            this.appInfo = appInfo;
//        }
//
//        @Override
//        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
//                throws RemoteException
//        {
//            appInfo.setAppCodeSize(pStats.codeSize);
//            appInfo.setAppDataSize(pStats.dataSize);
//            appInfo.setAppCacheSize(pStats.cacheSize);
//            appInfo.setAppTotalSize(pStats.codeSize + pStats.dataSize + pStats.cacheSize);
//            appInfo.setAppPkgSize(pStats.codeSize + pStats.dataSize + pStats.cacheSize);
//
//            Log.i(TAG, "codeSize---->" +
//                       Formatter.formatFileSize(mAppContext, pStats.codeSize) +
//                       "  , dataSize---->" +
//                       Formatter.formatFileSize(mAppContext, pStats.dataSize)
//
//                       + "  ,  cacheSize---->" +
//                       Formatter.formatFileSize(mAppContext, pStats.cacheSize));
//        }
//    }

    public void setVisibleItemIndex(int firstVisibleItem, int lastVisibleItem) {
        this.firstVisibleItem = firstVisibleItem;
        this.lastVisibleItem = lastVisibleItem;
    }

    public boolean isNeedRefreshItemVisible(final int firstVisibleItem, final int lastVisibleItem) {
        mNeedRefreshItems.clear();
        boolean isNeedRefreshItemVisible = false;
        Log.e(TAG,
              "firstVisibleItem=" + firstVisibleItem + " , lastVisibleItem=" + lastVisibleItem);
        for (int i = firstVisibleItem; i <= lastVisibleItem; i++) {
            if (mProgressMap.containsKey(i)) {
                mNeedRefreshItems.add(i);
                Log.e(TAG, "NeedRefreshItem------------->" + i);
                isNeedRefreshItemVisible = true;
            }
        }
        return isNeedRefreshItemVisible;
    }

    public void refreshSpecifiedItem() {
        for (int i = 0; i < mNeedRefreshItems.size(); i++) {
            // ListView的getChildAt()方法里的index是当前屏幕能够显示到的列表项的从第一个可见项的索引 0 开始到最后一个可见项的索引
            View view = mListView.getChildAt(mNeedRefreshItems.get(i) - firstVisibleItem);
            ProgressBar pb_download = (ProgressBar) view.findViewById(R.id.pb_app_install);
            pb_download.setProgress(mProgressMap.get(mNeedRefreshItems.get(i)));
            Log.e(TAG, "*********refreshSpecifiedItem------------->" + mNeedRefreshItems.get(i) +
                       " , progress=------>" + mProgressMap.get(mNeedRefreshItems.get(i)));
        }
    }

    // TODO 判断状态
    private void doAppTransaction(View child, int position) {

        Log.e(TAG, "doAppTransaction_Position-------------->" + position);

        ViewGroup rootView = (ViewGroup) child.getParent();
        ProgressBar pb_download = (ProgressBar) rootView.findViewById(R.id.pb_app_install);
        ImageView imgView_app_state = (ImageView) rootView.findViewById(R.id.imgView_app_state);
        imgView_app_state.setVisibility(View.GONE);
        pb_download.setVisibility(View.VISIBLE);

        mProgressMap.put(position, 0);
        // mProgressBarMap.put(position, pb_download);

        //Log.e(TAG, "ProgressBarId--------->" + pb_download.getId());
        //        new APKDownLoadTask(pb_download, position).execute(APKDOWNLOADURLS[count],
        //                                                            APKNAMES[count]);
        mAPKDownloader.downloadAPK(APKDOWNLOADURLS[count], APKNAMES[count], position);
        count++;
    }

    /**
     * 获取模糊过滤器(指定一些规则来过滤App)
     *
     * @return 模糊过滤器
     */
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new AppFilter();
        }
        return mFilter;
    }

    /**
     * 获取指定条件过滤器
     *
     * @return 指定条件过滤器
     */
    public Filter obtainFilter() {
        if (mSpecifiedFilter == null) {
            mSpecifiedFilter = new SpecifiedAppFilter();
        }
        return mSpecifiedFilter;
    }

    /**
     * 刷新搜索后的App列表
     *
     * @param newFilteredApps 新的过滤后的App
     */
    private void refreshAppFilterList(List<AppInfo> newFilteredApps) {
        mFilteredApps.clear();
        mFilteredApps.addAll(newFilteredApps);
        if (newFilteredApps.size() > 0) {
            Log.i(TAG, "++++++++++++++notifyDataSetChanged()+++++++++++++++");
            notifyDataSetChanged();
        } else {
            Log.i(TAG, "++++++++++++++Invalidated()+++++++++++++");
            notifyDataSetInvalidated();
        }
    }

    @Override
    public void onProgressChange(int progress, int position) {
        mProgressMap.put(position, progress);
        if (isNeedRefreshItemVisible(firstVisibleItem, lastVisibleItem)) {
            //mProgressBarMap.get(position).setProgress(progress);
            Log.e(TAG, "onProgressChange*************NeedRefreshItem**********" + position + " , " +
                       progress);
            refreshSpecifiedItem();
        }

        if (progress == 100) {
            Log.e(TAG, "********Position OK*********>>>>>>>>>>>" + position);
            //Toast.makeText(mAppContext, "position ok----->" + position, Toast.LENGTH_SHORT)
            // .show();
        }
    }
    //    /**
    //     * APK下载任务
    //     * 提供下载连接和APP名或者包名将APK下载到sdCard里或者没有sdCard下载到本应用的目录下
    //     * 下载完成后自动跳转到安装界面进行安装
    //     */
    //    public class APKDownloadTask extends AsyncTask<String, Integer, File> {
    //        ProgressBar mdownloadBar;
    //        int mPosition;
    //
    //        public APKDownloadTask(ProgressBar downloadBar, int position) {
    //            this.mdownloadBar = downloadBar;
    //            this.mPosition = position;
    //        }
    //
    //        @Override
    //        protected File doInBackground(String... params) {
    //            HttpURLConnection connection = null;
    //            File file = null;
    //            try {
    //                URL url = new URL(params[0]);
    //                connection = (HttpURLConnection) url.openConnection();
    //                connection.setRequestProperty("Accept-Encoding", "identity");
    //                InputStream in = connection.getInputStream();
    //
    //                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
    //                    Log.e(TAG, "*********connect error!!!!***********");
    //                    return null;
    //                }
    //
    //                Log.e(TAG, "**********downLoadAPK**********");
    //                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
    //
    //                    File filePath = new File(APKDIR);
    //                    if (!filePath.exists()) {
    //                        Log.e(TAG, "***********mkdir**********");
    //                        filePath.mkdir();
    //                        Log.e(TAG, "***********filePath exist**********" + filePath.exists());
    //                    }
    //                    File resultFile = new File(filePath, params[1]);
    //                    if (!resultFile.exists()) {
    //                        Log.e(TAG, "resultFile exist-------->" + resultFile.exists());
    //                        Log.e(TAG, "resultFile path-------->" + resultFile.getPath());
    //
    //                        FileOutputStream fos = new FileOutputStream(resultFile);
    //                        int availLength = connection.getContentLength();
    //                        Log.e(TAG, "availLength----------->" + availLength);
    //                        Log.e(TAG, "availLength2----------->" + in.available());
    //                        int bytesCount = 0;
    //                        int bytesRead = 0;
    //                        byte[] buffer = new byte[1024 * 1024];
    //                        while ((bytesRead = in.read(buffer)) > 0) {
    //                            fos.write(buffer, 0, bytesRead);
    //                            Log.e(TAG, "bytesRead----------->" + bytesRead);
    //                            bytesCount += bytesRead;
    //                            Log.e(TAG, "-----------bytesCount----------" + bytesCount);
    //                            publishProgress((int) (bytesCount / (float) availLength * 100));
    //                            Log.e(TAG,
    //                                  "publishProgress------->" + (bytesCount / (float) availLength));
    //                        }
    //                        in.close();
    //                        fos.close();
    //                        Log.e(TAG, "resultFile exist2-------->" + resultFile.exists());
    //                        Log.e(TAG, "resultFile path2-------->" + resultFile.getPath());
    //                        Log.e(TAG, "**********下载成功**********");
    //                        file = resultFile;
    //                    }
    //
    //                } else {
    //                    //TODO 写入应用目录
    //
    //                }
    //            } catch (MalformedURLException e) {
    //                Log.e(TAG, "*********error 1***********");
    //                Log.e(TAG, e.getMessage());
    //            } catch (FileNotFoundException e) {
    //                Log.e(TAG, "*********error 2***********");
    //                Log.e(TAG, e.getMessage());
    //            } catch (IOException e) {
    //                Log.e(TAG, "*********error 3***********");
    //                Log.e(TAG, e.getMessage());
    //            } finally {
    //                if (connection != null) {
    //                    connection.disconnect();
    //                }
    //            }
    //            return file;
    //        }
    //
    //        @Override
    //        protected void onProgressUpdate(Integer... values) {
    //            mdownloadBar.setProgress(values[0]);
    //            mProgressMap.put(mPosition, values[0]);
    //        }
    //
    //        @Override
    //        protected void onPostExecute(File file) {
    //            if (file != null) {
    //                mdownloadBar.setProgress(100);
    //                mProgressMap.put(mPosition, 100);
    //                //TODO 下载完成后发送下载完成后的广播 或者通过接口通知下载完成
    ////                Intent downloadedIntent = new Intent(AppSearchPanel.ACTION_APP_DOWNLOADED);
    ////                Log.e(TAG, "file.getName()------------>" + file.getName());
    ////                downloadedIntent.setData(Uri.parse("package:"));
    ////                mAppContext.sendBroadcast(downloadedIntent);
    //
    //                Intent installIntent = new Intent(Intent.ACTION_VIEW);
    //                Uri path = Uri.fromFile(file);
    //                String type = "application/vnd.android.package-archive";
    //                installIntent.setDataAndType(path, type);
    //                installIntent.addCategory(Intent.CATEGORY_DEFAULT);
    //                installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    //                mAppContext.startActivity(installIntent);
    //            }
    //        }
    //    }

    /**
     * 模糊过滤器
     * 通过一些过滤规则来过滤App
     */
    public class AppFilter extends Filter {

        /**
         * 原先的App数据
         */
        private ArrayList<AppInfo> mOriginalValues;

        public AppFilter() {
            mOriginalValues = new ArrayList<AppInfo>(mFilteredApps);
        }

        public void setValues(List<AppInfo> appInfos){
            mOriginalValues.clear();
            mOriginalValues.addAll(appInfos);
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            //            Log.i(TAG, "startWith------------>" + "bai".startsWith("bao"));

            Log.i(TAG, "constraint------------>" + constraint.toString());
            //输入的查询约束条件为空或字符数为0时显示空列表视图
            if (constraint == null || constraint.length() == 0) {
                Log.i(TAG, "constraint == null || constraint.length() == 0------------>" +
                           constraint.toString());
                ArrayList<AppInfo> list = new ArrayList<AppInfo>();
                results.values = list;
                results.count = list.size();
            } else {

                //将输入的查询关键字(中英文)转换为对应的小写拼音字母

                PackageManager pm = mAppContext.getPackageManager();
                //输入的约束字符的前缀
                String prefixString = HanziToPinyin
                        .getPinyin(constraint.toString()).toLowerCase();
                //复制原来的所有APP信息
                ArrayList<AppInfo> values = new ArrayList<AppInfo>(mOriginalValues);
                //待添加过滤后的APP
                ArrayList<AppInfo> newValues = new ArrayList<AppInfo>();
                for (int i = 0; i < values.size(); i++) {
                    AppInfo originalAppInfo = values.get(i);
                    //中英文转换后的拼音文本
                    String valueText =
                            HanziToPinyin
                                    .getPinyin(values.get(i).getAppName()).toLowerCase();
                    //App的拼音字母前缀或后缀是约束的关键字为过滤后的App
                    if (valueText.startsWith(prefixString) || valueText.endsWith(prefixString)) {
                        newValues.add(originalAppInfo);
                    } else {
                        //按空格分割拼音字母并一一查询是否和约束关键字拼音字母匹配(前缀或后缀)
                        String[] words = valueText.split(" ");
                        for (int j = 0; j < words.length; j++) {
                            //Log.i(TAG,"word------------>"+words[j]);
                            String lowerCaseWord = HanziToPinyin
                                    .getPinyin(words[j]).toLowerCase();
                            if (lowerCaseWord.startsWith(prefixString) ||
                                lowerCaseWord.endsWith(prefixString))
                            {
                                newValues.add(originalAppInfo);
                                break;
                            }
                        }
                    }
                }
                results.values = newValues;
                results.count = newValues.size();
            }

            Log.i(TAG, "filtered count------------>" + results.count);
            return results;

        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence constraint, FilterResults results) {
            refreshAppFilterList((List<AppInfo>) results.values);
        }
    }

    /**
     * 指定约束条件过滤器
     * 搜索指定的关键字查询
     * 查询不到就显示之前过滤后的App
     */
    private class SpecifiedAppFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            Log.i(TAG, "constraint++++++++++++++++++>" + constraint.toString());
            if (constraint == null || constraint.length() == 0) {
                Log.i(TAG, "constraint == null || constraint.length() == 0++++++++++++>" +
                           constraint.toString());
                ArrayList<AppInfo> list = new ArrayList<AppInfo>();
                results.values = list;
                results.count = list.size();
            } else {
                //输入的约束字符的前缀
                String prefixString = HanziToPinyin
                        .getPinyin(constraint.toString());
                PackageManager pm = mAppContext.getPackageManager();
                ArrayList<AppInfo> values = new ArrayList<AppInfo>(mFilteredApps);
                ArrayList<AppInfo> newValues = new ArrayList<AppInfo>();
                for (int i = 0; i < values.size(); i++) {
                    AppInfo originalAppInfo = values.get(i);
                    //中英文转换后的拼音文本
                    String valueText = HanziToPinyin
                            .getPinyin(values.get(i).getAppName());
                    if (valueText.equalsIgnoreCase(prefixString)) {
                        newValues.add(originalAppInfo);
                    }
                }
                results.values = newValues.size() == 0 ? values : newValues;
                results.count = newValues.size() == 0 ? values.size() : newValues.size();
            }
            Log.i(TAG, "Specified Filtered count------------>" + results.count);
            return results;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence constraint, FilterResults results) {
            refreshAppFilterList((List<AppInfo>) results.values);
        }
    }

}
