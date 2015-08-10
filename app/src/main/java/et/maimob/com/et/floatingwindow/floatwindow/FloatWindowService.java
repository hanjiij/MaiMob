package et.maimob.com.et.floatingwindow.floatwindow;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by jhj_Plus on 2015/7/9.
 * 悬浮窗后台服务
 */
public class FloatWindowService extends Service {
    private static final String TAG = "WindowFloatService";
    private Context mAppContext;
    private FloatWindowMgr mFloatWindowMgr;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        mAppContext = getApplicationContext();
        mFloatWindowMgr = FloatWindowMgr.getInstance(mAppContext);
        mFloatWindowMgr.getSmallFloatWindow().show();
        mFloatWindowMgr.startRefershSmallFloatWindowUsedMem();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //停止刷新内存占用百分值
        mFloatWindowMgr.stopRefershSmallFloatWindowUsedMem();
    }
}
