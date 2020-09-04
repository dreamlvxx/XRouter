package com.dream.xxrouter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.dream.annotation_ann.annotion.Route;

@Route(path = "main2")
public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }
}