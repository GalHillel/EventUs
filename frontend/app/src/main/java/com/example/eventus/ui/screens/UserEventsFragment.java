package com.example.eventus.ui.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventus.R;
import com.example.eventus.data.Database;
import com.example.eventus.data.ServerSideException;
import com.example.eventus.data.model.UserDisplay;
import com.example.eventus.ui.recycleViews.EventAdapter;
import com.example.eventus.data.model.UserEventDisplay;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UserEventsFragment extends Fragment  implements EventAdapter.OnShowMoreDetailsClickListener{

    private RecyclerView upcomingEventsRecyclerView;
    private List<UserEventDisplay> upcomingEventsList = new ArrayList<>();
    private UserDisplay user;

    public UserEventsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_events, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BottomNavigationView bottomNavigationView = view.findViewById(R.id.navigation);
        Menu navMenu = bottomNavigationView.getMenu();

        if (getArguments() != null) {
            user = (UserDisplay) getArguments().getSerializable("user");

            if(user != null && user.getUser_type().equals("Organizer")){
                navMenu.findItem(R.id.discover).setVisible(false);
            }
            else{
                navMenu.findItem(R.id.newEvent).setVisible(false);
            }
        }



        // Set up click listeners for buttons
        view.findViewById(R.id.discover).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_userEventsFragment_to_userDiscoverFragment, createNavigationBundle()));

        view.findViewById(R.id.profile).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_userEventsFragment_to_userProfileFragment, createNavigationBundle()));

        view.findViewById(R.id.messages).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_userEventsFragment_to_userMessagesFragment, createNavigationBundle()));

        view.findViewById(R.id.newEvent).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_userEventsFragment_to_createEventFragment, createNavigationBundle()));

        // Fetch user events using the database function
        try {
            UserEventDisplay[] userEvents = Database.getEventList(user.get_id());

            // Clear the existing list and add the fetched events
            upcomingEventsList.clear();
            upcomingEventsList.addAll(Arrays.asList(userEvents));

            Collections.sort(upcomingEventsList, (event1, event2) -> event1.getEventDate().compareTo(event2.getEventDate()));

        } catch (ServerSideException e) {
            // Handle the exception (e.g., show an error message)
            e.printStackTrace();
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
        }

        // Set up RecyclerView for Events
        upcomingEventsRecyclerView = view.findViewById(R.id.eventsList);
        EventAdapter eventAdapter = new EventAdapter(upcomingEventsList);
        eventAdapter.setOnShowMoreDetailsClickListener(this);
        upcomingEventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        upcomingEventsRecyclerView.setAdapter(eventAdapter);
    }

    // Method to create a common bundle for navigation
    private Bundle createNavigationBundle() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("user",user);

        return bundle;
    }

    @Override
    public void onShowMoreDetailsClick(int position) {
        UserEventDisplay clickedEvent = upcomingEventsList.get(position);
        Bundle args = createNavigationBundle();
        args.putString("eventId",clickedEvent.getId());
        NavHostFragment.findNavController(UserEventsFragment.this)
                .navigate(R.id.eventDetailsFragment,args);




    }
}
