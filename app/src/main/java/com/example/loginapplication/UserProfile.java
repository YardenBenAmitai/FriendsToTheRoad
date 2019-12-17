package com.example.loginapplication;

import java.util.List;

public class UserProfile {
    public String username;
    public String date_of_birth;
    public String country;

    public List<String> hobbies;
    public List<String> preferences;
    public String about_me="";

    public UserProfile(String username, String date_of_birth, String country) {
        this.username = username;
        this.date_of_birth = date_of_birth;
        this.country = country;
    }

    public void updateHobbies(List<String> h){
        hobbies.addAll(h);
    }

    public void updatePreferences(List<String> p){
        preferences.addAll(p);
    }

    public void updateAboutme(String s){
        about_me=s;
    }
}
