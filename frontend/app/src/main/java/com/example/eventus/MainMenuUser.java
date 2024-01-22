package com.example.eventus;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.navigation.Navigation;

public class MainMenuUser extends Fragment {

    private Button loginRegisterButton;

    public MainMenuUser() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_menu_user, container, false);

        // Initialize views
        initViews(view);

        // Set click listeners
        setButtonClickListeners(view);

        return view;
    }

    private void initViews(View view) {
        // Initialize button
        loginRegisterButton = view.findViewById(R.id.loginRegisterButton);
    }

    private void setButtonClickListeners(final View view) {
        // Set click listener for login/register button
        loginRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the LoginFragment
                Navigation.findNavController(view).navigate(R.id.action_mainMenuUserFragment_to_loginFragment);
            }
        });
    }
}
