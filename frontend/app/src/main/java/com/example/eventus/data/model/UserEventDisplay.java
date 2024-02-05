package com.example.eventus.data.model;

import java.io.Serializable;
import java.util.Date;

public class UserEventDisplay implements Serializable, Comparable<UserEventDisplay> {
    private String _id, name, location;
    private Date date;

    public UserEventDisplay(String _id, String name, Date date, String location) {
        this._id = _id;
        this.name = name;
        this.date = date;
        this.location = location;
    }

    public String getEventName() {
        return this.name;
    }

    public Date getEventDate() {
        return this.date;
    }

    public String getEventLocation() {
        return this.location;
    }

    public String getId() {
        return this._id;
    }

    @Override
    public int compareTo(UserEventDisplay other) {
        return this.date.compareTo(other.date);
    }
}
