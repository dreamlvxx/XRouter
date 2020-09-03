package com.dream.xxrouter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.dream.annotation_ann.annotion.Route;


@Route(path = "main_activity")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}