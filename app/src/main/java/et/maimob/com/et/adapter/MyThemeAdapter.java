package et.maimob.com.et.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import et.maimob.com.et.Config;
import et.maimob.com.et.R;
import et.maimob.com.et.datatype.ThemeInfo;

/**
 * Created by HJ on 2015/7/13.
 *
 * 我的主题adapter
 */
public class MyThemeAdapter
        extends BaseAdapter {

    private List<ThemeInfo> themeInfoList;
    private Context context;
    private SharedPreferences sharedPreferences;  // 获取记录是否被点击
    private SharedPreferences.Editor editor;

    public MyThemeAdapter(Context context, List<ThemeInfo> themeInfoList) {

        this.context = context;
        this.themeInfoList = themeInfoList;

        sharedPreferences = context.getSharedPreferences(Config.THEME_LIST_INFO,
                Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @Override
    public int getCount() {
        return themeInfoList.size();
    }

    @Override
    public ThemeInfo getItem(int position) {
        return themeInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(context).inflate(R.layout.theme_item_layout, null);


            viewHolder.ico = (ImageView) convertView.findViewById(R.id.theme_item_ico_imv);

            viewHolder.title = (TextView) convertView.findViewById(R.id.theme_item_title_tv);

            viewHolder.content = (TextView) convertView.findViewById(R.id.theme_item_content_tv);

            viewHolder.use = (Button) convertView.findViewById(R.id.theme_item_use_bt);
            viewHolder.used = (Button) convertView.findViewById(R.id.theme_item_used_bt);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.ico.setImageDrawable(getItem(position).getTheme_ico());
        viewHolder.title.setText(getItem(position).getTheme_title());
        viewHolder.content.setText(getItem(position).getTheme_content());

        viewHolder.use.setVisibility(View.VISIBLE);

        boolean b = sharedPreferences.getString(Config.THEME_LIST_USED_ITEM, "").equals(
                viewHolder.title.getText().toString());

        if (b) {

            viewHolder.used.setVisibility(View.VISIBLE);
        } else {

            viewHolder.used.setVisibility(View.INVISIBLE);
        }

        viewHolder.use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putString(Config.THEME_LIST_USED_ITEM,
                        viewHolder.title.getText().toString());
                editor.commit();
                MyThemeAdapter.this.notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public final class ViewHolder {
        public ImageView ico;
        public TextView title;
        public TextView content;
        public Button use;
        public Button used;
    }
}
