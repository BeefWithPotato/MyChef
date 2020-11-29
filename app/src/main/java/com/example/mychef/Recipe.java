package com.example.mychef;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;

public class Recipe {


    private String recipeName="";
    private String coverImage = "";
    public String story="";
    private ArrayList<String> ingredients;
    public ArrayList<String> stepImages;
    private ArrayList<String> stepDescriptions;
    private String tips="";
    private String kitchenWares="";
    private String authorUid="";
    private String authorUsername;
    private String category="";
    private int likes;
    private String time="";
    private String people="";

    public void setPeople(String number){ this.people = number; }

    public String getPeople(){return this.people;}

    public void setTime(String time){ this.time = time; }

    public String getTime(){ return this.time; }

    public void setCategory(String category){ this.category = category; }

    public String getCategory(){ return this.category; }

    public int getLikes() { return likes; }

    public void addLike() { this.likes += 1; }

    public void setLikes(int likes) { this.likes = likes; }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public String getAuthorUid() {
        return authorUid;
    }

    public void setAuthorUid(String authorUid) {
        this.authorUid = authorUid;
    }

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

    public String getStory() { return story; }

    public void setStory(String story) { this.story = story; }

    public ArrayList<String> getIngredients() { return ingredients; }

    public void initIngredients() { this.ingredients = new ArrayList<String>(30); }

    public void setIngredients(ArrayList<String> ingredients) { this.ingredients = ingredients; }

    public void addIngredient(String ingredient) { this.ingredients.add(ingredient); }

    public ArrayList<String> getStepImages() { return stepImages; }

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

    public String getKitchenWares() { return kitchenWares; }

    public void setKitchenWares(String kitchenWares) { this.kitchenWares = kitchenWares; }


}
