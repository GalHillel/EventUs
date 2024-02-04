package com.example.eventus.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.eventus.R;
import com.example.eventus.ui.events.UserEventDisplay;

public class UserEventDetailsShow extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_event_details_show, container, false);

        TextView eventNameDisplay = view.findViewById(R.id.Event_name_desplay);
        TextView eventDateDisplay = view.findViewById(R.id.Event_date_desplay);
        TextView eventLocationDisplay = view.findViewById(R.id.Event_location_desplay);

        Bundle args = getArguments();
        if (args != null) {
            UserEventDisplay selectedEvent = args.getParcelable("selectedEvent");

            if (selectedEvent != null) {
                eventNameDisplay.setText(selectedEvent.getEventName());
                eventDateDisplay.setText(selectedEvent.getEventDate().toString());  // Format the date as needed
                eventLocationDisplay.setText(selectedEvent.getEventLocation());
            }
        }

        return view;
    }
}
