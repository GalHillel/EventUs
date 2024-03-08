package com.example.eventus.ui.screens.UserMainScreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.eventus.R;
import com.example.eventus.data.model.LoggedInUser;
import com.example.eventus.data.model.UserEventDisplay;
import com.example.eventus.ui.screens.UserEvents.EventListFragment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

// TODO: Add an option for users to rate past events

public class UserEventsFragment extends Fragment {
    UserMainActivity holder;
    private final List<UserEventDisplay> upcomingEventsList = new ArrayList<>();
    private final List<UserEventDisplay> pastEventsList = new ArrayList<>();

    EventListFragment pastEventsListFragment;
    EventListFragment upcomingEventsFragment;

    public UserEventsFragment() {
    }

    public UserEventsFragment(UserMainActivity holder) {
        this.holder = holder;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_events, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (this.holder.getUserEvents() == null) {
            this.holder.loadEvents();
        }
        // Fetch user events using the database function
        upcomingEventsList.clear();
        pastEventsList.clear();
        Date d = new Date();
        for (UserEventDisplay e : this.holder.getUserEvents()) {
            if (e.getDate().after(d)) {
                upcomingEventsList.add(e);
            } else {
                pastEventsList.add(e);
            }
        }
        upcomingEventsList.sort(Comparator.comparing(UserEventDisplay::getDate));
        pastEventsList.sort(Comparator.comparing(UserEventDisplay::getDate));

        upcomingEventsFragment = new EventListFragment(this.holder.getUser(), upcomingEventsList);
        getChildFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.upcomingEventsList, upcomingEventsFragment).commit();

        pastEventsListFragment = new EventListFragment(this.holder.getUser(), pastEventsList);
        getChildFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.pastEventsList, pastEventsListFragment).commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == R.id.showMoreDetails) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null && data.getExtras() != null) {
                    LoggedInUser newUser = (LoggedInUser) data.getExtras().getSerializable("user");
                    if (newUser != null) {
                        if (newUser.getEvents().size() != this.holder.getUser().getEvents().size() || !newUser.getEvents().containsAll(this.holder.getUser().getEvents())) {
                            this.holder.setUser(newUser);
                            this.holder.loadEvents();
                        }
                    }
                    this.holder.update(R.id.myevents);
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(requireContext(), "an error has occurred", Toast.LENGTH_LONG).show();
            }
        }
    }
}
