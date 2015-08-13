package et.maimob.com.et.database;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;
//常用App
public class MAPP {
	public String packageName;
	public String name;
	public byte[] icon;
	
	public MAPP(ContentValues cv) {
		this.packageName 	= cv.getAsString("packageName");
		this.name 			= cv.getAsString("name");
		this.icon			= cv.getAsByteArray("icon");
	}
	
	public static void setAppInfo(Context ctx,String index, String packageName, String name, byte[] icon){
		DBHelper db = DBHelper.getInstance(ctx);
		db.updateMyApp(index, packageName, name, icon);
	}
	
	public static void setConfig(Context ctx, int params, int type){
		DBHelper db = DBHelper.getInstance(ctx);
		db.updateConfig(type, params);
	}
	public static int getstyeorfunisopen(Context ctx,int type)
	{
		DBHelper db = DBHelper.getInstance(ctx);
		int value=db.getconfig(type);
		return value;
	}
	public static void setStyleInfo(Context ctx, int appStyle){
		DBHelper db = DBHelper.getInstance(ctx);
		db.insertConfig(appStyle);
	}

	public static boolean upDateMyApp(Context context,String index, String packageName, String Name, byte[] icon)
	{
		try {
			DBHelper db = DBHelper.getInstance(context);
			db.updateMyApp(index,packageName,Name,icon);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public static List<MAPP> getApps(Context ctx){
		DBHelper db = DBHelper.getInstance(ctx);
		List<ContentValues> cv_List = db.findMyApps();
		List<MAPP> app_list = new ArrayList<MAPP>();
		if (cv_List != null) {
			for (int i = 0; i < cv_List.size(); i++) {
//				更改构造方法MAPP(ContentValues cv)这种形式 ——>改成你们自己要用的MAPP(String name,byte[] icon , String packageName,....)
//				String name = cv_List.get(i).getAsString("name");
//				String packageName = cv_List.get(i).getAsString("packageName");
//				byte[] icon = cv_List.get(i).getAsByteArray("icon");
//				MAPP mApp = new MAPP(name, icon, packageName,...);
				app_list.add(new MAPP(cv_List.get(i)));
			}			
		}
		return app_list;
	}

	public static boolean delMyApp(Context context,int index,List<MAPP> list)
	{
		try {
			DBHelper db = DBHelper.getInstance(context);
			db.delMyApp(index,list);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	
}
