package et.maimob.com.et.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import et.maimob.com.et.Config;
import et.maimob.com.et.IMainPanelDataChange;
import et.maimob.com.et.R;
import et.maimob.com.et.database.data.DateUtils;

/**
 * Created by HJ on 2015/8/12.
 */
public class AppStyleAdapter
        extends BaseAdapter {

    private Context context;
    private List<Map<String, Integer>> app_style_info;
    private Map<Integer, Integer> function_status;

    public AppStyleAdapter(Context context, List<Map<String, Integer>> app_style_info) {

        this.context = context;
        this.app_style_info = app_style_info;

        function_status = new HashMap<>();

        initMapStatus();
    }

    private void initMapStatus() {

        if (function_status.size() > 0) {
            function_status.clear();
        }

        for (int i = 0; i < app_style_info.size(); i++) {
            function_status.put(i,
                    DateUtils.getSharedPreference(context, Config.IS_APP_STYLE_FUNCTION_ + i));
        }
    }

    @Override
    public int getCount() {
        return app_style_info.size();
    }

    @Override
    public Map<String, Integer> getItem(int position) {
        return app_style_info.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        viewHolder = new ViewHolder();
        convertView =
                LayoutInflater.from(context).inflate(R.layout.app_style_listview_item, null);

        viewHolder.app_style_imageview =
                (ImageView) convertView.findViewById(R.id.app_style_two_1lv);
        viewHolder.app_function_imageview =
                (ImageView) convertView.findViewById(R.id.app_style_two_2lv);

        viewHolder.app_style_checkbox =
                (CheckBox) convertView.findViewById(R.id.app_style_two_1checkbox);
        viewHolder.app_function_checkbox =
                (CheckBox) convertView.findViewById(R.id.app_style_two_2checkbox);

        convertView.setTag(viewHolder);

        viewHolder.app_style_imageview
                .setBackgroundResource(getItem(position).get(Config.IS_APP_STYLE_IMAGE1));
        viewHolder.app_function_imageview
                .setBackgroundResource(getItem(position).get(Config.IS_APP_STYLE_IMAGE2));

        // 判断快捷功能是否开启
        boolean isopen = function_status.get(position) == 1;
        if (isopen) {

            viewHolder.app_function_checkbox.setChecked(isopen);
            viewHolder.app_function_imageview.setAlpha(0);
        } else {

            viewHolder.app_function_checkbox.setChecked(isopen);
            viewHolder.app_function_imageview.setAlpha(90);
        }

        if (DateUtils.getSharedPreference(context, Config.IS_APP_STYLE_FUNCTION_) == position) {

            viewHolder.app_style_checkbox.setChecked(true);
            viewHolder.app_function_checkbox.setEnabled(true);

            viewHolder.app_style_imageview.setAlpha(0);
        } else {

            viewHolder.app_style_checkbox.setChecked(false);
            viewHolder.app_function_checkbox.setEnabled(false);

            viewHolder.app_style_imageview.setAlpha(90);
            viewHolder.app_function_imageview.setAlpha(90);
        }

        viewHolder.app_style_checkbox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        DateUtils.setSharedPreference(context,
                                Config.IS_APP_STYLE_FUNCTION_, position);

                        if (isChecked) {

                            IMainPanelDataChange.getInstance()
                                    .changeMainPanelStyle(position % 2 == 0 ? 1 : 2,
                                            viewHolder.app_function_checkbox.isChecked());
                        }

                        AppStyleAdapter.this.notifyDataSetChanged();
                    }
                });


        viewHolder.app_function_checkbox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        function_status.put(position, isChecked ? 1 : 0);

                        DateUtils.setSharedPreference(context,
                                Config.IS_APP_STYLE_FUNCTION_ + position, isChecked ? 1 : 0);

                            IMainPanelDataChange.getInstance()
                                    .changeMainPanelStyle(position % 2 == 0 ? 1 : 2,isChecked);

                        AppStyleAdapter.this.notifyDataSetChanged();
                    }
                });


        return convertView;
    }

    public final class ViewHolder {
        ImageView app_style_imageview;
        ImageView app_function_imageview;
        CheckBox app_style_checkbox;
        CheckBox app_function_checkbox;
    }
}
