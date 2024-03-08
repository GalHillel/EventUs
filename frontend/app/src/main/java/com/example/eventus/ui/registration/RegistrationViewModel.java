package com.example.eventus.ui.registration;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.eventus.data.Database;
import com.example.eventus.data.model.User;

public class RegistrationViewModel extends ViewModel {

    private final MutableLiveData<Boolean> registrationSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public boolean register(String email, String password, String username, String userType) {
        try {
            User user = Database.addUser(email, username, password, userType);

            // Use postValue to update LiveData on the main thread
            registrationSuccess.postValue(user != null);
            errorMessage.postValue(user == null ? "Registration failed. Please try again." : null);
            return true;
        } catch (Exception e) {
            // Handle exceptions, log them, or show appropriate error messages
            errorMessage.postValue("An error occurred during registration.");
            return false;
        }

    }

}


