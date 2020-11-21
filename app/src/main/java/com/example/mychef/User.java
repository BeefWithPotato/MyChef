package com.example.mychef;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private String username;
    private String password;
    private Integer phone_number;
    private String id;
    private String email;
    private String bio;
    private String profileBg;
    private String userIcon;
    private Integer likes = 0;
    private Integer following = 0;
    private String gender;
    private String age;
    private ArrayList<String> follow = new ArrayList<String>();

    public ArrayList<String> getFollow() {
        return follow;
    }

    public void setFollow(ArrayList<String> follow) {
        this.follow = follow;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getFollowing() {
        return following;
    }

    public void setFollowing(Integer following) {
        this.following = following;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getProfileBg() {
        return profileBg;
    }

    public void setProfileBg(String profileBg) {
        this.profileBg = profileBg;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(Integer phone_number) {
        this.phone_number = phone_number;
    }
}
