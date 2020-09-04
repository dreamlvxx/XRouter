package com.dream.xxrouter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.dream.annotation_ann.annotion.Field;
import com.dream.annotation_ann.annotion.Route;
import com.dream.annotation_api.launcher.Router;

@Route(path = "main2")
public class MainActivity2 extends AppCompatActivity {

    @Field(name = "myField")
    public String myField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Router.getInstance().inject(this);
        Log.e("xxx", "onCreate: " + myField);
    }
}