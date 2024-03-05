package com.example.eventus.ui.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.eventus.R;
import com.example.eventus.data.Database;
import com.example.eventus.data.model.LoggedInUser;
import com.example.eventus.data.model.UserDisplay;
import com.example.eventus.ui.recycleViews.EventAdapter;
import com.example.eventus.data.model.UserEventDisplay;
import com.example.eventus.ui.screens.EventDetails.EventDetailsActivity;
import com.example.eventus.ui.screens.UserEvents.EventListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class UserDiscoverFragment extends Fragment {

    private EditText searchEditText;
    private List<UserEventDisplay> searchResults = new ArrayList<>();
    private LoggedInUser user;

    // TODO: Add an option for searching users by username

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public UserDiscoverFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_discover, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BottomNavigationView bottomNavigationView = view.findViewById(R.id.navigation);
        Menu navMenu = bottomNavigationView.getMenu();

        if (getArguments() != null) {
            user = (LoggedInUser) getArguments().getSerializable("user");

            if (user != null && user.getUser_type().equals("Organizer")) {
                navMenu.findItem(R.id.discover).setVisible(false);
            } else {
                navMenu.findItem(R.id.newEvent).setVisible(false);
            }
        }
        searchEditText = view.findViewById(R.id.searchEditText);
        Button searchButton = view.findViewById(R.id.searchButton);





        // Set up click listeners for buttons
        view.findViewById(R.id.profile).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_userDiscoverFragment_to_userProfileFragment, createNavigationBundle()));

        view.findViewById(R.id.myevents).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_userDiscoverFragment_to_userEventsFragment, createNavigationBundle()));

        view.findViewById(R.id.messages).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_userDiscoverFragment_to_userMessagesFragment, createNavigationBundle()));

        searchButton.setOnClickListener(v -> {
            // Perform event search
            performEventSearch();
        });
    }

    // Method to create a common bundle for navigation
    private Bundle createNavigationBundle() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        return bundle;
    }

    private void performEventSearch() {
        String searchQuery = searchEditText.getText().toString();

        // Create a HashMap with the search query
        HashMap<String, Object> searchParams = new HashMap<>();
        searchParams.put("name", searchQuery);

        // Execute the search in the background using ExecutorService
        try {
            UserEventDisplay[] temp = Database.searchEvents(searchParams);
            this.searchResults = Arrays.asList(temp);
        } catch (Exception e) {
            // Handle the exception, e.g., show an error message
            e.printStackTrace();
        }
        //filter events
        this.searchResults = searchResults.stream().filter(e->!this.user.getEvents().contains(e.getId())).collect(Collectors.toList());

        EventListFragment searchEventListFragment = new EventListFragment(this.user,this.searchResults);
        getChildFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.discoveredEventsList, searchEventListFragment)
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Shutdown the ExecutorService when the fragment is destroyed
        executorService.shutdown();
    }


}
