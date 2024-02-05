package com.example.eventus.ui.screens;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.eventus.R;
import com.example.eventus.data.Database;
import com.example.eventus.data.model.UserDisplay;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;

public class UserProfileFragment extends Fragment {

    private TextInputEditText usernameEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText oldPasswordEditText;
    private TextInputEditText newPasswordEditText;

    private MaterialButton saveNameButton;
    private MaterialButton saveContactButton;
    private MaterialButton savePasswordButton;

    private UserDisplay user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BottomNavigationView bottomNavigationView = view.findViewById(R.id.navigation);
        Menu navMenu = bottomNavigationView.getMenu();

        if (getArguments() != null) {
            user = (UserDisplay) getArguments().getSerializable("user");

            if(user != null && user.getUser_type().equals("Organizer")){
                navMenu.findItem(R.id.discover).setVisible(false);
            }
            else{
                navMenu.findItem(R.id.newEvent).setVisible(false);
            }
        }
        
        usernameEditText = view.findViewById(R.id.Username);
        emailEditText = view.findViewById(R.id.email);
        oldPasswordEditText = view.findViewById(R.id.oldPassword);
        newPasswordEditText = view.findViewById(R.id.newPassword);
        saveNameButton = view.findViewById(R.id.saveName);
        saveContactButton = view.findViewById(R.id.saveContact);
        savePasswordButton = view.findViewById(R.id.savePassword);

        // Set up click listeners for buttons in user_navigation
        view.findViewById(R.id.discover).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_userProfileFragment_to_userDiscoverFragment, createNavigationBundle()));

        view.findViewById(R.id.myevents).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_userProfileFragment_to_userEventsFragment, createNavigationBundle()));

        view.findViewById(R.id.messages).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_userProfileFragment_to_userMessagesFragment, createNavigationBundle()));

        view.findViewById(R.id.newEvent).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_userProfileFragment_to_createEventFragment, createNavigationBundle()));

        MaterialButton logoutButton = view.findViewById(R.id.logout);
        logoutButton.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_userProfileFragment_to_loginFragment));

        // Set up text change listeners
        setUpTextListeners();

        // Set up click listeners for buttons
        saveNameButton.setOnClickListener(v -> {
            String newUsername = usernameEditText.getText().toString();
            if (!TextUtils.isEmpty(newUsername)) {
                try {
                    HashMap<String, Object> updatedUserParams = new HashMap<>();
                    updatedUserParams.put("name", newUsername);
                    Database.editUser(user.get_id(), updatedUserParams);
                } catch (Exception e) {
                }
            }
        });

        saveContactButton.setOnClickListener(v -> {
            String newEmail = emailEditText.getText().toString();
            if (!TextUtils.isEmpty(newEmail)) {
                try {
                    HashMap<String, Object> updatedUserParams = new HashMap<>();
                    updatedUserParams.put("email", newEmail);
                    Database.editUser(user.get_id(), updatedUserParams);
                } catch (Exception e) {}
            }
        });

        savePasswordButton.setOnClickListener(v -> {
            
        });

    }

    private void setUpTextListeners() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateSaveButtonsState();
            }
        };

        usernameEditText.addTextChangedListener(textWatcher);
        emailEditText.addTextChangedListener(textWatcher);
        oldPasswordEditText.addTextChangedListener(textWatcher);
        newPasswordEditText.addTextChangedListener(textWatcher);

        // Initial state
        updateSaveButtonsState();
    }

    private void updateSaveButtonsState() {
        boolean isNameNotEmpty = !TextUtils.isEmpty(usernameEditText.getText());
        saveNameButton.setEnabled(isNameNotEmpty);

        boolean isContactNotEmpty = !TextUtils.isEmpty(emailEditText.getText());
        saveContactButton.setEnabled(isContactNotEmpty);

        boolean isPasswordNotEmpty = !TextUtils.isEmpty(oldPasswordEditText.getText())
                && !TextUtils.isEmpty(newPasswordEditText.getText());
        savePasswordButton.setEnabled(isPasswordNotEmpty);
    }

    // Method to create a common bundle for navigation
    private Bundle createNavigationBundle() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("user",user);
        return bundle;
    }
}
