package com.example.mychef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

public class ShopCarActivity extends AppCompatActivity {

    private ListView mLv1;

    private ImageButton mBanBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_car);

        mLv1 = (ListView) findViewById(R.id.shopCar_LV);
        mLv1.setAdapter(new ShopCarListViewAdapter(ShopCarActivity.this));

        //Button control
        mBanBack = (ImageButton) findViewById(R.id.Back_Button);
    }

    private void setListeners(){
        OnClick onClick = new OnClick();
        mBanBack.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()){
                case R.id.Back_Button:
                    intent = new Intent(ShopCarActivity.this, MainActivity.class);
                    break;
            }
            startActivity(intent);
        }
    }
}