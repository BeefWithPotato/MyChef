package com.example.mychef;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CreateRecipeActivity extends AppCompatActivity {

    private Button btnAddRecipe;
    private LinearLayout lltAddIngredients;
    private int ingredientNumber = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        btnAddRecipe = (Button) findViewById(R.id.add_ingredients);
        lltAddIngredients = (LinearLayout) findViewById(R.id.add_ingredientsLayout);



        btnAddRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int leftPadding = 0;
                //create a linear layout
                LinearLayout ingredientLine = new LinearLayout(v.getContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        100);
                lp.setMargins(195, 40, 0, 0);
                ingredientLine.setLayoutParams(lp);



                //create textView
                TextView ingredientName = new TextView(v.getContext());
                ingredientName.setLayoutParams(new LinearLayout.LayoutParams(160,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                String name = String.valueOf(ingredientNumber) + ".Name:";
                ingredientName.setText(name);
                ingredientName.setTextSize(15);
                ingredientName.setTextColor(Color.BLACK);
                //create EditText
                EditText nameText = new EditText(v.getContext());
                nameText.setLayoutParams(new LinearLayout.LayoutParams(150,
                        55));
                nameText.setBackground(ResourcesCompat.getDrawable(v.getResources(), R.drawable.edit_profile_edit_text, null));
                nameText.setTextSize(15);
                nameText.setPadding(10,0,0,0);
                //create dosage textView
                TextView dosage = new TextView(v.getContext());
                dosage.setLayoutParams(new LinearLayout.LayoutParams(190,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                dosage.setText("Dosage:");
                dosage.setTextSize(15);
                dosage.setTextColor(Color.BLACK);
                dosage.setPadding(40,0,0,0);
                //create dosage EditText
                EditText dosageText = new EditText(v.getContext());
                dosageText.setLayoutParams(new LinearLayout.LayoutParams(150,
                        55));
                dosageText.setBackground(ResourcesCompat.getDrawable(v.getResources(), R.drawable.edit_profile_edit_text, null));
                dosageText.setTextSize(15);
                dosageText.setPadding(0,0,0,0);



                ingredientLine.addView(ingredientName);
                ingredientLine.addView(nameText);
                ingredientLine.addView(dosage);
                ingredientLine.addView(dosageText);



                lltAddIngredients.addView(ingredientLine);
                ingredientNumber += 1;



            }
        });
    }


    }
