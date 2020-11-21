package com.example.mychef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
    private ImageButton mBanShopCar;

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
                   Log.i("Uid:", ":" + follow.get(0));

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

                                   Log.i("get recipe size:", ":" + recipes.size());
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