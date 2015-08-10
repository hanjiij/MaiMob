package et.maimob.com.et.datatype;

import android.graphics.drawable.Drawable;

/**
 * Created by HJ on 2015/7/9.
 * 主题信息类
 */
public class ThemeInfo {

    private Drawable themo_ico;
    private String theme_title;
    private String theme_content;

    public ThemeInfo() {
    }

    public ThemeInfo(Drawable themo_ico, String theme_title, String theme_content) {
        this.themo_ico = themo_ico;
        this.theme_title = theme_title;
        this.theme_content = theme_content;
    }

    public Drawable getTheme_ico() {
        return themo_ico;
    }

    public void setThemo_ico(Drawable themo_ico) {
        this.themo_ico = themo_ico;
    }

    public String getTheme_title() {
        return theme_title;
    }

    public void setTheme_title(String theme_title) {
        this.theme_title = theme_title;
    }

    public String getTheme_content() {
        return theme_content;
    }

    public void setTheme_content(String theme_content) {
        this.theme_content = theme_content;
    }
}
