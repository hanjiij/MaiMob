package et.maimob.com.et.function;

import android.hardware.Camera;

/**
 * Created by HJ on 2015/7/14.
 * 开启闪光灯类
 */
public class SetFlashLight {

    private static Camera camera;
    private static Camera.Parameters parameters;
    private static boolean isopen = false;

    /**
     * 设置手电筒开关状态,初始状态为关闭，点击再起，再次点击关闭
     *
     */
    public static void setLight() {

        if (isopen) {
            closeLight();
        } else {
            openLight();
        }
    }

    /**
     * 打开手电
     */
    private static void openLight() {

        camera = Camera.open();
        parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(parameters);
        camera.startPreview();
        isopen = true;
    }

    /**
     * 关闭手电
     */
    private static void closeLight() {
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(parameters);
        isopen = false;
        camera.stopPreview();
        camera.release();
    }
}
