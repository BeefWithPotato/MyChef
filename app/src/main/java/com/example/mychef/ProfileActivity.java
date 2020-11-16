package com.example.mychef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class ProfileActivity extends AppCompatActivity {

    private ImageView bg, profile_icon;
    private ImageButton mBanHome, btn_profile, mBanShopCar;
    private TextView username, bio;
    private Button logout, btn_edit;
    private GoogleSignInClient mGoogleSignInClient;
    public static final int PICK_IMAGE = 1;
    public static final int PICK_ICON = 2;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User");
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    private User userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Button control
        mBanHome = (ImageButton) findViewById(R.id.Home_Button);
        mBanShopCar = (ImageButton) findViewById(R.id.ShopCarButton);
        btn_profile = (ImageButton) findViewById(R.id.ProfileButton);
        bg = findViewById(R.id.profile_bg);
        logout = findViewById(R.id.btn_logout);
        profile_icon = findViewById(R.id.profile_image);
        username = findViewById(R.id.profile_username);
        bio = findViewById(R.id.profile_bio);
        btn_edit = findViewById(R.id.btn_edit_profile);
        setListeners();


        //get current user object from Firebase
        ref.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
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

    }

    private void setListeners(){
        OnClick onClick = new OnClick();
        mBanHome.setOnClickListener(onClick);
        mBanShopCar.setOnClickListener(onClick);
        btn_profile.setOnClickListener(onClick);
        bg.setOnClickListener(onClick);
        logout.setOnClickListener(onClick);
        profile_icon.setOnClickListener(onClick);
        btn_edit.setOnClickListener(onClick);
        bio.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()){
                case R.id.profile_bio:
                    intent = new Intent(ProfileActivity.this, DetailProfileActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_edit_profile:
                    intent = new Intent(ProfileActivity.this, DetailProfileActivity.class);
                    startActivity(intent);
                    break;
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
                    //select images from user album
                    intent = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(ProfileActivity.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
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
                case R.id.ProfileButton:
                    GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(ProfileActivity.this);
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if(acct == null && currentUser == null){
                        intent = new Intent(ProfileActivity.this, LoginActivity.class);
                    }
                    else {
                        intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                    }
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
                        ref.child(currentUser.getUid()).child("profileBg").setValue(downloadUri);

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
                        ref.child(currentUser.getUid()).child("userIcon").setValue(downloadUri);

                        Toast.makeText(ProfileActivity.this, "Icon Upload Succeed.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProfileActivity.this, "Icon Upload failed.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }



}