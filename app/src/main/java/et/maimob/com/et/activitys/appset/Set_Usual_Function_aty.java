package et.maimob.com.et.activitys.appset;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import et.maimob.com.et.Config;
import et.maimob.com.et.IMainPanelDataChange;
import et.maimob.com.et.R;
import et.maimob.com.et.adapter.MyUsualFunctionAdapter;
import et.maimob.com.et.database.DateUtils;
import et.maimob.com.et.database.MHelper;
import et.maimob.com.et.datatype.FunctionInfo;
import et.maimob.com.et.view.MyGridView;

public class Set_Usual_Function_aty
        extends ActionBarActivity {


    private MyGridView usual_function_gridview;
    private MyGridView usual_function_select_gridview;
    private List<FunctionInfo> usual_function_info;

    private MyUsualFunctionAdapter usual_function_adapter;
    private MyUsualFunctionAdapter usual_function_select_adapter;
    private Map<Integer, Integer> map;

    private String[] function_name_resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set__usual__function_aty);

        initUsualFunction();

        setListener();

//        initItemColor();

        getIsOpenFunction();
    }

    /**
     * 初始化数据
     */
    private void initUsualFunction() {

        usual_function_info = DateUtils.getFunctions(Set_Usual_Function_aty.this);

        usual_function_gridview = (MyGridView) findViewById(R.id.usual_function_gridveiw);
        usual_function_select_gridview =
                (MyGridView) findViewById(R.id.usual_function_select_gridview);

        usual_function_adapter =
                new MyUsualFunctionAdapter(Set_Usual_Function_aty.this, usual_function_info, false);
        usual_function_select_adapter =
                new MyUsualFunctionAdapter(Set_Usual_Function_aty.this, usual_function_info, true);

        usual_function_gridview.setAdapter(usual_function_adapter);
        usual_function_select_gridview.setAdapter(usual_function_select_adapter);

        map = new HashMap<>();

        getMapId();

        function_name_resources = getResources().getStringArray(R.array.funcName);
    }

    /**
     * 设置监听
     */
    private void setListener() {

        usual_function_select_gridview.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {

                        if (map.get(position) != null) { // 不为null表示已选择此快捷方式

                            usual_function_info.remove(map.get(position) - 1);
                            DateUtils.delFunction(Set_Usual_Function_aty.this, map.get(position));

                            usual_function_adapter.NotifyDataSetChanged(usual_function_info);
                            usual_function_select_adapter.NotifyDataSetChanged(usual_function_info);
                        } else if (usual_function_info.size() < 4) {

                            usual_function_info.add(new FunctionInfo(position,
                                    function_name_resources[position], getResources()
                                    .getDrawable(Config.function_black_image_resources[position])));

                            DateUtils.upDateFunc(Set_Usual_Function_aty.this,
                                    usual_function_info.size(), position);

                            usual_function_adapter.NotifyDataSetChanged(usual_function_info);
                            usual_function_select_adapter.NotifyDataSetChanged(usual_function_info);
                        } else {

                            Toast.makeText(Set_Usual_Function_aty.this, "已满四个啦<-_->",
                                    Toast.LENGTH_SHORT).show();
                        }

                        getMapId();

                        IMainPanelDataChange.getInstance().changeMainPanelShortcutSettings(
                                usual_function_info);
                    }
                });
    }

    private void getMapId() {

        if (map.size() > 0) {
            map.clear();
        }

        for (int i = 0; i < usual_function_info.size(); i++) {
            int id = usual_function_info.get(i).getId();
            map.put(id, i + 1);
        }
    }

    /**
     * 设置选择常用功能的背景颜色
     */
    private void initItemColor() {

        if (usual_function_info.size() > 0) {
            for (int i = 0; i < usual_function_info.size(); i++) {

                usual_function_select_gridview.getChildAt(usual_function_info.get(i).getId())
                        .findViewById(R.id.item_function_icon)
                        .setBackgroundResource(R.drawable.bg_function_open);
            }
        }
    }

    /**
     * 判断是否开启了常用功能，未开启弹窗框提示开启，取消返回到上个界面
     */
    private void getIsOpenFunction() {
        // 判断是否开启了常用功能，未开启则提示是否开启，确定开启，取消返回上一级
        if (DateUtils.getconfig(Set_Usual_Function_aty.this, MHelper.TYPE_FUNCTIONISOPEN) == 0) {

            AlertDialog.Builder alert = new AlertDialog.Builder(Set_Usual_Function_aty.this);

            alert.setMessage("常用功能未开启，是否开启常用功能！");

            alert.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    DateUtils
                            .setConfig(Set_Usual_Function_aty.this, 1, MHelper.TYPE_FUNCTIONISOPEN);

                    int type =
                            DateUtils.getconfig(Set_Usual_Function_aty.this, MHelper.TYPE_APPSTYLE);

                    DateUtils.setSharedPreference(Set_Usual_Function_aty.this,
                            type == 1 ? Config.IS_TWO_OPEN : Config.IS_NINE_OPEN, 1);

                    IMainPanelDataChange.getInstance().changeMainPanelStyle(type, true);
                }
            });

            alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Set_Usual_Function_aty.this.finish();
                }
            });

            alert.show();
        }
    }
}