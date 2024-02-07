package com.example.eventus.ui.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.eventus.R;
import com.example.eventus.data.model.UserDisplay;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserProfileFragment extends Fragment {
    private UserDisplay user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the user data from arguments
        if (getArguments() != null) {
            user = (UserDisplay) getArguments().getSerializable("user");
        }

        // Hide or show bottom navigation items based on user type
        if (user != null && user.getUser_type().equals("Organizer")) {
            hideNavigationItem(view, R.id.discover);
        } else {
            hideNavigationItem(view, R.id.newEvent);
        }


        // Set actual user name and bio
        TextView usernameTextView = view.findViewById(R.id.usernameTextView);
        TextView bioTextView = view.findViewById(R.id.bioTextView);
        if (user != null) {
            usernameTextView.setText(user.getName());
            //bioTextView.setText(user.getBio());
        }

        // Hide "Send Message" button if viewing own profile
        if (isViewingOwnProfile()) {
            view.findViewById(R.id.sendMessageButton).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.sendMessageButton).setOnClickListener(v -> {
            });
        }

        // Hide "Edit Profile" button if viewing other user's profile
        if (!isViewingOwnProfile()) {
            view.findViewById(R.id.editProfileButton).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.editProfileButton).setOnClickListener(v ->
                    Navigation.findNavController(v).navigate(R.id.action_userProfileFragment_to_editProfileFragment, createNavigationBundle()));
        }

        // Set up click listeners for buttons in user_navigation
        view.findViewById(R.id.discover).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_userProfileFragment_to_userDiscoverFragment, createNavigationBundle()));

        view.findViewById(R.id.myevents).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_userProfileFragment_to_userEventsFragment, createNavigationBundle()));

        view.findViewById(R.id.messages).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_userProfileFragment_to_userMessagesFragment, createNavigationBundle()));

        view.findViewById(R.id.newEvent).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_userProfileFragment_to_createEventFragment, createNavigationBundle()));
    }


    private boolean isViewingOwnProfile() {
        return true;
    }

    private void hideNavigationItem(View view, int itemId) {
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.navigation);
        Menu navMenu = bottomNavigationView.getMenu();
        navMenu.findItem(itemId).setVisible(false);
    }

    private Bundle createNavigationBundle() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        return bundle;
    }
}
