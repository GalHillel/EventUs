package com.example.eventus.data.model;

import java.util.ArrayList;

public class User {
    String _id, profilePic, name, email,password, userType;
    ArrayList<String> messages, events;

    public User(String name, String email, String password, String userType) {
        this._id = "";
        this.profilePic = "0";
        this.name = name;
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.messages = new ArrayList<String>();
        this.events = new ArrayList<String>();
    }

}
