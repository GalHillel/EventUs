package com.example.eventus.data.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserEvent extends UserEventDisplay{

    String creator_id, description;

    Map<String,Boolean> attendents;

    public UserEvent(String _id, String creator_id, String name, String location, String description, Date date) {
        super(_id,name,date,location);

        this.creator_id = creator_id;

        this.description = description;

        this.attendents = new HashMap<>();

    }

    public String getDescription(){
        return this.description;
    }
    public Map<String,Boolean> getAttendents(){
        return this.attendents;
    }
    public String getCreator_id(){return this.creator_id;}


    public void setDescription(String description) {
        this.description = description;
    }


}
