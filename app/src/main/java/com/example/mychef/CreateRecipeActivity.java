package com.example.mychef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class CreateRecipeActivity extends AppCompatActivity {

    private Button btnAddRecipe;
    private Button btnNext;
    private ImageView btn_back, cover;
    private ImageButton btnChangeCover;
    private LinearLayout lltAddIngredients;
    private int ingredientNumber = 3;
    private Recipe recipe;
    private EditText etRecipeName;
    private EditText etStory;
    private EditText etName1;
    private EditText etDosage1;
    private EditText etName2;
    private EditText etDosage2;
    private ArrayList<EditText> ingredientsArray;

    public static final int PICK_COVER = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        //initiate the recipe class
        recipe = new Recipe();
        recipe.initIngredients();
        recipe.initStepDescriptions();
        recipe.initStepImages();
        ingredientsArray = new ArrayList<EditText>(30);
        //set the ingredients
        etName1 = (EditText) findViewById(R.id.nameText);
        ingredientsArray.add(etName1);
        etDosage1 = (EditText) findViewById(R.id.dosage1);
        ingredientsArray.add(etDosage1);
        etName2 = (EditText) findViewById(R.id.nameText2);
        ingredientsArray.add(etName2);
        etDosage2 = (EditText) findViewById(R.id.dosage2);
        ingredientsArray.add(etDosage2);

        //Go back
        btn_back = (ImageView) findViewById(R.id.back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateRecipeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        //change cover image
        btnChangeCover = (ImageButton) findViewById(R.id.cover_image);
        btnChangeCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_COVER);
            }
        });



        //next on click
        btnNext = (Button) findViewById(R.id.next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //modify the user class according to the layout data
                //set recipe name
                etRecipeName = (EditText) findViewById(R.id.recipe_name_text);
                recipe.setRecipeName(etRecipeName.getText().toString());
                //set the story
                etStory = (EditText) findViewById(R.id.user_story);
                recipe.setStory(etStory.getText().toString());
                //modify the recipe ingredients
                for(int i = 0; i <ingredientsArray.size();i++) {
                    EditText ediText = ingredientsArray.get(i);
                    recipe.addIngredient(ediText.getText().toString());
                }

                Intent intent = new Intent(CreateRecipeActivity.this, CreateRecipeActivity2.class);
                Bundle bundle = new Bundle();
                bundle.putString("coverImage", recipe.getCoverImage());
                bundle.putStringArrayList("ingredients", recipe.getIngredients());
                bundle.putString("recipeName", recipe.getRecipeName());
                bundle.putString("story", recipe.getStory());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });




        //add new line on click
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
                lp.setMargins(55, 20, 0, 0);
                ingredientLine.setLayoutParams(lp);

                //create textView
                TextView ingredientName = new TextView(v.getContext());
                ingredientName.setLayoutParams(new LinearLayout.LayoutParams(180,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                String name = String.valueOf(ingredientNumber) + ".Name:";
                ingredientName.setText(name);
                ingredientName.setTextSize(15);
                ingredientName.setTextColor(Color.BLACK);
                //create EditText
                EditText nameText = new EditText(v.getContext());
                nameText.setLayoutParams(new LinearLayout.LayoutParams(280,
                        55));
                nameText.setBackground(ResourcesCompat.getDrawable(v.getResources(), R.drawable.edit_profile_edit_text, null));
                nameText.setTextSize(15);
                nameText.setPadding(10,0,0,0);
                ingredientsArray.add(nameText);
                //create dosage textView
                TextView dosage = new TextView(v.getContext());
                dosage.setLayoutParams(new LinearLayout.LayoutParams(205,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                dosage.setText("Amount:");
                dosage.setTextSize(15);
                dosage.setTextColor(Color.BLACK);
                dosage.setPadding(40,0,0,0);
                //create dosage EditText
                EditText dosageText = new EditText(v.getContext());
                dosageText.setLayoutParams(new LinearLayout.LayoutParams(320,
                        55));
                dosageText.setBackground(ResourcesCompat.getDrawable(v.getResources(), R.drawable.edit_profile_edit_text, null));
                dosageText.setTextSize(15);
                dosageText.setPadding(10,0,0,0);
                ingredientsArray.add(dosageText);



                ingredientLine.addView(ingredientName);
                ingredientLine.addView(nameText);
                ingredientLine.addView(dosage);
                ingredientLine.addView(dosageText);



                lltAddIngredients.addView(ingredientLine);
                ingredientNumber += 1;



            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if user choose an image complete
        if (requestCode == PICK_COVER) {
            //update bg ImageView
            cover = findViewById(R.id.cover_image2);
            cover.setImageURI(data.getData());
            recipe.setCoverImage(data.getData().toString());

        }
    }


}
