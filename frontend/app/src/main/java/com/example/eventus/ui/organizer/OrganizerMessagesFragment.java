package com.example.eventus.ui.organizer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.eventus.R;

public class OrganizerMessagesFragment extends Fragment {

    private String userId;
    private String userName;

    public OrganizerMessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_organizer_messages, container, false);

        // Retrieve user ID and name from arguments
        if (getArguments() != null) {
            userId = getArguments().getString("userId", "");
            userName = getArguments().getString("userName", "");
        }

        // Set up click listeners for buttons
        view.findViewById(R.id.profile).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_organizerMessages_to_organizerProfileFragment, createNavigationBundle()));

        view.findViewById(R.id.myevents).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_organizerMessages_to_organizerEvents, createNavigationBundle()));

        view.findViewById(R.id.newEvent).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_organizerMessages_to_createEventFragment, createNavigationBundle()));

        return view;
    }

    // Method to create a common bundle for navigation
    private Bundle createNavigationBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        bundle.putString("userName", userName);
        return bundle;
    }
}
