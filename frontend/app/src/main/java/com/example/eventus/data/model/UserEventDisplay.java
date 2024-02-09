package com.example.eventus.data.model;

import java.io.Serializable;
import java.util.Date;

public class UserEventDisplay implements Serializable, Comparable<UserEventDisplay> {
    protected String _id, name, location;
    protected Date date;
    protected boolean isPrivate;

    public UserEventDisplay(String _id, String name, Date date, String location) {
        this._id = _id;
        this.name = name;
        this.date = date;
        this.location = location;
        this.isPrivate = false;
    }

    public String getEventName() {
        return this.name;
    }

    public Date getDate() {
        return this.date;
    }

    public String getLocation() {
        return this.location;
    }
    public String getId() {
        return this._id;
    }
    public String getName() {
        return this.name;
    }
    

    public void setDate(Date date) {
        this.date = date;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(UserEventDisplay other) {
        return this.date.compareTo(other.date);
    }
}
