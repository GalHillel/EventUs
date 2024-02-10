package com.example.eventus.data.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User extends UserProfile{
    String email,password;
    Map<String,Boolean> messages;

    public User(String _id, String name, String user_type, String profile_pic, String email, String password) {
        super(_id,name,user_type,profile_pic);

        this.email = email;
        this.password = password;
        this.messages = new HashMap<String,Boolean>();

    }

    public Map<String, Boolean> getMessages() {
        return messages;
    }




}
