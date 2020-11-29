package com.example.mychef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

public class ShopCarActivity extends AppCompatActivity {

    private ListView mLv1;

    private ImageButton mBanBack;
    private Button mBanCko, backHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_car);

        mLv1 = (ListView) findViewById(R.id.shopCar_LV);
        mLv1.setAdapter(new ShopCarListViewAdapter(ShopCarActivity.this));

        //Button control
        mBanBack = (ImageButton) findViewById(R.id.Back_Button);
        mBanBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBanCko = (Button) findViewById(R.id.Check_out_button);
        mBanCko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mLv1.setAdapter(new CheckOutAdapter(ShopCarActivity.this));
                setContentView(R.layout.activity_check_out);

                backHome = (Button) findViewById(R.id.Home_button);
                backHome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
        });


    }

}