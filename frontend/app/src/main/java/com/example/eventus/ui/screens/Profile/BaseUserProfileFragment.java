package com.example.eventus.ui.screens.Profile;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.eventus.R;
import com.example.eventus.data.Database;
import com.example.eventus.data.ServerSideException;
import com.example.eventus.data.model.UserProfile;

public class BaseUserProfileFragment extends Fragment {
    private final UserProfile userProfile;


    public BaseUserProfileFragment(UserProfile p){
        this.userProfile = p;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base_user_profile, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ImageView profilePic = view.findViewById(R.id.userPhotoImageView);
        try {
            if (userProfile != null && userProfile.getProfile_pic().length() > 0) {
                Bitmap profile_icon = Database.getProfilePic(userProfile.get_id());
                profilePic.setImageBitmap(profile_icon);
            }
        } catch (ServerSideException e) {
            // Handle the exception (e.g., show an error message)
            e.printStackTrace();
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
        }



        RatingBar userRatingBar = view.findViewById(R.id.userRatingBar);
        userRatingBar.setEnabled(false);
        TextView ratingCountTextView = view.findViewById(R.id.ratingCountTextView);
        TextView usernameTextView = view.findViewById(R.id.usernameTextView);
        TextView bioTextView = view.findViewById(R.id.bioTextView);

        if (userProfile.getUser_type().equals("Organizer")) {
            userRatingBar.setVisibility(View.VISIBLE);
            ratingCountTextView.setVisibility(View.VISIBLE);

        } else {
            userRatingBar.setVisibility(View.GONE);
            ratingCountTextView.setVisibility(View.GONE);
        }

        // Set actual user name and bio

        bioTextView.setInputType(EditorInfo.TYPE_NULL);
        bioTextView.setBackground(null);
        bioTextView.setText(userProfile.getBio());
        usernameTextView.setText(userProfile.getName());


    }

}
