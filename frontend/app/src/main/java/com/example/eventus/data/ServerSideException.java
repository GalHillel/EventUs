package com.example.eventus.data;
import com.google.gson.Gson;
public class ServerSideException extends Exception{

    public ServerSideException(String message) {
        super(message);
    }
}
