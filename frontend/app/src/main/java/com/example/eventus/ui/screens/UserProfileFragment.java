package com.example.eventus.ui.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.eventus.R;
import com.example.eventus.data.Database;
import com.example.eventus.data.ServerSideException;
import com.example.eventus.data.model.User;
import com.example.eventus.data.model.UserDisplay;
import com.example.eventus.data.model.UserEventDisplay;
import com.example.eventus.data.model.UserProfile;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;
import java.util.Collections;

public class UserProfileFragment extends Fragment {
    private UserDisplay user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    //TODO when entering another users profile you can't go back to your own one,
    // possible solution: seperate the currnet user profile fragment into a myProfile fragment and a userProfile fragment
    //

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserDisplay uProfile = null;
        UserProfile userProfile = null;
        // Get the users' data from arguments
        if (getArguments() != null) {
            user = (UserDisplay) getArguments().getSerializable("user");
            uProfile = (UserDisplay) getArguments().getSerializable("other_user");

        }
        if(uProfile == null)
            uProfile = user;

        // Hide or show bottom navigation items based on user type
        if (user != null && user.getUser_type().equals("Organizer")) {
            hideNavigationItem(view, R.id.discover);
        } else {
            hideNavigationItem(view, R.id.newEvent);
        }

        try {
            if(uProfile != null)
                userProfile = Database.userProfile(uProfile.get_id());

        } catch (ServerSideException e) {
            // Handle the exception (e.g., show an error message)
            e.printStackTrace();
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
        }

        // Show or hide rating related views for organizers only
        RatingBar userRatingBar = view.findViewById(R.id.userRatingBar);
        TextView ratingCountTextView = view.findViewById(R.id.ratingCountTextView);
        Button saveRatingButton = view.findViewById(R.id.saveRatingButton);

        if (userProfile != null && userProfile.getUser_type().equals("Organizer") && !userProfile.get_id().equals(user.get_id())) {
            saveRatingButton.setVisibility(View.VISIBLE);
        } else {
            saveRatingButton.setVisibility(View.GONE);
        }


        // Set actual user name and bio
        TextView usernameTextView = view.findViewById(R.id.usernameTextView);
        TextView bioTextView = view.findViewById(R.id.bioTextView);
        if (userProfile != null) {
            usernameTextView.setText(userProfile.getName());
            bioTextView.setText(userProfile.getBio());
        }

        if (userProfile != null && !userProfile.get_id().equals(user.get_id())) {
            view.findViewById(R.id.sendMessageButton).setOnClickListener(v ->
                    Navigation.findNavController(v).navigate(R.id.action_userProfileFragment_to_createMessageFragment, createNavigationBundle())
            );

            view.findViewById(R.id.editProfileButton).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.sendMessageButton).setVisibility(View.GONE);

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
