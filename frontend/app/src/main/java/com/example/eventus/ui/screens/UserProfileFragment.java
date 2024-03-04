package com.example.eventus.ui.screens;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.eventus.R;
import com.example.eventus.data.Database;
import com.example.eventus.data.ServerSideException;
import com.example.eventus.data.model.UserDisplay;
import com.example.eventus.data.model.UserProfile;
import com.example.eventus.ui.screens.Messages.CreateMessageActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

/* TODO: 1. Implement rating for users and events - users may rate event *not* organizers
         2. Display events when user enter to organizer profile
         3. Remove navigation bar when entering another user profile - DONE
         4. Maybe remove the option to send message from profile - should work anyway
         5. Move logout button here - DONE
         6. FIX BUG WHERE BIO DOES NOT GET UPDATED UNTIL REFRESH!!!!
 */

public class UserProfileFragment extends Fragment {
    private UserDisplay user;
    private UserProfile userProfile;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserDisplay uProfile = null;

        // Get the users' data from arguments
        if (getArguments() != null) {
            user = (UserDisplay) getArguments().getSerializable("user");
            uProfile = (UserDisplay) getArguments().getSerializable("other_user");

        }
        if (uProfile == null)
            uProfile = user;

        // Hide or show bottom navigation items based on user type
        if (user != null && user.getUser_type().equals("Organizer")) {
            hideNavigationItem(view, R.id.discover);
        } else {
            hideNavigationItem(view, R.id.newEvent);
        }

        //Grab user profile that is being viewed from the database
        try {
            if (uProfile != null)
                userProfile = Database.userProfile(uProfile.get_id());

        } catch (ServerSideException e) {
            // Handle the exception (e.g., show an error message)
            e.printStackTrace();
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
        }

        ImageView profilePic = view.findViewById(R.id.userPhotoImageView);
        try {
            if (uProfile != null && uProfile.getProfile_pic().length() > 0) {
                Bitmap profile_icon = Database.getProfilePic(uProfile.get_id());
                profilePic.setImageBitmap(profile_icon);
            }
        } catch (ServerSideException e) {
            // Handle the exception (e.g., show an error message)
            e.printStackTrace();
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
        }

        ImageButton backButton = view.findViewById(R.id.backButton);
        Button sendMessageButton = view.findViewById(R.id.sendMessageButton);
        MaterialButton logoutButton = view.findViewById(R.id.logout);

        //TODO: remove
        RatingBar userRatingBar = view.findViewById(R.id.userRatingBar);
        userRatingBar.setEnabled(false);
        TextView ratingCountTextView = view.findViewById(R.id.ratingCountTextView);

        logoutButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_userProfileFragment_to_loginFragment);
            // Prints success message
            Toast.makeText(requireContext(), "Logging out", Toast.LENGTH_SHORT).show();
        });


        // Set actual user name and bio
        TextView usernameTextView = view.findViewById(R.id.usernameTextView);
        TextView bioTextView = view.findViewById(R.id.bioTextView);
        bioTextView.setInputType(EditorInfo.TYPE_NULL);
        bioTextView.setBackground(null);


        if (userProfile != null) {
            bioTextView.setText(userProfile.getBio());
            usernameTextView.setText(userProfile.getName());

            String test1 = userProfile.getBio();
            String test2 = bioTextView.getText().toString();
        }
        //different user
        if (userProfile != null && !userProfile.get_id().equals(user.get_id())) {
            view.findViewById(R.id.navigation).setVisibility(View.GONE);
            logoutButton.setVisibility(View.GONE);
            view.findViewById(R.id.sendMessageButton).setOnClickListener(this::onSendMessageButtonClick);

            view.findViewById(R.id.editProfileButton).setVisibility(View.GONE);
            backButton.setVisibility(View.VISIBLE);
            backButton.setOnClickListener(this::onBackButtonClicked);


            if (userProfile.getUser_type().equals("Organizer")) {

                userRatingBar.setVisibility(View.VISIBLE);
                ratingCountTextView.setVisibility(View.VISIBLE);

            } else {

                userRatingBar.setVisibility(View.GONE);
                ratingCountTextView.setVisibility(View.GONE);
            }
        } else {//same user

            view.findViewById(R.id.sendMessageButton).setVisibility(View.GONE);
            backButton.setVisibility(View.GONE);
            view.findViewById(R.id.editProfileButton).setOnClickListener(this::onEditProfileButtonClicked);
            if (userProfile != null && userProfile.getUser_type().equals("Organizer")) {

                userRatingBar.setVisibility(View.VISIBLE);
                ratingCountTextView.setVisibility(View.VISIBLE);

            } else {

                userRatingBar.setVisibility(View.GONE);
                ratingCountTextView.setVisibility(View.GONE);
            }
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

    private void onSendMessageButtonClick(View view) {
        Bundle args = new Bundle();
        args.putSerializable("user", this.user);

        UserDisplay[] others = {this.userProfile};
        args.putSerializable("other_users", others);

        //TODO handle activity fail
        Intent i = new Intent(this.getContext(), CreateMessageActivity.class);
        i.putExtras(args);
        startActivity(i);
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
    public void onEditProfileButtonClicked(View view){

        Bundle args = createNavigationBundle();
        args.putSerializable("userProfile", userProfile);
        Navigation.findNavController(view).navigate(R.id.action_userProfileFragment_to_editProfileFragment, args);
    }

    public void onBackButtonClicked(View view) {
        // Navigate back
        getParentFragmentManager().popBackStack();
    }
}
