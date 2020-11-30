package com.example.mychef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private ImageView bg, profile_icon;
    private ImageButton mBanHome, btn_profile, mBanShopCar, btn_createRecipe, mBanFavorites;
    private TextView username, bio;
    private Button logout, btn_edit, delete;
    private GoogleSignInClient mGoogleSignInClient;
    public static final int PICK_IMAGE = 1;
    public static final int PICK_ICON = 2;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    private User userInfo;
    ArrayList<Recipe> recipes = new ArrayList<Recipe>();
    private ListView profile_lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Button control
        mBanHome = (ImageButton) findViewById(R.id.Home_Button);
        mBanShopCar = (ImageButton) findViewById(R.id.ShopCarButton);
        btn_profile = (ImageButton) findViewById(R.id.ProfileButton);
        btn_createRecipe = (ImageButton) findViewById(R.id.NewRecipeButton);
        mBanFavorites = (ImageButton) findViewById(R.id.FavoritesButton);
        logout = findViewById(R.id.btn_logout);
        bg = findViewById(R.id.profile_bg);
        profile_icon = findViewById(R.id.profile_image);
        username = findViewById(R.id.profile_username);
        bio = findViewById(R.id.profile_bio);
        btn_edit = findViewById(R.id.btn_edit_profile);
        setListeners();



        // get current user object from Firebase
        ref.child("User").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInfo = dataSnapshot.getValue(User.class);
                //get username
                username.setText(userInfo.getUsername());
                //if user has set a bg image, then get and set it

                if(userInfo.getProfileBg() != null){
                    Glide.with(ProfileActivity.this).load(userInfo.getProfileBg()).into(bg);
                }
                //if not use default
                else{
                    bg.setImageResource(R.drawable.bg_grey);
                }

                //if user has set an icon, then get and set it
                if(userInfo.getUserIcon() != null){
                    Glide.with(ProfileActivity.this).load(userInfo.getUserIcon()).into(profile_icon);
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
        ref.child("Recipe").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildren() != null){
                    for (DataSnapshot recipeSnapshot: dataSnapshot.getChildren()) {
                        Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                        recipes.add(recipe);
                    }
                    Log.i("get recipe size:", ":" + recipes.size());
                    profile_lv.setAdapter(new ProfileListAdapter(ProfileActivity.this));
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
                Intent intent = new Intent(ProfileActivity.this, DetailedRecipeActivity.class);
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
        logout.setOnClickListener(onClick);
        btn_edit.setOnClickListener(onClick);
        bio.setOnClickListener(onClick);
        mBanFavorites.setOnClickListener(onClick);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ref.child("User").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInfo = dataSnapshot.getValue(User.class);
                //get username
                username.setText(userInfo.getUsername());
                //if user has set a bg image, then get and set it

                if(userInfo.getProfileBg() != null){
                    Glide.with(ProfileActivity.this).load(userInfo.getProfileBg()).into(bg);
                }
                //if not use default
                else{
                    bg.setImageResource(R.drawable.bg_grey);
                }

                //if user has set an icon, then get and set it
                if(userInfo.getUserIcon() != null){
                    Glide.with(ProfileActivity.this).load(userInfo.getUserIcon()).into(profile_icon);
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

        //Add data to recipe list
        recipes = new ArrayList<Recipe>();
        ref.child("Recipe").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildren() != null){
                    for (DataSnapshot recipeSnapshot: dataSnapshot.getChildren()) {
                        Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                        recipes.add(recipe);
                    }
                    Log.i("get recipe size:", ":" + recipes.size());
                    profile_lv.setAdapter(new ProfileListAdapter(ProfileActivity.this));
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
                Intent intent = new Intent(ProfileActivity.this, DetailedRecipeActivity.class);
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

    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()){
                case R.id.btn_logout:
                    //sign out
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestEmail()
                            .build();
                    // Build a GoogleSignInClient with the options specified by gso.
                    mGoogleSignInClient = GoogleSignIn.getClient(ProfileActivity.this, gso);
                    //google sign out
                    mGoogleSignInClient.signOut();
                    //Firebase sign out
                    FirebaseAuth.getInstance().signOut();
                    intent = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(ProfileActivity.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.profile_bio:
                    intent = new Intent(ProfileActivity.this, DetailProfileActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_edit_profile:
                    intent = new Intent(ProfileActivity.this, DetailProfileActivity.class);
                    startActivity(intent);
                    break;
                case R.id.Home_Button:
                    intent = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ShopCarButton:
                    intent = new Intent(ProfileActivity.this, ShopCarActivity.class);
                    startActivity(intent);
                    break;
                case R.id.NewRecipeButton:
                    RotateAnimation r = new RotateAnimation(0.0f, 60.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    r.setDuration((long) 2*500);
                    r.setRepeatCount(0);
                    btn_createRecipe.startAnimation(r);

                    intent = new Intent(ProfileActivity.this, CreateRecipeActivity.class);
                    startActivity(intent);
                    break;
                case R.id.FavoritesButton:
                    intent = new Intent(ProfileActivity.this, FavoritesActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    public class ProfileListAdapter extends BaseAdapter {

        private Context mContext;
        private LayoutInflater mLayoutInflater;
        ViewHolder holder = null;
        public ProfileListAdapter(Context context){
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
                holder = new ProfileListAdapter.ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.profile_recipe_cover_image);
                holder.TitleText = (TextView) convertView.findViewById(R.id.profile_recipe_title);

            } else{
                holder = (ProfileListAdapter.ViewHolder) convertView.getTag();
            }

            Log.i("title:", ":" + recipes.size());
            holder.TitleText.setText(recipes.get(position).getRecipeName());
            Glide.with(holder.imageView.getContext()).load(recipes.get(position).getCoverImage()).into(holder.imageView);

            convertView.setTag(holder);

            return convertView;
        }
    }

}