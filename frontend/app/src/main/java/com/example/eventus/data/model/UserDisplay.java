package com.example.eventus.data.model;

import java.io.Serializable;

public class UserDisplay implements Serializable {
    private String _id;
    private String name;

    public UserDisplay(String _id, String name) {
        this._id = _id;
        this.name = name;
    }

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }
}
