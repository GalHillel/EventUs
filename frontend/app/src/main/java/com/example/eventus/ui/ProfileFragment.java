package com.example.eventus.ui;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.eventus.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ProfileFragment extends Fragment {

    private TextInputEditText usernameEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText oldPasswordEditText;
    private TextInputEditText newPasswordEditText;

    private MaterialButton saveNameButton;
    private MaterialButton saveContactButton;
    private MaterialButton savePasswordButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usernameEditText = view.findViewById(R.id.Username);
        emailEditText = view.findViewById(R.id.email);
        oldPasswordEditText = view.findViewById(R.id.oldPassword);
        newPasswordEditText = view.findViewById(R.id.newPassword);
        saveNameButton = view.findViewById(R.id.saveName);
        saveContactButton = view.findViewById(R.id.saveContact);
        savePasswordButton = view.findViewById(R.id.savePassword);

        // Set up click listeners for buttons in user_navigation
        view.findViewById(R.id.discover).setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_userDiscoverFragment));

        view.findViewById(R.id.myevents).setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_userEventsFragment));

        MaterialButton logoutButton = view.findViewById(R.id.logout);

        logoutButton.setOnClickListener(v -> {Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_loginFragment);});


        // Set up text change listeners
        setUpTextListeners();

        // Set up click listeners for buttons
        saveNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Save Name button click
                // Implement logic to save name
            }
        });

        saveContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Save Contact button click
                // Implement logic to save contacts
            }
        });

        savePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Save Password button click
                // Implement logic to save password
            }
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
}
