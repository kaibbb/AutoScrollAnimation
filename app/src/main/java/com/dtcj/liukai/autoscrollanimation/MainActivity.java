package com.dtcj.liukai.autoscrollanimation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((AutoScrollView)findViewById(R.id.autosvone)).startScroll(0, 2, R.drawable.as1, R.drawable.as2);
        ((AutoScrollView)findViewById(R.id.autosvtwo)).startScroll(1, 2, R.drawable.as3, R.drawable.as1);
        ((AutoScrollView)findViewById(R.id.autosvthree)).startScroll(0, 2, R.drawable.as2, R.drawable.as1);
    }
}
