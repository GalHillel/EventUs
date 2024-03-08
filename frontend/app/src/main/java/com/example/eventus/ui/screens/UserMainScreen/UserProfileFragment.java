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
import com.example.eventus.ui.screens.EditProfile.EditProfileActivity;
import com.example.eventus.ui.screens.Profile.BaseUserProfileFragment;

public class UserProfileFragment extends Fragment {
    private final UserMainActivity holder;

    public UserProfileFragment() {
        this.holder = null;
    }

    public UserProfileFragment(UserMainActivity holder) {

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
        assert holder != null;
        if (holder.getUser() == null && getArguments() != null) {
            user = (LoggedInUser) getArguments().getSerializable("user");
        }

        view.findViewById(R.id.logout).setOnClickListener(this::onLogoutButtonClicked);
        view.findViewById(R.id.editProfileButton).setOnClickListener(this::onEditProfileButtonClicked);

        assert holder != null;
        BaseUserProfileFragment userProfileFragment = new BaseUserProfileFragment(holder.getUser());
        getChildFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.user_profile, userProfileFragment).commit();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == R.id.activity_edit_profile) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null && data.getExtras() != null) {
                    LoggedInUser newUser = (LoggedInUser) data.getExtras().getSerializable("user");
                    if (newUser != null) {
                        assert this.holder != null;
                        this.holder.setUser(newUser);
                    }

                    assert this.holder != null;
                    this.holder.update(R.id.profile);
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(requireContext(), "an error has occurred", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onEditProfileButtonClicked(View view) {
        Bundle args = new Bundle();
        assert holder != null;
        args.putSerializable("user", holder.getUser());
        Intent i = new Intent(this.getContext(), EditProfileActivity.class);
        i.putExtras(args);
        startActivityForResult(i, R.id.activity_edit_profile);
    }

    public void onLogoutButtonClicked(View view) {
        //Navigation.findNavController(view).navigate(R.id.action_userProfileFragment_to_loginFragment);
        assert holder != null;
        holder.success();
        // Prints success message
        Toast.makeText(requireContext(), "Logging out", Toast.LENGTH_SHORT).show();
    }

}
