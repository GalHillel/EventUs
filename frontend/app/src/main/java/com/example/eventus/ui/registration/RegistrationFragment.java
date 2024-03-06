package com.example.eventus.ui.registration;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.eventus.R;

public class RegistrationFragment extends Fragment {

    private RegistrationViewModel registrationViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registrationViewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);

        EditText emailEditText = view.findViewById(R.id.email);
        EditText passwordEditText = view.findViewById(R.id.password);
        EditText passwordValidationEditText = view.findViewById(R.id.passwordValidation);
        EditText usernameEditText = view.findViewById(R.id.username);
        RadioButton radioOrganizer = view.findViewById(R.id.radioOrganizer);
        Button registerButton = view.findViewById(R.id.register);

        registerButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String passwordValidation = passwordValidationEditText.getText().toString().trim();
            String username = usernameEditText.getText().toString().trim();
            String userType = radioOrganizer.isChecked() ? "Organizer" : "Participant";

            // Check if any field is empty
            if (email.isEmpty() || password.isEmpty() || passwordValidation.isEmpty() || username.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if the email is in the correct format
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(requireContext(), "Invalid email format. Please enter a valid email address.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if passwords match
            if (!passwordValidation.equals(password)) {
                Toast.makeText(requireContext(), "Passwords do not match. Please try again.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Attempt registration
            boolean registrationSuccess = registrationViewModel.register(email, password, username, userType);

            if (registrationSuccess) {
                // Registration successful, display message
                Toast.makeText(requireContext(), "Registration successful!", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(RegistrationFragment.this)
                        .navigate(R.id.action_registrationFragment_to_loginFragment);
            } else {
                // Registration failed
                Toast.makeText(requireContext(), "Registration failed. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });

        TextView loginLink = view.findViewById(R.id.loginLink);
        loginLink.setOnClickListener(v -> NavHostFragment.findNavController(RegistrationFragment.this)
                .navigate(R.id.action_registrationFragment_to_loginFragment));
    }
}
