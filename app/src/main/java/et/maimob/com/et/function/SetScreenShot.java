package et.maimob.com.et.function;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cj.ScreenShotUtil.ScreentShotUtil;
import com.cj.ScreenShotUtil.ShellUtils;

/**
 * Created by HJ on 2015/7/14.
 * <p>
 * 截屏操作类
 * 回掉函数成功：0表示正在截图，1表示截图成功：-1表示未获得root权限，截图失败
 */
public class SetScreenShot {

    //    private SuccessEnable successEnable;
//    private FailEnable failEnable;
    private Context context;
    private Handler handler;
    private Message message;
    private ScreenShotListener screenShotListener;
    private static SetScreenShot setScreenShot;

    private SetScreenShot(Context context/*, SuccessEnable successEnable, FailEnable failEnable*/) {

        this.context = context;
//        this.successEnable = successEnable;
//        this.failEnable = failEnable;
        initHandler();
    }

    public static SetScreenShot getInstance(Context context){
        if (setScreenShot==null) {
            setScreenShot=new SetScreenShot(context);
        }
        return setScreenShot;
    }

    /**
     * handler初始化
     */
    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (msg.what == 1) {

//                    successEnable.onSuccess(0);
                    screenShotListener.onScreenShotListener(0);
                } else if (msg.what == 2) {

//                    successEnable.onSuccess(1);
                    screenShotListener.onScreenShotListener(1);
                } else if (msg.what == 3) {

//                    failEnable.onFail(0);
                    screenShotListener.onScreenShotListener(-1);

                }
            }
        };
    }

    /**
     * 截图函数
     *
     * @param filestr 存储截图的路径 如：/sdcard/screenaaa.jpg
     */
    public void setScreenShot(final String filestr) {

        new Thread(new Runnable() {
            @Override
            public void run() {

//                boolean a = RootCommand(
//                        "su -c 'screencap /mnt/sdcard/screenaaa.png' && adb pull /sdcard/screenaaa.png");

                message = new Message();
                message.what = 1;  // 通知成功，正在截图
                handler.sendMessage(message);

                boolean a = ShellUtils.checkRootPermission();  // 判断是否具备root权限

                if (a) {

                    ScreentShotUtil.getInstance().takeScreenshot(context, filestr);

                    System.out.println("通知成功，截图成功");
                    message = new Message();
                    message.what = 2;  // 通知成功，截图成功
                    handler.sendMessage(message);

                } else {

                    System.out.println("未获得root权限，截图失败");
                    message = new Message();
                    message.what = 3;  // 通知失败，未获得root权限，截图失败
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

//    /**
//     * 应用程序运行命令获取 Root权限，设备必须已破解(获得ROOT权限)
//     *
//     * @param command 命令：String apkRoot="chmod 777 "+getPackageCodePath();
//     *                RootCommand(apkRoot);
//     * @return 应用程序是/否获取Root权限
//     */
//    private boolean RootCommand(String command) {
//        Process process = null;
//        DataOutputStream os = null;
//        try {
//            process = Runtime.getRuntime().exec("su");
//            os = new DataOutputStream(process.getOutputStream());
//            os.writeBytes(command + "\n");
//            os.writeBytes("exit\n");
//            os.flush();
//            process.waitFor();
//        } catch (Exception e) {
//            Log.d("*** DEBUG ***", "ROOT REE" + e.getMessage());
//            return false;
//        } finally {
//            try {
//                if (os != null) {
//                    os.close();
//                }
//                process.destroy();
//            } catch (Exception e) {
//            }
//        }
//        Log.d("*** DEBUG ***", "Root SUC ");
//        return true;
//    }

//    public interface SuccessEnable {
//        void onSuccess(int state);
//    }
//
//    public interface FailEnable {
//        void onFail(int err);
//    }

    /**0表示正在截图，1表示截图成功失败：0表示未获得root权限，截图失败
     * @param screenShotListener
     */
    public void setScreenShotListener(ScreenShotListener screenShotListener) {
        this.screenShotListener = screenShotListener;
    }

    public interface ScreenShotListener {
        void onScreenShotListener(int status);
    }
}
