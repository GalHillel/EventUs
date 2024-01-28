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

public class UserDiscoverFragment extends Fragment {

    public UserDiscoverFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_discover, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up click listeners for buttons
        view.findViewById(R.id.profile).setOnClickListener(v -> {
            // Navigate to Profile screen
            Navigation.findNavController(v).navigate(R.id.action_userDiscoverFragment_to_profileFragment);
        });

        view.findViewById(R.id.myevents).setOnClickListener(v -> {
            // Navigate to Events screen
            Navigation.findNavController(v).navigate(R.id.action_userDiscoverFragment_to_userEventsFragment);
        });
    }
}
