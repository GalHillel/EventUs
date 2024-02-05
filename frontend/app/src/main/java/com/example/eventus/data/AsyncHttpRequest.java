package com.example.eventus.data;

import android.os.AsyncTask;

import com.example.eventus.data.model.ServerResponse;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class AsyncHttpRequest extends AsyncTask<Void, Void, ServerResponse> {
    private String dir;
    private HashMap<String, Object> payloadData;
    private String method;
    private ServerResponse serverResponse;
    private final Gson gson;

    public AsyncHttpRequest(String dir, HashMap<String, Object> payloadStr, String method) {
        this.dir = dir;
        this.payloadData = payloadStr;
        this.method = method;
        gson = new Gson();
    }

    @Override
    protected ServerResponse doInBackground(Void... voids) {

        try {
            this.serverResponse = sendHttpRequest();
            return this.serverResponse;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ServerResponse getServerResponse() {
        return this.serverResponse;
    }


    private String getQueryString(HashMap<String, Object> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8"));
        }
        return result.toString();
    }

    private ServerResponse sendHttpRequest() throws Exception {
        String url = "http://10.0.2.2:3000/" + this.dir;
        String payloadStr = "";

        // Prepare the JSON data
        if(this.method.equals("GET") || this.method.equals("DELETE")){
            url = url + "?" + getQueryString(this.payloadData);
        }
        else if (this.method.equals("POST") || this.method.equals("PATCH")) {
            payloadStr = gson.toJson(this.payloadData);
        }

        // Create a URL object
        URL urlObject = new URL(url);

        // Open a connection to the URL
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();

        // Set the request method
        connection.setRequestMethod(this.method);

        if (this.method.equals("POST") || this.method.equals("PATCH")) {
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
        }


        // Get the response code
        int responseCode = connection.getResponseCode();
        String responseStr = "";
        if(responseCode != HttpURLConnection.HTTP_NO_CONTENT){
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }

                // Print the response from the server
                responseStr = responseBuilder.toString();
            }
        }


        // Close the connection
        connection.disconnect();

        return new ServerResponse(responseCode, responseStr);
    }

}
