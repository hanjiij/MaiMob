package et.maimob.com.et.function;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;

import java.io.DataOutputStream;

/**
 * Created by HJ on 2015/7/15.
 * 设置飞行模式开关类
 * 接口返回值成功：0表示已成功关闭飞行模式，1表示已成功开启飞行模式，2表示正在开启飞行模式，3表示正在关闭飞行模式
 * -1表示自动开启失败，未获得root权限,跳转到设置界面（不需要你那边跳转，这边已近做了）
 */
public class SetFlightMode {

    private static SetFlightMode setFlightMode;
    private Context context;
//    private SuccessEnable successEnable;
//    private FailEnable failEnable;
    private Handler handler;
    private Message message;
    private FlightModeStatus flightModeStatus;

    /**
     * @param context       上下文环境
     */
    private SetFlightMode(Context context/*, SuccessEnable successEnable, FailEnable failEnable*/) {

        this.context = context;
//        this.successEnable = successEnable;
//        this.failEnable = failEnable;
        initHandler();
    }

    public static SetFlightMode getIntence(Context context){
        if (setFlightMode==null) {
            setFlightMode=new SetFlightMode(context);
        }
        return setFlightMode;
    }

    /**
     * handler初始化
     */
    private void initHandler() {

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (flightModeStatus !=null) {

                    if (msg.what == 1) {

//                    successEnable.onSuccess(getAirplaneMode() ? 0 : 1);
                        flightModeStatus.onStatus(getAirplaneMode() ? 0 : 1);
                    } else if (msg.what == 2) {

//                    successEnable.onSuccess(2);
                        flightModeStatus.onStatus(2);
                    } else if (msg.what == 3) {

//                    successEnable.onSuccess(1);
                        flightModeStatus.onStatus(1);
                    } else if (msg.what == 4) {

//                    failEnable.onFail(0);
                        flightModeStatus.onStatus(-1);
                        context.startActivity(new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS));
                    } else if (msg.what == 5) {

//                    successEnable.onSuccess(0)
//
                        flightModeStatus.onStatus(0);
                    } else if (msg.what == 6) {

//                    failEnable.onFail(0);
                        flightModeStatus.onStatus(-1);
                        context.startActivity(
                                new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS));  //开启关闭失败跳转到系统设置界面
                    }
                }
            }
        };
    }

    /**
     * 设置手机飞行模式，若已开启则关闭。未开启则开启
     */
    public void setAirplaneModeOn() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {


            message = new Message();
            message.what = 1;                       // 通知成功，开启或关闭飞行模式
            handler.sendMessage(message);

            Settings.System.putInt(context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, !getAirplaneMode() ? 1 : 0);
            Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
            intent.putExtra("state", !getAirplaneMode());
            context.sendBroadcast(intent);

        } else {

            if (!getAirplaneMode()) {

                message = new Message();
                message.what = 2;                       // 通知成功，正在开启飞行模式
                handler.sendMessage(message);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (RootCommand("settings put global airplane_mode_on 1;" +
                                "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state true")) {

                            message = new Message();
                            message.what = 3;                       // 通知成功，已成功开启飞行模式
                            handler.sendMessage(message);

                        } else {

                            message = new Message();
                            message.what = 4;                       // 通知失败，开启失败，未获得root权限
                            handler.sendMessage(message);
                        }
                    }
                }).start();
            } else {
//                successEnable.onSuccess(3);

                if (flightModeStatus !=null) {
                    flightModeStatus.onStatus(3);
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (RootCommand("settings put global airplane_mode_on 0;" +
                                "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state false")) {

                            message = new Message();
                            message.what = 5;                       // 通知成功，已成功关闭飞行模式
                            handler.sendMessage(message);

                        } else {

                            message = new Message();
                            message.what = 6;                       // 通知失败，关闭失败，未获得root权限
                            handler.sendMessage(message);
                        }
                    }
                }).start();
            }
        }
    }

    /**
     * 应用程序运行命令获取 Root权限，设备必须已破解(获得ROOT权限)
     *
     * @param command 需要运行的命令内容
     *                命令：String apkRoot="chmod 777 "+getPackageCodePath();
     *                RootCommand(apkRoot);
     * @return 应用程序是/否获取Root权限
     */
    private boolean RootCommand(String command) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            Log.d("*** DEBUG ***", "ROOT REE" + e.getMessage());
            return false;
        } finally {
            try {

                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        Log.d("*** DEBUG ***", "Root SUC ");
        return true;
    }

    /**
     * 判断手机是否是飞行模式
     *
     * @return 1代表开启状态，0代表关闭状态
     */
    public boolean getAirplaneMode() {
        int isAirplaneMode = Settings.System.getInt(context.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0);
        return (isAirplaneMode == 1) ? true : false;
    }

//    public interface SuccessEnable {
//        void onSuccess(int state);
//    }
//
//    public interface FailEnable {
//        void onFail(int err);
//    }


    /**状态返回接口
     * @param flightModeStatus
     *
     * status：0表示已成功关闭飞行模式，1表示已成功开启飞行模式，2表示正在开启飞行模式，3表示正在关闭飞行模式
     * -1表示自动开启失败，未获得root权限,跳转到设置界面（不需要你那边跳转，这边已近做了）
     */
    public void onStatusListener(FlightModeStatus flightModeStatus){
        this.flightModeStatus = flightModeStatus;
    }

    public interface FlightModeStatus {
        void onStatus(int status);
    }
}
