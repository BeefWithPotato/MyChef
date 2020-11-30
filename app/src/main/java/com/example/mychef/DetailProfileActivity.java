package com.example.mychef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import java.io.IOException;
import java.lang.reflect.Field;

public class DetailProfileActivity extends AppCompatActivity {

    private ImageView btn_back, bg_image, avatar;
    private TextView username, email, age, bio, gender;
    private View row_username, row_email, row_age, row_bio, row_gender, row_avatar, row_bg;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User");
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    User userInfo;
    public static final int PICK_IMAGE = 1;
    public static final int PICK_ICON = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_profile);

        btn_back = findViewById(R.id.detail_profile_back);
        username = findViewById(R.id.detail_username);
        email = findViewById(R.id.detail_email);
        age = findViewById(R.id.detail_age);
        gender = findViewById(R.id.detail_gender);
        bio = findViewById(R.id.detail_bio);
        avatar = findViewById(R.id.detail_avatar);
        bg_image = findViewById(R.id.detail_bg_image);

        row_username= findViewById(R.id.username_relative_layout);
        row_email= findViewById(R.id.email_relative_layout);
        row_age= findViewById(R.id.age_relative_layout);
        row_gender= findViewById(R.id.gender_relative_layout);
        row_bio= findViewById(R.id.bio_relative_layout);
        row_avatar = findViewById(R.id.avatar_relative_layout);
        row_bg = findViewById(R.id.bg_image_relative_layout);
        setListeners();

        //get current user object from Firebase
        ref.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInfo = dataSnapshot.getValue(User.class);

                username.setText(userInfo.getUsername());
                email.setText(userInfo.getEmail());
                //if user has set a bg image, then get and set it

                if(userInfo.getAge() != null){
                    age.setText(userInfo.getAge());
                }

                if(userInfo.getGender() != null){
                    gender.setText(userInfo.getGender());
                }

                if(userInfo.getBio() != null){
                    bio.setText(userInfo.getBio());
                }
                if(userInfo.getUserIcon() != null){
                    Glide.with(DetailProfileActivity.this).load(userInfo.getUserIcon()).into(avatar);
                }
                if(userInfo.getProfileBg() != null){
                    Glide.with(DetailProfileActivity.this).load(userInfo.getProfileBg()).into(bg_image);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        ref.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInfo = dataSnapshot.getValue(User.class);

                username.setText(userInfo.getUsername());
                email.setText(userInfo.getEmail());
                //if user has set a bg image, then get and set it

                if(userInfo.getAge() != null){
                    age.setText(userInfo.getAge());
                }

                if(userInfo.getGender() != null){
                    gender.setText(userInfo.getGender());
                }

                if(userInfo.getBio() != null){
                    bio.setText(userInfo.getBio());
                }
                if(userInfo.getUserIcon() != null){
                    Glide.with(DetailProfileActivity.this).load(userInfo.getUserIcon()).into(avatar);
                }
                if(userInfo.getProfileBg() != null){
                    Glide.with(DetailProfileActivity.this).load(userInfo.getProfileBg()).into(bg_image);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void setListeners(){
        OnClick onClick = new OnClick();
        row_username.setOnClickListener(onClick);
        row_gender.setOnClickListener(onClick);
        row_age.setOnClickListener(onClick);
        row_email.setOnClickListener(onClick);
        row_bio.setOnClickListener(onClick);
        row_bg.setOnClickListener(onClick);
        row_avatar.setOnClickListener(onClick);
        btn_back.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener {
        @SuppressLint("RestrictedApi")
        @Override
        public void onClick(View v) {
            Intent intent = null;
            Bundle bundle = null;
            switch (v.getId()) {
                case R.id.detail_profile_back:
                    finish();
                    break;
                case R.id.username_relative_layout:
                    intent = new Intent(DetailProfileActivity.this, EditUsernameActivity.class);
                    bundle = new Bundle();
                    bundle.putString("username", userInfo.getUsername());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case R.id.age_relative_layout:
                    intent = new Intent(DetailProfileActivity.this, EditAgeActivity.class);
                    if(userInfo.getAge() != null){
                        bundle = new Bundle();
                        bundle.putString("age", userInfo.getAge());
                        intent.putExtras(bundle);
                    }
                    startActivity(intent);
                    break;
                case R.id.gender_relative_layout:
                    intent = new Intent(DetailProfileActivity.this, EditGenderActivity.class);
                    startActivity(intent);
                    break;
                case R.id.bio_relative_layout:
                    intent = new Intent(DetailProfileActivity.this, EditBioActivity.class);
                    if(userInfo.getBio() != null){
                        bundle = new Bundle();
                        bundle.putString("bio", userInfo.getBio());
                        intent.putExtras(bundle);
                    }
                    startActivity(intent);
                    break;
                case R.id.avatar_relative_layout:
                    intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_ICON);
                    break;
                case R.id.bg_image_relative_layout:
                    intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                    break;
            }

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if user choose an image complete
        if (requestCode == PICK_IMAGE) {
            //update bg ImageView
            bg_image.setImageURI(data.getData());
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

                        Toast.makeText(DetailProfileActivity.this, "Background Upload Succeed.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DetailProfileActivity.this, "Background Upload failed.", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
        else if(requestCode == PICK_ICON){
            try {
                //update profile icon ImageView
                avatar.setImageURI(data.getData());
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

                            Toast.makeText(DetailProfileActivity.this, "Icon Upload Succeed.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DetailProfileActivity.this, "Icon Upload failed.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}