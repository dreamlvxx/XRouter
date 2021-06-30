package com.dream.xxrouter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dream.annotation_ann.annotion.Route;
import com.dream.annotation_api.launcher.Router;


@Route(path = "main_activity")
public class MainActivity extends AppCompatActivity {

    TextView tv_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.util.Log.e("xxx","aaaaaaaaaa");
        tv_btn = findViewById(R.id.tv_btn);
        tv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Router.init(getApplicationContext());
                Router.getInstance()
                        .setPath("main2")
                        .setField("myField","ioioioss")
                        .navigation(MainActivity.this);
            }
        });
    }
}