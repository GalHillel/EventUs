package com.example.eventus.data;
import com.google.gson.Gson;
public class ServerSideException extends Exception{
    private int returnCode;
    public ServerSideException(String message) {
        super(message);
        this.returnCode = -1;
    }
    public ServerSideException(int returnCode,String message) {
        super(message);
        this.returnCode = returnCode;
    }

    public int getReturnCode() {
        return returnCode;
    }
}
