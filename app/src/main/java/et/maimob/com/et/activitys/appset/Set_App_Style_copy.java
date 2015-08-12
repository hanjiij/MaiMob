package et.maimob.com.et.activitys.appset;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import et.maimob.com.et.Config;
import et.maimob.com.et.R;
import et.maimob.com.et.adapter.AppStyleAdapter;

public class Set_App_Style_copy
        extends ActionBarActivity {

    List<Map<String,Integer>> app_style_info;

    int [] imageresource1={R.drawable.two1,R.drawable.two1,R.drawable.two1};
    int [] imageresource2={R.drawable.two2,R.drawable.two2,R.drawable.two2};


    ListView app_style_listview;
    AppStyleAdapter appStyleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set__app__style_copy);

        app_style_info=new ArrayList<>();

        initListData();

        app_style_listview = (ListView) findViewById(R.id.app_style_listview);

        appStyleAdapter=new AppStyleAdapter(Set_App_Style_copy.this,app_style_info);

        app_style_listview.setAdapter(appStyleAdapter);
    }

    /**
     * 初始化链表数据
     */
    private void initListData() {

        for (int i = 0; i < 3; i++) {

            Map<String,Integer> map=new HashMap<>();

            map.put(Config.IS_APP_STYLE_IMAGE1,imageresource1[i]);
            map.put(Config.IS_APP_STYLE_IMAGE2,imageresource2[i]);

            app_style_info.add(map);
        }

    }
}
