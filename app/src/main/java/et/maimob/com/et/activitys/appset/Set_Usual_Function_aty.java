package et.maimob.com.et.activitys.appset;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import et.maimob.com.et.Config;
import et.maimob.com.et.IMainPanelDataChange;
import et.maimob.com.et.R;
import et.maimob.com.et.adapter.MyUsualFunctionAdapter;
import et.maimob.com.et.database.data.DateUtils;
import et.maimob.com.et.database.data.MHelper;
import et.maimob.com.et.datatype.FunctionInfo;
import et.maimob.com.et.view.MyGridView;

public class Set_Usual_Function_aty
        extends ActionBarActivity {
    private MyGridView usual_function_gridveiw;  // 常用功能选择gridview
    private List<FunctionInfo> functionInfoList;  // 常用功能链表数据

    private List<FunctionInfo> functionInfoListcopy;  // 常用功能链表数据
    private MyUsualFunctionAdapter myUsualFunctionAdapter;  //  常用功能适配器

//    private SharedPreferences sharedPreferences;  // 获取记录是否被点击
//    private SharedPreferences.Editor editor;

    private AlertDialog shortcut_select_dialog;  // 弹出框快捷方式选择
    private MyGridView shortcut_select_gridview;
    private MyUsualFunctionAdapter shortcut_select_adapter;
    private List<FunctionInfo> shortcut_select_list;
    private View shortcut_select_view;  // 选择功能弹出框视图
    private int selectShortCutPosition = 0;  // 判断点击的是第几个快捷键
    private ImageView close_function_dialog;

    private String functionstr = "function_";

    private FunctionInfo default_functionInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set__usual__function_aty);

        default_functionInfo =
                new FunctionInfo(-1, "", getResources().getDrawable(R.drawable.icon_add));

        initShortCut();

        setListener();

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

    /**
     * 设置监听
     */
    private void setListener() {

        usual_function_gridveiw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                setDialog();
                selectShortCutPosition = position;
            }
        });

        shortcut_select_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                editor.putInt(functionstr + selectShortCutPosition,
//                        shortcut_select_list.get(position).getId());
//                editor.commit();

                DateUtils.upDateFunc(Set_Usual_Function_aty.this, selectShortCutPosition + 1,
                        shortcut_select_list.get(position).getId());

                setFunctionDatas();

                shortcut_select_list.remove(position);
                shortcut_select_adapter.NotifyDataSetChanged(shortcut_select_list);

                shortcut_select_dialog.cancel();
            }
        });

        System.out.println(">>>>>>>>"+"设置监听");
        close_function_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("============");
                shortcut_select_dialog.cancel();
            }
        });

    }

    /**
     * 初始化快捷功能
     */
    private void initShortCut() {

        usual_function_gridveiw = (MyGridView) findViewById(R.id.usual_function_gridveiw);
        functionInfoList = new ArrayList<>();
        functionInfoListcopy = new ArrayList<>();
        myUsualFunctionAdapter =
                new MyUsualFunctionAdapter(Set_Usual_Function_aty.this, functionInfoList,
                        new MyUsualFunctionAdapter.Position() {
                            @Override
                            public void onPosition(int position) {

                                Confirm_delete(position);

//                                Toast.makeText(Set_Usual_Function_aty.this, position + "",
//                                        Toast.LENGTH_SHORT).show();
                            }
                        }, false);

//        sharedPreferences = getSharedPreferences(Config.SHORTCUT_CONFIG, Activity.MODE_PRIVATE);
//        editor = sharedPreferences.edit();


        usual_function_gridveiw.setAdapter(myUsualFunctionAdapter);

        setFunctionDatas();   // 设置更新数据

        // 弹出框部分
        shortcut_select_dialog = new AlertDialog.Builder(Set_Usual_Function_aty.this).create();

        shortcut_select_view =
                getLayoutInflater().inflate(R.layout.dialogview_shortcut_layout, null);

        shortcut_select_gridview =
                (MyGridView) shortcut_select_view.findViewById(R.id.app_info_gridveiw_dialog);

        close_function_dialog =
                (ImageView) shortcut_select_view.findViewById(R.id.close_dialog_function);

        shortcut_select_list = new ArrayList<>();

        shortcut_select_adapter =
                new MyUsualFunctionAdapter(Set_Usual_Function_aty.this, shortcut_select_list, null,
                        true);

        shortcut_select_gridview.setAdapter(shortcut_select_adapter);
    }

    /**
     * 弹出选择常用功能对话框
     */
    private void setDialog() {

        setShortCutSelectDatas();

//        shortcut_select_dialog.setView(shortcut_select_view);

//        shortcut_select_dialog.setTitle(getResources().getString(R.string.please_select_shortcut));

        shortcut_select_dialog.show();

        shortcut_select_dialog.getWindow().setContentView(shortcut_select_view);

//        DisplayMetrics windowManager = getResources().getDisplayMetrics();
//
//        int w = windowManager.widthPixels;
//        int h = windowManager.heightPixels;
//
//        WindowManager.LayoutParams params = shortcut_select_dialog.getWindow().getAttributes();
//        params.width = (int) (w * (7.0 / 8));  // 800
//        params.height = (int) (h * (1.5 / 4));  // 1280
//        shortcut_select_dialog.getWindow().setAttributes(params);

    }


    /**
     * 弹出确认删除快捷入口dialog
     *
     * @param position 删除的位置信息
     */
    private void Confirm_delete(final int position) {
        AlertDialog.Builder alert = new AlertDialog.Builder(Set_Usual_Function_aty.this);

        alert.setMessage("是否要删除此快捷方式");

        alert.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                DateUtils.delFunction(Set_Usual_Function_aty.this, position + 1);

//                shortcut_select_list.add(functionInfoList.get(position));

                setFunctionDatas();

//                shortcut_select_adapter.NotifyDataSetChanged(shortcut_select_list);
            }
        });

        alert.setNegativeButton("取消", null);

        alert.show();
    }


    /**
     * 设置弹出框中常用快捷键数据
     */
    private void setShortCutSelectDatas() {

//        if (shortcut_select_list.size() != 0) {
//            shortcut_select_list.clear();
//        }

//        String[] shortcut_values = getResources().getStringArray(R.array.shortcut_values);

        shortcut_select_list = DateUtils.getAllFuns(Set_Usual_Function_aty.this);

        for (int i = 0; i < functionInfoList.size(); i++) {
            for (int j = 0; j < shortcut_select_list.size(); j++) {

                if (functionInfoList.get(i).getId() == shortcut_select_list.get(j).getId()) {
                    shortcut_select_list.remove(j);
                    break;
                }
            }
        }

//        for (int i = 1; i <= shortcut_values.length; i++) {
//
//            String name = shortcut_values[i - 1];
//
//            FunctionInfo functionInfo = new FunctionInfo(i, name);
//
//            //避免重复显示
//            int j;
//            for (j = 0; j < functionInfoList.size(); j++) {
//                if (functionInfoList.get(j).equals(functionInfo)) {
//                    break;
//                }
//            }
//
//            if (j == (functionInfoList.size())) {
//                shortcut_select_list.add(functionInfo);
//            }
//        }

        shortcut_select_adapter.NotifyDataSetChanged(shortcut_select_list);
    }

    /**
     * 为常用功能链表添加数据
     */
    private void setFunctionDatas() {

        if (functionInfoList.size() != 0) {

            functionInfoList.clear();
        }

//        String[] shortcut_values = getResources().getStringArray(R.array.shortcut_values);
//
//        for (int i = 1; i < 4; i++) {
//
//            int id = sharedPreferences.getInt(functionstr + i, i);
//            String name = shortcut_values[id - 1];
//
//            FunctionInfo functionInfo = new FunctionInfo(id, name);
//            functionInfoList.add(functionInfo);
//        }

        functionInfoList = DateUtils.getFunctions(Set_Usual_Function_aty.this);

        if (functionInfoList.size() < 4) {
            functionInfoList.add(default_functionInfo);
        }

        myUsualFunctionAdapter.NotifyDataSetChanged(functionInfoList);

        //将数据传递给前端
        if (functionInfoListcopy.size() > 0) {
            functionInfoListcopy.clear();
        }
        functionInfoListcopy.addAll(functionInfoList);

        if (functionInfoListcopy.get(functionInfoListcopy.size() - 1).getId() == -1) {

            functionInfoListcopy.remove(functionInfoListcopy.size() - 1);
        }

        IMainPanelDataChange.getInstance().changeMainPanelShortcutSettings(functionInfoListcopy);
    }
}