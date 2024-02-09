package com.example.eventus.data.model;

import java.io.Serializable;
import java.util.Objects;

public class UserDisplay implements Serializable {
    protected String _id;
    protected String name, user_type;
    protected String profile_pic;

    public UserDisplay(String _id, String name, String user_type,String profile_pic) {
        this._id = _id;
        this.name = name;
        this.user_type = user_type;
        this.profile_pic = profile_pic;
    }

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }
    public String getUser_type(){return user_type;}
    public void setName(String name){
        this.name = name;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDisplay that = (UserDisplay) o;
        return Objects.equals(_id, that._id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id);
    }
}
