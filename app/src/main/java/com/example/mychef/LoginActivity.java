package com.example.mychef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText ed1;
    private EditText ed2;
    private Button btnLogin;
    private EditText mEtUsername;
    private Button btn_forget;
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ed1 = (EditText) findViewById(R.id.et_1);
        Drawable drawable1 =  getResources().getDrawable(R.drawable.user_white);
        drawable1.setBounds(0, 0, 70, 70);
        ed1.setCompoundDrawables(drawable1,null, null, null);

        ed1 = (EditText) findViewById(R.id.et_2);
        Drawable drawable2 =  getResources().getDrawable(R.drawable.password);
        drawable2.setBounds(0, 0, 70, 70);
        ed1.setCompoundDrawables(drawable2,null, null, null);

        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Login Successfully!", Toast.LENGTH_SHORT).show();

                //jump to home
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Register Button Pressed!", Toast.LENGTH_SHORT).show();

                //jump to register
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btn_forget = (Button) findViewById(R.id.btn_forget);
        btn_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Forget Button Pressed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}