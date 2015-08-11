package et.maimob.com.et.floatingwindow.floatwindow;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by jhj_Plus on 2015/7/10.
 * 悬浮窗口管理者
 */
public class FloatWindowMgr {
    private static final String TAG = "FloatWindowMgr";
    private static FloatWindowMgr sFloatWindowMgr;
    private static ActivityManager mActivityManager;
    private Context mAppContext;

    private SmallFloatWindow mSmallFloatWindow;
    private BigFloatWindow mBigFloatWindow;

    private FloatWindowMgr(Context context) {
        mAppContext = context.getApplicationContext();
        mActivityManager = (ActivityManager) mAppContext.getSystemService(Context.ACTIVITY_SERVICE);
        mSmallFloatWindow = new SmallFloatWindow(context);
        mBigFloatWindow = new BigFloatWindow(context);
    }

    public static FloatWindowMgr getInstance(Context context) {
        if (sFloatWindowMgr == null) {
            sFloatWindowMgr = new FloatWindowMgr(context);
        }
        return sFloatWindowMgr;
    }

    /**
     * 获取已使用的内存百分比
     * api 16以下读取meninfo文件
     *
     * @return 字符串形式的已使用内存百分比
     */
    public static String getUsedMemoryPercentValue() {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        mActivityManager.getMemoryInfo(memoryInfo);
        long freeMem = memoryInfo.availMem;//byte
        long totalMem = 0;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            String menInfoPath = "/proc/meminfo";
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(menInfoPath), 2048);
                String line = br.readLine().split("\\s+")[1];
                totalMem = Integer.parseInt(line) * 1024;//KB转byte
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "read file error");
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "close stream erroe");
                    }
                }
            }
        } else {
            totalMem = memoryInfo.totalMem;//byte
            Log.i(TAG, "freeMem------------>" + freeMem + " ,totalMem------------>" + totalMem +
                       ", percent------------>" +
                       (int) (Float.valueOf(
                               String.format("%.2f", (float) (totalMem - freeMem) / totalMem)) *
                              100) + "%");
        }
        return (int) (
                Float.valueOf(String.format("%.2f", (float) (totalMem - freeMem) / totalMem)) *
                100) + "%";
    }


    //    /**
    //     * 获取大悬浮窗
    //     * @return 大悬浮窗
    //     */
    //    public BigFloatWindow getBigFloatWindow() {
    //        return mBigFloatWindow;
    //    }

    //    /**
    //     * 移除小悬浮窗
    //     */
    //    public void removeSmallFloatWindow() {
    //        mSmallFloatWindow.removeSmallFloatWindow();
    //    }

    //    /**
    //     * 移除大悬浮窗
    //     */
    //    public void removeBigFloatWindow() {
    //        mBigFloatWindow.removeBigFloatWindow();
    //    }

    /**
     * 获取小悬浮窗
     *
     * @return 小悬浮窗
     */
    public SmallFloatWindow getSmallFloatWindow() {
        return mSmallFloatWindow;
    }

    /**
     * 移除正在显示的悬浮窗
     */
    public void removeShowingFloatWindow() {
        mSmallFloatWindow.removeSmallFloatWindow();
        mBigFloatWindow.removeBigFloatWindow();
    }

    public void startFloatWindowService(){
        Intent intent = new Intent(mAppContext, FloatWindowService.class);
        mAppContext.startService(intent);
    }
    /**
     * 停止显示悬浮窗的服务
     */
    public void stopFloatWindowService() {
        removeShowingFloatWindow();
        Intent stopFloatWindowIntent = new Intent(mAppContext, FloatWindowService.class);
        mAppContext.stopService(stopFloatWindowIntent);
    }

    /**
     * 返回大悬浮窗
     */
    public void backToBigFloatWindow() {
        mSmallFloatWindow.removeSmallFloatWindow();
        mBigFloatWindow.show();
    }

    /**
     * 返回小悬浮窗
     */
    public void backToSmallFloatWindow() {
        mBigFloatWindow.removeBigFloatWindow();
        mSmallFloatWindow.show();
    }

    public void startRefershSmallFloatWindowUsedMem() {
        mSmallFloatWindow.startRefreshUsedMemTask();
    }

    public void stopRefershSmallFloatWindowUsedMem() {
        mSmallFloatWindow.stopRefreshUsedMemTask();
    }

}
