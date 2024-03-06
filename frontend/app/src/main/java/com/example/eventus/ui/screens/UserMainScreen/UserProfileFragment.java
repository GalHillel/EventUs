package com.example.eventus.ui.screens.UserMainScreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.eventus.R;
import com.example.eventus.data.model.LoggedInUser;
import com.example.eventus.ui.screens.EditProfile.EditProfileActivity;
import com.example.eventus.ui.screens.Messages.CreateMessageActivity;
import com.example.eventus.ui.screens.Profile.BaseUserProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/*
TODO: 1. Implement rating for users and events - users may rate event *not* organizers
         2. Display events when user enter to organizer profile
         3. Remove navigation bar when entering another user profile - DONE
         4. Maybe remove the option to send message from profile - should work anyway
         5. Move logout button here - DONE
         6. FIX BUG WHERE BIO DOES NOT GET UPDATED UNTIL REFRESH!!!!
 */

public class UserProfileFragment extends Fragment {
    private UserMainActivity holder;

    public UserProfileFragment(){
        this.holder = null;
    }
    public UserProfileFragment(UserMainActivity holder){

        this.holder = holder;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoggedInUser user;
        // Get the users' data from arguments
        if (holder.getUser() == null && getArguments() != null) {
            user = (LoggedInUser) getArguments().getSerializable("user");
        }

        /*
        // Hide or show bottom navigation items based on user type
        if (user != null && user.getUser_type().equals("Organizer")) {
            hideNavigationItem(view, R.id.discover);
        } else {
            hideNavigationItem(view, R.id.newEvent);
        }

         */

        view.findViewById(R.id.logout).setOnClickListener(this::onLogoutButtonClicked);
        view.findViewById(R.id.editProfileButton).setOnClickListener(this::onEditProfileButtonClicked);

        BaseUserProfileFragment userProfileFragment = new BaseUserProfileFragment(holder.getUser());
        getChildFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.user_profile, userProfileFragment)
                .commit();


        /*
        // Set up click listeners for buttons in user_navigation
        view.findViewById(R.id.discover).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_userProfileFragment_to_userDiscoverFragment, createNavigationBundle()));

        view.findViewById(R.id.myevents).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_userProfileFragment_to_userEventsFragment, createNavigationBundle()));

        view.findViewById(R.id.messages).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_userProfileFragment_to_userMessagesFragment, createNavigationBundle()));

        view.findViewById(R.id.newEvent).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_userProfileFragment_to_createEventFragment, createNavigationBundle()));
        */
    }


    private void hideNavigationItem(View view, int itemId) {
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.navigation);
        Menu navMenu = bottomNavigationView.getMenu();
        navMenu.findItem(itemId).setVisible(false);
    }

    private Bundle createNavigationBundle() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", holder.getUser());
        return bundle;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == R.id.activity_edit_profile){
            if(resultCode == Activity.RESULT_OK){
                if(data != null && data.getExtras() != null){
                    LoggedInUser newUser = (LoggedInUser) data.getExtras().getSerializable("user");
                    if(newUser != null){
                        this.holder.setUser(newUser);
                    }

                    this.holder.update(R.id.profile);
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(requireContext(), "an error has occurred",Toast.LENGTH_LONG).show();
            }
        }
    }
    //TODO fix
    public void onEditProfileButtonClicked(View view){
        Bundle args = new Bundle();
        args.putSerializable("user", holder.getUser());
        //TODO handle activity fail
        Intent i = new Intent(this.getContext(), EditProfileActivity.class);
        i.putExtras(args);
        startActivityForResult(i,R.id.activity_edit_profile);
    }

    //TODO fix
    public void onLogoutButtonClicked(View view){
        //Navigation.findNavController(view).navigate(R.id.action_userProfileFragment_to_loginFragment);
        holder.success();
        // Prints success message
        Toast.makeText(requireContext(), "Logging out", Toast.LENGTH_SHORT).show();
    }

}
