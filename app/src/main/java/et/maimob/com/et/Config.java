package et.maimob.com.et;

/**
 * Created by HJ on 2015/7/10.
 * <p/>
 * 变量存储，工具类
 */
public class Config {

    public static String THEME_LIST_INFO = "theme_list_info";
    public static String THEME_LIST_USED_ITEM = "theme_list_used_item";
    public static String IS_FIRST_START = "is_first_start";

    public static String SHORTCUT_CONFIG = "shortcut_config";

    public static String ID = "id";
    public static String APP_NAME = "app_name";
    public static String APP_PACKAGE_NAME = "app_package_name";
    public static String APP_ICO = "app_ico";
    public static String IS_SYSTEM_APP = "is_system_app";
    public static String APP_TABLE_NAME = "AppInfo";
    public static String DEFAULT_APP_NAME = "default_app_name";
    public static String DEFAULT_APP_ICO = "default_app_ico";

    public static String IS_STYLE = "is_style";
    public static String IS_ITEM_TWO = "is_item_two";
    public static String IS_ITEM_NINE = "is_item_nine";

    public static String IS_FLOATING_WINDOW = "is_floating_window";
    public static String IS_TWO_OPEN = "is_two_open";
    public static String IS_NINE_OPEN = "is_nine_open";

    public static String IS_APP_STYLE_IMAGE1 = "is_app_style_image1";
    public static String IS_APP_STYLE_IMAGE2 = "is_app_style_image2";
    public static String IS_APP_STYLE_FUNCTION_ = "is_app_style_function_";

    public static int[] function_black_image_resources =
            {R.drawable.icon_func_wifi, R.drawable.icon_func_net, R.drawable.icon_func_bluetooth,
                    R.drawable.icon_func_screen, R.drawable.icon_func_light,
                    R.drawable.icon_func_system, R.drawable.icon_func_app,
                    R.drawable.icon_func_hotspot, R.drawable.icon_func_air,
                    R.drawable.icon_func_mute, R.drawable.icon_func_gps};

    public static int[] function_white_image_resources =
            {R.drawable.icon_func_wifi_click, R.drawable.icon_func_net_click,
                    R.drawable.icon_func_bluetooth_click, R.drawable.icon_func_screen_click,
                    R.drawable.icon_func_light_click, R.drawable.icon_func_system_click,
                    R.drawable.icon_func_app_click, R.drawable.icon_func_hotspot_click,
                    R.drawable.icon_func_air_click, R.drawable.icon_func_mute_click,
                    R.drawable.icon_func_gps_click};


    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375) {
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }
}
