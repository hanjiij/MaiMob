package et.maimob.com.et.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

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

    private boolean isHide;
    private Position position;

    public MyUsualFunctionAdapter(Context context, List<FunctionInfo> functionInfoList,
                                  Position position, boolean isHide) {

        this.functionInfoList = functionInfoList;
        this.context = context;
        this.position = position;
        this.isHide = isHide;
    }

    @Override
    public int getCount() {
        return functionInfoList.size();
    }

    @Override
    public FunctionInfo getItem(int position) {
        return functionInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.gridveiw_item_layout, null);

            viewHolder.ico = (ImageView) convertView.findViewById(R.id.item_app_icon);
            viewHolder.name = (TextView) convertView.findViewById(R.id.item_app_name);
            viewHolder.delete = (ImageView) convertView.findViewById(R.id.delete_app_shortcut);

            convertView.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.ico.setImageDrawable(getItem(position).getIco());
        viewHolder.name.setText(getItem(position).getName());

        viewHolder.delete.setVisibility(
                (isHide || getItem(position).getId() == -1) ? View.INVISIBLE : View.VISIBLE);

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyUsualFunctionAdapter.this.position.onPosition(position);
            }
        });

        return convertView;
    }


    public final class ViewHolder {
        public ImageView ico;
        public TextView name;
        public ImageView delete;
    }


    /**
     * 通知数据更新
     *
     * @param functionInfoList 更新的列表信息
     */
    public void NotifyDataSetChanged(List<FunctionInfo> functionInfoList) {

        this.functionInfoList = functionInfoList;

        this.notifyDataSetChanged();
    }


    public interface Position {
        void onPosition(int position);
    }
}
