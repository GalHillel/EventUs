package com.example.eventus.ui.screens;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.example.eventus.data.FileUploader;
import com.example.eventus.data.ServerSideException;
import com.example.eventus.data.model.ServerResponse;
import com.example.eventus.data.model.UserDisplay;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Objects;

public class EditProfileFragment extends Fragment {

    private TextInputEditText usernameEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText oldPasswordEditText;
    private TextInputEditText newPasswordEditText;
    private TextInputEditText bioEditText;
    private ImageView profilePhotoImageView;

    private MaterialButton saveNameButton;
    private MaterialButton saveContactButton;
    private MaterialButton savePasswordButton;

    private UserDisplay user;
    private Uri uri = null;

    /*TODO: Make edit profile cleaner and easier to use, one click save for all fields like edit event
            Implement save profile pic
     */

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
        ImageButton choosePhotoButton = view.findViewById(R.id.choosePhotoButton);
        MaterialButton saveProfilePicture = view.findViewById(R.id.saveProfilePicture);
        MaterialButton saveBioButton = view.findViewById(R.id.saveBio);

        try {
            if (user != null && user.getProfile_pic().length() > 0) {
                Bitmap profile_icon = Database.getProfilePic(user.get_id());
                profilePhotoImageView.setImageBitmap(profile_icon);
            }
        } catch (ServerSideException e) {
            // Handle the exception (e.g., show an error message)
            e.printStackTrace();
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
        }


        // Set up listeners
        MaterialButton logoutButton = view.findViewById(R.id.logout);
        logoutButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_userProfileFragment_to_loginFragment);
            // Prints success message
            Toast.makeText(requireContext(), "Logging out", Toast.LENGTH_SHORT).show();
        });

        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v ->
                Navigation.findNavController(v).navigateUp());

        setUpTextListeners();

        choosePhotoButton.setOnClickListener(v -> choosePhotoFromGallery());

        //TODO resizing profile pics
        saveProfilePicture.setOnClickListener(v -> {
            // Get the Drawable from the ImageView
            Drawable drawable = profilePhotoImageView.getDrawable();

            // Convert the Drawable into a Bitmap
            Bitmap bitmap = null;
            if (drawable instanceof BitmapDrawable) {
                bitmap = ((BitmapDrawable) drawable).getBitmap();
            } else {
                // If the drawable is not a BitmapDrawable, create a new Bitmap
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
            }

            // Convert the Bitmap into a byte array
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] pic = stream.toByteArray();
            //TODO add removing profile pic
            Database.uploadProfilePic(pic, new FileUploader.UploadCallback() {
                @Override
                public void onSuccess(ServerResponse response) {
                    try{


                        String profile_id = response.getPayload();
                        HashMap<String, Object> updatedUserParams = new HashMap<>();
                        updatedUserParams.put("profile_pic", profile_id);
                        Database.editUser(user.get_id(), updatedUserParams);
                        Toast.makeText(requireContext(), "Your bio has been updated successfully", Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        //TODO handle exceptions
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(ServerResponse errorMessage) {
                    //TODO handle exceptions
                    Log.e("Err", errorMessage.toString());
                }
            });
        });

        saveBioButton.setOnClickListener(v -> {
            String newBio = Objects.requireNonNull(bioEditText.getText()).toString();
            if (!TextUtils.isEmpty(newBio)) {
                try {
                    HashMap<String, Object> updatedUserParams = new HashMap<>();
                    updatedUserParams.put("bio", newBio);
                    Database.editUser(user.get_id(), updatedUserParams);
                    bioEditText.setText("");
                    // Prints success message
                    Toast.makeText(requireContext(), "Your bio has been updated successfully", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("Err", Objects.requireNonNull(e.getMessage()));
                }
            }
        });

        // Set up click listeners for buttons
        saveNameButton.setOnClickListener(v -> {
            String newUsername = Objects.requireNonNull(usernameEditText.getText()).toString();
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
                    Log.e("Err", Objects.requireNonNull(e.getMessage()));
                }
            }
        });

        saveContactButton.setOnClickListener(v -> {
            String newEmail = Objects.requireNonNull(emailEditText.getText()).toString();
            if (!TextUtils.isEmpty(newEmail)) {
                try {
                    HashMap<String, Object> updatedUserParams = new HashMap<>();
                    updatedUserParams.put("email", newEmail);
                    Database.editUser(user.get_id(), updatedUserParams);
                    emailEditText.setText("");
                    // Prints success message
                    Toast.makeText(requireContext(), "Your Email has changed successfully to: " + newEmail, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("Err", Objects.requireNonNull(e.getMessage()));
                }
            }
        });

        savePasswordButton.setOnClickListener(v -> {
            String oldPass = Objects.requireNonNull(oldPasswordEditText.getText()).toString();
            String newPass = Objects.requireNonNull(newPasswordEditText.getText()).toString();
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
                    Log.e("Err", Objects.requireNonNull(e.getMessage()));
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
            uri = data.getData();
            // Set the selected image to the profile photo ImageView
            profilePhotoImageView.setImageURI(uri);
        }
    }
}
