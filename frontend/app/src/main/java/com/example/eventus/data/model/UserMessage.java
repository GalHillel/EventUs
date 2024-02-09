package com.example.eventus.data.model;

import java.util.List;
import java.util.Date;
public class UserMessage {
    String _id;
    String sender_id;
    List<String> receiver_ids;
    String title;
    String content;
    Date date_sent;
}
