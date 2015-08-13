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
import et.maimob.com.et.datatype.AppInfo;

/**
 * Created by HJ on 2015/7/7.
 * <p>
 * 程序列表弹出框适配器
 */
public class MyAppInfoAdapter
        extends BaseAdapter {

    private List<AppInfo> appInfoList;
    private Context context;
    private boolean isHide;
    private Position position;

    public MyAppInfoAdapter(Context context, List<AppInfo> appInfoList, Position position,
                            boolean isHide) {

        this.appInfoList = appInfoList;
        this.context = context;
        this.position = position;
        this.isHide = isHide;
    }

    @Override
    public int getCount() {

        return appInfoList.size();
    }

    @Override
    public AppInfo getItem(int position) {
        return appInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.usual_app_gridveiw_item_layout, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.item_app_icon);

        imageView.setBackgroundDrawable(getItem(position).getIcon());

        ((TextView) view.findViewById(R.id.item_app_name)).setText(getItem(position).getAppName());

        ImageView delete_view = (ImageView) view.findViewById(R.id.delete_app_shortcut);

        if (isHide) {
            delete_view.setVisibility(View.INVISIBLE);
        }
        if ((position == getCount() - 1) && (getItem(position).getAppName().equals(""))) {

            delete_view.setVisibility(View.INVISIBLE);
        } else {

            delete_view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramView) {
                    MyAppInfoAdapter.this.position.onposition(position);
                }
            });
        }

        return view;
    }

    public void NotifyDataSetChanged(List<AppInfo> appInfoList) {

        this.appInfoList = appInfoList;

        this.notifyDataSetChanged();
    }

    public interface Position {
        void onposition(int position);
    }
}
