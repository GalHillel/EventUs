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

    public void register(String email, String password, String passwordValidation, String username, String userType) {
        // Validate input if needed (you can add validation logic here)

        // Call Database method for user registration
        Database database = new Database();
        User user = database.addUser(email, username, password, userType);

        // Handle the response from the database (user registration success or failure)
        if (user != null) {
            // Registration successful
            registrationSuccess.setValue(true);

            // Perform any additional actions if needed
            // For example, you can navigate to the login screen
        } else {
            // Registration failed, handle the error
            errorMessage.setValue("Registration failed. Please try again.");

            // You can log the error or perform additional error handling here
        }
    }
}


