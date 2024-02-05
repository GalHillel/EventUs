// LoginFragment.java
package com.example.eventus.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.eventus.R;
import com.example.eventus.data.Database;
import com.example.eventus.data.ServerSideException;
import com.example.eventus.data.model.User;
import com.example.eventus.data.model.UserDisplay;
import com.example.eventus.databinding.FragmentLoginBinding;
import com.example.eventus.ui.registration.RegistrationFragment;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final EditText emailFromTheUser = binding.email;
        final EditText passwordFromTheUser = binding.password;

        final ProgressBar loadingProgressBar = binding.loading;
        final CheckBox checkOrganizer = binding.checkOrganizer;

        TextView loginBtn = view.findViewById(R.id.login);
        loginBtn.setOnClickListener(v -> {
            String userType = (checkOrganizer.isChecked())? "Organizer": "Participant";
            String emailToSendToLoginFunction = emailFromTheUser.getText().toString().trim();
            String passwordToSendToLoginFunction = passwordFromTheUser.getText().toString().trim();

            //default users for testing
//            if(emailToSendToLoginFunction.equals("") && passwordToSendToLoginFunction.equals("")){
//                if(userType.equals("Participant")){
//                    emailToSendToLoginFunction = "user1@gmail.com";
//                    passwordToSendToLoginFunction = "userPass";
//                }
//                else{
//                    emailToSendToLoginFunction = "ziv.morgan@gmail.com";
//                    passwordToSendToLoginFunction = "newPass";
//                }
//            }

            if (passwordToSendToLoginFunction.length() <= 5) {
                // Show a message indicating incorrect password length
                Toast.makeText(requireContext(), "The password length must be greater than 5", Toast.LENGTH_SHORT).show();
                loadingProgressBar.setVisibility(View.GONE); // Hide loading indicator
                return; // Do not proceed with login
            }


            try {
                UserDisplay userToLogIn = Database.userLogin(emailToSendToLoginFunction, passwordToSendToLoginFunction, userType);
                emailFromTheUser.setText("");
                passwordFromTheUser.setText("");


                // Now you can use the userId and userName as needed
                // For example, you might want to pass them to another fragment or activity
                Bundle args = new Bundle();
                args.putSerializable("user",userToLogIn);

                NavHostFragment.findNavController(LoginFragment.this)
                        .navigate(R.id.action_loginFragment_to_userEventsFragment, args);
            } catch (Exception e) {
                Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        });


        TextView registerLink = binding.registerLink;
        registerLink.setOnClickListener(view1 -> NavHostFragment.findNavController(LoginFragment.this)
                .navigate(R.id.action_loginFragment_to_registrationFragment));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
