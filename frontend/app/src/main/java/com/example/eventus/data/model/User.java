package com.example.eventus.data.model;

import java.util.ArrayList;

public class User {
    String _id, profile_pic, name, email,password, user_type;
    ArrayList<String> messages, events;

    public User(String name, String email, String password, String user_type) {
        this._id = "";
        this.profile_pic = "";
        this.name = name;
        this.email = email;
        this.password = password;
        this.user_type = user_type;
        this.messages = new ArrayList<String>();
        this.events = new ArrayList<String>();
    }

    public String getId(){
        return this._id;
    }

}
