package com.example.mychef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
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

    private ImageView bg;

    private Button logout;
    private GoogleSignInClient mGoogleSignInClient;
    private StorageReference mStorageRef;
    public static final int PICK_IMAGE = 1;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User");
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        bg = findViewById(R.id.progile_bg);

        //if user has uploaded a bg image, get from Firebase
        ref.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User userInfo = dataSnapshot.getValue(User.class);
                if(userInfo.getProfileBg() != null){
                    Glide.with(ProfileActivity.this).load(userInfo.getProfileBg()).into(bg);
                }
                //if not use default
                else{
                    bg.setImageResource(R.drawable.profile_bg);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        //sign out
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        logout = findViewById(R.id.btn_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //google sign out
                mGoogleSignInClient.signOut();
                //Firebase sign out
                FirebaseAuth.getInstance().signOut();
                //select images from user album
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(ProfileActivity.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if user choose an image complete
        if (requestCode == PICK_IMAGE) {
            //update bg ImageView
            bg.setImageURI(data.getData());
            Uri file = data.getData();
            // Upload image to Firebase
            // Create a storage reference from our app
            mStorageRef = FirebaseStorage.getInstance().getReference();
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

                        Toast.makeText(ProfileActivity.this, "Upload Succeed.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProfileActivity.this, "Upload failed.", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }



}