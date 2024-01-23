package com.example.eventus.data;

import java.io.*;

import java.util.HashMap;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.example.eventus.data.model.ServerResponse;
import com.example.eventus.data.model.User;
import com.google.gson.Gson;


public class ButtonEvents{
    static Gson gson;

    public ButtonEvents(){
        gson = new Gson();
    }


    private ServerResponse sendHttpRequest(String dir, HashMap<String, Object> payloadData, String method) throws Exception{

        String url = "http://localhost:3000/"+dir;
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

        return new ServerResponse(responseCode,responseStr);
    }

    /** TODO error handling
     * Registers a new user
     * @param email user email
     * @param name username
     * @param password password
     * @param userType creator/user
     * @throws Exception
     */
    public User registerButton(String email, String name, String password, String userType) throws Exception{
        HashMap<String, Object> payloadData = new HashMap<String, Object>();
        payloadData.put("name",name);
        payloadData.put("email",email);
        payloadData.put("password",password);
        payloadData.put("userType",userType);

        ServerResponse response = sendHttpRequest("users",payloadData,"POST");
        if (response.getReturnCode() == 201){
            return gson.fromJson(response.getPayload(), User.class);
        }
        return null;

    }

    /** TODO add response and error handling
     * creates a new event
     * @param creator_id id of event creator
     * @param name name of the event
     * @param date date of the event
     * @param location location of the event
     * @param description description of the event
     * @throws Exception
     */
    public void createEventsButton(String creator_id, String name, String date, String location, String description) throws Exception{
        HashMap<String, Object> jsonData = new HashMap<String, Object>();
        jsonData.put("name",name);
        jsonData.put("creator_id",creator_id);
        jsonData.put("date",date);
        jsonData.put("location",location);
        jsonData.put("description",description);

        ServerResponse response = sendHttpRequest("events",jsonData,"POST");
    }
}
