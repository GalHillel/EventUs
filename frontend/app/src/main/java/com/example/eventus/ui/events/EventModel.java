package com.example.eventus.ui.events;

public class EventModel {
    private String eventName;
    private String eventDate;
    private String eventLocation;
    private String eventDescription; // New field for event description

    public EventModel(String eventName, String eventDate, String eventLocation, String eventDescription) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
        this.eventDescription = eventDescription;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public String getEventDescription() {
        return eventDescription;
    }
}
