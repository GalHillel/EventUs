package com.example.eventus.data.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class NewUserMessage {
    protected List<String> receiver_ids;
    protected String content;
    protected String _id;
    protected String[] sender_id;
    protected String title;
    protected Date date_sent;

    public List<String> getReceiver_ids() {
        return receiver_ids;
    }

    public String getContent() {
        return content;
    }
    public String get_id() {
        return _id;
    }
    public String getSender_id() {
        return sender_id[0];
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public Date getDate_sent() {
        return date_sent;
    }

    public String getDateString() {
        return this.date_sent.toString();
    }

}
