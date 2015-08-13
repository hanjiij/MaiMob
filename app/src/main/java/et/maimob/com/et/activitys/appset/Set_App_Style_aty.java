package et.maimob.com.et.activitys.appset;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import et.maimob.com.et.Config;
import et.maimob.com.et.IMainPanelDataChange;
import et.maimob.com.et.R;
import et.maimob.com.et.database.DateUtils;
import et.maimob.com.et.database.MHelper;


public class Set_App_Style_aty
        extends ActionBarActivity {

    private ImageView app_style_two_1lv, app_style_two_2lv, app_style_nine_1lv, app_style_nine_2lv;
    private CheckBox app_style_two_1checkbox, app_style_two_2checkbox, app_style_nine_1checkbox,
            app_style_nine_2checkbox;

//    private SharedPreferences sharedPreferences;  // 获取记录是否被点击
//    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set__app__style_aty);


        init();

        setListener();
    }

    private void init() {
        app_style_two_1lv = (ImageView) findViewById(R.id.app_style_two_1lv);
        app_style_two_2lv = (ImageView) findViewById(R.id.app_style_two_2lv);
        app_style_nine_1lv = (ImageView) findViewById(R.id.app_style_nine_1lv);
        app_style_nine_2lv = (ImageView) findViewById(R.id.app_style_nine_2lv);

        app_style_two_1checkbox = (CheckBox) findViewById(R.id.app_style_two_1checkbox);
        app_style_two_2checkbox = (CheckBox) findViewById(R.id.app_style_two_2checkbox);
        app_style_nine_1checkbox = (CheckBox) findViewById(R.id.app_style_nine_1checkbox);
        app_style_nine_2checkbox = (CheckBox) findViewById(R.id.app_style_nine_2checkbox);

//        sharedPreferences = getSharedPreferences(Config.THEME_LIST_INFO,
//                Activity.MODE_PRIVATE);
//        editor = sharedPreferences.edit();

//        boolean is_style =
//                sharedPreferences.getString(Config.IS_STYLE, "two").equals("two");  // 判断选择的主题
//        boolean is_item_two =
//                sharedPreferences.getBoolean(Config.IS_ITEM_TWO, false);   // 判断主题一是否勾选
//        boolean is_item_nine = sharedPreferences.getBoolean(Config.IS_ITEM_NINE, false);


        boolean is_style = DateUtils.getconfig(Set_App_Style_aty.this, MHelper.TYPE_APPSTYLE) == 1;

        boolean is_item_two =
                DateUtils.getSharedPreference(Set_App_Style_aty.this, Config.IS_TWO_OPEN) != 0;
        boolean is_item_nine =
                DateUtils.getSharedPreference(Set_App_Style_aty.this, Config.IS_NINE_OPEN) != 0;

        app_style_two_1checkbox.setChecked(is_style);
        app_style_two_2checkbox.setChecked(is_item_two);
        app_style_two_2checkbox.setEnabled(is_style);

        app_style_nine_1checkbox.setChecked(!is_style);
        app_style_nine_2checkbox.setChecked(is_item_nine);
        app_style_nine_2checkbox.setEnabled(!is_style);


        if (is_style) {

            app_style_two_1lv.setAlpha(0);

            if (is_item_two) {
                app_style_two_2lv.setAlpha(0);
            }
        } else {

            app_style_nine_1lv.setAlpha(0);

            if (is_item_nine) {
                app_style_nine_2lv.setAlpha(0);
            }
        }

    }

    private void setListener() {

        //两行的checkbox1选择监听
        app_style_two_1checkbox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        IMainPanelDataChange.getInstance().changeMainPanelStyle(isChecked ? 1 : 2,
                                isChecked ? app_style_two_2checkbox.isChecked() :
                                        app_style_nine_2checkbox.isChecked());

                        DateUtils.setConfig(Set_App_Style_aty.this, isChecked ? 1 : 2,
                                MHelper.TYPE_APPSTYLE);

                        DateUtils.setConfig(Set_App_Style_aty.this,
                                isChecked ? (app_style_two_2checkbox.isChecked() ? 1 : 0) :
                                        (app_style_nine_2checkbox.isChecked() ? 1 : 0),
                                MHelper.TYPE_FUNCTIONISOPEN);

                        if (isChecked) {

                            app_style_two_1lv.setAlpha(0);

                            if (app_style_two_2checkbox.isChecked()) {
                                app_style_two_2lv.setAlpha(0);
                            }

                            app_style_nine_1checkbox.setChecked(!isChecked);
                            app_style_two_2checkbox.setEnabled(true);

                        } else {

                            app_style_two_1lv.setAlpha(80);
                            app_style_two_2lv.setAlpha(80);

                            app_style_nine_1checkbox.setChecked(!isChecked);
                            app_style_two_2checkbox.setEnabled(false);
                        }

//                        editor.putString(Config.IS_STYLE, "two");  // 写入样式选择
//                        editor.commit();
                    }
                });

        //两行的checkbox2选择监听
        app_style_two_2checkbox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                        IMainPanelDataChange.getInstance().changeMainPanelStyle(1, isChecked);

                        DateUtils.setConfig(Set_App_Style_aty.this, isChecked ? 1 : 0,
                                MHelper.TYPE_FUNCTIONISOPEN);

                        if (isChecked) {
                            app_style_two_2lv.setAlpha(0);

//                            editor.putBoolean(Config.IS_ITEM_TWO, true);  // 写入样式选择
//                            editor.commit();
                        } else {
                            app_style_two_2lv.setAlpha(80);

//                            editor.putBoolean(Config.IS_ITEM_TWO, false);  // 写入样式选择
//                            editor.commit();
                        }

                        DateUtils.setSharedPreference(Set_App_Style_aty.this, Config.IS_TWO_OPEN,
                                isChecked ? 1 : 0);
                    }
                });

        //九宫格checkbox1选择监听
        app_style_nine_1checkbox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (isChecked) {

                            app_style_nine_1lv.setAlpha(0);

                            if (app_style_nine_2checkbox.isChecked()) {
                                app_style_nine_2lv.setAlpha(0);
                            }

                            app_style_two_1checkbox.setChecked(!isChecked);
                            app_style_nine_2checkbox.setEnabled(true);

                        } else {

                            app_style_nine_1lv.setAlpha(80);
                            app_style_nine_2lv.setAlpha(80);

                            app_style_two_1checkbox.setChecked(!isChecked);
                            app_style_nine_2checkbox.setEnabled(false);
                        }
                    }
                });

        //九宫格checkbox2选择监听
        app_style_nine_2checkbox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                        IMainPanelDataChange.getInstance().changeMainPanelStyle(2, isChecked);

                        DateUtils.setConfig(Set_App_Style_aty.this, isChecked ? 1 : 0,
                                MHelper.TYPE_FUNCTIONISOPEN);

                        if (isChecked) {

                            app_style_nine_2lv.setAlpha(0);
//                            editor.putBoolean(Config.IS_ITEM_NINE, true);  // 写入样式选择
//                            editor.commit();
                        } else {

                            app_style_nine_2lv.setAlpha(80);
//                            editor.putBoolean(Config.IS_ITEM_NINE, false);  // 写入样式选择
//                            editor.commit();
                        }

                        DateUtils.setSharedPreference(Set_App_Style_aty.this, Config.IS_NINE_OPEN,
                                isChecked ? 1 : 0);
                    }
                });
    }
}
