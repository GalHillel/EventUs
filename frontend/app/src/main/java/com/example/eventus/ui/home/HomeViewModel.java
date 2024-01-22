package com.example.eventus.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> greetingText;

    public HomeViewModel() {
        greetingText = new MutableLiveData<>();
        // Set the initial greeting text (you can modify this as needed)
        greetingText.setValue("Hello, username!");
    }

    // Method to get the greeting text LiveData
    public LiveData<String> getGreetingText() {
        return greetingText;
    }

    // You can add more methods and LiveData as needed for your HomeFragment

}
