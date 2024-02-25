package com.example.eventus.data;

import android.os.AsyncTask;

import androidx.core.content.res.TypedArrayUtils;

import com.example.eventus.data.model.ServerResponse;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AsyncHttpRequest extends AsyncTask<Void, Void, ServerResponse> {
    private String dir;
    private HashMap<String, Object> payloadData, query;
    private String method;
    private ServerResponse serverResponse;
    private final Gson gson;

    public AsyncHttpRequest(String dir, HashMap<String, Object> payloadData, HashMap<String, Object> query, String method) {
        this.dir = dir;
        this.payloadData = (payloadData != null)? payloadData:new HashMap<>();
        this.method = method;
        this.query = query;
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


    public static String getQueryString(HashMap<String, Object> params) throws UnsupportedEncodingException {
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
        String query ="";

        if(this.query != null){
            query = getQueryString((this.query));
        }
        // Prepare the JSON data
        if(this.method.equals("GET") || this.method.equals("DELETE")){
            String payloadQuery = getQueryString(this.payloadData);
            if(query.length() > 0 && payloadQuery.length() > 0)
                query = query + "&" + payloadQuery;
            else if(query.length() == 0)
                query = payloadQuery;
        }
        else if (this.method.equals("POST") || this.method.equals("PATCH")) {
            payloadStr = gson.toJson(this.payloadData);
        }

        if(query.length() > 0)
            url = url + "?" + query;

        // Create a URL object
        URL urlObject = new URL(url);

        // Open a connection to the URL
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();

        if(this.method.equals("POST_PIC")){
            /*
            String boundary = "===" + System.currentTimeMillis() + "===";
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            // Get the output stream of the connection
            OutputStream outputStream = connection.getOutputStream();
            StringBuilder s1 = new StringBuilder();
            StringBuilder s2 = new StringBuilder();

            // Write multipart/form-data headers
            s1.append("--").append(boundary).append("\r\n");
            s1.append("Content-Disposition: form-data; name=\"icon\"; filename=\"newImg.png\"").append("\r\n");
            s1.append("Content-Type: image/png").append("\r\n");
            s1.append("\r\n");
            byte[] data1 = s1.toString().getBytes(StandardCharsets.UTF_8);

            // Write image data
            byte[] imageData = (byte[]) payloadData.get("icon");

            s2.append("\r\n");
            s2.append("--" + boundary + "--").append("\r\n");
            byte[] data2 = s2.toString().getBytes(StandardCharsets.UTF_8);
            byte[] fullData = new byte[data1.length+data2.length+imageData.length];
            System.arraycopy(data1, 0, fullData, 0, data1.length);
            System.arraycopy(imageData, 0, fullData, data1.length, imageData.length);
            System.arraycopy(data2, 0, fullData, data1.length+imageData.length, data2.length);
            outputStream.write(fullData,0,fullData.length);


            // End of multipart/form-data

             */
            


        }
        else{
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
        }




        // Get the response code
        int responseCode = connection.getResponseCode();
        String responseStr = "";
        if(responseCode != HttpURLConnection.HTTP_NO_CONTENT && responseCode < 400){
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
        else if(responseCode != HttpURLConnection.HTTP_NO_CONTENT){
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))){
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
