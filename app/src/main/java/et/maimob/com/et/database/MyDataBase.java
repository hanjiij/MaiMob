package et.maimob.com.et.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import et.maimob.com.et.Config;


/**
 * Created by HJ on 2015/7/8.
 */
public class MyDataBase
        extends SQLiteOpenHelper {


    public MyDataBase(Context context, String name, SQLiteDatabase.CursorFactory factory,
                      int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
    }

    public MyDataBase(Context context, String name, int version) {
        this(context, name, null, version);
        // TODO Auto-generated constructor stub
    }

    public MyDataBase(Context context, String name) {
        this(context, name, 1);
        // TODO Auto-generated constructor stub
    }


    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL(
                "create table " + Config.APP_TABLE_NAME + " (" + Config.ID +
                        " integer primary key," + Config.APP_NAME +
                        " text," + Config.APP_PACKAGE_NAME + " text not null," + Config.APP_ICO +
                        " BLOB," + Config.DEFAULT_APP_NAME + " text," + Config.DEFAULT_APP_ICO +
                        " BLOB" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
