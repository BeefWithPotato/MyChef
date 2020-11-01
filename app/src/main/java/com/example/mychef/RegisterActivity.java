package com.example.mychef;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    private EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        phone = (EditText) findViewById(R.id.set_phone);
        Drawable drawable1 =  getResources().getDrawable(R.drawable.phone);
        drawable1.setBounds(0, 0, 70, 70);
        phone.setCompoundDrawables(drawable1,null, null, null);
    }
}