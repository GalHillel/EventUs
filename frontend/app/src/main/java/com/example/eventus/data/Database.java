package com.example.eventus.data;

import java.util.HashMap;
import java.net.HttpURLConnection;

import com.example.eventus.data.model.ServerResponse;
import com.example.eventus.data.model.User;
import com.example.eventus.data.model.UserDisplay;
import com.example.eventus.data.model.UserEvent;
import com.example.eventus.data.model.UserEventDisplay;
import com.google.gson.Gson;


public class Database {
    static Gson gson = new Gson();

    //POST requests
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


    //GET requests
    /**
     *
     * Checks if the user exists in the database and returns it if found
     *
     * @param email    user email
     * @param password user password
     * @param user_type type of user Organizer or Participant
     * @return User entry in the database
     * @throws Exception response error
     */
    public static UserDisplay userLogin(String email, String password, String user_type) throws Exception{
        HashMap<String, Object> payloadData = new HashMap<String, Object>();
        payloadData.put("email", email);
        payloadData.put("password", password);
        payloadData.put("user_type", user_type);

        AsyncHttpRequest task = new AsyncHttpRequest("users/login", payloadData, "GET");
        task.execute();
        task.get();
        ServerResponse response = task.getServerResponse();
        if (response.getReturnCode() == HttpURLConnection.HTTP_OK) {
            return gson.fromJson(response.getPayload(), UserDisplay.class);
        }
        else{
            throw new ServerSideException(response.getPayload());
        }

    }

    /**
     * TODO Test and add error handling
     * Get user events for some user
     * @param user_id the user we want to get the events from
     * @return array of eventDisplay
     */
    public static UserEventDisplay[] getEventList(String user_id) throws Exception{
        AsyncHttpRequest task = new AsyncHttpRequest("users/"+user_id+"/events",  new HashMap<String, Object>(), "GET");
        task.execute();
        task.get();
        ServerResponse response = task.getServerResponse();
        if (response.getReturnCode() == HttpURLConnection.HTTP_OK) {
            return gson.fromJson(response.getPayload(), UserEventDisplay[].class);
        }
        else{
            throw new ServerSideException(response.getPayload());
        }
    }

    /**
     * TODO Test and error handling
     * get list of user displays from the database for some event
     * @param event_id _id of the event we want to get the users from
     * @return array of userDisplay
     * @throws Exception ServerSideException or other exception
     */
    public static UserDisplay[] getUserList(String event_id) throws Exception{
        AsyncHttpRequest task = new AsyncHttpRequest("events/"+event_id+"/users",  new HashMap<String, Object>(), "GET");
        task.execute();
        task.get();
        ServerResponse response = task.getServerResponse();
        if (response.getReturnCode() == HttpURLConnection.HTTP_OK) {
            return gson.fromJson(response.getPayload(), UserDisplay[].class);
        }
        else{
            throw new ServerSideException(response.getPayload());
        }
    }


    /**
     * TODO Test and error handling
     * loads a certain event
     * @param event_id id of the event
     * @return UserEvent object
     * @throws Exception ServerSideException or other exception
     */
    public static UserEvent loadEvent(String event_id) throws Exception{

        AsyncHttpRequest task = new AsyncHttpRequest("events/event/"+event_id,  new HashMap<String, Object>(), "GET");
        task.execute();
        task.get();
        ServerResponse response = task.getServerResponse();
        if (response.getReturnCode() == HttpURLConnection.HTTP_OK) {
            return gson.fromJson(response.getPayload(), UserEvent.class);
        }
        else{
            throw new ServerSideException(response.getPayload());
        }
    }

    /**
     * TODO error handling and testing
     * @param search paramaters to search for an event, a key is a field in UserEvent
     * @return all valid EventDisplays given the search terms
     * @throws Exception ServerSideException or other exception
     */
    public static UserEventDisplay[] searchEvents(HashMap<String,Object> search) throws Exception{

        AsyncHttpRequest task = new AsyncHttpRequest("events/search",  search, "GET");
        task.execute();
        task.get();
        ServerResponse response = task.getServerResponse();
        if (response.getReturnCode() == HttpURLConnection.HTTP_OK) {
            return gson.fromJson(response.getPayload(), UserEventDisplay[].class);
        }
        else{
            throw new ServerSideException(response.getPayload());
        }
    }

    /**
     * TODO error handling and testing
     * joins a user to an event, only use for participants
     * @param user_id _id of user
     * @param event_id _id of event
     * @throws Exception ServerSideException or other exception
     */
    public static void joinEvent(String user_id, String event_id) throws Exception{
        HashMap<String, Object> payloadData = new HashMap<String, Object>();
        payloadData.put("_id", user_id);
        AsyncHttpRequest task = new AsyncHttpRequest("events/"+event_id+"/joinEvent",  payloadData, "PATCH");
        task.execute();
        task.get();
        ServerResponse response = task.getServerResponse();
        if (response.getReturnCode() != HttpURLConnection.HTTP_NO_CONTENT) {
            throw new ServerSideException(response.getPayload());
        }

    }

    /**
     * TODO error handling and testing
     * removes a user to an event, only use for participants
     * @param user_id _id of user
     * @param event_id _id of event
     * @throws Exception ServerSideException or other exception
     */
    public static void exitEvent(String user_id, String event_id) throws Exception{
        HashMap<String, Object> payloadData = new HashMap<String, Object>();
        payloadData.put("_id", event_id);
        AsyncHttpRequest task = new AsyncHttpRequest("users/"+user_id+"/exitEvent",  payloadData, "PATCH");
        task.execute();
        task.get();
        ServerResponse response = task.getServerResponse();
        if (response.getReturnCode() != HttpURLConnection.HTTP_NO_CONTENT) {
            throw new ServerSideException(response.getPayload());
        }

    }

    /**
     * TODO error handling and testing
     * removes a user to an event, only use for participants
     * @param event_id _id of event
     * @param newEventParams hashmap keys are the field UserEvent we want to update, values are the new field
     * @throws Exception ServerSideException or other exception
     */
    public static void editEvent(String event_id, HashMap<String,Object> newEventParams) throws Exception{

        AsyncHttpRequest task = new AsyncHttpRequest("events/"+event_id+"/edit",  newEventParams, "PATCH");
        task.execute();
        task.get();
        ServerResponse response = task.getServerResponse();
        if (response.getReturnCode() != HttpURLConnection.HTTP_NO_CONTENT) {
            throw new ServerSideException(response.getPayload());
        }
    }

}
