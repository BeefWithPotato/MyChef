package com.example.mychef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditGenderActivity extends AppCompatActivity {
    private ImageView btn_back;
    private Button save;
    private RadioGroup mgr1;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String gender = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_gender);

        btn_back = findViewById(R.id.edit_gender_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(EditGenderActivity.this, DetailProfileActivity.class);
//                startActivity(intent);
                finish();
            }
        });

        mgr1 = (RadioGroup) findViewById(R.id.rg_1);
        mgr1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                gender = radioButton.getText().toString();
                Toast.makeText(EditGenderActivity.this, "Choose " + radioButton.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        save = findViewById(R.id.gender_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User");
                ref.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User userInfo = dataSnapshot.getValue(User.class);
                        if (gender != null) {
                            ref.child(currentUser.getUid()).child("gender").setValue(gender);
                            Toast.makeText(EditGenderActivity.this, "Change succeed.", Toast.LENGTH_SHORT).show();

                            finish();
                        }
                        else {
                            finish();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });


    }
}