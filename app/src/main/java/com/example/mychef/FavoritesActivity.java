package com.example.mychef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FavoritesActivity extends AppCompatActivity {

    private ListView mLv2;

    private ImageButton btn_profile, btn_createRecipe, mBanShopCar, mBanHome;

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
        btn_createRecipe = (ImageButton) findViewById(R.id.NewRecipeButton);
        setListeners();
    }

    private void setListeners(){
        OnClick onClick = new OnClick();
        mBanHome.setOnClickListener(onClick);
        mBanShopCar.setOnClickListener(onClick);
        btn_profile.setOnClickListener(onClick);
        btn_createRecipe.setOnClickListener(onClick);
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
                case R.id.NewRecipeButton:
                    RotateAnimation r = new RotateAnimation(0.0f, 60.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    r.setDuration((long) 2*500);
                    r.setRepeatCount(0);
                    btn_createRecipe.startAnimation(r);

                    intent = new Intent(FavoritesActivity.this, CreateRecipeActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ProfileButton:
                    GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(FavoritesActivity.this);
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if(acct == null && currentUser == null){
                        intent = new Intent(FavoritesActivity.this, LoginActivity.class);
                    }
                    else {
                        intent = new Intent(FavoritesActivity.this, ProfileActivity.class);
                    }
                    startActivity(intent);
                    break;
            }
            startActivity(intent);
        }
    }
}