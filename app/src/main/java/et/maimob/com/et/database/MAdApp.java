package et.maimob.com.et.database;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//推荐应用		先不用写他的数据库。等服务器配置好再写
public class MAdApp {
	public int id;
	public String name;
	public String icon;
	public String url;
	public int isInstall;
	public int isDownload;
	public String size;
	public String version;
	public Context mContext;
	
	public MAdApp(Context ctx, ContentValues cv) {
		this.mContext  	=  	ctx;
		this.name		=	cv.getAsString("name");
		this.id			= 	cv.getAsInteger("id");
		this.icon		=	cv.getAsString("icon");
		this.url			=	cv.getAsString("url");
		this.isInstall	=	cv.getAsInteger("isInstall");
		this.isDownload	=	cv.getAsInteger("isDownload");
		this.size		=	cv.getAsString("size");
		this.version 	= 	cv.getAsString("version");
	}
	
	//服务器没有搭建好，无法用	,可以自己拼写出一个json数组出来插入
	
	//插入推广App信息
	public static void insertAdAppInfo(Context ctx, JSONArray jarray){
		DBHelper dbHelper = DBHelper.getInstance(ctx);
		Log.i("FFF", "~!~!~!  " + jarray.toString());
		for (int i = 0; i < jarray.length(); i++) {
			JSONObject jobj = jarray.optJSONObject(i);
			dbHelper.checkAdAppInfo(jobj);
		}
	}
	
	//得到推广的App
	public static List<MAdApp> getAdApps(Context ctx, List<Integer> adappId){
		List<MAdApp> adapps = new ArrayList<MAdApp>();
		DBHelper db = DBHelper.getInstance(ctx);
		for (int i = 0; i < adappId.size(); i++) {
			ContentValues cv = db.findAdAppInfoById(adappId.get(i));
			MAdApp adApp = new MAdApp(ctx, cv);
			adapps.add(adApp);
		}
		return adapps;
	}
	
	
}
