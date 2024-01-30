package com.example.eventus.ui.registration;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.eventus.data.Database;
import com.example.eventus.data.model.User;

public class RegistrationViewModel extends ViewModel {

    private MutableLiveData<Boolean> registrationSuccess = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<Boolean> getRegistrationSuccess() {
        return registrationSuccess;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void register(String email, String password, String username, String userType) {
        // Validate input if needed (you can add validation logic here)

        // Call Database method for user registration


        // Use a background thread or AsyncTask to perform network operations
        new Thread(() -> {
            try {
                User user = Database.addUser(email, username, password, userType);

                // Use postValue to update LiveData on the main thread
                registrationSuccess.postValue(user != null);
                errorMessage.postValue(user == null ? "Registration failed. Please try again." : null);
            } catch (Exception e) {
                // Handle exceptions, log them, or show appropriate error messages
                errorMessage.postValue("An error occurred during registration.");
            }
        }).start();
    }

}


