package com.example.mychef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

public class SubscribePageActivity extends AppCompatActivity {

    private GridView mGv;

    private Button mBanSubscribe;
    private Button mBanRecommend;
    private Button mBanCategory;
    private ImageButton mBanHome;
    private ImageButton mBanShopCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe_page);
        mGv = (GridView) findViewById(R.id.home_gv);
        mGv.setAdapter(new SubscribeGridViewAdapter(SubscribePageActivity.this));

        //Button control
        mBanSubscribe = (Button) findViewById(R.id.Subscribe_button);
        mBanRecommend = (Button) findViewById(R.id.Recommend_button);
        mBanCategory = (Button) findViewById(R.id.Category_button);
        mBanHome = (ImageButton) findViewById(R.id.Home_Button);
        mBanShopCar = (ImageButton) findViewById(R.id.ShopCarButton);
        setListeners();
    }

    private void setListeners(){
        SubscribePageActivity.OnClick onClick = new SubscribePageActivity.OnClick();
        mBanSubscribe.setOnClickListener(onClick);
        mBanRecommend.setOnClickListener(onClick);
        mBanCategory.setOnClickListener(onClick);
        mBanHome.setOnClickListener(onClick);
        mBanShopCar.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()){
                case R.id.ShopCarButton:
                    intent = new Intent(SubscribePageActivity.this, ShopCarActivity.class);
                    break;
                case R.id.Category_button:
                    intent = new Intent(SubscribePageActivity.this, CategoryPageActivity.class);
                    break;
                case R.id.Home_Button:
                case R.id.Recommend_button:
                    intent = new Intent(SubscribePageActivity.this, MainActivity.class);
                    break;
                case R.id.Subscribe_button:
                    intent = new Intent(SubscribePageActivity.this, SubscribePageActivity.class);
                    break;
            }
            startActivity(intent);
        }
    }
}