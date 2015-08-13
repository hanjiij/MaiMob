package et.maimob.com.et.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import et.maimob.com.et.Config;
import et.maimob.com.et.R;
import et.maimob.com.et.datatype.FunctionInfo;

/**
 * Created by HJ on 2015/7/16.
 * 常用功能类
 */
public class MyUsualFunctionAdapter
        extends BaseAdapter {

    private List<FunctionInfo> functionInfoList;
    private Context context;
    private boolean isSelect;
    private String[] function_name_resources;
    private Map<Integer,Integer> map;

    public MyUsualFunctionAdapter(Context context, List<FunctionInfo> functionInfoList,
                                  boolean isSelect) {

        this.functionInfoList = functionInfoList;
        this.context = context;
        this.isSelect = isSelect;
        function_name_resources = context.getResources().getStringArray(R.array.funcName);

        map=new HashMap<>();
        getMapId();
    }

    @Override
    public int getCount() {

        if (isSelect) {
            return 11;
        } else {
            return functionInfoList.size();
        }
    }

    @Override
    public FunctionInfo getItem(int position) {

        if (isSelect) {
            return null;
        } else {
            return functionInfoList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (isSelect) {

            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.usual_function_sel_gridveiw_item_layout, null);

            ImageView function_ico = (ImageView) convertView.findViewById(R.id.item_function_icon);

            TextView function_name = (TextView) convertView.findViewById(R.id.item_function_name);

            if (map.get(position)==null) {

                function_ico.setBackgroundResource(R.drawable.bg_function_close);
                function_ico.setImageResource(Config.function_black_image_resources[position]);
            }else {

                function_ico.setBackgroundResource(R.drawable.bg_function_open);
                function_ico.setImageResource(Config.function_white_image_resources[position]);
            }

            function_name.setText(function_name_resources[position]);

            return convertView;
        } else {

            View view = LayoutInflater.from(context)
                    .inflate(R.layout.usual_function_gridveiw_item_layout, null);
            ((ImageView) (view.findViewById(R.id.item_function_icon))).setImageDrawable(
                    getItem(position).getIco());
            return view;
        }
    }

    @Override
    public boolean isEnabled(int position) {

        if (isSelect) {
            return super.isEnabled(position);
        } else {
            return false;
        }
    }

    /**
     * 通知数据更新
     *
     * @param functionInfoList 更新的列表信息
     */
    public void NotifyDataSetChanged(List<FunctionInfo> functionInfoList) {

        this.functionInfoList = functionInfoList;

        getMapId();

        this.notifyDataSetChanged();
    }

    private void getMapId(){

        if (map.size()>0) {
            map.clear();
        }

        for (int i = 0; i < functionInfoList.size(); i++) {
            int id=functionInfoList.get(i).getId();
            map.put(id,id);
        }
    }
}
