package com.example.mychef;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CreateRecipeActivity2 extends AppCompatActivity {


    private Button btnAddStep;
    private LinearLayout lltAddSteps;
    private int stepNum = 2;
    private Button btnAddKitchenware;
    private LinearLayout lltAddKitchenware;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe2);

        //add kitchenware button control
        btnAddKitchenware = (Button) findViewById(R.id.add_new_kitchenware);
        lltAddKitchenware = (LinearLayout) findViewById(R.id.add_kitchenwareLayout);
        btnAddKitchenware.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create a EditText to store text
                EditText kitchenwareText = new EditText(v.getContext());
                kitchenwareText.setBackground(ResourcesCompat.getDrawable(v.getResources(), R.drawable.edit_profile_edit_text, null));
                kitchenwareText.setTextSize(15);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(470, 55);
                lp.setMargins(0,10,0,0);

                lltAddKitchenware.addView(kitchenwareText, lp);


            }
        });




        //add steps button control
        btnAddStep = (Button) findViewById(R.id.add_new_step);
        lltAddSteps = (LinearLayout) findViewById(R.id.add_stepsLayout);
        btnAddStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create a RelativeLayout to store addition step
                RelativeLayout stepLine = new RelativeLayout(v.getContext());
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                stepLine.setLayoutParams(lp);


                //create step number
                TextView stepNumber = new TextView(v.getContext());
                stepNumber.setLayoutParams(new LinearLayout.LayoutParams(180,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                String name = "Step " + String.valueOf(stepNum);
                stepNumber.setText(name);
                stepNumber.setTextSize(15);
                stepNumber.setTextColor(Color.BLACK);
                RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp1.setMargins(90,0,0,0);
                //create step image button
                ImageButton stepImage = new ImageButton(v.getContext());
                stepImage.setBackground(ResourcesCompat.getDrawable(v.getResources(), R.drawable.food_tmp, null));
                RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(800, 533);
                lp2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                lp2.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
                lp2.setMargins(20, 60, 0, 0);
                //create step description
                TextView stepDescription = new TextView(v.getContext());
                stepDescription.setLayoutParams(new LinearLayout.LayoutParams(180,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                stepDescription.setText("Step description:");
                stepDescription.setTextSize(15);
                stepDescription.setTextColor(Color.BLACK);
                RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp3.setMargins(90,660,0,0);
                //create step description EditText
                EditText descriptionText = new EditText(v.getContext());
                descriptionText.setBackground(ResourcesCompat.getDrawable(v.getResources(), R.drawable.edit_profile_edit_text, null));
                descriptionText.setTextSize(15);
                RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(530, 60);
                lp4.setMargins(430,660,0,0);










                stepLine.addView(stepNumber, lp1);
                stepLine.addView(stepImage, lp2);
                stepLine.addView(stepDescription, lp3);
                stepLine.addView(descriptionText, lp4);



                lltAddSteps.addView(stepLine);
                stepNum += 1;






            }
        });


    }
}