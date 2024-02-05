package com.example.eventus.data.model;

import java.io.Serializable;
import java.util.Objects;

public class UserDisplay implements Serializable {
    private String _id;
    private String name, user_type;

    public UserDisplay(String _id, String name, String user_type) {
        this._id = _id;
        this.name = name;
        this.user_type = user_type;
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
