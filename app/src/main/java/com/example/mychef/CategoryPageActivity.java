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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class CategoryPageActivity extends AppCompatActivity {

    private GridView mGv;

    private Button mBanSubscribe;
    private Button mBanRecommend;
    private Button mBanCategory;
    private ImageButton mBanHome;
    private ImageButton mBanShopCar;
    private ImageButton btn_profile, btn_createRecipe, mBanFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_page);
        mGv = (GridView) findViewById(R.id.home_gv);
        mGv.setAdapter(new CategoryGridViewAdapter(CategoryPageActivity.this));

        //Button control
        mBanSubscribe = (Button) findViewById(R.id.Subscribe_button);
        mBanRecommend = (Button) findViewById(R.id.Recommend_button);
        mBanCategory = (Button) findViewById(R.id.Category_button);
        mBanHome = (ImageButton) findViewById(R.id.Home_Button);
        mBanShopCar = (ImageButton) findViewById(R.id.ShopCarButton);
        btn_profile = (ImageButton) findViewById(R.id.ProfileButton);
        btn_createRecipe = (ImageButton) findViewById(R.id.NewRecipeButton);
        mBanFavorites = (ImageButton) findViewById(R.id.FavoritesButton);
        setListeners();
    }

    private void setListeners(){
        OnClick onClick = new OnClick();
        mBanSubscribe.setOnClickListener(onClick);
        mBanRecommend.setOnClickListener(onClick);
        mBanCategory.setOnClickListener(onClick);
        mBanShopCar.setOnClickListener(onClick);
        btn_profile.setOnClickListener(onClick);
        btn_createRecipe.setOnClickListener(onClick);
        mBanFavorites.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()){
                case R.id.ShopCarButton:
                    intent = new Intent(CategoryPageActivity.this, ShopCarActivity.class);
                    break;
                case R.id.Category_button:
                    intent = new Intent(CategoryPageActivity.this, CategoryPageActivity.class);
                    break;
                case R.id.Recommend_button:
                    intent = new Intent(CategoryPageActivity.this, MainActivity.class);
                    break;
                case R.id.Subscribe_button:
                    intent = new Intent(CategoryPageActivity.this, SubscribePageActivity.class);
                    break;
                case R.id.NewRecipeButton:
                    RotateAnimation r = new RotateAnimation(0.0f, 60.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    r.setDuration((long) 2*500);
                    r.setRepeatCount(0);

                    GoogleSignInAccount currAccount = GoogleSignIn.getLastSignedInAccount(CategoryPageActivity.this);
                    FirebaseUser current = FirebaseAuth.getInstance().getCurrentUser();
                    if(currAccount == null && current == null){
                        intent = new Intent(CategoryPageActivity.this, LoginActivity.class);
                    }
                    else {
                        intent = new Intent(CategoryPageActivity.this, CreateRecipeActivity.class);
                    }
                    break;
                case R.id.ProfileButton:
                    GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(CategoryPageActivity.this);
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if(acct == null && currentUser == null){
                        intent = new Intent(CategoryPageActivity.this, LoginActivity.class);
                    }
                    else {
                        intent = new Intent(CategoryPageActivity.this, ProfileActivity.class);
                    }
                    break;
                case R.id.FavoritesButton:
                    intent = new Intent(CategoryPageActivity.this, FavoritesActivity.class);
                    break;
            }
            startActivity(intent);
        }
    }
}