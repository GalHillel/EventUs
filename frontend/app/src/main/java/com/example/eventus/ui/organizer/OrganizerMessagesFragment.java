package com.example.eventus.ui.organizer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.eventus.R;

public class OrganizerMessagesFragment extends Fragment {

    public OrganizerMessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_organizer_messages, container, false);

        // Set up click listeners for buttons
        view.findViewById(R.id.profile).setOnClickListener(v -> {
            // Navigate to OrganizerProfileFragment
            Navigation.findNavController(v).navigate(R.id.action_organizerMessages_to_organizerProfileFragment);
        });

        view.findViewById(R.id.myevents).setOnClickListener(v -> {
            // Navigate to OrganizerEventsFragment
            Navigation.findNavController(v).navigate(R.id.action_organizerMessages_to_organizerEvents);
        });

        view.findViewById(R.id.createEventFragment).setOnClickListener(v -> {
            // Navigate to OrganizerProfileFragment
            Navigation.findNavController(v).navigate(R.id.action_organizerMessages_to_createEventFragment);
        });

        return view;
    }
}
