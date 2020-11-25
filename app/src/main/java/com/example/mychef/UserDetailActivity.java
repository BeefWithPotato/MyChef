package com.example.mychef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
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

public class UserDetailActivity extends AppCompatActivity {

    private ImageView bg, profile_icon, back;
    private TextView username, bio;
    private GoogleSignInClient mGoogleSignInClient;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    private User userInfo;
    ArrayList<Recipe> recipes = new ArrayList<Recipe>();
    String select_user_uid;
    private ListView profile_lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        bg = findViewById(R.id.profile_bg);
        profile_icon = findViewById(R.id.profile_image);
        username = findViewById(R.id.profile_username);
        bio = findViewById(R.id.profile_bio);

        //back
        back = findViewById(R.id.user_profile_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //get select user object from Firebase
        Bundle bundle = getIntent().getExtras();
        select_user_uid = bundle.getString("uid");

        ref.child("User").child(select_user_uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInfo = dataSnapshot.getValue(User.class);
                //get username
                username.setText(userInfo.getUsername());
                //if user has set a bg image, then get and set it

                if(userInfo.getProfileBg() != null){
                    Glide.with(UserDetailActivity.this).load(userInfo.getProfileBg()).into(bg);
                }
                //if not use default
                else{
                    bg.setImageResource(R.drawable.bg_grey);
                }

                //if user has set an icon, then get and set it
                if(userInfo.getUserIcon() != null){
                    Glide.with(UserDetailActivity.this).load(userInfo.getUserIcon()).into(profile_icon);
                }
                //if not use default
                else{
                    profile_icon.setImageResource(R.drawable.usericon);
                }

                if(userInfo.getBio() != null){
                    bio.setText(userInfo.getBio());
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        // ListView
        profile_lv = findViewById(R.id.profile_lv);
        //Add data to recipe list
        ref.child("Recipe").child(select_user_uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildren() != null){
                    for (DataSnapshot recipeSnapshot: dataSnapshot.getChildren()) {
                        Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                        recipes.add(recipe);
                    }
                    Log.i("get recipe size:", ":" + recipes.size());
                    profile_lv.setAdapter(new UserDetailAdapter(UserDetailActivity.this));
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });


        profile_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //put recipe class in bundle
                Recipe recipe = recipes.get(position);
                Intent intent = new Intent(UserDetailActivity.this, DetailedRecipeActivity.class);
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

                intent.putExtras(bundle);
                startActivity(intent);

                Toast.makeText(UserDetailActivity.this, "pos" + position, Toast.LENGTH_SHORT).show();
            }
        });



    }



    public class UserDetailAdapter extends BaseAdapter {

        private Context mContext;
        private LayoutInflater mLayoutInflater;
        private ArrayList<Recipe> mRecipes;
        ViewHolder holder = null;
        public UserDetailAdapter(Context context){
            this.mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return recipes.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        class ViewHolder{
            public ImageView imageView;
            public TextView TitleText;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null){
                convertView = mLayoutInflater.inflate(R.layout.profile_list_item, null);
                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.profile_recipe_cover_image);
                holder.TitleText = (TextView) convertView.findViewById(R.id.profile_recipe_title);
                convertView.setTag(holder);
            } else{
                holder = (ViewHolder) convertView.getTag();
            }

            Log.i("title:", ":" + recipes.size());
            holder.TitleText.setText(recipes.get(position).getRecipeName());
            Glide.with(holder.imageView.getContext()).load(recipes.get(position).getCoverImage()).into(holder.imageView);
            return convertView;
        }
    }
}