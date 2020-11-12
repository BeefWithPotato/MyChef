package com.example.mychef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText email, username, password, retype_password;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = (EditText) findViewById(R.id.set_email);
        Drawable drawable1 =  getResources().getDrawable(R.drawable.email);
        drawable1.setBounds(0, 0, 55, 55);
        email.setCompoundDrawables(drawable1,null, null, null);

        username = (EditText) findViewById(R.id.set_username);
        password = (EditText) findViewById(R.id.set_password);
        retype_password = (EditText) findViewById(R.id.retype_password);

        register = (Button) findViewById(R.id.btn_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check if two passwords match
                if(!(password.getText().toString().equals(retype_password.getText().toString()))){
                    Log.i("Passwords", "1st:" + password.getText().toString() + " 2nd:" + retype_password.getText().toString());
                    Toast.makeText(RegisterActivity.this, "Register failed: Password Not Match!", Toast.LENGTH_LONG).show();
                }
                //check if email address format is valid
                else if(!isEmailValid(email.getText().toString())){
                    Toast.makeText(RegisterActivity.this, "Register failed: Invalid Email", Toast.LENGTH_LONG).show();
                }
                else {
                    //Authentication
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        //send info to database
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User");
                                        User newUser = new User();
                                        newUser.setUsername(username.getText().toString());
                                        newUser.setPassword(password.getText().toString());
                                        newUser.setEmail(email.getText().toString());
                                        ref.child(user.getUid()).setValue(newUser);
                                        Toast.makeText(RegisterActivity.this, "Authentication succeed.", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(RegisterActivity.this, "Register failed.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}