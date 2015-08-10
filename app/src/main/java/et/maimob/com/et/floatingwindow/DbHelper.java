package et.maimob.com.et.floatingwindow;

/**
 * Created by jhj_Plus on 2015/7/27.
 */
//public class DbHelper {
//    private static final String TAG = "DbHelper";
//    private static final String[] PKGNAMES =
//            {"game", "music", "adobe", "avast", "earth", "fq", "templerun", "wifi"};
//    private static final String[] NAMES =
//            {"game", "music", "adobe", "avast", "earth", "fq", "templerun", "wifi"};
//    private static final int[] ICONIDS =
//            {R.mipmap.ic_game, R.mipmap.ic_music, R.mipmap.icon_adobe_air, R.mipmap.icon_avast,
//             R.mipmap.icon_earth, R.mipmap.icon_fq, R.mipmap.icon_templerun, R.mipmap.icon_wifi};
//
//    public static List<AppInfo> getAppsInfo(Context context) {
//        List<AppInfo> appsInfo = new ArrayList<AppInfo>();
//        DBHelper dbHelper = DBHelper.getInstance(context);
//        testInsertApps(context, dbHelper);
//        List<ContentValues> list_values = dbHelper.findMyApps();
//        for (ContentValues values : list_values) {
//            com.jhj.dev.fw.mainpanel.AppInfo appInfo =
//                    new com.jhj.dev.fw.mainpanel.AppInfo(values.getAsString("name"), values.getAsString("packageName"),
//                                byte2Bitmap(values.getAsByteArray("icon")));
//            appsInfo.add(appInfo);
//        }
//        return appsInfo;
//    }
//
//    private static void testInsertApps(Context context, DBHelper dbHelper) {
//        dbHelper.insertMyApp(PKGNAMES, NAMES, bitmap2Byte(context));
//    }
//
//    private static byte[][] bitmap2Byte(Context context) {
//        byte[][] icons = new byte[8][];
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        for (int i = 0; i < ICONIDS.length; i++) {
//            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), ICONIDS[i]);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
//            icons[i] = bos.toByteArray();
//            bos.reset();
//        }
//        return icons;
//    }
//
//    private static Bitmap byte2Bitmap(byte[] bytes)
//    {
//        if (bytes.length != 0) {
//            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//        }
//        return null;
//    }
//}
