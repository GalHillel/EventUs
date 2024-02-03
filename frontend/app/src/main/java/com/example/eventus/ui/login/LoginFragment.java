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
import com.example.eventus.data.model.User;
import com.example.eventus.databinding.FragmentLoginBinding;
import com.example.eventus.ui.registration.RegistrationFragment;

public class LoginFragment extends Fragment {

//    private LoginViewModel loginViewModel;
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
//        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
//                .get(LoginViewModel.class);

        final EditText emailFromTheUser = binding.email;
        final EditText passwordFromTheUser = binding.password;
//        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;
        final CheckBox checkOrganizer = binding.checkOrganizer;

        final String userType = (checkOrganizer.isChecked())? "Organizer": "Participant";
        final String emailToSendToLoginFunction = emailFromTheUser.getText().toString().trim();
        final String passwordToSendToLoginFunction = emailFromTheUser.getText().toString().trim();

//        loginViewModel.getLoginFormState().observe(getViewLifecycleOwner(), loginFormState -> {
//            if (loginFormState == null) {
//                return;
//            }
//            loginButton.setEnabled(loginFormState.isDataValid());
//            if (loginFormState.getUsernameError() != null) {
//                usernameEditText.setError(getString(loginFormState.getUsernameError()));
//            }
//            if (loginFormState.getPasswordError() != null) {
//                passwordEditText.setError(getString(loginFormState.getPasswordError()));
//            }
//        });

//        loginViewModel.getLoginResult().observe(getViewLifecycleOwner(), loginResult -> {
//            if (loginResult == null) {
//                return;
//            }
//            loadingProgressBar.setVisibility(View.GONE);
//            if (loginResult.getError() != null) {
//                showLoginFailed(loginResult.getError());
//            }
//            if (loginResult.getSuccess() != null) {
//                updateUiWithUser(loginResult.getSuccess());
//            }
//        });

//        TextWatcher afterTextChangedListener = new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // ignore
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // ignore
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
//                        passwordEditText.getText().toString());
//            }
//        };
//        usernameEditText.addTextChangedListener(afterTextChangedListener);
//        passwordEditText.addTextChangedListener(afterTextChangedListener);
//        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                loginViewModel.login(usernameEditText.getText().toString(),
//                        passwordEditText.getText().toString(), checkOrganizer.isChecked());
//            }
//            return false;
//        });
        // Add a TextWatcher to monitor changes in the password field
//        passwordFromTheUser.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                // Enable or disable the login button based on password length
//                loginButton.setEnabled(editable.length() > 5);
//            }
//        });

//        TextView loginLink = view.findViewById(R.id.login);
//        loginLink.setOnClickListener(view1 ->
//                {
//                    String userMail =emailFromTheUser.getText().toString().trim();
//                    if(userMail.equals("yoni")) {
//                        NavHostFragment.findNavController(LoginFragment.this)
//                                .navigate(R.id.action_loginFragment_to_userEventsFragment);
//                    }
//
//
//                });

        TextView loginBtn = view.findViewById(R.id.login);
        loginBtn.setOnClickListener(v -> {
//            loadingProgressBar.setVisibility(View.VISIBLE);


            if (passwordFromTheUser.getText().length() <= 5) {
                // Show a message indicating incorrect password length
                Toast.makeText(requireContext(), "The password length must be greater than 5", Toast.LENGTH_SHORT).show();
                loadingProgressBar.setVisibility(View.GONE); // Hide loading indicator
                return; // Do not proceed with login
            }

            User userToLogIn ;
            try {
                userToLogIn =Database.userLogin(emailToSendToLoginFunction,passwordToSendToLoginFunction,userType) ;
                emailFromTheUser.setText("");
                passwordFromTheUser.setText("");

                if (checkOrganizer.isChecked()) {
                    NavHostFragment.findNavController(LoginFragment.this)
                            .navigate(R.id.action_loginFragment_to_organizerEvents);
                } else {
                    NavHostFragment.findNavController(LoginFragment.this)
                            .navigate(R.id.action_loginFragment_to_userEventsFragment);
                }
            } catch (Exception e) {
                String errorMessage = e.toString().trim();
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                passwordFromTheUser.setText("");
            }

        });

        TextView registerLink = binding.registerLink;
        registerLink.setOnClickListener(view1 -> NavHostFragment.findNavController(LoginFragment.this)
                .navigate(R.id.action_loginFragment_to_registrationFragment));

    }

//    private void updateUiWithUser(LoggedInUserView model) {
//        String welcome = getString(R.string.welcome) + model.getDisplayName();
//        // TODO : initiate successful logged in experience
//        if (getContext() != null && getContext().getApplicationContext() != null) {
//            Toast.makeText(getContext().getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
//        }
//    }
//
//    private void showLoginFailed(@StringRes Integer errorString) {
//        if (getContext() != null && getContext().getApplicationContext() != null) {
//            Toast.makeText(
//                    getContext().getApplicationContext(),
//                    errorString,
//                    Toast.LENGTH_LONG).show();
//        }
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
