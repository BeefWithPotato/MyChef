package com.example.mychef;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {

    private GridView mGv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGv = (GridView) findViewById(R.id.home_gv);
        mGv.setAdapter(new HomeGridViewAdapter(MainActivity.this));
    }
}