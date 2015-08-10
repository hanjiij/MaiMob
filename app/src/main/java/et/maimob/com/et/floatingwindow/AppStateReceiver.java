package et.maimob.com.et.floatingwindow;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by jhj_Plus on 2015/7/20.
 */
public class AppStateReceiver extends BroadcastReceiver {
    private static final String TAG = "AppStateReceiver";
    private static final String ACTION_APP_INSTALLED = Intent.ACTION_PACKAGE_ADDED;
    private static final String ACTION_APP_DOWNLOAD_COMPLETE =
            DownloadManager.ACTION_DOWNLOAD_COMPLETE;
    private static final String ACTION_APP__NOTIFICATION_CLICKED =
            DownloadManager.ACTION_NOTIFICATION_CLICKED;

    private static AppStateReceiver sAppStateReceiver;
    private OnAppInstalledListener mInstalledListener;
    private DownloadManager mDownloadManager;
    private Context mAppContext;
    private APKDownloader mAPKDownloader;

    public AppStateReceiver(Context context) {
        Log.e(TAG, "-----------------AppStateReceiver()-------------");
        mAppContext = context.getApplicationContext();
        mAPKDownloader = APKDownloader.getInstance(mAppContext);
        mDownloadManager = (DownloadManager) mAppContext.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    public static AppStateReceiver getInstance(Context context) {
        if (sAppStateReceiver == null) {
            sAppStateReceiver = new AppStateReceiver(context);
        }
        return sAppStateReceiver;
    }

    public void setOnAppInstalledListener(OnAppInstalledListener installedListener) {
        mInstalledListener = installedListener;

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "*********onReceive************");
        String action = intent.getAction();
        //应用安装完成时的广播
        if (action.equals(ACTION_APP_INSTALLED)) {
            String pkgName = intent.getData().getSchemeSpecificPart();
            //mInstalledListener.onAppInstalled(pkgName);
            Log.e(TAG, "getData----------->" + intent.getData().toString());
            Log.e(TAG, "scheme----------->" + intent.getData().getScheme());
            Log.e(TAG, "installed pkgName------>" + pkgName);
            Toast.makeText(context, "app is installed", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.equals(action, ACTION_APP_DOWNLOAD_COMPLETE)) {
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            mAPKDownloader.unregisterAPKDataObserver(downloadId);

            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            Cursor cursor = mDownloadManager.query(query);
            if (cursor != null && cursor.moveToFirst()) {
                cursor.moveToFirst();

                String apkName = cursor.getString(
                        cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                String apkUri =
                        cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));

                Intent installIntent = new Intent(Intent.ACTION_VIEW);
                Uri path = Uri.parse(apkUri);
                String type = "application/vnd.android.package-archive";
                installIntent.setDataAndType(path, type);
                installIntent.addCategory(Intent.CATEGORY_DEFAULT);
                installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mAppContext.startActivity(installIntent);

                Log.e(TAG, "<----------downloaded APK------>");
                Toast.makeText(context, apkName + "is downloaded", Toast.LENGTH_SHORT).show();
            }

        } else if (TextUtils.equals(action, ACTION_APP__NOTIFICATION_CLICKED)) {
            Log.e(TAG, "<----------notification_clicked------>");
            Toast.makeText(context, "app notification_clicked", Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnAppInstalledListener {
        //返回包名
        void onAppInstalled(String pkgName);
    }
}
