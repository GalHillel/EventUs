package com.example.eventus.data.model;

import java.io.Serializable;
import java.util.ArrayList;

public class UserDisplay implements Serializable {
    String _id, name;
    
    public UserDisplay(String name, String email, String password, String user_type) {
        this._id = "";
        this.name = name;
    }
}
