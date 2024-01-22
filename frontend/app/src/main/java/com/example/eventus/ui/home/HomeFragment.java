package com.example.eventus.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.eventus.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView greetingText = root.findViewById(R.id.greetingText);
        final Button signOutButton = root.findViewById(R.id.signOutButton);

        // Observe changes in the greeting text
        homeViewModel.getGreetingText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String greeting) {
                greetingText.setText(greeting);
            }
        });

        // Add click listener for the "Sign Out" button
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use NavHostFragment to navigate back to the login fragment
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_homeFragment_to_loginFragment);
            }
        });

        // Add similar click listeners for other buttons as needed

        return root;
    }
}
