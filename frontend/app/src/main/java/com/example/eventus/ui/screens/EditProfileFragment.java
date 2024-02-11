package com.example.eventus.ui.screens;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

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

public class EditProfileFragment extends Fragment {

    private TextInputEditText usernameEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText oldPasswordEditText;
    private TextInputEditText newPasswordEditText;
    private TextInputEditText bioEditText;
    private ImageView profilePhotoImageView;
    private ImageButton choosePhotoButton;

    private MaterialButton saveNameButton, saveProfilePicture, saveBioButton;
    private MaterialButton saveContactButton;
    private MaterialButton savePasswordButton;

    private UserDisplay user;
    //TODO make edit profile cleaner and easier to use, oneclick save for all fields like edit event
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            user = (UserDisplay) getArguments().getSerializable("user");
        }

        usernameEditText = view.findViewById(R.id.Username);
        emailEditText = view.findViewById(R.id.email);
        oldPasswordEditText = view.findViewById(R.id.oldPassword);
        newPasswordEditText = view.findViewById(R.id.newPassword);
        saveNameButton = view.findViewById(R.id.saveName);
        saveContactButton = view.findViewById(R.id.saveContact);
        savePasswordButton = view.findViewById(R.id.savePassword);
        bioEditText = view.findViewById(R.id.bio);
        profilePhotoImageView = view.findViewById(R.id.profilePhotoImageView);
        choosePhotoButton = view.findViewById(R.id.choosePhotoButton);
        saveProfilePicture = view.findViewById(R.id.saveProfilePicture);
        saveBioButton = view.findViewById(R.id.saveBio);



        // Set up listeners
        MaterialButton logoutButton = view.findViewById(R.id.logout);
        logoutButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_userProfileFragment_to_loginFragment);
            // Prints success message
            Toast.makeText(requireContext(), "Login out", Toast.LENGTH_SHORT).show();
        });

        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v ->
                Navigation.findNavController(v).navigateUp());

        setUpTextListeners();

        choosePhotoButton.setOnClickListener(v -> choosePhotoFromGallery());

        // TODO: Implement save profile pic
        saveProfilePicture.setOnClickListener(v -> {
            // Get the URI of the selected image
            /**Uri selectedImageUri = getImageUri();
            if (selectedImageUri != null) {
                // Upload the selected image to the server
                //uploadProfilePicture(selectedImageUri);
            }**/
        });

        saveBioButton.setOnClickListener(v -> {
            String newBio = bioEditText.getText().toString();
            if (!TextUtils.isEmpty(newBio)) {
                try {
                    HashMap<String, Object> updatedUserParams = new HashMap<>();
                    updatedUserParams.put("bio", newBio);
                    Database.editUser(user.get_id(), updatedUserParams);
                    bioEditText.setText("");
                    // Prints success message
                    Toast.makeText(requireContext(), "Your bio has been updated successfully", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("Err", e.getMessage());
                }
            }
        });

        // Set up click listeners for buttons
        saveNameButton.setOnClickListener(v -> {
            String newUsername = usernameEditText.getText().toString();
            if (!TextUtils.isEmpty(newUsername)) {
                try {
                    HashMap<String, Object> updatedUserParams = new HashMap<>();
                    updatedUserParams.put("name", newUsername);
                    Database.editUser(user.get_id(), updatedUserParams);
                    usernameEditText.setText("");
                    user.setName(newUsername);
                    // Prints success message
                    Toast.makeText(requireContext(), "Your name has changed successfully to: " + newUsername, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("Err",e.getMessage());
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
                    emailEditText.setText("");
                    // Prints success message
                    Toast.makeText(requireContext(), "Your Email has changed successfully to: " + newEmail, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("Err",e.getMessage());
                }
            }
        });

        savePasswordButton.setOnClickListener(v -> {
            String oldPass = oldPasswordEditText.getText().toString();
            String newPass = newPasswordEditText.getText().toString();
            if (!TextUtils.isEmpty(oldPass) && !TextUtils.isEmpty(newPass)) {
                try {
                    HashMap<String, Object> updatedUserParams = new HashMap<>();
                    updatedUserParams.put("oldPassword", oldPass);
                    updatedUserParams.put("password", newPass);
                    Database.editUser(user.get_id(), updatedUserParams);
                    oldPasswordEditText.setText("");
                    newPasswordEditText.setText("");
                    // Prints success message
                    Toast.makeText(requireContext(), "Your password has changed successfully", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("Err",e.getMessage());
                }
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

    // Method to create a common bundle for navigation
    private Bundle createNavigationBundle() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("user",user);
        return bundle;
    }

    // Method to choose a photo from the gallery
    private void choosePhotoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    // Method to handle the result of choosing a photo from the gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            // Set the selected image to the profile photo ImageView
            profilePhotoImageView.setImageURI(uri);
        }
    }
}
