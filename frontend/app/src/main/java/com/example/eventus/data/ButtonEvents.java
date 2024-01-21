package com.example.eventus.data;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ButtonEvents {

    private int sendHttpRequest(String dir,HashMap<String, Object> payload,String method) throws Exception{
        String url = "http://localhost:3000/"+dir;
        // Prepare the JSON data
        StringBuilder payloadStr = new StringBuilder("{");
        for (Map.Entry<String, Object> e : payload.entrySet()){
            payloadStr.append('"').append(e.getKey()).append('"').append(':').append(e.getValue().toString()).append(",");
        }
        payloadStr.setCharAt(payloadStr.length()-1,'}');

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
            byte[] input = payloadStr.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // Get the response code
        int responseCode = connection.getResponseCode();

        // Close the connection
        connection.disconnect();

        return responseCode;
    }
    public void registerButton(String email, String name, String password, String userType) throws Exception{
        HashMap<String, Object> jsonData = new HashMap<String, Object>();
        jsonData.put("name",name);
        jsonData.put("email",email);
        jsonData.put("password",password);
        jsonData.put("userType",userType);

        sendHttpRequest("users",jsonData,"POST");


    }
}
