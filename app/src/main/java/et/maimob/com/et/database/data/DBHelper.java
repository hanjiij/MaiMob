package et.maimob.com.et.database.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;


import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper mInstance = null;
    private static final String DB_NAME = "et.db";
    private static final int DB_VERSION = 1;
    //推荐应用表/搜索返回应用表
    private static final String CREATE_ADAPP = "CREATE TABLE IF NOT EXISTS adapp (" +
            "id integer," +
            "name varchar," +
            "icon blob," +
            "url varchar," +
            "isInstall integer," +
            "isDownload integer," +
            "size varchar," +
            "version varcahr" +
            ")";
    //主题表
    private static final String CREATE_THEME = "CREATE TABLE IF NOT EXISTS theme (" +
            "id integer," +
            "name varchar," +
            "smallImgUrl varchar," +
            "smallImgPath varchar," +
            "bigImgUrl varchar," +
            "bigImgPath varchar," +
            "isDownload integer," +
            "isInstall integer," +
            "price integer," +
            "icon blob," +
            "url varchar" +
            ")";
    //功能表
    private static final String CREATE_FUNCTION = "CREATE TABLE IF NOT EXISTS function (" +
            "id integer," +
            "name varchar," +
            "icon integer" +
            ")";

    //配置表
    private static final String CREATE_CONFIG = "CREATE TABLE IF NOT EXISTS config (" +
            "appStyle integer," +
            "functionOpen integer," +
            "theme integer," +
            "AppName1 varchar," +
            "AppPackage1 varchar," +
            "AppIcon1 BLOB," +

            "AppName2 varchar," +
            "AppPackage2 varchar," +
            "AppIcon2 blob," +

            "AppName3 varchar," +
            "AppPackage3 varchar," +
            "AppIcon3 blob," +

            "AppName4 varchar," +
            "AppPackage4 varchar," +
            "AppIcon4 blob," +

            "AppName5 varchar," +
            "AppPackage5 varchar," +
            "AppIcon5 blob," +

            "AppName6 varchar," +
            "AppPackage6 varchar," +
            "AppIcon6 blob," +

            "AppName7 varchar," +
            "AppPackage7 varchar," +
            "AppIcon7 blob," +

            "AppName8 varchar," +
            "AppPackage8 varchar," +
            "AppIcon8 blob," +

            "MyFunction1 integer," +
            "MyFunction2 integer," +
            "MyFunction3 integer," +
            "MyFunction4 integer" +
            ")";

    public DBHelper(Context context, String name, CursorFactory factory,
                    int version) {
        super(context, DB_NAME, null, DB_VERSION);
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(CREATE_CONFIG);
        db.execSQL(CREATE_FUNCTION);
        db.execSQL(CREATE_ADAPP);
        db.execSQL(CREATE_THEME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CONFIG);
        db.execSQL(CREATE_FUNCTION);
        db.execSQL(CREATE_ADAPP);
        db.execSQL(CREATE_THEME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    static synchronized DBHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new DBHelper(ctx, DB_NAME, null, DB_VERSION);
        }
        return mInstance;
    }

    /**
     * Function
     */
    public final boolean configFunctionable()
    {

            SQLiteDatabase db = getWritableDatabase();
            Cursor cursor=db.rawQuery("select * from function",null);
            if(cursor.getCount()>10)
            {
                return true;
            }else {
                return false;
            }

    }
    //插入功能信息
    public final boolean insertFunction(int id, String name, int icon) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put("id", id);
            cv.put("name", name);
            cv.put("icon", icon);
            db.insert("Function", null, cv);
            db.close();
            return true;
        } catch (Exception e) {
            db.close();
            return false;
        }
    }

    //通过Id得到功能

    public final ContentValues findFunctionById(int id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        Cursor c = null;
        try {
            c = db.rawQuery("select * from function where id = ?", new String[]{String.valueOf(id)});
            if (c.moveToFirst()) {
                cv.put("id", c.getInt(c.getColumnIndex("id")));
                cv.put("name", c.getString(c.getColumnIndex("name")));
                cv.put("icon", c.getInt(c.getColumnIndex("icon")));
            }
            c.close();
            db.close();
        } catch (Exception e) {
            if (c != null) {
                c.close();
            }
            db.close();
        }
        return cv;
    }

    /**
     * AdApp
     */

    //插入/更新推荐App/搜索app的信息
    public final boolean checkAdAppInfo(JSONObject jobj) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = null;
        try {
            c = db.rawQuery("select * from adapp where id = ?", new String[]{String.valueOf(jobj.getInt("aid"))});
            if (c.moveToFirst()) {
                db.execSQL("update adapp set name = ?, icon = ?, url = ?, size = ?, version = ? where id = ?",
                        new String[]{jobj.getString("name"), jobj.getString("icon"), jobj.getString("url"),
                                jobj.getString("size"), jobj.getString("version"), String.valueOf(jobj.getInt("aid"))});
            } else {
                ContentValues cv = new ContentValues();
                cv.put("id", jobj.getInt("aid"));
                cv.put("name", jobj.getString("name"));
                cv.put("icon", jobj.getString("icon"));
                cv.put("url", jobj.getString("url"));
                cv.put("size", jobj.getString("size"));
                cv.put("version", jobj.getString("version"));
                db.insert("adapp", null, cv);
            }
            c.close();
            db.close();
            return true;
        } catch (Exception e) {
            if (c != null) {
                c.close();
            }
            db.close();
            return false;
        }
    }

    /**
     * 删除一个常用App
     * @param index	常用App的删除位置 1~8
     * @param apps	当前常用App集合
     * @return
     */
    public boolean delMyApp(int index , List<MAPP> apps){
        SQLiteDatabase db = getWritableDatabase();
        String packageName = null;
        String name = null;
        byte[] icon = null;
        String packageIndex;
        String nameIndex;
        String iconIndex;
        try {
            for (int i = index; i < apps.size(); i++) {
                MAPP app = apps.get(i);
                packageName = app.packageName;
                name = app.name;
                icon = app.icon;

                packageIndex = "AppPackage" + i;
                nameIndex = "AppName" + i;
                iconIndex = "AppIcon" + i;

                ContentValues cv = new ContentValues();
                cv.put(packageIndex, packageName);
                cv.put(nameIndex, name);
                cv.put(iconIndex, icon);
                db.update("config",cv,null,null);
            }
            packageName = null;
            icon = null;
            name = null;
            packageIndex = "AppPackage" + apps.size();
            nameIndex = "AppName" + apps.size();
            iconIndex = "AppIcon" + apps.size();
            ContentValues cv_end = new ContentValues();
            cv_end.put(packageIndex, packageName);
            cv_end.put(iconIndex, icon);
            cv_end.put(nameIndex, name);
            db.update("config", cv_end, null, null);

        } catch (Exception e) {
            db.close();
            return false;
        }
        db.close();
        return true;
    }

    /**
     * @param index
     * @param list
     * @return
     */
    //删除常用功能
    public boolean delFunction(int index,List<MFunction> list)
    {
        SQLiteDatabase db = getWritableDatabase();
//        String name=null;
        int id=0;
//        int icon=0;
        String fundelIndex;
        try {
            for (int i=index;i<list.size();i++)
            {
                MFunction mFunction=list.get(i);
                id=mFunction.id;
                fundelIndex="MyFunction"+i;
                ContentValues cv = new ContentValues();
                cv.put(fundelIndex, id);
                db.update("config", cv, null, null);
            }

            fundelIndex="MyFunction"+list.size();

            ContentValues cv_end = new ContentValues();
            cv_end.put(fundelIndex, "");
            db.update("config", cv_end, null, null);


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
      return true;
    }

    //获得推广/搜索的App信息
    public final ContentValues findAdAppInfoById(int id) {
        ContentValues cv = null;
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = null;
        try {
            c = db.rawQuery("select * from adapp where id = ?", new String[]{String.valueOf(id)});
            if (c.moveToFirst()) {
                cv = new ContentValues();
                cv.put("id", c.getInt(c.getColumnIndex("id")));
                cv.put("name", c.getString(c.getColumnIndex("name")));
                cv.put("icon", c.getString(c.getColumnIndex("icon")));
                cv.put("url", c.getString(c.getColumnIndex("url")));
                cv.put("isDownload", c.getInt(c.getColumnIndex("isDownload")));
                cv.put("isInstall", c.getInt(c.getColumnIndex("isInstall")));
                cv.put("size", c.getString(c.getColumnIndex("size")));
                cv.put("version", c.getString(c.getColumnIndex("version")));
            }
            c.close();
            db.close();
        } catch (Exception e) {
            if (c != null) {
                c.close();
            }
            db.close();
        }
        return cv;
    }

    /**
     * Theme
     */

    //插入主题信息
    @SuppressWarnings("null")
    public final boolean checkThemeInfo(JSONObject jobj) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = null;
        try {
            c = db.rawQuery("select * from theme where id = ?", new String[]{String.valueOf(jobj.getInt("tid"))});
            ContentValues cv = null;
            cv = new ContentValues();
            cv.put("name", jobj.getString("name"));
            cv.put("icon", jobj.getString("icon"));
            cv.put("url", jobj.getString("url"));
            cv.put("price", jobj.getString("price"));
            if (c.moveToFirst()) {
                db.update("theme", cv, "id = ?", new String[]{String.valueOf(jobj.getInt("tid"))});
            } else {
                cv.put("id", jobj.getInt("tid"));
                db.insert("theme", null, cv);
            }

            db.close();
            return true;
        } catch (Exception e) {
            db.close();
            return false;
        }
    }

    //主题下载完毕，更新theme表
    public final boolean updateThemeDownloadInfo(String sImgPath, String bImgPath, int id) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL("update theme set smallImgPath = ?, bigImgPath = ? where id = ?",
                    new String[]{sImgPath, bImgPath, String.valueOf(id)});
            db.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public final List<ContentValues> findThemes() {
        List<ContentValues> cv_List = new ArrayList<ContentValues>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = null;
        try {
            c = db.rawQuery("select * from theme", new String[]{});
            if (c.moveToFirst()) {
                do {
                    ContentValues cv = new ContentValues();
                    cv.put("id", c.getInt(c.getColumnIndex("id")));
                    cv.put("name", c.getString(c.getColumnIndex("name")));
                    cv.put("smallImgPath", c.getString(c.getColumnIndex("smallImgPath")));
                    cv.put("bigImgPath", c.getString(c.getColumnIndex("bigImgPath")));
                    cv.put("url", c.getString(c.getColumnIndex("url")));
                    cv.put("isDownload", c.getInt(c.getColumnIndex("isDownload")));
                    cv.put("price", c.getInt(c.getColumnIndex("price")));
                    cv.put("icon", c.getString(c.getColumnIndex("icon")));
                    cv_List.add(cv);
                } while (c.moveToNext());
            }
            c.close();
            db.close();
        } catch (Exception e) {
            cv_List = null;
            if (c != null) {
                c.close();
            }
            db.close();
        }
        return cv_List;
    }


    /**
     * Config
     */

    //更新App面板样式/常用功能是否开启/选择的主题
    public final boolean updateConfig(int type, int params) {
        SQLiteDatabase db = getWritableDatabase();
        String key = null;
        try {
            if (type == MHelper.TYPE_APPSTYLE) {
                key = "appStyle";
            } else if (type == MHelper.TYPE_FUNCTIONISOPEN) {
                key = "functionOpen";
            } else if (type == MHelper.TYPE_THEME) {
                key = "theme";
            }
            if (key != null) {
                db.execSQL("update config set " + key + " = ?", new String[]{String.valueOf(params)});
            }
            db.close();
            return true;
        } catch (Exception e) {
            db.close();
            return false;
        }
    }

    /** 获取样式，是否开启常用功能
     * @param type 静态常量 在----》（MHelper）
     * @return
     */
    public final int getconfig(int type)
    {
        SQLiteDatabase db = getWritableDatabase();
        String key = null;
        int valus = 0;
        try {
            if (type == MHelper.TYPE_APPSTYLE) {
                key = "appStyle";
                System.out.println("appStyle============>");
            }
            if (type == MHelper.TYPE_FUNCTIONISOPEN) {
                key = "functionOpen";
                System.out.println("functionOpen------------>");
            }
            if (type == MHelper.TYPE_THEME) {
                key = "theme";
                System.out.println("theme----------->");
            }
            if (key != null) {

                Cursor c = db.rawQuery("select * from config", new String[]{});
                if(  c.moveToNext())
                {
                    valus=c.getInt(c.getColumnIndex(key));
                }
            }
            db.close();
        } catch (Exception e) {
            db.close();
            System.out.println("出现异常········");

        }
        return valus;
    }

    //配置App样式
    public final boolean insertConfig(int appStyle) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = null;
        try {
            c = db.rawQuery("select * from config where appstyle != 0", new String[]{});
            if (c.moveToFirst()) {
//                ContentValues contentValues=new ContentValues();
//                contentValues.put("appStyle", String.valueOf(appStyle));
//                contentValues.put("functionOpen", 1);
//                contentValues.put("theme", 0);
//                db.update("config",contentValues,null,null);
//                System.out.println("c.moveToFirst()==============>"+c.moveToFirst());
            } else {
                ContentValues cv = new ContentValues();
                cv.put("appStyle", String.valueOf(appStyle));
                cv.put("functionOpen", 1);
                cv.put("theme", 0);
                cv.put("AppName1", "");
                cv.put("AppPackage1", "");
                cv.put("AppIcon1", "");

                cv.put("AppName2", "");
                cv.put("AppPackage2", "");
                cv.put("AppIcon2", "");

                cv.put("AppName3", "");
                cv.put("AppPackage3", "");
                cv.put("AppIcon3", "");

                cv.put("AppName4", "");
                cv.put("AppPackage4", "");
                cv.put("AppIcon4", "");

                cv.put("AppName5", "");
                cv.put("AppPackage5", "");
                cv.put("AppIcon5", "");

                cv.put("AppName6", "");
                cv.put("AppPackage6", "");
                cv.put("AppIcon6", "");

                cv.put("AppName7", "");
                cv.put("AppPackage7", "");
                cv.put("AppIcon7", "");

                cv.put("AppName8", "");
                cv.put("AppPackage8", "");
                cv.put("AppIcon8", "");

                cv.put("MyFunction1", "");
                cv.put("MyFunction2", "");
                cv.put("MyFunction3", "");
                cv.put("MyFunction4", "");

                db.insert("config", null, cv);
            }
            c.close();
            db.close();
            return true;
        } catch (Exception e) {
            if (c != null) {
                c.close();
            }
            db.close();
            return false;
        }
    }

    //更新我的应用
    public final boolean updateMyApp(String index, String packageName, String Name, byte[] icon) {
        SQLiteDatabase db = getWritableDatabase();
        String packageIndex = "AppPackage" + index;
        String nameIndex = "AppName" + index;
        String iconIndex = "AppIcon" + index;

        ContentValues cv = new ContentValues();
        cv.put(packageIndex, packageName);
        cv.put(nameIndex, Name);
        cv.put(iconIndex, icon);

        try {
            db.update("config", cv, null, null);
            db.close();
            return true;
        } catch (Exception e) {
            db.close();
            return false;
        }
    }



    //更新我的常用功能
    public final boolean updateMyFunc(int index, int id) {
        SQLiteDatabase db = getWritableDatabase();
        String functionIndex = "MyFunction" + index;
        String sql = "update config set " + functionIndex + " = ?";
        try {
            db.execSQL(sql, new String[]{String.valueOf(id)});
            System.out.println("db.execSQL==============>");
            db.close();
            return true;
        } catch (Exception e) {
            db.close();
            System.out.println("catch (Exception e)==============>");
            return false;
        }
    }

    //得到常用应用
    public final List<ContentValues> findMyApps() {
        List<ContentValues> cv_list = new ArrayList<ContentValues>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = null;
        try {
            c = db.rawQuery("select * from config", new String[]{});
            if (c.moveToFirst()) {
                for (int i = 1; i < 9; i++) {
                    String packageIndex = "AppPackage" + i;
                    String nameIndex = "AppName" + i;
                    String iconIndex = "AppIcon" + i;
                    String packageName = c.getString(c.getColumnIndex(packageIndex));
                    String name = c.getString(c.getColumnIndex(nameIndex));
                    byte[] icon = c.getBlob(c.getColumnIndex(iconIndex));
                    ContentValues cv = new ContentValues();
                    cv.put("name", name);
                    cv.put("icon", icon);
                    cv.put("packageName", packageName);
                    if (name != null && !"".equals(name)) {
                        cv_list.add(cv);
                    }
                }
                c.close();
                db.close();
            }
        } catch (Exception e) {
            cv_list = null;
            if (c != null) {
                c.close();
            }
            db.close();
        }
        return cv_list;
    }

    //得到已设置的常用功能
    public final List<Integer> findFunctionIds() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = null;
        List<Integer> id_list = new ArrayList<Integer>();
        try {
            c = db.rawQuery("select * from config", new String[]{});
            if (c.moveToFirst()) {
                for (int i = 1; i < 5; i++) {
                    String functionIndex = "MyFunction" + i;
                    int functionId = c.getInt(c.getColumnIndex(functionIndex));
                    if (functionId != 0) {
                        id_list.add(functionId);
                    }
                }
            }
            c.close();
            db.close();
        } catch (Exception e) {
            if (c != null) {
                c.close();
            }
            db.close();
            id_list = null;
        }
        return id_list;
    }
}
