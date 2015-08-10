package et.maimob.com.et.floatingwindow;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import et.maimob.com.et.R;
import et.maimob.com.et.floatingwindow.floatwindow.FloatWindowMgr;

/**
 * Created by jhj_Plus on 2015/7/9.
 */
public class TestFragment extends Fragment {
    public static final String APKDIR =
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/downloadedAPK";
    public static final String ACTION_APP_DOWNLOADED = "app.intent.action.APP_DOWNLOADED";
    private static final String TAG = "TestFragment";
    private static final String APKDOWNLOADURL =
            "http://apk.hiapk.com/appdown/com.estrongs.android" +
            ".pop?webparams=sviptodoc291cmNlPTkz";
    private static final String APKNAME = "ES文件浏览器.apk";
    private static final String ACTION_APP_INSTALLED = Intent.ACTION_PACKAGE_ADDED;
    private static final int RERUEST_INSTALL = 1;
    private ProgressBar mProgressBar;
    private AppStateReceiver mAppStateReceiver;
    private Button btn_add;
    private Button btn_remove;
    private Button btn_download;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        IntentFilter appStateIF = new IntentFilter();
        appStateIF.addAction(ACTION_APP_INSTALLED);
        appStateIF.addAction(ACTION_APP_DOWNLOADED);appStateIF.addDataScheme("package");
        //
        mAppStateReceiver = AppStateReceiver.getInstance(getActivity());
        getActivity().registerReceiver(mAppStateReceiver, appStateIF);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_test, container, false);
        btn_add = (Button) root.findViewById(R.id.btn_add_floatWindow);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FloatWindowMgr.getInstance(getActivity()).startFloatWindowService();
                getActivity().finish();
            }
        });
        btn_remove = (Button) root.findViewById(R.id.btn_remove_floatWindow);
        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //停止服务
                FloatWindowMgr.getInstance(getActivity()).stopFloatWindowService();
            }
        });
        btn_download = (Button) root.findViewById(R.id.btn_download);
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new APKDownLoadTask().execute(APKDOWNLOADURL, APKNAME);
            }
        });

        mProgressBar = (ProgressBar) root.findViewById(R.id.pb_app_install);
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "***************onActivityResult************");
        if (requestCode != RERUEST_INSTALL) {
            return;
        }
        Log.e(TAG, "resultCode-------->" + resultCode);
        //        if (resultCode== Activity.RESULT_CANCELED) {
        //             Log.e(TAG,"************RESULT_CANCELED**********");
        //        } else if (resultCode==Activity.RESULT_OK) {
        //            Log.e(TAG,"************RESULT_OK**********");
        //        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mAppStateReceiver);
    }

    /**
     * APK下载任务
     * 提供下载连接和APP名或者包名将APK下载到sdCard里或者没有sdCard下载到本应用的目录下
     * 下载完成后自动跳转到安装界面进行安装
     */
    public class APKDownLoadTask extends AsyncTask<String, Integer, File> {

        @Override
        protected File doInBackground(String... params) {
            HttpURLConnection connection = null;
            File file = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Accept-Encoding", "identity");
                InputStream in = connection.getInputStream();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "*********connect error!!!!***********");
                    return null;
                }

                Log.e(TAG, "**********downLoadAPK**********");
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                    File filePath = new File(APKDIR);
                    if (!filePath.exists()) {
                        Log.e(TAG, "***********mkdir**********");
                        filePath.mkdir();
                        Log.e(TAG, "***********filePath exist**********" + filePath.exists());
                    }
                    File resultFile = new File(filePath, params[1]);
                    if (!resultFile.exists()) {
                        Log.e(TAG, "resultFile exist-------->" + resultFile.exists());
                        Log.e(TAG, "resultFile path-------->" + resultFile.getPath());

                        FileOutputStream fos = new FileOutputStream(resultFile);
                        int availLength = connection.getContentLength();
                        Log.e(TAG, "availLength----------->" + availLength);
                        Log.e(TAG, "availLength2----------->" + in.available());
                        int bytesCount = 0;
                        int bytesRead = 0;
                        byte[] buffer = new byte[1024 * 1024];
                        while ((bytesRead = in.read(buffer)) > 0) {
                            fos.write(buffer, 0, bytesRead);
                            Log.e(TAG, "bytesRead----------->" + bytesRead);
                            bytesCount += bytesRead;
                            Log.e(TAG, "-----------bytesCount----------" + bytesCount);
                            publishProgress((int) (bytesCount / (float) availLength * 100));
                            Log.e(TAG,
                                  "publishProgress------->" + (bytesCount / (float) availLength));
                        }
                        in.close();
                        fos.close();
                        Log.e(TAG, "resultFile exist2-------->" + resultFile.exists());
                        Log.e(TAG, "resultFile path2-------->" + resultFile.getPath());
                        Log.e(TAG, "**********下载成功**********");
                        file = resultFile;
                    }

                } else {
                    //TODO 写入应用目录

                }
            } catch (MalformedURLException e) {
                Log.e(TAG, "*********error 1***********");
                Log.e(TAG, e.getMessage());
            } catch (FileNotFoundException e) {
                Log.e(TAG, "*********error 2***********");
                Log.e(TAG, e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "*********error 3***********");
                Log.e(TAG, e.getMessage());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return file;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mProgressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(File file) {
            if (file != null) {
                mProgressBar.setProgress(100);
                //TODO 下载完成后发送下载完成后的广播 或者通过接口通知下载完成
                Intent downloadedIntent = new Intent(ACTION_APP_DOWNLOADED);
                Log.e(TAG, "file.getName()------------>" + file.getName());
                downloadedIntent.setData(Uri.parse("package:"));
                getActivity().sendBroadcast(downloadedIntent);

                Intent installIntent = new Intent(Intent.ACTION_VIEW);
                Uri path = Uri.fromFile(file);
                String type = "application/vnd.android.package-archive";
                installIntent.setDataAndType(path, type);
                installIntent.addCategory(Intent.CATEGORY_DEFAULT);
                installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivityForResult(installIntent, RERUEST_INSTALL);
            }
        }
    }
}
