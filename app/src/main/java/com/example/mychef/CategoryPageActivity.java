package com.example.mychef;

import androidx.appcompat.app.AlertDialog;
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

import java.util.ArrayList;
import java.util.Locale;

public class CategoryPageActivity extends AppCompatActivity {

    private GridView mGv;
    ArrayList<Category> categories = new ArrayList<Category>();
    private Button mBanSubscribe;
    private Button mBanRecommend;
    private Button mBanCategory, explore;
    private ImageButton mBanHome;
    private ImageButton mBanShopCar;
    private ImageButton btn_profile, btn_createRecipe, mBanFavorites;
    private ImageButton mBanSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_page);

        //init category classes
        //1 BBQ
        Category a = new Category();
        a.name = "BBQ";
        a.url = "https://specials-images.forbesimg.com/imageserve/5da9a71f0d2c2f0007f33d43/960x0.jpg?fit=scale";
        categories.add(a);
        //2 Braising
        Category b = new Category();
        b.name = "Braising";
        b.url = "https://tmbidigitalassetsazure.blob.core.windows.net/rms3-prod/attachments/37/1200x1200/exps41100_SD163324D08_18_1b_WEB.jpg";
        categories.add(b);
        //3 Baking
        Category c = new Category();
        c.name = "Baking";
        c.url = "https://img-s-msn-com.akamaized.net/tenant/amp/entityid/BB11ExMr.img?h=552&w=750&m=6&q=60&u=t&o=f&l=f&x=765&y=658";
        categories.add(c);
        //4 Stir-frying
        Category d = new Category();
        d.name = "Stir-frying";
        d.url = "https://d1e3z2jco40k3v.cloudfront.net/-/media/mccormick-us/recipes/lawrys/c/800/cashew_chicken_stir_fry_recipes_800x800.jpg";
        categories.add(d);
        //5 Soup
        Category e = new Category();
        e.name = "Soup";
        e.url = "https://www.seriouseats.com/recipes/images/2017/12/20171115-chicken-soup-vicky-wasik-11-1500x1125.jpg";
        categories.add(e);
        //6 Steaming
        Category f = new Category();
        f.name = "Steaming";
        f.url = "https://asianinspirations.com.au/wp-content/uploads/2018/09/Steaming-in-Bamboo-Steamer-940x627.jpg";
        categories.add(f);


        mGv = (GridView) findViewById(R.id.home_gv);
        mGv.setAdapter(new CategoryGridViewAdapter(CategoryPageActivity.this, categories));

        //Button control
        mBanSubscribe = (Button) findViewById(R.id.Subscribe_button);
        mBanRecommend = (Button) findViewById(R.id.Recommend_button);
        mBanCategory = (Button) findViewById(R.id.Category_button);
        mBanHome = (ImageButton) findViewById(R.id.Home_Button);
        mBanShopCar = (ImageButton) findViewById(R.id.ShopCarButton);
        btn_profile = (ImageButton) findViewById(R.id.ProfileButton);
        btn_createRecipe = (ImageButton) findViewById(R.id.NewRecipeButton);
        mBanFavorites = (ImageButton) findViewById(R.id.FavoritesButton);
        mBanSearch = (ImageButton) findViewById(R.id.search_button);
        explore = (Button) findViewById(R.id.Recommend_button);
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
        mBanSearch.setOnClickListener(onClick);
        explore.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            GoogleSignInAccount currAccount = GoogleSignIn.getLastSignedInAccount(CategoryPageActivity.this);
            FirebaseUser current = FirebaseAuth.getInstance().getCurrentUser();
            switch (v.getId()){
                case R.id.Recommend_button:
                    intent = new Intent(CategoryPageActivity.this, MainActivity.class);
                    break;
                case R.id.Subscribe_button:
                    if(currAccount == null && current == null){
                        intent = new Intent(CategoryPageActivity.this, LoginActivity.class);
                    }
                    else {
                        intent = new Intent(CategoryPageActivity.this, SubscribePageActivity.class);
                    }
                    break;
                case R.id.search_button:
                    intent = new Intent(CategoryPageActivity.this, SearchPageActivity.class);
                    break;
                case R.id.ShopCarButton:
                    intent = new Intent(CategoryPageActivity.this, ShopCarActivity.class);
                    break;
                case R.id.NewRecipeButton:
                    RotateAnimation r = new RotateAnimation(0.0f, 60.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    r.setDuration((long) 2*500);
                    r.setRepeatCount(0);
//
//                    GoogleSignInAccount currAccount = GoogleSignIn.getLastSignedInAccount(CategoryPageActivity.this);
//                    FirebaseUser current = FirebaseAuth.getInstance().getCurrentUser();
                    if(currAccount == null && current == null){
                        intent = new Intent(CategoryPageActivity.this, LoginActivity.class);
                    }
                    else {
                        intent = new Intent(CategoryPageActivity.this, CreateRecipeActivity.class);
                    }
                    break;
                case R.id.ProfileButton:
//                    GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(CategoryPageActivity.this);
//                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if(currAccount == null && current == null){
                        intent = new Intent(CategoryPageActivity.this, LoginActivity.class);
                    }
                    else {
                        intent = new Intent(CategoryPageActivity.this, ProfileActivity.class);
                    }
                    break;
                case R.id.FavoritesButton:
                    if(currAccount == null && current == null){
                        intent = new Intent(CategoryPageActivity.this, LoginActivity.class);
                    }
                    else {
                        intent = new Intent(CategoryPageActivity.this, FavoritesActivity.class);
                    }
                    break;
            }
            startActivity(intent);
        }
    }

    public class Category{
        public String name;
        public String url;
    }
}