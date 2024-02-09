package com.example.eventus.data.model;

import java.util.ArrayList;

public class UserProfile extends UserDisplay{
    String bio;
    int rating;
    int num_ratings;
    ArrayList<String> events;

    public UserProfile(String _id, String name, String user_type,String profile_pic) {
        super(_id, name, user_type,profile_pic);
        this.events = new ArrayList<String>();
        this.num_ratings = 0;
        this.rating = 0;
        this.bio = "";

    }


}
