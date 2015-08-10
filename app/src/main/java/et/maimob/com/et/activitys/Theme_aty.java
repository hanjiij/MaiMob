package et.maimob.com.et.activitys;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import et.maimob.com.et.Config;
import et.maimob.com.et.R;
import et.maimob.com.et.adapter.ThemeAdapter;
import et.maimob.com.et.adapter.ThemeAdapter.ViewHolder;
import et.maimob.com.et.datatype.ThemeInfo;

public class Theme_aty
        extends ActionBarActivity {

    private ListView theme_listview;
    private ThemeAdapter themeAdapter;
    private List<ThemeInfo> themeInfos;
    private TextView my_gold_textview;
    private Button get_gold_button;


    private Handler handler;  // 消息handler
    private Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_aty);

        init();

        InitHandler();

        setListener();
    }

    /**
     * 初始化
     */
    private void init() {

        theme_listview = (ListView) findViewById(R.id.theme_listview);

        themeInfos = new ArrayList<>();

        // 为主题列表添加数据
        setThemeInfoData();

        themeAdapter = new ThemeAdapter(Theme_aty.this, themeInfos, new ThemeAdapter.Position() {
            @Override
            public void onPosition(final int position) {

                // 回掉函数中添加线程模拟下载
                new AsyncTask<Integer, Integer, Integer>() {

                    @Override
                    protected Integer doInBackground(Integer... params) {

                        for (int i = 0; i <= 100; i++) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            publishProgress(i);
                        }

                        return null;
                    }

                    @Override
                    protected void onPreExecute() {

                        super.onPreExecute();

                        themeAdapter.setDownLoadStatus(position, 1);  // 设置为正在下载状态

                        updateDownloadFinish(position, false);
                    }

                    @Override
                    protected void onProgressUpdate(Integer... values) {
                        super.onProgressUpdate(values);

                        updateProgressPartly(position, values[0]);
                    }

                    @Override
                    protected void onPostExecute(Integer integer) {
                        super.onPostExecute(integer);

                        updateDownloadFinish(position, true);

                        themeAdapter.setIsDownloadComplete(position);  // 设置为下载完成状态

//                        themeAdapter.setDownLoadStatus(position,0);
                    }
                }.execute();

            }
        });

        theme_listview.setAdapter(themeAdapter);

        my_gold_textview = (TextView) findViewById(R.id.my_gold_textview);
        get_gold_button = (Button) findViewById(R.id.get_gold_button);

        init_Gold();
    }


    /**
     * 初始化handler
     */
    private void InitHandler() {

        handler = new Handler() {
            @Override
            public void handleMessage(final Message msg) {
                super.handleMessage(msg);

                updateProgressPartly(msg.what, (Integer) msg.obj);
            }
        };
    }

    /**
     * 发送消息
     *
     * @param what 消息的标识
     * @param obj  传递的进度值
     */
    private void SendMessage(int what, int obj) {

        message = new Message();
        message.what = what;
        message.obj = obj;
        handler.sendMessage(message);
    }


    /**
     * 更新列表的部分
     * 设置下载进度的值
     *
     * @param position 需要更新的项的id
     * @param progress 下载的进度
     */
    private void updateProgressPartly(int position, int progress) {

        int firstVisiblePosition = theme_listview.getFirstVisiblePosition();
        int lastVisiblePosition = theme_listview.getLastVisiblePosition();
        if (position >= firstVisiblePosition && position <= lastVisiblePosition) {

            View view = theme_listview.getChildAt(position - firstVisiblePosition);
            if (view.getTag() instanceof ViewHolder) {

                ViewHolder vh = (ViewHolder) view.getTag();

                vh.downloading.setCanvasProgress(progress);

//                vh.downloading.setEnabled(false);
//                if (progress >= 100) {
//
//                    vh.downloading.setVisibility(View.INVISIBLE);
//                    vh.use.setVisibility(View.VISIBLE);
//
//                    SharedPreferences sharedPreferences = getSharedPreferences(
//                            Config.THEME_LIST_INFO,
//                            Activity.MODE_PRIVATE);
//
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putInt(vh.title.getText().toString(), 1);
//                    editor.commit();
//
//                }
            }
        }
    }


    /**
     * 设置下载完成状态
     *
     * @param position 需要更新的项的id
     * @param status   状态
     */
    private void updateDownloadFinish(int position, boolean status) {

        int firstVisiblePosition = theme_listview.getFirstVisiblePosition();
        int lastVisiblePosition = theme_listview.getLastVisiblePosition();
        if (position >= firstVisiblePosition && position <= lastVisiblePosition) {

            View view = theme_listview.getChildAt(position - firstVisiblePosition);
            if (view.getTag() instanceof ViewHolder) {

                ViewHolder vh = (ViewHolder) view.getTag();
                if (status) {

                    vh.downloading.setVisibility(View.INVISIBLE);
                    vh.use.setVisibility(View.VISIBLE);

                    SharedPreferences sharedPreferences = getSharedPreferences(
                            Config.THEME_LIST_INFO,
                            Activity.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(vh.title.getText().toString(), 1);
                    editor.commit();
                } else {
                    vh.downloading.setEnabled(false);
                }
            }
        }
    }


    /**
     * 初始化金币的数量
     */
    private void init_Gold() {

        int gold_number = 2000;

        my_gold_textview.setText(getResources().getString(R.string.my_gold) + gold_number);
    }


    /**
     * 从数据库中获得数据填充到主题列表中
     */
    private void setThemeInfoData() {

        for (int i = 0; i < 20; i++) {
            ThemeInfo themeInfo = new ThemeInfo(getResources().getDrawable(R.drawable.ic_launcher),
                    i + ":" + "主题的标题为" + i, "主题的内容为" + i);

            themeInfos.add(themeInfo);
        }
    }

    /**
     * 设置按钮的监听
     */
    private void setListener() {

        get_gold_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int gold_number = 5000;

                my_gold_textview.setText(getResources().getString(R.string.my_gold) + gold_number);
            }
        });
    }
}