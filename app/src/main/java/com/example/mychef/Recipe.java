package com.example.mychef;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;

public class Recipe {


    private String recipeName;
    private String coverImage;
    private String story;
    private ArrayList<String> ingredients;
    private ArrayList<String> stepImages;
    private ArrayList<String> stepDescriptions;
    private String tips;
    private ArrayList<String> kitchenWares;

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getStory() { return coverImage; }

    public void setStory(String story) { this.story = story; }

    public ArrayList<String> getIngredients() { return ingredients; }

    public void initIngredients() { this.ingredients = new ArrayList<String>(30); }

    public void setIngredients(ArrayList<String> ingredients) { this.ingredients = ingredients; }

    public void addIngredient(String ingredient) { this.ingredients.add(ingredient); }

    public String getStepImage(int index) { return this.stepImages.get(index); }

    public void initStepImages() { this.stepImages = new ArrayList<String>(30); }

    public void setStepImages(ArrayList<String> stepImages) { this.stepImages = stepImages; }

    public void changeImage(int index, String image){ this.stepImages.set(index, image); }

    public int countImages(){ return this.stepImages.size(); }

    public void addImage(String image){ this.stepImages.add(image); }

    public void addStepImage(String stepImage) { this.stepImages.add(stepImage); }

    public ArrayList<String> getStepDescriptions() { return stepDescriptions; }

    public void initStepDescriptions() { this.stepDescriptions = new ArrayList<String>(30); }

    public void setStepDescriptions(ArrayList<String> descriptions) { this.stepDescriptions = descriptions; }

    public void addStepDescription(String stepDescription) { this.stepDescriptions.add(stepDescription); }

    public String getTips() { return tips; }

    public void setTips(String tips) { this.tips = tips; }

    public ArrayList<String> getKitchenWares() { return kitchenWares; }

    public void initKitchenWares() { this.kitchenWares = new ArrayList<String>(30); }

    public void setKitchenWares(ArrayList<String> kitchenWares) { this.kitchenWares = kitchenWares; }

    public void addKitchenWare(String KitchenWare) { this.kitchenWares.add(KitchenWare); }

}
