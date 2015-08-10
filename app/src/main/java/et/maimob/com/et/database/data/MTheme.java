package et.maimob.com.et.database.data;

import android.content.ContentValues;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//主题
public class MTheme {
	public int id;
	public String smallImgPath;
	public String bigImgPath;
	public int isDownload;
	public int price;
	public String icon;
	public String url;
	public String name;
	
	@Override
	public String toString() {
		return "name~~~  " + name + "~~~ price  " + price;
	}
	
	public MTheme(ContentValues cv) {
		this.id 				= cv.getAsInteger("id");
		this.smallImgPath 	= cv.getAsString("smallImgPath");
		this.bigImgPath		= cv.getAsString("bigImgPath");
		this.isDownload		= cv.getAsInteger("isDownload");
		this.price			= cv.getAsInteger("price");
		this.icon			= cv.getAsString("icon");
		this.url				= cv.getAsString("url");
		this.name			= cv.getAsString("name");
	}
	
	public static void insertThemeInfo(Context ctx,JSONArray jsonArray){
		DBHelper db = DBHelper.getInstance(ctx);
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject obj = jsonArray.optJSONObject(i);
			db.checkThemeInfo(obj);
		}
	}
	
	public static List<MTheme> getThemes(Context ctx){
		DBHelper dbHelper = DBHelper.getInstance(ctx);
		List<MTheme> theme_list = new ArrayList<MTheme>();
		List<ContentValues> cv_list = dbHelper.findThemes();
		if (cv_list != null) {
			for (int i = 0; i < cv_list.size(); i++) {
				theme_list.add(new MTheme(cv_list.get(i)));
			}
		}else {
			cv_list = null;
		}
		return theme_list;
	}
}
