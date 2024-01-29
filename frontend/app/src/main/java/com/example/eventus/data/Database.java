package com.example.eventus.data;

import java.io.*;

import java.util.ArrayList;
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

    private ServerResponse sendHttpRequest(String dir, HashMap<String, Object> payloadData, String method) throws Exception {

        String url = "http://localhost:3000/" + dir;
        // Prepare the JSON data
        String payloadStr = gson.toJson(payloadData);

        // Create a URL object
        URL urlObject = new URL(url);

        // Open a connection to the URL
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();

        // Set the request method to POST
        connection.setRequestMethod(method);

        // Enable input/output streams
        connection.setDoOutput(true);

        // Set the content type to JSON
        connection.setRequestProperty("Content-Type", "application/json");

        // Get the output stream of the connection
        try (OutputStream os = connection.getOutputStream()) {
            // Write the JSON data to the output stream
            byte[] input = payloadStr.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // Get the response code
        int responseCode = connection.getResponseCode();
        String responseStr;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }

            // Print the response from the server
            responseStr = responseBuilder.toString();
        }

        // Close the connection
        connection.disconnect();

        return new ServerResponse(responseCode, responseStr);
    }

    /**
     * TODO error handling
     * Registers a new user
     *
     * @param email     user email
     * @param name      username
     * @param password  password
     * @param user_type creator/user
     * @throws Exception
     */
    public User addUser(String email, String name, String password, String user_type) {
        HashMap<String, Object> payloadData = new HashMap<String, Object>();
        payloadData.put("name", name);
        payloadData.put("email", email);
        payloadData.put("password", password);
        payloadData.put("userType", user_type);
        try {
            ServerResponse response = sendHttpRequest("users", payloadData, "POST");
            if (response.getReturnCode() == 201) {
                return gson.fromJson(response.getPayload(), User.class);
            }
        } catch (Exception e) {
            return null;
        }

        return null;

    }

    /**
     * TODO add response and error handling
     * creates a new event
     *
     * @param creator_id  id of event creator
     * @param name        name of the event
     * @param date        date of the event
     * @param location    location of the event
     * @param description description of the event
     * @throws Exception
     */
    public void addEvent(String creator_id, String name, String date, String location, String description) throws Exception {
        HashMap<String, Object> jsonData = new HashMap<String, Object>();
        jsonData.put("name", name);
        jsonData.put("creator_id", creator_id);
        jsonData.put("date", date);
        jsonData.put("location", location);
        jsonData.put("description", description);

        ServerResponse response = sendHttpRequest("events", jsonData, "POST");
    }

    /**
     * TODO implement
     * Get user events for some user
     *
     * @param user
     * @return list of events
     */
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

    /**
     * TODO implement
     * Checks if the user exists in the database and returns it if found
     *
     * @param email    user email
     * @param password user password
     * @return User entry in the database or null if not found
     */
    public static User userLogin(String email, String password) {
        return null;
    }
}
