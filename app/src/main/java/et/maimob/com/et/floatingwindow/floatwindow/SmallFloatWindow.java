package et.maimob.com.et.floatingwindow.floatwindow;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Field;

import et.maimob.com.et.R;


/**
 * Created by jhj_Plus on 2015/7/9.
 * 小悬浮窗
 * 当前显示百分比
 * TODO 是否要另起刷新线程刷新小悬浮窗的剩余内存百分比
 */
public class SmallFloatWindow implements View.OnTouchListener, View.OnClickListener {
    private static final String TAG = "SmallFloatWindow";
    private boolean isShowing;
    private boolean isFirstShow;
    private Context mAppContext;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mSmallWindowParams;
    private ViewGroup rootView;
    private View mSmallFloatView;
    private float xInView;
    private float yInView;
    private float xInScreen;
    private float yInScreen;
    private float xFirstInScreen;
    private float yFirstInScreen;
    private int statusBarHeight;
    private boolean isRefreshUsedMem;

    public SmallFloatWindow(Context context) {
        initData(context);
    }

    /**
     * 初始化小悬浮窗的显示数据
     *
     * @param context 全局应用上下文
     */
    private void initData(Context context) {
        mAppContext = context.getApplicationContext();
        mWindowManager = (WindowManager) mAppContext.getSystemService(Context.WINDOW_SERVICE);
        mSmallWindowParams = new WindowManager.LayoutParams();

        rootView = (ViewGroup) LayoutInflater.from(mAppContext)
                                             .inflate(R.layout.float_window_small, null);
        mSmallFloatView = rootView.findViewById(R.id.floatView_small);
        mSmallFloatView.setOnTouchListener(this);
        mSmallFloatView.setOnClickListener(this);

        rootView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                         View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        int screenWidth = mWindowManager.getDefaultDisplay().getWidth();
        int screenHeight = mWindowManager.getDefaultDisplay().getHeight();
        int smallFloatWindowWidth = mSmallFloatView.getMeasuredWidth();
        int smallFloatWindowHeight = mSmallFloatView.getMeasuredHeight();

        Log.i(TAG, "sceenWidth------------->" + screenWidth);
        Log.i(TAG, "sceenHeight------------->" + screenHeight);
        Log.i(TAG, "Measured_smallFloatWindowWidth------------->" + smallFloatWindowWidth);
        Log.i(TAG, "Measured_smallFloatWindowHeight------------->" + smallFloatWindowHeight);


        mSmallWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mSmallWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                                   WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        mSmallWindowParams.format = PixelFormat.RGBA_8888;
        mSmallWindowParams.gravity = Gravity.START | Gravity.TOP;
        isFirstShow = true;
        mSmallWindowParams.x = 0;
        mSmallWindowParams.y = screenHeight - smallFloatWindowHeight >> 1;
        mSmallWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mSmallWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        mSmallFloatView.post(new Runnable() {
            @Override
            public void run() {
                ((Button) mSmallFloatView).setText(FloatWindowMgr.getUsedMemoryPercentValue());
            }
        });
    }

    /**
     * 更新拖动后的小悬浮窗的位置
     */
    private void updateSmallFloatViewPosition() {
        mSmallWindowParams.x = (int) (xInScreen - xInView);
        mSmallWindowParams.y = (int) (yInScreen - yInView);
        mWindowManager.updateViewLayout(rootView, mSmallWindowParams);
    }

    /**
     * 用于获取状态栏的高度
     *
     * @return 返回状态栏高度的像素值。
     */
    private int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = mAppContext.getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }

    /**
     * 显示小悬浮窗
     */
    public void show() {
        if (!isShowing && mWindowManager != null && rootView != null &&
            mSmallWindowParams != null)
        {
            if (!isFirstShow) {
                mSmallWindowParams.x = (int) (xInScreen - xInView);
                mSmallWindowParams.y = (int) (yInScreen - yInView);
            }
            mWindowManager.addView(rootView, mSmallWindowParams);
            isShowing = true;
            return;
        }
        isFirstShow = false;
    }

    /**
     * 移除小悬浮窗
     */
    public void removeSmallFloatWindow() {
        if (isShowing && mWindowManager != null && rootView != null) {
            mWindowManager.removeView(rootView);
            isShowing = false;
        }
    }

    /**
     * 小悬浮窗的点击事件
     * 点击小悬浮窗打开大的悬浮窗
     */
    @Override
    public void onClick(View v) {
        //TODO 打开大悬浮窗
        Toast.makeText(mAppContext, "---smallFloatView is click---", Toast.LENGTH_SHORT).show();
        FloatWindowMgr floatWindowMgr = FloatWindowMgr.getInstance(mAppContext);
        floatWindowMgr.backToBigFloatWindow();
    }

    /**
     * 小悬浮窗的触摸事件
     * 实现可拖拉悬浮窗
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        FloatWindowMgr floatWindowMgr = FloatWindowMgr.getInstance(mAppContext);
        int eventType = event.getAction();
        switch (eventType) {
            case MotionEvent.ACTION_DOWN:
                xInView = event.getX();
                yInView = event.getY();
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                xFirstInScreen = event.getRawX();
                yFirstInScreen = event.getRawY() - getStatusBarHeight();
                break;
            case MotionEvent.ACTION_MOVE:
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                // 手指移动的时候更新小悬浮窗的位置
                updateSmallFloatViewPosition();

                break;
            case MotionEvent.ACTION_UP:
                boolean isOpenBigFloatWindow =
                        xInScreen == xFirstInScreen && yInScreen == yFirstInScreen;
                if (isOpenBigFloatWindow) {
                    //TODO 打开大悬浮窗
                    floatWindowMgr.backToBigFloatWindow();
                }
                break;
            default:
                break;
        }
        return false;  //此处必须返回false，否则OnClickListener获取不到监听
    }

    public void startRefreshUsedMemTask() {
        new RefreshUsedMemTask().start();
        isRefreshUsedMem = true;
    }

    public void stopRefreshUsedMemTask() {
        isRefreshUsedMem = false;
    }

    private class RefreshUsedMemTask extends Thread {
        @Override
        public void run() {
            while (isRefreshUsedMem) {
                Log.i(TAG, "************RefreshUsedMemTask************");
                try {
                    mSmallFloatView.post(new Runnable() {
                        @Override
                        public void run() {
                            ((Button) mSmallFloatView)
                                    .setText(FloatWindowMgr.getUsedMemoryPercentValue());
                        }
                    });
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Log.e(TAG, "error!!!!!" + e.getMessage());
                }
            }
        }
    }
}
