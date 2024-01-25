package com.example.eventus.ui.eventsManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import com.example.eventus.R;
import com.example.eventus.data.Database;
import com.example.eventus.data.model.UserEvent;

import java.util.ArrayList;

public class Events extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage_events_section7, container, false);


        ///ArrayList<UserEvent> events = Database.getEventList(user);

        // Set click listener for login/register button
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the login fragment
                NavController navController = Navigation.findNavController(requireActivity(), R.id.sec7);
                navController.navigate(R.id.action_managerEventsList_to_managerMenu);
            }
        });

        return view;
    }
}
