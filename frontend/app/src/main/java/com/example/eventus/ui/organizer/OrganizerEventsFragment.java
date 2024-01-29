package com.example.eventus.ui.organizer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventus.R;
import com.example.eventus.ui.events.EventAdapter;
import com.example.eventus.ui.events.EventModel;

import java.util.ArrayList;
import java.util.List;

public class OrganizerEventsFragment extends Fragment {

    private RecyclerView upcomingEventsRecyclerView;
    private RecyclerView pastEventsRecyclerView;

    // Replace with your actual data model for events
    private List<EventModel> upcomingEventsList = new ArrayList<>();
    private List<EventModel> pastEventsList = new ArrayList<>();

    public OrganizerEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_organizer_event, container, false);

        // Set up click listeners for buttons
        view.findViewById(R.id.messages).setOnClickListener(v -> {
            // Navigate to OrganizerMessagesFragment
            Navigation.findNavController(v).navigate(R.id.action_organizerEvents_to_organizerMessages);
        });

        view.findViewById(R.id.profile).setOnClickListener(v -> {
            // Navigate to OrganizerProfileFragment
            Navigation.findNavController(v).navigate(R.id.action_organizerEvents_to_organizerProfileFragment);
        });

        upcomingEventsRecyclerView = view.findViewById(R.id.eventsList);
        pastEventsRecyclerView = view.findViewById(R.id.pastEventsList);

        // Replace with your logic to populate upcomingEventsList and pastEventsList
        populateDummyData();

        // Set up RecyclerView for Upcoming Events
        EventAdapter upcomingEventsAdapter = new EventAdapter(upcomingEventsList);
        upcomingEventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        upcomingEventsRecyclerView.setAdapter(upcomingEventsAdapter);

        // Set up RecyclerView for Past Events
        EventAdapter pastEventsAdapter = new EventAdapter(pastEventsList);
        pastEventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        pastEventsRecyclerView.setAdapter(pastEventsAdapter);

        return view;
    }

    // TODO:Replace this with actual data retrieval logic
    private void populateDummyData() {
        // Example: Adding dummy data
        upcomingEventsList.add(new EventModel("Event 1", "2024-02-01", "Location 1"));
        upcomingEventsList.add(new EventModel("Event 2", "2024-03-15", "Location 2"));

        pastEventsList.add(new EventModel("Past Event 1", "2023-12-10", "Location A"));
        pastEventsList.add(new EventModel("Past Event 2", "2023-11-05", "Location B"));
    }
}
