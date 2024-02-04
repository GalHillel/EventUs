package com.example.eventus.ui.organizer;

import android.os.Bundle;
import android.util.Log;
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
import com.example.eventus.data.Database;
import com.example.eventus.data.ServerSideException;
import com.example.eventus.ui.events.EventAdapter;
import com.example.eventus.ui.events.UserEventDisplay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrganizerEventsFragment extends Fragment {

    private RecyclerView upcomingEventsRecyclerView;
    private List<UserEventDisplay> upcomingEventsList = new ArrayList<>();
    private String userId;
    private String userName;

    public OrganizerEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_organizer_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            userId = getArguments().getString("userId", "");
            userName = getArguments().getString("userName", "");
        }
        Log.d("CreateEventFragment", "User ID: " + userId);

        // Set up click listeners for buttons
        view.findViewById(R.id.messages).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_organizerEvents_to_organizerMessages, createNavigationBundle()));

        view.findViewById(R.id.profile).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_organizerEvents_to_organizerProfileFragment, createNavigationBundle()));

        view.findViewById(R.id.newEvent).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_organizerEvents_to_createEventFragment, createNavigationBundle()));

        upcomingEventsRecyclerView = view.findViewById(R.id.eventsList);

        // Fetch organizer events using the database function
        try {
            // Use the getEventList function to retrieve user events
            UserEventDisplay[] organizerEvents = Database.getEventList(userId);

            // Clear the existing list and add the fetched events
            upcomingEventsList.clear();
            upcomingEventsList.addAll(Arrays.asList(organizerEvents));
        } catch (ServerSideException e) {
            // Handle the exception (e.g., show an error message)
            e.printStackTrace();
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
        }

        // Set up RecyclerView for Upcoming Events
        EventAdapter upcomingEventsAdapter = new EventAdapter(upcomingEventsList);
        upcomingEventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        upcomingEventsRecyclerView.setAdapter(upcomingEventsAdapter);
    }

    // Method to create a common bundle for navigation
    private Bundle createNavigationBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        bundle.putString("userName", userName);
        return bundle;
    }
}
