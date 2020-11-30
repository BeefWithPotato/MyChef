package com.example.mychef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class SearchPageActivity extends AppCompatActivity {

    private TextView cancel, no_result;
    private SearchView sv;
    private GridView mGv;
    ArrayList<Recipe> recipes;
    String target;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
//    CategoryPageActivity.Category search_by_category = new CategoryPageActivity.Category();
    String search_by_category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        //back
        cancel = findViewById(R.id.search_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mGv = (GridView) findViewById(R.id.search_gv);
        no_result = findViewById(R.id.no_result);
        sv = findViewById(R.id.search_bar);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                no_result.setVisibility(View.INVISIBLE);
                recipes = new ArrayList<Recipe>();
                mGv.setAdapter(new HomeGridViewAdapter(SearchPageActivity.this, recipes));
                target = sv.getQuery().toString();
                //search through all the recipes in the database
                ref.child("Recipe").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.getChildren() != null) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                for (DataSnapshot recipeSnapshot : ds.getChildren()) {
                                    Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                                    String title = recipe.getRecipeName();

                                    if (title.toLowerCase().contains(target.toLowerCase())) {
                                        recipes.add(recipe);
                                    }
                                }
                            }
                            if(recipes.size() == 0){
                                no_result.setVisibility(View.VISIBLE);
                            }
                            else {
                                mGv.setAdapter(new HomeGridViewAdapter(SearchPageActivity.this, recipes));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        Bundle bundle = getIntent().getExtras();
        recipes = new ArrayList<Recipe>();
        if(bundle != null){
            search_by_category = bundle.getString("category");
                ref.child("Recipe").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.getChildren() != null) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                for (DataSnapshot recipeSnapshot : ds.getChildren()) {

                                    Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                                    String category = recipe.getCategory();
                                    if (category.toLowerCase().contains(search_by_category.toLowerCase())) {
                                        recipes.add(recipe);
                                    }

                                }
                            }
                            if (recipes.size() != 0) {
                                mGv.setAdapter(new HomeGridViewAdapter(SearchPageActivity.this, recipes));
                            } else {
                                no_result.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        }



        mGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //put recipe class in bundle
                Recipe recipe = recipes.get(position);
                Intent intent = new Intent(SearchPageActivity.this, DetailedRecipeActivity.class);
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


    }
}