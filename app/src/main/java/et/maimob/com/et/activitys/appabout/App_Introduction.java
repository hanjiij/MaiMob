package et.maimob.com.et.activitys.appabout;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import et.maimob.com.et.Config;
import et.maimob.com.et.R;


public class App_Introduction
        extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app__introduction);

        String content = Config.ToDBC(getResources().getString(R.string.about_app_content));

        ((TextView) findViewById(R.id.about_app_content)).setText(content);
    }
}
