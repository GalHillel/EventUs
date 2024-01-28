package com.example.eventus.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.eventus.R;

public class UserEventsFragment extends Fragment {

    public UserEventsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_events, container, false);

    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up click listeners for buttons
        view.findViewById(R.id.discover).setOnClickListener(v -> {
            // Navigate to Discover screen
            Navigation.findNavController(v).navigate(R.id.action_userEventsFragment_to_userDiscoverFragment);
        });

        view.findViewById(R.id.profile).setOnClickListener(v -> {
            // Navigate to Profile screen
            Navigation.findNavController(v).navigate(R.id.action_userEventsFragment_to_profileFragment);
        });
    }
}
