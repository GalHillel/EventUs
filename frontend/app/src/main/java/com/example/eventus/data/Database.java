package com.example.eventus.data;

import java.io.*;

import java.util.HashMap;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.example.eventus.data.model.ServerResponse;
import com.example.eventus.data.model.User;
import com.example.eventus.data.model.UserEvent;
import com.google.gson.Gson;


public class Database {
    static Gson gson = new Gson();



    /**
     * TODO error handling
     * Registers a new user
     *
     * @param email     user email
     * @param name      username
     * @param password  password
     * @param user_type creator/user
     *
     */
    public static User addUser(String email, String name, String password, String user_type) throws Exception {
        HashMap<String, Object> payloadData = new HashMap<String, Object>();
        payloadData.put("name", name);
        payloadData.put("email", email);
        payloadData.put("password", password);
        payloadData.put("user_type", user_type);
        AsyncHttpRequest task = new AsyncHttpRequest("users", payloadData, "POST");
        task.execute();
        task.get();
        ServerResponse response = task.getServerResponse();
        if (response.getReturnCode() == HttpURLConnection.HTTP_CREATED) {
            return gson.fromJson(response.getPayload(), User.class);
        }
        else{
            throw new ServerSideException(response.getPayload());
        }


    }
    /**
     * TODO Test and add error handling
     * Checks if the user exists in the database and returns it if found
     *
     * @param email    user email
     * @param password user password
     * @param user_type type of user Organizer or Participant
     * @return User entry in the database
     * @throws Exception response error
     */
    public static User userLogin(String email, String password, String user_type) throws Exception{
        HashMap<String, Object> payloadData = new HashMap<String, Object>();
        payloadData.put("email", email);
        payloadData.put("password", password);
        payloadData.put("user_type", user_type);

        AsyncHttpRequest task = new AsyncHttpRequest("users/login", payloadData, "GET");
        task.execute();
        task.get();
        ServerResponse response = task.getServerResponse();
        if (response.getReturnCode() == HttpURLConnection.HTTP_OK) {
            return gson.fromJson(response.getPayload(), User.class);
        }
        else{
            throw new ServerSideException(response.getPayload());
        }

    }

    /** TODO Test and add error handling
     * creates a new event
     * @param creator_id  id of event creator
     * @param name        name of the event
     * @param date        date of the event
     * @param location    location of the event
     * @param description description of the event
     */
    public static UserEvent addEvent(String creator_id, String name, String date, String location, String description) throws Exception{
        HashMap<String, Object> payloadData = new HashMap<String, Object>();
        payloadData.put("name", name);
        payloadData.put("creator_id", creator_id);
        payloadData.put("date", date);
        payloadData.put("location", location);
        payloadData.put("description", description);

        AsyncHttpRequest task = new AsyncHttpRequest("events", payloadData, "POST");
        task.execute();
        task.get();
        ServerResponse response = task.getServerResponse();

        if (response.getReturnCode() == HttpURLConnection.HTTP_CREATED) {
            return gson.fromJson(response.getPayload(), UserEvent.class);
        }
        else{
            throw new ServerSideException(response.getPayload());
        }
    }

    /**
     * TODO Test and add error handling
     * Get user events for some user
     * @param user the user we want to get the events from
     * @return array of events or null if not found
     */
    public static UserEvent[] getEventList(User user) throws Exception{
        AsyncHttpRequest task = new AsyncHttpRequest("users/"+user.getId()+"/events",  new HashMap<String, Object>(), "GET");
        task.execute();
        task.get();
        ServerResponse response = task.getServerResponse();
        if (response.getReturnCode() == HttpURLConnection.HTTP_OK) {
            return gson.fromJson(response.getPayload(), UserEvent[].class);
        }
        else{
            throw new ServerSideException(response.getPayload());
        }
    }


    public static void joinEvent(User user, String event_id) throws Exception{
        HashMap<String, Object> payloadData = new HashMap<String, Object>();
        payloadData.put("_id", user.getId());
        AsyncHttpRequest task = new AsyncHttpRequest("events/"+event_id+"/joinEvent",  payloadData, "PATCH");
        task.execute();
        task.get();
        ServerResponse response = task.getServerResponse();
        if (response.getReturnCode() != HttpURLConnection.HTTP_NO_CONTENT) {
            throw new ServerSideException(response.getPayload());
        }

    }

}
