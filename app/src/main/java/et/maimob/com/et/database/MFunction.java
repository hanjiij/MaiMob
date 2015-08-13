package et.maimob.com.et.database;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

//功能
public class MFunction {
	public String name;
	public int icon;
	public int id;
	public Context mContext;
	
	public MFunction(ContentValues cv, Context ctx) {
		this.mContext = ctx;
		this.name 	=  	cv.getAsString("name");
		this.icon 	= 	cv.getAsInteger("icon");
		this.id 		= 	cv.getAsInteger("id");
	}
	
	@Override
	public String toString() {
		return "id  " + id  +"name  " + name;
	}
	
	public static void setFunc(Context ctx,int id , String name, int icon){
		DBHelper db = DBHelper.getInstance(ctx);

		if(db.configFunctionable())
		{
			return;
		}
		else{
			db.insertFunction(id, name, icon);
		}

	}
	
	public static List<MFunction> getFunctions(Context ctx){
		DBHelper db = DBHelper.getInstance(ctx);
		List<MFunction> func_list = new ArrayList<MFunction>();
		List<Integer> id_list = db.findFunctionIds();
		if (id_list != null) {
			for (int i = 0; i < id_list.size(); i++) {/////
				func_list.add(new MFunction(db.findFunctionById(id_list.get(i)), ctx));
			}
		}
		return func_list;
	}

	public static boolean delFunc(Context context,int index,List<MFunction> list)
	{
		try {
			DBHelper db = DBHelper.getInstance(context);
			db.delFunction(index,list);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static void setCommonFunc(Context ctx,int index,int id){
		DBHelper db = DBHelper.getInstance(ctx);
		db.updateMyFunc(index, id);
	}
	
}
