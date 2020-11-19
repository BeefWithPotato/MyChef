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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;


public class CreateRecipeActivity2 extends AppCompatActivity {


    private Button btnAddStep;
    private LinearLayout lltAddSteps;
    private int stepNum = 2;
    private Button btnAddKitchenware;
    private LinearLayout lltAddKitchenware;
    private Recipe recipe;
    private ImageButton firstStepImage;
    private ArrayList<ImageButton> stepImagesBtnArray;
    private ArrayList<EditText> etDescriptionArray;
    private ArrayList<EditText> etKitchenwareArray;
    private Button publish;
    int imagePointer;

    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe2);

        //initiate the edit text arrays
        etDescriptionArray = new ArrayList<EditText>(30);
        etKitchenwareArray = new ArrayList<EditText>(30);
        EditText etDescription = (EditText) findViewById(R.id.description1);
        etDescriptionArray.add(etDescription);
        EditText etKitchenware = (EditText) findViewById(R.id.kitchenware1);
        etKitchenwareArray.add(etKitchenware);


        //initiate recipe class
        recipe = new Recipe();
        recipe.initIngredients();
        recipe.initStepDescriptions();
        recipe.initStepImages();
        recipe.initKitchenWares();
        //take data from bundle
        Bundle bundle = getIntent().getExtras();
        recipe.setCoverImage(bundle.getString("coverImage"));
        recipe.setRecipeName(bundle.getString("recipeName"));
        recipe.setStory(bundle.getString("story"));
        recipe.setIngredients(bundle.getStringArrayList("ingredients"));

        //handle the publish button, first modify the user class's description and kitchenware.
        publish = (Button) findViewById(R.id.publish);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //modify recipe description
                for(int i = 0; i <etDescriptionArray.size();i++) {
                    EditText ediText = etDescriptionArray.get(i);
                    recipe.addStepDescription(ediText.getText().toString());
                }
                //modify recipe kitchenware
                for(int i = 0; i <etKitchenwareArray.size();i++) {
                    EditText ediText = etKitchenwareArray.get(i);
                    recipe.addKitchenWare(ediText.getText().toString());
                }
                //modify tips
                EditText tipText = (EditText) findViewById(R.id.tipText);
                recipe.setTips(tipText.getText().toString());

                //upload cover image and get it's url back
                Uri coverFile = Uri.parse((String)recipe.getCoverImage());
                StorageReference coverRef = mStorageRef.child("images/" + "RecipeImages/" + currentUser.getUid() + "/coverImage.jpg");
                UploadTask uploadTask = coverRef.putFile(coverFile);
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return coverRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            //upload download url to Firebase
                            String downloadUri = task.getResult().toString();
                            Log.d("the url is", coverRef.getDownloadUrl().toString());
                            Log.d("testtestestestestes", "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
                            recipe.setCoverImage(downloadUri);
                        } else {
                            Toast.makeText(CreateRecipeActivity2.this, "Cover Image Upload failed.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                //upload all the step images and get it's url back

                for(int i = 0; i <recipe.countImages();i++) {
                    imagePointer = i;
                    String file = recipe.getStepImage(i);
                    Uri stepFile = Uri.parse((String)file);
                    String name = "coverImage" + String.valueOf(i) + ".jpg";
                    StorageReference stepRef = mStorageRef.child("images/" + "RecipeImages/" + "stepImages" + currentUser.getUid() + name);
                    uploadTask = stepRef.putFile(stepFile);
                    urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            Log.d("the url is", stepRef.getDownloadUrl().toString());
                            Log.d("testtestestestestes", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                            return stepRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                //upload download url to Firebase
                                String downloadUri = task.getResult().toString();
                                recipe.changeImage(imagePointer, downloadUri);
                            } else {
                                Toast.makeText(CreateRecipeActivity2.this, "Cover Image Upload failed.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });


                }

                //upload User class to the realtime database
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Recipe");
                ref.child(user.getUid()).child("recipe1").setValue(recipe);
                Toast.makeText(CreateRecipeActivity2.this, "Recipe upload succeed.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CreateRecipeActivity2.this, MainActivity.class);
                startActivity(intent);

            }
        });






        //handle the first step image button and init the image button array
        firstStepImage = (ImageButton) findViewById(R.id.step_image1);
        stepImagesBtnArray = new ArrayList<ImageButton>(30);
        stepImagesBtnArray.add(firstStepImage);
        firstStepImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0);
            }
        });



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
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(470, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0,10,0,0);
                etKitchenwareArray.add(kitchenwareText);
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
                RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(530, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp4.setMargins(430,660,0,0);
                etDescriptionArray.add(descriptionText);


                stepLine.addView(stepNumber, lp1);
                stepLine.addView(stepImage, lp2);
                stepLine.addView(stepDescription, lp3);
                stepLine.addView(descriptionText, lp4);


                lltAddSteps.addView(stepLine);
                stepNum += 1;

                //make listeners for the image button
                stepImagesBtnArray.add(stepImage);
                stepImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        int code = stepImagesBtnArray.indexOf(stepImage);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), code);
                    }
                });



            }
        });


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //update bg ImageView
        if (data != null) {
            if (requestCode == recipe.countImages()){
                recipe.addImage(data.getData().toString());
                ImageButton imageBtn = stepImagesBtnArray.get(requestCode);
                imageBtn.setImageURI(data.getData());

            }else{
                if(requestCode < recipe.countImages()){
                    recipe.changeImage(requestCode, data.getData().toString());
                    ImageButton imageBtn = stepImagesBtnArray.get(requestCode);
                    imageBtn.setImageURI(data.getData());
                }else{
                    Toast.makeText(CreateRecipeActivity2.this, "modify previous steps first please!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}

