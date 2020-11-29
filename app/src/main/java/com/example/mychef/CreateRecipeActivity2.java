package com.example.mychef;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;


public class CreateRecipeActivity2 extends AppCompatActivity {

    private Button btnAddStep;
    private LinearLayout lltAddSteps;
    private int stepNum = 2;
    private Button btnAddKitchenware;
    private EditText kitchenware;
    private Recipe recipe;
    private ImageButton firstStepImage;
    private ImageView btn_back;
    private ArrayList<ImageButton> stepImagesBtnArray;
    private ArrayList<EditText> etDescriptionArray;
    private ArrayList<RelativeLayout> StepLineArray;
    private Button publish;
    private Button preview;
    private CheckBox boxBBQ;
    private CheckBox boxBraising;
    private CheckBox boxBaking;
    private CheckBox boxStirFrying;
    private CheckBox boxSoup;
    private CheckBox boxSteaming;
    private Button btnDeleteStep;


    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe2);

        //go back
        btn_back = (ImageView) findViewById(R.id.back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateRecipeActivity2.this, CreateRecipeActivity.class);
                startActivity(intent);
            }
        });

        //initiate the edit text arrays
        etDescriptionArray = new ArrayList<EditText>(30);
        EditText etDescription = (EditText) findViewById(R.id.description1);
        etDescriptionArray.add(etDescription);



        //initiate recipe class
        recipe = new Recipe();
        recipe.initIngredients();
        recipe.initStepDescriptions();
        recipe.initStepImages();
        recipe.setLikes(0);
        //take data from bundle
        Bundle bundle = getIntent().getExtras();
        recipe.setAuthorUid(currentUser.getUid());
        recipe.setCoverImage(bundle.getString("coverImage"));
        recipe.setRecipeName(bundle.getString("recipeName"));
        recipe.setStory(bundle.getString("story"));
        recipe.setIngredients(bundle.getStringArrayList("ingredients"));
        recipe.setTime(bundle.getString("cookingTime"));
        recipe.setPeople(bundle.getString("diners"));

        //handle checkbox
        boxBBQ = (CheckBox)findViewById(R.id.BBQ);
        boxBBQ.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    boxBraising.setChecked(false);
                    boxBaking.setChecked(false);
                    boxStirFrying.setChecked(false);
                    boxSoup.setChecked(false);
                    boxSteaming.setChecked(false);
                    recipe.setCategory("BBQ");
                }
            }
        });
        boxBraising = (CheckBox)findViewById(R.id.Braising);
        boxBraising.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    boxBBQ.setChecked(false);
                    boxBaking.setChecked(false);
                    boxStirFrying.setChecked(false);
                    boxSoup.setChecked(false);
                    boxSteaming.setChecked(false);
                    recipe.setCategory("Braising");
                }
            }
        });
        boxBaking = (CheckBox)findViewById(R.id.Baking);
        boxBaking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    boxBraising.setChecked(false);
                    boxBBQ.setChecked(false);
                    boxStirFrying.setChecked(false);
                    boxSoup.setChecked(false);
                    boxSteaming.setChecked(false);
                    recipe.setCategory("Baking");
                }
            }
        });
        boxStirFrying = (CheckBox)findViewById(R.id.Stir_frying);
        boxStirFrying.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    boxBraising.setChecked(false);
                    boxBaking.setChecked(false);
                    boxBBQ.setChecked(false);
                    boxSoup.setChecked(false);
                    boxSteaming.setChecked(false);
                    recipe.setCategory("Stir-Frying");
                }
            }
        });
        boxSoup = (CheckBox)findViewById(R.id.Soup);
        boxSoup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    boxBraising.setChecked(false);
                    boxBaking.setChecked(false);
                    boxStirFrying.setChecked(false);
                    boxBBQ.setChecked(false);
                    boxSteaming.setChecked(false);
                    recipe.setCategory("Soup");
                }
            }
        });
        boxSteaming = (CheckBox)findViewById(R.id.Steaming);
        boxSteaming.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    boxBraising.setChecked(false);
                    boxBaking.setChecked(false);
                    boxStirFrying.setChecked(false);
                    boxSoup.setChecked(false);
                    boxBBQ.setChecked(false);
                    recipe.setCategory("Steaming");
                }
            }
        });


        //handle the preview button
        preview = (Button) findViewById(R.id.preview);
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //modify recipe description
                for(int i = 0; i <etDescriptionArray.size();i++) {
                    EditText ediText = etDescriptionArray.get(i);
                    recipe.addStepDescription(ediText.getText().toString());
                }
                //modify recipe kitchenware
                EditText kitchenwareText = (EditText) findViewById(R.id.kitchenware1);
                recipe.setKitchenWares(kitchenwareText.getText().toString());
                //modify tips
                EditText tipText = (EditText) findViewById(R.id.tipText);
                recipe.setTips(tipText.getText().toString());

                Intent intent = new Intent(CreateRecipeActivity2.this, PreviewActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString("recipeName", recipe.getRecipeName());
                bundle.putString("coverImage", recipe.getCoverImage());
                bundle.putString("story", recipe.getStory());
                bundle.putStringArrayList("ingredients", recipe.getIngredients());
                bundle.putStringArrayList("stepImages", recipe.getStepImages());
                bundle.putStringArrayList("stepDescriptions", recipe.getStepDescriptions());
                bundle.putString("tips", recipe.getTips());
                bundle.putString("kitchenWares", recipe.getKitchenWares());
                bundle.putString("authorUid", recipe.getAuthorUid());
                bundle.putString("authorUsername", recipe.getAuthorUsername());
                bundle.putInt("likes", recipe.getLikes());
                bundle.putString("cookingTime", recipe.getTime());
                bundle.putString("people", recipe.getPeople());
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });



        //handle the publish button, first modify the user class's description and kitchenware.
        publish = (Button) findViewById(R.id.publish);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //set up an alert dialog
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("UpLoading......");
                final AlertDialog alert1 = alert.create();
                alert1.setCancelable(false);
                alert1.show();

                //modify recipe description
                for(int i = 0; i <etDescriptionArray.size();i++) {
                    EditText ediText = etDescriptionArray.get(i);
                    recipe.addStepDescription(ediText.getText().toString());
                }
                //modify recipe kitchenware
                EditText kitchenwareText = (EditText) findViewById(R.id.kitchenware1);
                recipe.setKitchenWares(kitchenwareText.getText().toString());
                //modify tips
                EditText tipText = (EditText) findViewById(R.id.tipText);
                recipe.setTips(tipText.getText().toString());

                //upload cover image and get it's url back
                Uri coverFile = Uri.parse((String)recipe.getCoverImage());
                StorageReference coverRef = mStorageRef.child("images/" + "RecipeImages/" + currentUser.getUid() + "/" + recipe.getRecipeName() + "/coverImage.jpg");
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
                            recipe.setCoverImage(downloadUri);
                            if (recipe.countImages() == 0){
                                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                FirebaseUser user = mAuth.getCurrentUser();
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Recipe");
                                ref.child(user.getUid()).child(user.getUid() + recipe.getRecipeName()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        User userInfo = dataSnapshot.getValue(User.class);
                                        if (userInfo == null) {
                                            ref.child(user.getUid()).child(user.getUid() + recipe.getRecipeName()).setValue(recipe);
                                            Toast.makeText(CreateRecipeActivity2.this, "Recipe upload succeed.", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(CreateRecipeActivity2.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                        else {
                                            // Signed in successfully, show authenticated UI.
                                            Toast.makeText(CreateRecipeActivity2.this, "Duplicate Recipe title.", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(CreateRecipeActivity2.this, CreateRecipeActivity.class);
                                            startActivity(intent);
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }else{
                                uploadStepImage(recipe, 0);
                            }
                        } else {
                            Toast.makeText(CreateRecipeActivity2.this, "Cover Image Upload failed.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

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




        //add steps button control
        StepLineArray = new ArrayList<RelativeLayout>(30);
        btnAddStep = (Button) findViewById(R.id.add_new_step);
        lltAddSteps = (LinearLayout) findViewById(R.id.add_stepsLayout);
        btnAddStep.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
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
                String name = "Step " + String.valueOf(stepNum) + ":";
                stepNumber.setText(name);
                stepNumber.setTextSize(15);
                stepNumber.setTextColor(Color.BLACK);
                RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp1.setMargins(90,0,0,0);


                //create step image button
                ImageButton stepImage = new ImageButton(v.getContext());
                stepImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                stepImage.setBackground(ResourcesCompat.getDrawable(v.getResources(), R.drawable.add_image, null));

                RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(300, 300);
                lp2.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
                lp2.setMargins(20, 60, 0, 0);


                //create step description EditText
                EditText descriptionText = new EditText(v.getContext());
                descriptionText.setMinHeight(600);
                descriptionText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                descriptionText.setGravity(Gravity.START);
                descriptionText.setHint("Add step details ...");
                descriptionText.setPadding(20, 20, 20, 20);
                descriptionText.setBackground(ResourcesCompat.getDrawable(v.getResources(), R.drawable.edit_profile_edit_text, null));
                descriptionText.setTextSize(20);
                RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp4.setMargins(20,400,20,20);

                etDescriptionArray.add(descriptionText);

                stepLine.addView(stepNumber, lp1);
                stepLine.addView(stepImage, lp2);
                stepLine.addView(descriptionText, lp4);

                lltAddSteps.addView(stepLine);
                StepLineArray.add(stepLine);
                stepNum += 1;

                //set listeners for the image button
                stepImagesBtnArray.add(stepImage);
                stepImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        int code = stepImagesBtnArray.indexOf(stepImage);
                        if(code > recipe.countImages()){
                            Toast.makeText(CreateRecipeActivity2.this, "please modify the previous steps !", Toast.LENGTH_LONG).show();
                        }else{
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), code);
                        }

                    }
                });

            }
        });

        //delete steps button control
        btnDeleteStep = (Button) findViewById(R.id.delete_step);
        btnDeleteStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StepLineArray.size() >= 1){
                    lltAddSteps.removeView(StepLineArray.get(StepLineArray.size()-1));
                    StepLineArray.remove(StepLineArray.get(StepLineArray.size()-1));
                    stepNum -= 1;
                }

            }
        });


    }
    public void uploadStepImage(Recipe recipe, int index){
        if (recipe.countImages() == index){
            return;
        }
        String file = recipe.getStepImage(index);
        Uri stepFile = Uri.parse((String)file);
        String name = "stepImage" + String.valueOf(index) + ".jpg";
        StorageReference stepRef = mStorageRef.child("images/" + "RecipeImages/" + "stepImages/" + currentUser.getUid() + name);
        UploadTask uploadTask = stepRef.putFile(stepFile);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                return stepRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    //upload download url to Firebase
                    String downloadUri = task.getResult().toString();
                    recipe.changeImage(index, downloadUri);
                    uploadStepImage(recipe, index + 1);
                    if (index == recipe.countImages() - 1){
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        FirebaseUser user = mAuth.getCurrentUser();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Recipe");
                        ref.child(user.getUid()).child(user.getUid() + recipe.getRecipeName()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User userInfo = dataSnapshot.getValue(User.class);
                                if (userInfo == null) {
                                    ref.child(user.getUid()).child(user.getUid() + recipe.getRecipeName()).setValue(recipe);
                                    Toast.makeText(CreateRecipeActivity2.this, "Recipe upload succeed.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(CreateRecipeActivity2.this, MainActivity.class);
                                    startActivity(intent);
                                }
                                else {
                                    // Signed in successfully, show authenticated UI.
                                    Toast.makeText(CreateRecipeActivity2.this, "Duplicate Recipe title.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(CreateRecipeActivity2.this, CreateRecipeActivity.class);
                                    startActivity(intent);
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                } else {
                    Toast.makeText(CreateRecipeActivity2.this, "Cover Image Upload failed.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                }
            }
        }
    }

}

