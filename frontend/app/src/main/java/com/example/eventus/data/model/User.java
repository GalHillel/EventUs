package com.example.eventus.data.model;

import java.util.ArrayList;

public class User extends UserDisplay{
    String email,password,bio;
    int rating;
    int num_ratings;
    ArrayList<String> messages, events;

    public User(String _id, String name, String user_type, String profile_pic, String email, String password) {
        super(_id,name,user_type,profile_pic);
        this.email = email;
        this.password = password;
        this.messages = new ArrayList<String>();
        this.events = new ArrayList<String>();
        this.num_ratings = 0;
        this.rating = 0;
        this.bio = "";
    }



}
