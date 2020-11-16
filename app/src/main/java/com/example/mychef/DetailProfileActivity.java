package com.example.mychef;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;

public class DetailProfileActivity extends AppCompatActivity {

    private ImageView btn_back;
    private TextView username, email, age, bio, gender;
    private View row_username, row_email, row_age, row_bio, row_gender;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User");
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private User userInfo;
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
        row_username= findViewById(R.id.username_relative_layout);
        row_email= findViewById(R.id.email_relative_layout);
        row_age= findViewById(R.id.age_relative_layout);
        row_gender= findViewById(R.id.gender_relative_layout);
        row_bio= findViewById(R.id.bio_relative_layout);
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
        btn_back.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener {
        @SuppressLint("RestrictedApi")
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.detail_profile_back:
                    intent = new Intent(DetailProfileActivity.this, ProfileActivity.class);
                    break;
                case R.id.username_relative_layout:
                    intent = new Intent(DetailProfileActivity.this, EditUsernameActivity.class);
                    break;
                case R.id.age_relative_layout:
                    intent = new Intent(DetailProfileActivity.this, EditAgeActivity.class);
                    break;
                case R.id.gender_relative_layout:
                    intent = new Intent(DetailProfileActivity.this, EditGenderActivity.class);
                    break;
                case R.id.bio_relative_layout:
                    intent = new Intent(DetailProfileActivity.this, EditBioActivity.class);
                    break;
            }
            startActivity(intent);
        }
    }

}