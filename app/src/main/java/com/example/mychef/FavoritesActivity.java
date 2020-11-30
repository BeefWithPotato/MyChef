package com.example.mychef;

import androidx.annotation.NonNull;
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
import android.widget.ListView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {

    private ListView mLv2;
    private ImageButton btn_profile, btn_createRecipe, mBanShopCar, mBanHome;

    ArrayList<Recipe> recipes = new ArrayList<Recipe>();
    ArrayList<String> likes;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        mLv2 = (ListView) findViewById(R.id.Favorites_LV);

        //get current user object from Firebase

        ref.child("User").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User userInfo = dataSnapshot.getValue(User.class);
                if(userInfo.getLikedRecipes().size() != 0){
                    likes = userInfo.getLikedRecipes();

                    // add data
                    ref.child("Recipe").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds: dataSnapshot.getChildren()) {
                                ref.child("Recipe").child(ds.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot recipeSnapshot: ds.getChildren()) {
                                            Log.i("recipeSnapshot", ":" + recipeSnapshot.getKey());
                                            for(int i = 0; i < likes.size(); i++) {
                                                if (recipeSnapshot.getKey().contains(likes.get(i))) {
                                                    Log.i("liked recipe:", ":" + likes.get(i));
                                                    Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                                                    recipes.add(recipe);
                                                    Log.i("Favourite recipe size:", ":" + recipes.size());
                                                    mLv2.setAdapter(new FavoritesLisViewAdapter(FavoritesActivity.this, recipes));
                                                }
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {}
                                });
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        mLv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //put recipe class in bundle
                Recipe recipe = recipes.get(position);
                Intent intent = new Intent(FavoritesActivity.this, DetailedRecipeActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString("recipeName", recipe.getRecipeName());
                bundle.putString("coverImage", recipe.getCoverImage());
                bundle.putString("story", recipe.getStory());
                bundle.putStringArrayList("ingredients", recipe.getIngredients());
                bundle.putStringArrayList("stepImages", recipe.getStepImages());
                bundle.putStringArrayList("stepDescriptions", recipe.getStepDescriptions());
                bundle.putString("tips", recipe.getTips());
                bundle.putString("kitchenWares", recipe.getKitchenWares());
                bundle.putString("authorUid", recipe.getAuthorUid());
                bundle.putString("authorUsername", recipe.getAuthorUsername());
                bundle.putInt("likes", recipe.getLikes());
                bundle.putString("cookingTime", recipe.getTime());
                bundle.putString("people", recipe.getPeople());
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });


        //Button control
        mBanHome = (ImageButton) findViewById(R.id.Home_Button);
        mBanShopCar = (ImageButton) findViewById(R.id.ShopCarButton);
        btn_profile = (ImageButton) findViewById(R.id.ProfileButton);
        btn_createRecipe = (ImageButton) findViewById(R.id.NewRecipeButton);
        setListeners();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ref.child("User").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User userInfo = dataSnapshot.getValue(User.class);
                if(userInfo.getLikedRecipes().size() != 0){
                    likes = userInfo.getLikedRecipes();

                    // add data
                    ref.child("Recipe").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds: dataSnapshot.getChildren()) {
                                ref.child("Recipe").child(ds.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot recipeSnapshot: ds.getChildren()) {
                                            Log.i("recipeSnapshot", ":" + recipeSnapshot.getKey());
                                            for(int i = 0; i < likes.size(); i++) {
                                                if (recipeSnapshot.getKey().contains(likes.get(i))) {
                                                    Log.i("liked recipe:", ":" + likes.get(i));
                                                    Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                                                    recipes.add(recipe);
                                                    Log.i("Favourite recipe size:", ":" + recipes.size());
                                                    mLv2.setAdapter(new FavoritesLisViewAdapter(FavoritesActivity.this, recipes));
                                                }
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {}
                                });
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        mLv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //put recipe class in bundle
                Recipe recipe = recipes.get(position);
                Intent intent = new Intent(FavoritesActivity.this, DetailedRecipeActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString("recipeName", recipe.getRecipeName());
                bundle.putString("coverImage", recipe.getCoverImage());
                bundle.putString("story", recipe.getStory());
                bundle.putStringArrayList("ingredients", recipe.getIngredients());
                bundle.putStringArrayList("stepImages", recipe.getStepImages());
                bundle.putStringArrayList("stepDescriptions", recipe.getStepDescriptions());
                bundle.putString("tips", recipe.getTips());
                bundle.putString("kitchenWares", recipe.getKitchenWares());
                bundle.putString("authorUid", recipe.getAuthorUid());
                bundle.putString("authorUsername", recipe.getAuthorUsername());
                bundle.putInt("likes", recipe.getLikes());
                bundle.putString("cookingTime", recipe.getTime());
                bundle.putString("people", recipe.getPeople());
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
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