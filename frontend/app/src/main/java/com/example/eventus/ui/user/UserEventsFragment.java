package com.example.eventus.ui.user;

import android.os.Bundle;
import android.provider.ContactsContract;
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
import com.example.eventus.ui.events.UserEventDisplay;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserEventsFragment extends Fragment {

    private RecyclerView eventsRecyclerView;
    private List<UserEventDisplay> eventsList = new ArrayList<>();

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
        eventsList.add(new UserEventDisplay("4455552424252558", "Lior & Eden Wedding", new Date(2024,12,10), "Hertselia"));
        eventsList.add(new UserEventDisplay("484515151213", "Shar 25 Birthday", new Date(2024,3,15), "58 - Petach Tikva"));
    }
}