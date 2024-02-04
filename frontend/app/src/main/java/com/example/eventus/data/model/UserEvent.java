package com.example.eventus.data.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserEvent {

    String _id, creator_id, name, location, description;
    Date date;
    ArrayList<String> attendents;

    public UserEvent(String creator_id, String name, String location, String description, Date date) {
        this._id = "";
        this.creator_id = creator_id;
        this.name = name;
        this.location = location;
        this.description = description;
        this.date = date;
        this.attendents = new ArrayList<String>();

    }


    /**
     * FOR TESTING ONLY!
     * @param name
     */
    public UserEvent(String name){
        this.name = name;
    }


    public String getId() { return this._id; }
    public String getName() {
        return this.name;
    }

    public Date getDate() {
        return this.date;
    }

    public String getLocation() {
        return this.location;
    }
    public String getDescription(){
        return this.description;
    }
    public List<String> getAttendents(){
        return this.attendents;
    }
}
