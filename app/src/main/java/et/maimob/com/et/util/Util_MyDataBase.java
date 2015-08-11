//package et.maimob.com.et.util;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.graphics.drawable.Drawable;
//
//import java.util.List;
//
//import et.maimob.com.et.Config;
//import et.maimob.com.et.database.MyDataBase;
//import et.maimob.com.et.datatype.AppInfo;
//
///**
// * Created by HJ on 2015/7/8.
// * <p>
// * 数据库操作工具类
// */
//public class Util_MyDataBase {
//
//
//    private static SQLiteDatabase db;
//
//
//    public static SQLiteDatabase GetDatabase(Context context) {
//
//        if (db == null || !db.isOpen()) {
//            db = new MyDataBase(context, Config.APP_TABLE_NAME).getWritableDatabase();
//        }
//        return db;
//    }
//
//    /**
//     * 关闭数据库
//     */
//    public static void CloseSqlDataBase() {
//
//        if (db != null && db.isOpen()) {
//            db.close();
//        }
//    }
//
//
//    /**
//     * 项数据库中插入数据
//     *
//     * @param context 上下文环境
//     * @param appInfo 要添加到数据库中的appinfo信息
//     */
//    public static void InsertDataBase(Context context, AppInfo appInfo) {
//
//
//        ContentValues values = new ContentValues();
//        //数据库中添加appname
//        values.put(Config.APP_NAME, appInfo.getAppName());
//        //数据库中添加packagename
//        values.put(Config.APP_PACKAGE_NAME, appInfo.getPackageName());
//        //数据库中添加ico
//        values.put(Config.APP_ICO, Util_MyDrawable2Byte.DrawableToBytes(appInfo.getIcon()));
//
//        //数据库中添加备份appname
//        values.put(Config.DEFAULT_APP_NAME, appInfo.getAppName());
//
//        //数据库中添加备份ico
//        values.put(Config.DEFAULT_APP_ICO, Util_MyDrawable2Byte.DrawableToBytes(appInfo.getIcon()));
//
//        GetDatabase(context).insert(Config.APP_TABLE_NAME, null, values);
//
//        System.out.println("插入数据成功");
//    }
//
//    /**
//     * 从数据库中查询数据并添加到list中
//     *
//     * @param context            上下文环境
//     * @param select_AppInfoList 查询到的数据所要添加的链表
//     */
//    public static void SetDatabaseToList(Context context, List<AppInfo> select_AppInfoList) {
//
//        Cursor cursor = GetDatabase(context).query(Config.APP_TABLE_NAME,
//                new String[]{Config.APP_NAME, Config.APP_PACKAGE_NAME, Config.APP_ICO},
//                null, null, null, null, null);
//
//        while (cursor.moveToNext()) {
//
//            String app_name = cursor.getString(cursor.getColumnIndex(Config.APP_NAME));
//            String app_package_name =
//                    cursor.getString(cursor.getColumnIndex(Config.APP_PACKAGE_NAME));
//            byte[] app_ico = cursor.getBlob(cursor.getColumnIndex(Config.APP_ICO));
//
//            AppInfo appInfo =
//                    new AppInfo(app_name, app_package_name,
//                            Util_MyDrawable2Byte.Bytes2Drawable(app_ico));
//            select_AppInfoList.add(appInfo);
//        }
//        cursor.close();
//    }
//
//    /**
//     * 判断数据库中是否存在指定包名的程序
//     *
//     * @param context      上下文环境
//     * @param package_name 查询的包名
//     * @return 返回是否存在
//     */
//    public static boolean Is_Package_Name_Table(Context context, String package_name) {
//
//        Cursor cursor = GetDatabase(context).query(Config.APP_TABLE_NAME, null,
//                Config.APP_PACKAGE_NAME + "=?",
//                new String[]{package_name}, null, null, null, null);
//
//        boolean a = cursor.moveToNext();
//
//        cursor.close();
//
//        return a;
//    }
//
//    /**以指定包名从数据库中查询出信息，并返回app info
//     * @param context
//     * @param package_name
//     * @return
//     */
//    public static AppInfo Get_Package_Name_To_AppInfo(Context context, String package_name) {
//
//        AppInfo appInfo = new AppInfo();
//
//        Cursor cursor = GetDatabase(context).query(Config.APP_TABLE_NAME,null,
//                Config.APP_PACKAGE_NAME + "=?",
//                new String[]{package_name}, null, null, null, null);
//
//        cursor.moveToNext();
//
//        appInfo.setAppName(cursor.getString(cursor.getColumnIndex(Config.DEFAULT_APP_NAME)));
//
//        appInfo.setPackageName(package_name);
//
//        appInfo.setIcon(Util_MyDrawable2Byte
//                .Bytes2Drawable(cursor.getBlob(cursor.getColumnIndex(Config.DEFAULT_APP_ICO))));
//
//        return appInfo;
//    }
//
//    /**
//     * 删除数据库中指定包名的数据
//     *
//     * @param context      上下文环境
//     * @param package_name 所要删除的包名
//     */
//    public static void DeleteDatabase(Context context, String package_name) {
//        GetDatabase(context).delete(Config.APP_TABLE_NAME,
//                Config.APP_PACKAGE_NAME + "=?", new String[]{package_name});
//    }
//
//    /**
//     * 修改数据库中程序的name、ico、
//     * 同时修改
//     *
//     * @param context      上下文环境
//     * @param package_name 需要修改的程序包名
//     * @param app_name     修改成appname
//     * @param app_ico      修改成appico
//     */
//    public static void UpDateDataBase(Context context, String package_name, String app_name,
//                                      Drawable app_ico) {
//
//        ContentValues values = new ContentValues();
//        values.put(Config.APP_NAME, app_name);
//        values.put(Config.APP_ICO, Util_MyDrawable2Byte.DrawableToBytes(app_ico));
//
//        GetDatabase(context).update(Config.APP_TABLE_NAME, values, Config.APP_PACKAGE_NAME + "=?",
//                new String[]{package_name});
//
//    }
//
//    /**
//     * 修改数据库中程序的name、ico、
//     * 只修改app名称
//     *
//     * @param context      上下文环境
//     * @param package_name 需要修改的程序的包名
//     * @param app_name     修改成appname
//     */
//    public static void UpDateDataBase(Context context, String package_name, String app_name) {
//
//        ContentValues values = new ContentValues();
//        values.put(Config.APP_NAME, app_name);
//
//        GetDatabase(context).update(Config.APP_TABLE_NAME, values, Config.APP_PACKAGE_NAME + "=?",
//                new String[]{package_name});
//    }
//
//    /**
//     * 修改数据库中程序的name、ico、
//     * 只修改app的ico
//     *
//     * @param context      上下文环境
//     * @param package_name 需要修改的包名
//     * @param app_ico      修改成appico
//     */
//    public static void UpDateDataBase(Context context, String package_name, Drawable app_ico) {
//
//        ContentValues values = new ContentValues();
//        values.put(Config.APP_ICO, Util_MyDrawable2Byte.DrawableToBytes(app_ico));
//
//        GetDatabase(context).update(Config.APP_TABLE_NAME, values, Config.APP_PACKAGE_NAME + "=?",
//                new String[]{package_name});
//    }
//}
