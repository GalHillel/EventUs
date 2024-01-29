package com.example.eventus.data;

import com.example.eventus.data.model.User;
import com.example.eventus.data.model.UserEvent;

import java.util.ArrayList;

public class DatabaseDemo {
    ArrayList<User> users;
    ArrayList<UserEvent> events;



    public User addUser(String email, String name, String password, String user_type) {
        return null;
    }

    public void addEvent(String creator_id, String name, String date, String location, String description) {

    }

    public static ArrayList<UserEvent> getEventList(User user) {
        ArrayList<UserEvent> lst = new ArrayList<UserEvent>();
        UserEvent uEvent1 = new UserEvent("event 1"),
                uEvent2 = new UserEvent("event 2"),
                uEvent3 = new UserEvent("event 3");
        lst.add(uEvent1);
        lst.add(uEvent2);
        lst.add(uEvent3);
        return lst;
    }
}
