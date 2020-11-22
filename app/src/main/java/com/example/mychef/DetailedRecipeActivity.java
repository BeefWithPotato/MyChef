package com.example.mychef;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailedRecipeActivity extends AppCompatActivity {



    private Recipe recipe;
    private User userInfo;
    private TextView userName;
    private ImageView cover;
    private CircleImageView userIcon;
    private TextView recipeName;
    private TextView userStory;
    private TextView tips;

    private LinearLayout ingredientLayout;
    private LinearLayout stepLayout;
    private LinearLayout kitchenWareLayout;


    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_recipe);
        //initiate recipe class
        recipe = new Recipe();
        recipe.initIngredients();
        recipe.initStepDescriptions();
        recipe.initStepImages();
        recipe.initKitchenWares();
        //take data from bundle
        Bundle bundle = getIntent().getExtras();
        recipe.setRecipeName(bundle.getString("recipeName"));
        recipe.setCoverImage(bundle.getString("coverImage"));
        recipe.setStory(bundle.getString("story"));
        recipe.setIngredients(bundle.getStringArrayList("ingredients"));
        recipe.setStepImages(bundle.getStringArrayList("stepImages"));
        recipe.setStepDescriptions(bundle.getStringArrayList("stepDescriptions"));
        recipe.setTips(bundle.getString("tips"));
        recipe.setKitchenWares(bundle.getStringArrayList("kitchenWares"));
        recipe.setAuthorUid(bundle.getString("authorUid"));
        recipe.setAuthorUsername(bundle.getString("authorUsername"));

        //set the cover image
        cover = findViewById(R.id.cover_image);
        Glide.with(this).load(recipe.getCoverImage()).into(cover);

        //set the user icon and user name
        userIcon = findViewById(R.id.profile_image);
        userName = findViewById(R.id.user_name);
        ref.child("User").child(recipe.getAuthorUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInfo = dataSnapshot.getValue(User.class);
                //get username
                userName.setText(userInfo.getUsername());

                //if user has set an icon, then get and set it
                if(userInfo.getUserIcon() != null){
                    Glide.with(DetailedRecipeActivity.this).load(userInfo.getUserIcon()).into(userIcon);
                }
                //if not use default
                else{
                    userIcon.setImageResource(R.drawable.usericon);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        //set recipe name and story
        recipeName = findViewById(R.id.recipe_name);
        recipeName.setText(recipe.getRecipeName());
        userStory = findViewById(R.id.user_story);
        userStory.setText(recipe.getStory());

        //set the ingredients
        ingredientLayout = findViewById(R.id.ingredients_layout);
        for (int j=0; j < recipe.getIngredients().size(); j = j + 2){
            //create a new layout to store ingredients
            RelativeLayout ingredientsLine = new RelativeLayout(this);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0,50,0,0);
            ingredientsLine.setLayoutParams(lp);
            //create name text views for the relative layout
            TextView name = new TextView(this);
            name.setLayoutParams(new LinearLayout.LayoutParams(180,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            name.setText(recipe.getIngredients().get(j));
            name.setTextSize(15);
            name.setTextColor(Color.BLACK);
            RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(160,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp1.setMargins(350,0,0,0);
            //create dosage text view for the relative layout
            TextView dosage = new TextView(this);
            dosage.setLayoutParams(new LinearLayout.LayoutParams(180,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            dosage.setText(recipe.getIngredients().get(j + 1));
            dosage.setTextSize(15);
            dosage.setTextColor(Color.BLACK);
            RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp2.setMargins(600,0,0,0);


            ingredientsLine.addView(name, lp1);
            ingredientsLine.addView(dosage, lp2);
            ingredientLayout.addView(ingredientsLine);
        }



        //set the stepImage and stepDescriptions
        stepLayout = findViewById(R.id.steps_layout);
        for (int j=0; j < recipe.getStepImages().size(); j++){
            //create a new layout to store ingredients
            RelativeLayout stepLine = new RelativeLayout(this);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0,50,0,0);
            stepLine.setLayoutParams(lp);
            //create step text views for the step images
            TextView stepName = new TextView(this);
            stepName.setLayoutParams(new LinearLayout.LayoutParams(180,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            String stepTitle = "Step" + String.valueOf(j+1);
            stepName.setText(stepTitle);
            stepName.setTextSize(16);
            stepName.setTextColor(Color.BLACK);
            RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(160,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp1.setMargins(0,0,0,0);
            lp1.addRule(RelativeLayout.CENTER_HORIZONTAL);
            //create step image for the stepLine
            ImageView stepImage = new ImageButton(this);
            stepImage.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(this).load(recipe.getStepImage(j)).into(stepImage);
            RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(800, 533);
            lp2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            lp2.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            lp2.setMargins(0, 60, 0, 0);
            //create step description for the stepLine
            TextView stepDes = new TextView(this);
            stepDes.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            stepDes.setText(recipe.getStepDescriptions().get(j));
            stepDes.setTextSize(15);
            stepDes.setTextColor(Color.BLACK);
            RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp3.setMargins(70,600,70,0);
            lp3.addRule(RelativeLayout.CENTER_HORIZONTAL);


            stepLine.addView(stepName, lp1);
            stepLine.addView(stepImage, lp2);
            stepLine.addView(stepDes, lp3);
            stepLayout.addView(stepLine);
        }
        //add user tips
        tips = findViewById(R.id.tips);
        tips.setText(recipe.getTips());

        //set the KitchenWares
        kitchenWareLayout = findViewById(R.id.kitchenWares);
        if (recipe.getKitchenWares().size() == 0){
            TextView kitchenWare = new TextView(this);
            kitchenWare.setText("The author did not provide any kitchenware");
            kitchenWare.setTextSize(15);
            kitchenWare.setTextColor(Color.BLACK);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(490,40,0,0);

            kitchenWareLayout.addView(kitchenWare, lp);

        }
        for (int j=0; j < recipe.getKitchenWares().size(); j++){

            //create step text views for the step images
            TextView kitchenWare = new TextView(this);
            kitchenWare.setText(recipe.getKitchenWares().get(j));
            kitchenWare.setTextSize(15);
            kitchenWare.setTextColor(Color.BLACK);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(490,40,0,0);

            kitchenWareLayout.addView(kitchenWare, lp);
        }


    }
}