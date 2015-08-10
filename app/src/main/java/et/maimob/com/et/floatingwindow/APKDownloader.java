package et.maimob.com.et.floatingwindow;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jhj_Plus on 2015/7/17.
 */
public class APKDownloader {
    public static final String APKINSTALLDIR = "downloadedAPK";
    private static final String TAG = "APKDownloader";
    private static final Uri OBSERVERURI = Uri.parse("content://downloads/my_downloads");
    private static APKDownloader sAPKDownloader;
    private Context mAppContext;
    private Map<Long, APKDataObserver> mDataObserverMap;
    private DownloadManager mDownloadManager;
    private OnProgressChangeListener mOnProgressChangeListener;


    @SuppressLint("UseSparseArrays")
    public APKDownloader(Context context) {
        mAppContext = context.getApplicationContext();
        mDownloadManager = (DownloadManager) mAppContext.getSystemService(Context.DOWNLOAD_SERVICE);
        mDataObserverMap = new HashMap<Long, APKDataObserver>();
    }

    public static APKDownloader getInstance(Context context) {
        if (sAPKDownloader == null) {
            sAPKDownloader = new APKDownloader(context);
        }
        return sAPKDownloader;
    }

    public synchronized void downloadAPK(String urlSpec, String apkName,int position) {
        File apkFile = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                apkName);
        if (apkFile.exists()) {
            Log.e(TAG, "------------file is exists-----------");
            return;
        }

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            Uri uri = Uri.parse(urlSpec);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            request.setTitle("Download");
            request.setDescription(apkName + "is downloading...");
            request.setVisibleInDownloadsUi(true);
            request.setNotificationVisibility(
                    DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.allowScanningByMediaScanner();
            request.setAllowedOverRoaming(false);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, apkName);

            long id = mDownloadManager.enqueue(request);
            APKDataObserver apkDataObserver = new APKDataObserver(null, id,position);
            mDataObserverMap.put(id, apkDataObserver);
            mAppContext.getContentResolver()
                       .registerContentObserver(OBSERVERURI, true, apkDataObserver);

        } else {
            //TODO 写入应用目录
        }
    }

    private void queryDownloadStatus(long id,int position) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(id);
        Cursor cursor = mDownloadManager.query(query);

        if (cursor != null && cursor.moveToFirst()) {

            long apkSize =
                    cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            long downloadBytesSoFar = cursor.getLong(
                    cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            int downloadProgress = (int) (downloadBytesSoFar / (float) apkSize * 100);

            String apkName=cursor.getString(cursor.getColumnIndex(DownloadManager
                                                                          .COLUMN_LOCAL_FILENAME));
            Log.e(TAG, "apkName----->"+apkName+" , apkSize--->" + apkSize + " , downloadBytesSoFar--->" +
                       downloadBytesSoFar +
                       " , downloadProgress--->" + downloadProgress);

            mOnProgressChangeListener.onProgressChange(downloadProgress, position);

            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                case DownloadManager.STATUS_RUNNING:

                    break;
                case DownloadManager.STATUS_PENDING:

                    break;
                case DownloadManager.STATUS_PAUSED:

                    break;
                case DownloadManager.STATUS_FAILED:

                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    break;
                default:
                    break;
            }
        }
    }

    public void setOnProgressChangeListener(OnProgressChangeListener onProgressChangeListener) {
        mOnProgressChangeListener = onProgressChangeListener;
    }

    public void unregisterAPKDataObserver(long downloadId) {
        if (mDataObserverMap.containsKey(downloadId)) {
            Log.e(TAG,"**************unregisterAPKDataObserver************");
            mAppContext.getContentResolver()
                       .unregisterContentObserver(mDataObserverMap.get(downloadId));
        }
    }

    public interface OnProgressChangeListener {
        void onProgressChange(int progress, int position);
    }

    /**
     * 监视 sdcard download 目录下下载的 APK 数据变化，
     * 然后更新下载进度
     */
    private class APKDataObserver  extends ContentObserver {
        private long downloadId;
        private int position;
        public APKDataObserver(Handler handler, long downloadId,int position) {
            super(handler);
            this.downloadId = downloadId;
            this.position=position;
        }

        @Override
        public void onChange(boolean selfChange) {
            Log.e(TAG, "**************onChange*************");
            queryDownloadStatus(downloadId,position);
        }
    }
    //    /**
    //     * @param context used to check the device version and DownloadManager information
    //     * @return true if the download manager is available
    //     */
    //    public static boolean isDownloadManagerAvailable(Context context) {
    //        try {
    //            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
    //                return false;
    //            }
    //            Intent intent = new Intent(Intent.ACTION_MAIN);
    //            intent.addCategory(Intent.CATEGORY_LAUNCHER);
    //            intent.setClassName("com.android.providers.downloads.ui", "com.android.providers.downloads.ui.DownloadList");
    //            List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
    //                                                                                       PackageManager.MATCH_DEFAULT_ONLY);
    //            return list.size() > 0;
    //        } catch (Exception e) {
    //            return false;
    //        }
    //    }
}
