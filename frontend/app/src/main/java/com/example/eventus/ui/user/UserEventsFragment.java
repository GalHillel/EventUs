package com.example.eventus.ui.user;

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

public class UserEventsFragment extends Fragment {

    private RecyclerView eventsRecyclerView;
    private List<EventModel> eventsList = new ArrayList<>();

    public UserEventsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_events, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up click listeners for buttons
        view.findViewById(R.id.discover).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_userEventsFragment_to_userDiscoverFragment));

        view.findViewById(R.id.profile).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_userEventsFragment_to_profileFragment));

        view.findViewById(R.id.messages).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_userEventsFragment_to_userMessagesFragment));

        // data for events
        populateDummyData();

        // Set up RecyclerView for Events
        eventsRecyclerView = view.findViewById(R.id.eventsList);
        EventAdapter eventAdapter = new EventAdapter(eventsList);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        eventsRecyclerView.setAdapter(eventAdapter);
    }

    private void populateDummyData() {
        // Example: Adding EVENTS
        eventsList.add(new EventModel("Lior & Eden Wedding", "2024-02-01", "Lago - Rishon Lezion", "first time"));
        eventsList.add(new EventModel("Shar 25 Birthday", "2024-03-15", "58 - Petach Tikva", "somthing"));
    }
}