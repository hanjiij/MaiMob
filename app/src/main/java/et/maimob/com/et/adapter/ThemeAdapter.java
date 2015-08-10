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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import et.maimob.com.et.Config;
import et.maimob.com.et.R;
import et.maimob.com.et.datatype.ThemeInfo;
import et.maimob.com.et.view.DownLoadingButton;

/**
 * Created by HJ on 2015/7/9.
 * 主题adapter
 */
public class ThemeAdapter
        extends BaseAdapter {

    private List<ThemeInfo> themeInfoList;  // 主题项的列表数据
    private Context context;
    private SharedPreferences sharedPreferences;  // 获取记录是否被点击
    private SharedPreferences.Editor editor;

//    private Handler handler;  // 消息handler
//    private Message message;
//    private boolean isThread = false;

    private Map<Integer, Integer> downloadStatus;  //下载状态哈希表

    private Position position;


    /**
     * @param context       上下文环境
     * @param themeInfoList 主题列表的数据内容
     */
    public ThemeAdapter(Context context, List<ThemeInfo> themeInfoList, Position position) {

        this.context = context;
        this.themeInfoList = themeInfoList;
        this.position = position;

        Init();
    }

    /**
     * 初始化数据
     */
    private void Init() {

        sharedPreferences = context.getSharedPreferences(Config.THEME_LIST_INFO,
                Activity.MODE_PRIVATE);

        editor = sharedPreferences.edit();


        downloadStatus = new HashMap<>();

//        InitHandler();
//        InitUpDateThread();
    }


//    /**
//     * 初始化handler
//     */
//    private void InitHandler() {  // 0，通知更新。1，创建线程模拟下载任务
//
//        handler = new Handler() {
//            @Override
//            public void handleMessage(final Message msg) {
//                super.handleMessage(msg);
//
//                if (msg.what == 0) {
//
//                    notifyDataSetChanged();
//                    System.out.println("更新界面");
//
//                } else if (message.what == 1) {
//
//                    final Map<String, Object> map = (Map<String, Object>) msg.obj;
//
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            System.out.println("开始下载");
//                            for (int i = 1; i <= 100; i++) {
//
//                                isThread = true;
//
//                                try {
//
//                                    Thread.sleep(100);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//
//                                downloadStatus.put((int) map.get("position"), i);
//                            }
//
//                            editor.putInt(getItem((int) map.get("position")).getTheme_title(), 1);
//                            editor.commit();
//                            SendMessage(0, null);
//
//                            isThread = false;
//                        }
//                    }).start();
//                }
//            }
//        };
//    }
//
//
//
//    /**
//     * 当有进度更新时，通知界面更新
//     */
//    private void InitUpDateThread() {
//
//        new Timer().schedule(new TimerTask() {
//
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                if (isThread) {
//
//                    SendMessage(0, null);
//                }
//            }
//        }, 0, 800);
//    }
//
//
//    /**
//     * 发送消息
//     *
//     * @param what 消息的标识
//     * @param obj  传递的进度值
//     */
//    private void SendMessage(int what, Object obj) {
//
//        message = new Message();
//        message.what = what;
//        message.obj = obj;
//        handler.sendMessage(message);
//    }

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
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.theme_item_layout, null);
            viewHolder.ico = (ImageView) convertView.findViewById(R.id.theme_item_ico_imv);
            viewHolder.title = (TextView) convertView.findViewById(R.id.theme_item_title_tv);
            viewHolder.content = (TextView) convertView.findViewById(R.id.theme_item_content_tv);

            viewHolder.download = (Button) convertView.findViewById(R.id.theme_item_download_bt);

            viewHolder.downloading =
                    (DownLoadingButton) convertView.findViewById(R.id.theme_item_downloading_bt);

            viewHolder.use = (Button) convertView.findViewById(R.id.theme_item_use_bt);
            viewHolder.used = (Button) convertView.findViewById(R.id.theme_item_used_bt);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.ico.setImageDrawable(getItem(position).getTheme_ico());
        viewHolder.title.setText(getItem(position).getTheme_title());
        viewHolder.content.setText(getItem(position).getTheme_content());

        int a = sharedPreferences.getInt(getItem(position).getTheme_title(), 0);  //判断当前项是否下载完成

        switch (a) {
            case 0:

                switch (downloadStatus.get(position) != null ? downloadStatus.get(position) : 0) {
                    case 0:
                        viewHolder.download.setVisibility(View.VISIBLE);
                        viewHolder.downloading.setVisibility(View.INVISIBLE);
                        viewHolder.use.setVisibility(View.INVISIBLE);
                        viewHolder.used.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        viewHolder.downloading.setVisibility(View.VISIBLE);
                        viewHolder.download.setVisibility(View.INVISIBLE);
                        viewHolder.use.setVisibility(View.INVISIBLE);
                        viewHolder.used.setVisibility(View.INVISIBLE);
//                        viewHolder.downloading.setCanvasProgress(0);
                        viewHolder.downloading.setEnabled(false);
                        break;
                }

//                if (downloadStatus.get(position) == null) {  // 判断状态链表中数据是否为null，为空则表示未选中此项进行下载
//
//                    viewHolder.downloading.setEnabled(true);
//                    viewHolder.downloading.setCanvasProgress(0);
//
//                    viewHolder.downloading.setVisibility(View.VISIBLE);
//                    viewHolder.use.setVisibility(View.INVISIBLE);
//
//                } else {
//
//                    int progress = downloadStatus.get(position);
//
//                    viewHolder.downloading.setEnabled(false);
//                    viewHolder.downloading.setCanvasProgress(progress);
//
//                    viewHolder.downloading.setVisibility(View.VISIBLE);
//
//                    viewHolder.use.setVisibility(View.INVISIBLE);
//                }

                break;
            case 1:

                viewHolder.downloading.setVisibility(View.INVISIBLE);
                viewHolder.download.setVisibility(View.INVISIBLE);

                boolean b = sharedPreferences.getString(Config.THEME_LIST_USED_ITEM, "")
                        .equals( //判断是否点击了使用，点击了则设置为已使用
                                viewHolder.title.getText().toString());
                if (b) {

                    viewHolder.used.setVisibility(View.VISIBLE);
                    viewHolder.use.setVisibility(View.INVISIBLE);
                } else {
                    viewHolder.used.setVisibility(View.INVISIBLE);
                    viewHolder.use.setVisibility(View.VISIBLE);
                }
                break;
        }



        viewHolder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewHolder.downloading.setVisibility(View.VISIBLE);
                viewHolder.download.setVisibility(View.INVISIBLE);
                viewHolder.use.setVisibility(View.INVISIBLE);
                viewHolder.used.setVisibility(View.INVISIBLE);
                ThemeAdapter.this.position.onPosition(position);

//                Map<String, Object> map = new HashMap<String, Object>();
//
//                map.put("position", position);
//                map.put("ViewHolder", viewHolder);
//
//                SendMessage(1, map);


//                new AsyncTask<Integer, Integer, Integer>() {
//
//                    @Override
//                    protected Integer doInBackground(Integer... params) {
//
//                        for (int i = 1; i <= 100; i++) {
//                            try {
//                                Thread.sleep(100);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//
//                            publishProgress(i);
//                        }
//
//                        return null;
//                    }
//
//                    @Override
//                    protected void onPreExecute() {
//                        super.onPreExecute();
//
//                        viewHolder.downloading.setEnabled(false);
//                        downloadStatus.put(position, 0);
//                    }
//
//                    @Override
//                    protected void onProgressUpdate(Integer... values) {
//                        super.onProgressUpdate(values);
//
//                        viewHolder.downloading.setCanvasProgress(values[0]);
//
//                        downloadStatus.put(position, values[0]);
//
//                        System.out.println("+++"+downloadStatus.toString());
//                    }
//
//                    @Override
//                    protected void onPostExecute(Integer integer) {
//                        super.onPostExecute(integer);
//
//                        editor.putInt(viewHolder.title.getText().toString(), 1);
//                        editor.commit();
//
//                        System.out.println("内容为："+viewHolder.title.getText().toString());
//                        ThemeAdapter.this.notifyDataSetChanged();
//                    }
//                }.execute();
            }
        });

        viewHolder.use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putString(Config.THEME_LIST_USED_ITEM,
                        viewHolder.title.getText().toString());
                editor.commit();
                ThemeAdapter.this.notifyDataSetChanged();
            }
        });

        return convertView;
    }

    /**
     * 设置下载状态
     *
     * @param position
     * @param isDownLoad
     */
    public void setDownLoadStatus(int position, int isDownLoad) {
        downloadStatus.put(position, isDownLoad);
    }


    /**编辑为下载完成，将下载完成项添加到数据中
     * @param index
     */
    public void setIsDownloadComplete(int index) {

        editor.putInt(getItem(index).getTheme_title(), 1);
        editor.commit();
    }

    /**
     * 列表视图的id项
     */
    public final class ViewHolder {

        public ImageView ico;
        public TextView title;
        public TextView content;
        public Button download;
        public DownLoadingButton downloading;
        public Button use;
        public Button used;
    }

    public interface Position {
        void onPosition(int position);
    }
}
