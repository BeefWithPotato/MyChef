package com.example.mychef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditAgeActivity extends AppCompatActivity {

    private ImageView btn_back;
    private Button save;
    private EditText age;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_age);

        btn_back = findViewById(R.id.edit_age_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(EditAgeActivity.this, DetailProfileActivity.class);
//                startActivity(intent);
                finish();
            }
        });
        age = findViewById(R.id.edit_age);
        Bundle bundle = getIntent().getExtras();
        age.setText(bundle.getString("age"));

        save = findViewById(R.id.age_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User");
                ref.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User userInfo = dataSnapshot.getValue(User.class);
                        if (age.getText().toString() != null) {
                            ref.child(currentUser.getUid()).child("age").setValue(age.getText().toString());
                            Toast.makeText(EditAgeActivity.this, "Change succeed.", Toast.LENGTH_SHORT).show();

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