package com.example.loginapplication.JavaObjects;

public class UserProfile {
    public String username;
    public String date_of_birth;
    public String avatar_file_path="";
    public String signin_role="";
    public String TripsCreatedList="";
    public String TripsJoinedList= "";
    public String about_me="";

    public UserProfile(String username, String date_of_birth) {
        this.username = username;
        this.date_of_birth = date_of_birth;
    }


}
