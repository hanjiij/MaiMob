package et.maimob.com.et.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import et.maimob.com.et.Config;
import et.maimob.com.et.R;
import et.maimob.com.et.datatype.AppInfo;
import et.maimob.com.et.datatype.FunctionInfo;

/**
 * Created by Administrator on 2015/7/27.
 */
public class DateUtils {

    /**
     * 设置function的排版样式，是否开启，主题
     *
     * @param ctx
     * @param params 你想改动的appStyle，functionOpen，theme
     * @param type   MHelper中的三个数字
     */
    public static void setConfig(Context ctx, int params, int type) {
        MAPP.setConfig(ctx, params, type);
    }

    /**
     * 获得 样式 是否开启常用功能
     *
     * @param ctx
     * @param type 在MHelper中的静态变量
     * @return
     */
    public static int getconfig(Context ctx, int type) {
        int value = MAPP.getstyeorfunisopen(ctx, type);
        return value;
    }

    /**
     * 插入11个功能项
     *
     * @param context
     */
    public static void setallfun(Context context) {
        for (int i = 0; i < 11; i++) {
            setFunction(context, i);
        }
    }


    /**
     * 插入常用功能
     *
     * @param context 环境
     * @param id      功能id
     */
    private static void setFunction(Context context, int id) {
        String[] names = context.getResources().getStringArray(R.array.funcName);
        String name = names[id];

        int[] icons = getDrawable();
        int icon = icons[id];

        MFunction.setFunc(context, id + 1, name, icon);//被改动过
    }

    private static int[] getDrawable() {
        return Config.function_black_image_resources;
    }

    /**
     * 得到所有常用功能11个
     *
     * @param context
     * @return 所有常用功能
     */
    public static List<FunctionInfo> getAllFuns(Context context) {
        String[] names = context.getResources().getStringArray(R.array.funcName);
        int[] icons = getDrawable();
        List<FunctionInfo> list = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            String name = names[i];
            int icon = icons[i];
            FunctionInfo functionInfo =
                    new FunctionInfo(i, name, BytesToDrawable(resToByte(context, icon)));
            list.add(functionInfo);
        }
        return list;
    }


    /**
     * 获得已设置的常用功能
     *
     * @param context 环境
     * @return List<MFunction> list
     */
    public static List<FunctionInfo> getFunctions(Context context) {
        List<MFunction> list = MFunction.getFunctions(context);
        List<FunctionInfo> fList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            int icon = list.get(i).icon;
            System.out.println("icon--------------->" + icon);
            byte[] bytes = resToByte(context, icon);
            System.out.println("bytes--------------->" + bytes.length);
            Drawable drawable = BytesToDrawable(bytes);
            System.out.println("drawable------------->" + drawable);
            int id = list.get(i).id - 1;
            System.out.println("id---------->" + id);
            String name = list.get(i).name;
            System.out.println("name---------->" + name);
            FunctionInfo functionInfo = new FunctionInfo(id, name, drawable);
            fList.add(functionInfo);
            System.out.println("------------->success");
        }
        return fList;
    }

    /**
     * 插入常用APP
     *
     * @param c       环境
     * @param index   app位置索引
     * @param appInfo AppInfo appInfo APP信息（PackageName，AppName，Icon）
     */
    public static void insertApp(Context c, int index, AppInfo appInfo) {
        MAPP.setAppInfo(c, index + "", appInfo.getPackageName(), appInfo.getAppName(),
                DrawableToBytes(appInfo.getIcon()));
    }

    /**
     * 获得常用APP
     *
     * @param context 环境
     * @return List<Map<String,Object>> list
     */
    public static List<AppInfo> getApps(Context context) {
        List<MAPP> app_list = MAPP.getApps(context);
        List<MAPP> list = MAPP.getApps(context);
        List<AppInfo> newList = new ArrayList<AppInfo>();
        for (int i = 0; i < app_list.size(); i++) {
            String name = list.get(i).name;
            String namePackage = list.get(i).packageName;
            Drawable drawable = BytesToDrawable(list.get(i).icon);
            AppInfo appInfo = new AppInfo(name, namePackage, drawable);
            newList.add(appInfo);
        }
        return newList;
//        Bitmap ivBitmap = null;
//        for (int i = 0; i < app_list.size(); i++) {
//            if (i == 0) {
//                ivBitmap = BitmapFactory.decodeByteArray(app_list.get(0).icon, 0,
//                        app_list.get(0).icon.length);
//            }
//        }
//        return ivBitmap;
    }

    /**
     * 更新常用功能
     *
     * @param context 环境
     * @param index   索引
     * @param id      id
     * @return 成功返回true
     */
    public static boolean upDateFunc(Context context, int index, int id) {
        try {
            MFunction.setCommonFunc(context, index, id + 1);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 更新常用APP
     *
     * @param context 环境
     * @param index   索引
     * @param appInfo AppInfo
     * @return 成功返回 true
     */
    public static boolean upDateMyApp(Context context, int index, AppInfo appInfo) {
        try {
            String name = appInfo.getAppName();
            String packageName = appInfo.getPackageName();
            Drawable drawable = appInfo.getIcon();
            byte[] icon = DrawableToBytes(drawable);
            MAPP.upDateMyApp(context, index + "", packageName, name, icon);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 删除常用APP方法
     *
     * @param context 环境
     * @param index   删除位置的索引 1~8
     * @return 删除成功，返回true
     */
    public static boolean delMyApp(Context context, int index) {
        try {
            List<MAPP> list = MAPP.getApps(context);
            MAPP.delMyApp(context, index, list);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 删除常用功能
     *
     * @param context 环境
     * @param index   索引 1~8
     * @return 成功返回true
     */
    public static boolean delFunction(Context context, int index) {
        try {
            List<MFunction> list = MFunction.getFunctions(context);
            MFunction.delFunc(context, index, list);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * @param bm drawable转成字节数组
     * @return byte[]
     */
    private static byte[] DrawableToBytes(Drawable bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        drawableToBitmap(bm).compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 字节转成drawable
     *
     * @param b 字节转换成drawable
     * @return
     */
    private static Drawable BytesToDrawable(byte[] b) {
        if (b.length != 0) {
            return new BitmapDrawable(BitmapFactory.decodeByteArray(b, 0, b.length));
        } else {
            return null;
        }
    }

    /**
     * drawable转成bitmap
     *
     * @param drawable drawable 转化成bitmap
     * @return
     */
    private static Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    /**
     * 把实际id地址转成字节数组
     *
     * @param context
     * @param res
     * @return
     */
    private static byte[] resToByte(Context context, int res) {

        byte[] xx = null;
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), res);
        int size = bmp.getWidth() * bmp.getHeight() * 4;
        ByteArrayOutputStream baos = null;
        baos = new ByteArrayOutputStream(size);
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        xx = baos.toByteArray();
        return xx;
    }

    /**
     * 存放功能是否显示
     *
     * @param context
     * @param key     键key
     * @param isShow  是否显示
     */
    public static void setSharedPreference(Context context, String key, int isShow) {
        SharedPreferences preferences = context.getSharedPreferences("config", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, isShow);
        editor.commit();
    }

    /**
     * 获得已设置的完成的样式
     *
     * @param context
     * @param key     键
     * @return 返回0或一
     */
    public static int getSharedPreference(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences("config", 0);
        int show = preferences.getInt(key, 0);
        return show;
    }
}
