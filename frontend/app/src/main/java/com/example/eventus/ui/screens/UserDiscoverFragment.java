package com.example.eventus.ui.screens;

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
import com.example.eventus.data.model.UserDisplay;
import com.example.eventus.ui.recycleViews.EventAdapter;
import com.example.eventus.data.model.UserEventDisplay;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserDiscoverFragment extends Fragment implements EventAdapter.OnShowMoreDetailsClickListener{

    private EditText searchEditText;
    private Button searchButton;
    private List<UserEventDisplay> searchResults = new ArrayList<>();
    private RecyclerView eventsRecyclerView;
    private EventAdapter eventAdapter;
    private UserDisplay user;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public UserDiscoverFragment() {}

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
            user = (UserDisplay) getArguments().getSerializable("user");

            if(user != null && user.getUser_type().equals("Organizer")){
                navMenu.findItem(R.id.discover).setVisible(false);
            }
            else{
                navMenu.findItem(R.id.newEvent).setVisible(false);
            }
        }

        searchEditText = view.findViewById(R.id.searchEditText);
        searchButton = view.findViewById(R.id.searchButton);
        eventsRecyclerView = view.findViewById(R.id.eventsList);

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
        bundle.putSerializable("user",user);
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
            // Update UI on the main thread with the search results

            this.eventAdapter = new EventAdapter(this.searchResults);
            this.eventAdapter.setOnShowMoreDetailsClickListener(this);
            this.eventsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            this.eventsRecyclerView.setAdapter(this.eventAdapter);


        } catch (Exception e) {
            // Handle the exception, e.g., show an error message
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Shutdown the ExecutorService when the fragment is destroyed
        executorService.shutdown();
    }

    @Override
    public void onShowMoreDetailsClick(int position) {
        UserEventDisplay clickedEvent = searchResults.get(position);
        Bundle args = createNavigationBundle();
        args.putString("eventId",clickedEvent.getId());
        NavHostFragment.findNavController(UserDiscoverFragment.this)
                .navigate(R.id.eventDetailsFragment,args);

    }
}
