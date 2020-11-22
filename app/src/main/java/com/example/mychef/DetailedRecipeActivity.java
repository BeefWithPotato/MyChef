package com.example.mychef;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class DetailedRecipeActivity extends AppCompatActivity {



    private Recipe recipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_recipe);
    }
}