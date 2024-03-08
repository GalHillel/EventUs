package com.example.eventus.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.eventus.R;
import com.example.eventus.data.Database;
import com.example.eventus.data.model.LoggedInUser;
import com.example.eventus.databinding.FragmentLoginBinding;
import com.example.eventus.ui.screens.UserMainScreen.UserMainActivity;

import java.util.Objects;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText emailFromTheUser = binding.email;
        final EditText passwordFromTheUser = binding.password;
        final CheckBox checkOrganizer = binding.checkOrganizer;

        TextView loginBtn = view.findViewById(R.id.login);
        loginBtn.setOnClickListener(v -> {
            String userType = (checkOrganizer.isChecked()) ? "Organizer" : "Participant";
            String emailToSendToLoginFunction = Objects.requireNonNull(emailFromTheUser.getText()).toString().trim();
            String passwordToSendToLoginFunction = Objects.requireNonNull(passwordFromTheUser.getText()).toString().trim();

            //default users for testing
            if (emailToSendToLoginFunction.isEmpty() && passwordToSendToLoginFunction.isEmpty()) {
                if (userType.equals("Participant")) {
                    emailToSendToLoginFunction = "ziv@gmail.com";
                    passwordToSendToLoginFunction = "zivPass";
                } else {
                    emailToSendToLoginFunction = "zivO@gmail.com";
                    passwordToSendToLoginFunction = "zivOPass";
                }
            }

            // Check if all fields are filled
            if (emailToSendToLoginFunction.isEmpty() || passwordToSendToLoginFunction.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            LoggedInUser userToLogIn = null;
            try {
                userToLogIn = Database.userLogin(emailToSendToLoginFunction, passwordToSendToLoginFunction, userType);
                emailFromTheUser.setText("");
                passwordFromTheUser.setText("");

                // Prints success message
                Toast.makeText(requireContext(), "Welcome " + userToLogIn.getName(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }

            Bundle args = new Bundle();
            args.putSerializable("user", userToLogIn);

            Intent i = new Intent(requireContext(), UserMainActivity.class);
            i.putExtras(args);
            startActivity(i);
        });

        TextView registerLink = binding.registerLink;
        registerLink.setOnClickListener(view1 -> NavHostFragment.findNavController(LoginFragment.this).navigate(R.id.action_loginFragment_to_registrationFragment));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}