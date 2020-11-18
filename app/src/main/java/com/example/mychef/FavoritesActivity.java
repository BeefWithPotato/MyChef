package com.example.mychef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;

public class FavoritesActivity extends AppCompatActivity {

    private ListView mLv2;

    private ImageButton mBanHome;
    private ImageButton mBanShopCar;
    private ImageButton btn_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        mLv2 = (ListView) findViewById(R.id.Favorites_LV);
        mLv2.setAdapter(new FavoritesLisViewAdapter(FavoritesActivity.this));

        //Button control
        mBanHome = (ImageButton) findViewById(R.id.Home_Button);
        mBanShopCar = (ImageButton) findViewById(R.id.ShopCarButton);
        btn_profile = (ImageButton) findViewById(R.id.ProfileButton);
        setListeners();
    }

    private void setListeners(){
        OnClick onClick = new OnClick();
        mBanHome.setOnClickListener(onClick);
        mBanShopCar.setOnClickListener(onClick);
        btn_profile.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()){
                case R.id.ShopCarButton:
                    intent = new Intent(FavoritesActivity.this, ShopCarActivity.class);
                    break;
                case R.id.Home_Button:
                    intent = new Intent(FavoritesActivity.this, MainActivity.class);
                    break;
                case R.id.ProfileButton:
                    intent = new Intent(FavoritesActivity.this, LoginActivity.class);
                    break;
            }
            startActivity(intent);
        }
    }
}