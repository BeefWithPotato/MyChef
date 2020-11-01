package com.example.mychef;

import androidx.appcompat.app.AppCompatActivity;

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
            }
        });

        mEtUsername = (EditText) findViewById(R.id.et_1);
        mEtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("Login", s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}