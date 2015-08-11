package et.maimob.com.et.activitys;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import et.maimob.com.et.R;
import et.maimob.com.et.adapter.MyThemeAdapter;
import et.maimob.com.et.datatype.ThemeInfo;

public class My_Theme_aty
        extends ActionBarActivity {

    private ListView theme_listview;
    private MyThemeAdapter themeAdapter;
    private List<ThemeInfo> themeInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__theme_aty);


        //测试
        init();

        setListener();

    }

    private void init() {

        theme_listview = (ListView) findViewById(R.id.my_theme_listveiw);

        themeInfos = new ArrayList<>();

        setThemeInfosData();

        themeAdapter = new MyThemeAdapter(My_Theme_aty.this, themeInfos);


        theme_listview.setAdapter(themeAdapter);

    }


    private void setThemeInfosData() {

        for (int i = 0; i < 20; i++) {
            ThemeInfo themeInfo = new ThemeInfo(getResources().getDrawable(R.drawable.ic_launcher),
                    i + ":" + "主题的标题为" + i, "主题的内容为" + i);

            themeInfos.add(themeInfo);
        }
    }

    private void setListener() {

    }
}
