package et.maimob.com.et.activitys.appabout;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import et.maimob.com.et.R;


public class App_User_Feedback
        extends ActionBarActivity {

    EditText editText_content;
    Button submit_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app__user__feedback);

        init();

        setListener();
    }

    /**
     * 初始化
     */
    private void init() {
        editText_content = (EditText) findViewById(R.id.user_feedback_edittext);
        submit_button = (Button) findViewById(R.id.user_feedback_button);
    }

    /**
     * 事件响应
     */
    private void setListener() {

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editText_content.getText().toString();

                if (content.equals("")) {
                    Toast.makeText(App_User_Feedback.this, "请填写反馈建议再提交", Toast.LENGTH_SHORT).show();
                }else {
                    editText_content.setText("");

                    Toast.makeText(App_User_Feedback.this, "提交成功，谢谢您的建议", Toast.LENGTH_SHORT).show();

                    App_User_Feedback.this.finish();
                }
            }
        });
    }
}
