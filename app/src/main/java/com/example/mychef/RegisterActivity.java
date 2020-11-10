package com.example.mychef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText phone, username, password;
    private Button register;

    private DatabaseReference ref;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        phone = (EditText) findViewById(R.id.set_phone);
        Drawable drawable1 =  getResources().getDrawable(R.drawable.phone);
        drawable1.setBounds(0, 0, 70, 70);
        phone.setCompoundDrawables(drawable1,null, null, null);

        username = (EditText) findViewById(R.id.set_username);
        password = (EditText) findViewById(R.id.set_password);

        register = (Button) findViewById(R.id.btn_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get data in the inputs
                int phone_number = Integer.parseInt(phone.getText().toString());

                //Firebase
                ref = FirebaseDatabase.getInstance().getReference().child("User");
                user = new User();
                user.setUsername(username.getText().toString());
                user.setPassword(password.getText().toString());
                user.setId(1);
                //reff.push().setValue(user);
                ref.child("user1").setValue(user);

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}