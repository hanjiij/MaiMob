package et.maimob.com.et.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import et.maimob.com.et.R;
import et.maimob.com.et.activitys.appset.Set_App_Style_aty;
import et.maimob.com.et.activitys.appset.Set_Usual_App_aty;
import et.maimob.com.et.activitys.appset.Set_Usual_Function_aty;


public class Set_App_aty
        extends ActionBarActivity {


    private Button set_app_style_bt, set_usual_app_bt, set_usual_function_bt, about_app_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_set_app_aty);

        init();

        setListener();
    }

    private void setListener() {

        set_app_style_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Set_App_aty.this, Set_App_Style_aty.class));
            }
        });

        set_usual_app_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Set_App_aty.this, Set_Usual_App_aty.class));
            }
        });

        set_usual_function_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Set_App_aty.this, Set_Usual_Function_aty.class));
            }
        });

        about_app_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Set_App_aty.this, About_App_aty.class));
            }
        });
    }

    private void init() {

        set_usual_app_bt = (Button) findViewById(R.id.set_usual_app_bt);
        about_app_bt = (Button) findViewById(R.id.about_app);
        set_app_style_bt = (Button) findViewById(R.id.set_app_style);
        set_usual_function_bt = (Button) findViewById(R.id.set_usual_function);
    }
}
