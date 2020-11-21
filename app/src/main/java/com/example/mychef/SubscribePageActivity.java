package com.example.mychef;

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

public class SubscribePageActivity extends AppCompatActivity {

    private GridView mGv;
    ArrayList<Recipe> recipes = new ArrayList<Recipe>();
    ArrayList<String> follow;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();


    private Button mBanSubscribe;
    private Button mBanRecommend;
    private Button mBanCategory;
    private ImageButton mBanHome;
    private ImageButton mBanShopCar, btn_profile, btn_createRecipe, mBanFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe_page);

        mGv = (GridView) findViewById(R.id.home_gv);
        //get follow users
        //get current user object from Firebase
        ref.child("User").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               User userInfo = dataSnapshot.getValue(User.class);
               if(userInfo.getFollow().size() != 0){
                   follow = userInfo.getFollow();

                   // add data
                   for(int i = 0; i < follow.size(); i++){
                       ref.child("Recipe").child(follow.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(DataSnapshot dataSnapshot) {
                               if(dataSnapshot.getChildren() != null){
                                   for (DataSnapshot recipeSnapshot: dataSnapshot.getChildren()) {
                                       Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                                       recipes.add(recipe);
                                   }
                                   mGv.setAdapter(new SubscribeGridViewAdapter(SubscribePageActivity.this, recipes));
                               }
                           }
                           @Override
                           public void onCancelled(DatabaseError databaseError) {}
                       });
                   }
               }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        mGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SubscribePageActivity.this, "pos" + position, Toast.LENGTH_SHORT).show();
            }
        });

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
        SubscribePageActivity.OnClick onClick = new SubscribePageActivity.OnClick();
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
                    intent = new Intent(SubscribePageActivity.this, ShopCarActivity.class);
                    break;
                case R.id.Category_button:
                    intent = new Intent(SubscribePageActivity.this, CategoryPageActivity.class);
                    break;
                case R.id.Recommend_button:
                    intent = new Intent(SubscribePageActivity.this, MainActivity.class);
                    break;
                case R.id.Subscribe_button:
                    intent = new Intent(SubscribePageActivity.this, SubscribePageActivity.class);
                    break;
                case R.id.NewRecipeButton:
                    RotateAnimation r = new RotateAnimation(0.0f, 60.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    r.setDuration((long) 2*500);
                    r.setRepeatCount(0);

                    GoogleSignInAccount currAccount = GoogleSignIn.getLastSignedInAccount(SubscribePageActivity.this);
                    FirebaseUser current = FirebaseAuth.getInstance().getCurrentUser();
                    if(currAccount == null && current == null){
                        intent = new Intent(SubscribePageActivity.this, LoginActivity.class);
                    }
                    else {
                        intent = new Intent(SubscribePageActivity.this, CreateRecipeActivity.class);
                    }
                    break;
                case R.id.ProfileButton:
                    GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(SubscribePageActivity.this);
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if(acct == null && currentUser == null){
                        intent = new Intent(SubscribePageActivity.this, LoginActivity.class);
                    }
                    else {
                        intent = new Intent(SubscribePageActivity.this, ProfileActivity.class);
                    }
                    break;
                case R.id.FavoritesButton:
                    intent = new Intent(SubscribePageActivity.this, FavoritesActivity.class);
                    break;

            }
            startActivity(intent);
        }
    }
}