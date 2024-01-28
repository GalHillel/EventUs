package com.example.eventus.ui.eventsManager;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.eventus.R;


public class ManagerMenu extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manager_menu_sescion4, container, false);

        // Set click listener for login/register button
        view.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the login fragment
                NavController navController = Navigation.findNavController(requireActivity(), R.id.sec4);
                navController.navigate(R.id.action_managerMenu_to_managerEventsList);
            }
        });

        return view;
    }

}

