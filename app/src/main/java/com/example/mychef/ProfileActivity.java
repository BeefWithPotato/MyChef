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
    private Button logout, btn_edit;
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
        bg = findViewById(R.id.profile_bg);
        logout = findViewById(R.id.btn_logout);
        profile_icon = findViewById(R.id.profile_image);
        username = findViewById(R.id.profile_username);
        bio = findViewById(R.id.profile_bio);
        btn_edit = findViewById(R.id.btn_edit_profile);
        setListeners();



        //get current user object from Firebase
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
                Toast.makeText(ProfileActivity.this, "pos" + position, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setListeners(){
        OnClick onClick = new OnClick();
        mBanHome.setOnClickListener(onClick);
        mBanShopCar.setOnClickListener(onClick);
        btn_profile.setOnClickListener(onClick);
        btn_createRecipe.setOnClickListener(onClick);
        bg.setOnClickListener(onClick);
        logout.setOnClickListener(onClick);
        profile_icon.setOnClickListener(onClick);
        btn_edit.setOnClickListener(onClick);
        bio.setOnClickListener(onClick);
        mBanFavorites.setOnClickListener(onClick);
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
                case R.id.profile_bg:
                    intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                    break;
                case R.id.profile_image:
                    intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_ICON);
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if user choose an image complete
        if (requestCode == PICK_IMAGE) {
            //update bg ImageView
            bg.setImageURI(data.getData());
            Uri file = data.getData();
            // Upload image to Firebase
            //construct the position url where you want to store
            StorageReference bgRef = mStorageRef.child("images/" + "profiles/" + currentUser.getUid() + "/profileBackground.jpg");

            UploadTask uploadTask = bgRef.putFile(file);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return bgRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        //upload download url to Firebase
                        String downloadUri = task.getResult().toString();
                        ref.child("User").child(currentUser.getUid()).child("profileBg").setValue(downloadUri);

                        Toast.makeText(ProfileActivity.this, "Background Upload Succeed.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProfileActivity.this, "Background Upload failed.", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
        else if(requestCode == PICK_ICON){
            //update profile icon ImageView
            profile_icon.setImageURI(data.getData());
            Uri file = data.getData();
            // Upload profile icon to Firebase
            //construct the position url where you want to store
            StorageReference iconRef = mStorageRef.child("images/" + "profiles/" + currentUser.getUid() + "/profileIcon.jpg");

            UploadTask uploadTask = iconRef.putFile(file);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return iconRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        //upload download url to Firebase
                        String downloadUri = task.getResult().toString();
                        ref.child("User").child(currentUser.getUid()).child("userIcon").setValue(downloadUri);

                        Toast.makeText(ProfileActivity.this, "Icon Upload Succeed.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProfileActivity.this, "Icon Upload failed.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }




    public class ProfileListAdapter extends BaseAdapter {

        private Context mContext;
        private LayoutInflater mLayoutInflater;
        private  ArrayList<Recipe> mRecipes;
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
                convertView.setTag(holder);
            } else{
                holder = (ProfileListAdapter.ViewHolder) convertView.getTag();
            }

            Log.i("title:", ":" + recipes.size());
            holder.TitleText.setText(recipes.get(position).getRecipeName());
            Glide.with(holder.imageView.getContext()).load(recipes.get(position).getCoverImage()).into(holder.imageView);
            return convertView;
        }
    }

}