package com.example.mychef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditBioActivity extends AppCompatActivity {

    private ImageView btn_back;
    private Button save;
    private EditText bio;
    private TextView wc;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bio);

        btn_back = findViewById(R.id.edit_bio_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(EditBioActivity.this, DetailProfileActivity.class);
//                startActivity(intent);
                finish();
            }
        });

        bio = findViewById(R.id.edit_bio);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            bio.setText(bundle.getString("bio"));
        }

        wc = findViewById(R.id.wc_bio);
        Integer maxNum = 70;
        bio.addTextChangedListener(new TextWatcher() {

            private CharSequence wordNum;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wordNum = s;
            }

            @Override
            // reference: https://www.cnblogs.com/zhujiabin/p/5280170.html
            public void afterTextChanged(Editable s) {
                wc.setText("" + (maxNum - s.length()) +"/" + maxNum);
                selectionStart = bio.getSelectionStart();
                selectionEnd = bio.getSelectionEnd();
                if (wordNum.length() > maxNum) {
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    bio.setText(s);
                    bio.setSelection(tempSelection);
                    Toast toast = Toast.makeText(EditBioActivity.this, Html.fromHtml("<font color='#FF0000' ><b>" + "Maximum 70 words!" + "</b></font>"), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User");
                ref.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User userInfo = dataSnapshot.getValue(User.class);
                        if (bio.getText().toString() != null) {
                            ref.child(currentUser.getUid()).child("bio").setValue(bio.getText().toString());
                            Toast.makeText(EditBioActivity.this, "Change succeed.", Toast.LENGTH_SHORT).show();

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