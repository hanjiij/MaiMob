package et.maimob.com.et.DownLoadManager;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;

/**下载工具类
 *
 * Created by Administrator on 2015/7/26.
 */
public class DownLoads {
    private int percent;
    private String name;//安装路径的名字
    private String FileName;//下载文件名
    private DownloadManager manager ;
    private long downLoadId;
    private static DownLoads downLoads=null;
    public final Uri CONTENT_URI = Uri.parse("content://downloads/my_downloads");

    private  IntentFilter intentFilter;
    private  DownloadCompleteReceiver receiver;

    private DownLoads() {}

    /**获取实例
     * @return
     */
    public static DownLoads getInstance()
    {
        if (downLoads==null)
        {
            downLoads=new DownLoads();
        }
        return downLoads;
    }

    /**新建下载任务
     * @param context  上下文环境
     * @param url  下载地址
     */
   public void setDownLoadTask(Context context,String url)
   {
       if (receiver==null)
       {
           receiver = new DownloadCompleteReceiver();
       }
       if (intentFilter==null)
       {
           intentFilter=new IntentFilter();
           intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
           intentFilter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
           context.registerReceiver(receiver, intentFilter);
       }

       manager= (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
       DownloadManager.Request down=new DownloadManager.Request (Uri.parse(url));
       FileName=url.substring(url.lastIndexOf("/") + 1);//要下载的文件名
       //设置允许使用的网络类型，这里是移动网络和wifi都可以
       down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
       //禁止发出通知，既后台下载
       down.setShowRunningNotification(true);
       //显示下载界面

       down.setVisibleInDownloadsUi(true);
       down.setTitle("正在下载...");
       down.setDescription(FileName+"正在下载");
       //设置下载后文件存放的位置
       down.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, FileName);
       downLoadId=manager.enqueue(down);
       context.getContentResolver()
               .registerContentObserver(CONTENT_URI, true, new DownloadChangeObserver(null));
   }


    class DownloadChangeObserver extends ContentObserver {
        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public DownloadChangeObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            queryDownloadStatus();
            System.out.println("下载百分比======>"+getPercent());
        }
    }

    private void queryDownloadStatus() {
        DownloadManager.Query query=new DownloadManager.Query();
        query.setFilterById(downLoadId);
        Cursor cursor=manager.query(query);
        if (cursor.moveToFirst())
        {
            name=cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
            int fileSizeIdx =
                    cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);//文件的总大小
            int bytesDLIdx =
                    cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);//下载下来的文件
            int fileSize = cursor.getInt(fileSizeIdx);
            System.out.println("文件总大小---------->"+fileSize);
            int bytesDL = cursor.getInt(bytesDLIdx);
            System.out.println("下载量---------->" + bytesDL);
            percent= (int) (((bytesDL*1.0)/fileSize)*100);
//            System.out.println("下载百分比---------------------->"+percent);
        }else {
            System.out.println("==============?...>没有返现目录");
        }
    }

    private void install(Context context)
    {
        if(receiver != null)
        {
            context.unregisterReceiver(receiver);
        }
        Intent i=new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + name), "application/vnd.android.package-archive");
        context.startActivity(i);

    }

    /**返回下载的大小
     * @return 返回0-100进度值
     */
    public int getPercent()
    {
        return percent;
    }

    class DownloadCompleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
                install(context);
            }else if(DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(intent.getAction())){
                long[] ids = intent.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);
                //点击通知栏取消下载
                manager.remove(ids);
                Toast.makeText(context, "取消下载", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
