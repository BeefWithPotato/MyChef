package com.example.mychef;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private GridView mGv;
    ArrayList<Recipe> recipes = new ArrayList<Recipe>();
    private Recipe newRecipe;
    private User currentUserInfo;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    private Button mBanSubscribe;
    private Button mBanRecommend;
    private Button mBanCategory;
    private ImageButton mBanHome;
    private ImageButton mBanShopCar;
    private ImageButton btn_profile;
    private ImageButton btn_createRecipe;
    private ImageButton mBanFavorites;
    private ImageButton mBanSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // GridView
        mGv = (GridView) findViewById(R.id.home_gv);
        // add data
        ref.child("Recipe").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildren() != null){
                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                        for (DataSnapshot recipeSnapshot: ds.getChildren()) {
                            Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                            recipes.add(recipe);
                        }
                    }
                    Log.i("get recipe size:", ":" + recipes.size());
                    mGv.setAdapter(new HomeGridViewAdapter(MainActivity.this, recipes));
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        mGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recipe recipe = recipes.get(position);
                String refName = recipe.getAuthorUid() + recipe.getRecipeName();
                ref.child("Recipe").child(recipe.getAuthorUid()).child(refName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        newRecipe = dataSnapshot.getValue(Recipe.class);
                        //put recipe class in bundle

                        Intent intent = new Intent(MainActivity.this, DetailedRecipeActivity.class);
                        Bundle bundle = new Bundle();

                        bundle.putString("recipeName", newRecipe.getRecipeName());
                        bundle.putString("coverImage", newRecipe.getCoverImage());
                        bundle.putString("story", newRecipe.getStory());
                        bundle.putStringArrayList("ingredients", newRecipe.getIngredients());
                        bundle.putStringArrayList("stepImages", newRecipe.getStepImages());
                        bundle.putStringArrayList("stepDescriptions", newRecipe.getStepDescriptions());
                        bundle.putString("tips", newRecipe.getTips());
                        bundle.putString("kitchenWares", newRecipe.getKitchenWares());
                        bundle.putString("authorUid", newRecipe.getAuthorUid());
                        bundle.putString("authorUsername", newRecipe.getAuthorUsername());
                        bundle.putInt("likes", newRecipe.getLikes());
                        bundle.putString("cookingTime", newRecipe.getTime());
                        bundle.putString("people", newRecipe.getPeople());

                        intent.putExtras(bundle);
                        startActivity(intent);


                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });


            }
        });


        //Button control
        mBanSubscribe = (Button) findViewById(R.id.Subscribe_button);
        mBanRecommend = (Button) findViewById(R.id.Recommend_button);
        mBanCategory = (Button) findViewById(R.id.Category_button);
        mBanShopCar = (ImageButton) findViewById(R.id.ShopCarButton);
        btn_profile = (ImageButton) findViewById(R.id.ProfileButton);
        btn_createRecipe = (ImageButton) findViewById(R.id.NewRecipeButton);
        mBanFavorites = (ImageButton) findViewById(R.id.FavoritesButton);
        mBanSearch = (ImageButton) findViewById(R.id.search_button);
        setListeners();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recipes = new ArrayList<Recipe>();
        ref.child("Recipe").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildren() != null){
                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                        for (DataSnapshot recipeSnapshot: ds.getChildren()) {
                            Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                            recipes.add(recipe);
                        }
                    }
                    Log.i("get recipe size:", ":" + recipes.size());
                    mGv.setAdapter(new HomeGridViewAdapter(MainActivity.this, recipes));
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        mGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recipe recipe = recipes.get(position);
                String refName = recipe.getAuthorUid() + recipe.getRecipeName();
                ref.child("Recipe").child(recipe.getAuthorUid()).child(refName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        newRecipe = dataSnapshot.getValue(Recipe.class);
                        //put recipe class in bundle

                        Intent intent = new Intent(MainActivity.this, DetailedRecipeActivity.class);
                        Bundle bundle = new Bundle();

                        bundle.putString("recipeName", newRecipe.getRecipeName());
                        bundle.putString("coverImage", newRecipe.getCoverImage());
                        bundle.putString("story", newRecipe.getStory());
                        bundle.putStringArrayList("ingredients", newRecipe.getIngredients());
                        bundle.putStringArrayList("stepImages", newRecipe.getStepImages());
                        bundle.putStringArrayList("stepDescriptions", newRecipe.getStepDescriptions());
                        bundle.putString("tips", newRecipe.getTips());
                        bundle.putString("kitchenWares", newRecipe.getKitchenWares());
                        bundle.putString("authorUid", newRecipe.getAuthorUid());
                        bundle.putString("authorUsername", newRecipe.getAuthorUsername());
                        bundle.putInt("likes", newRecipe.getLikes());
                        bundle.putString("cookingTime", newRecipe.getTime());
                        bundle.putString("people", newRecipe.getPeople());

                        intent.putExtras(bundle);
                        startActivity(intent);


                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }
        });
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
    }

    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            Intent intent = null;
            switch (v.getId()){
                case R.id.search_button:
                    intent = new Intent(MainActivity.this, SearchPageActivity.class);
                    break;
                case R.id.ShopCarButton:
                    intent = new Intent(MainActivity.this, ShopCarActivity.class);
                    break;
                case R.id.Category_button:
                    intent = new Intent(MainActivity.this, CategoryPageActivity.class);
                    break;
                case R.id.Recommend_button:
                    intent = new Intent(MainActivity.this, MainActivity.class);
                    break;
                case R.id.Subscribe_button:
                    if(acct == null && currentUser == null){
                        intent = new Intent(MainActivity.this, LoginActivity.class);
                    }
                    else {
                        intent = new Intent(MainActivity.this, SubscribePageActivity.class);
                    }
                    break;
                case R.id.ProfileButton:
                    if(acct == null && currentUser == null){
                        intent = new Intent(MainActivity.this, LoginActivity.class);
                    }
                    else {
                        intent = new Intent(MainActivity.this, ProfileActivity.class);
                    }
                    break;
                case R.id.NewRecipeButton:
                    RotateAnimation r = new RotateAnimation(0.0f, 60.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    r.setDuration((long) 2*500);
                    r.setRepeatCount(0);

                    if(acct == null && currentUser == null){
                        intent = new Intent(MainActivity.this, LoginActivity.class);
                    }
                    else {
                        intent = new Intent(MainActivity.this, CreateRecipeActivity.class);
                    }
                    break;
                case R.id.FavoritesButton:
                    if(acct == null && currentUser == null){
                        intent = new Intent(MainActivity.this, LoginActivity.class);
                    }
                    else {
                        intent = new Intent(MainActivity.this, FavoritesActivity.class);
                    }
                    break;
            }
            startActivity(intent);
        }
    }


}