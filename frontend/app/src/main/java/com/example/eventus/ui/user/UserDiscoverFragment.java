package com.example.eventus.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.eventus.R;
import com.example.eventus.data.Database;
import com.example.eventus.ui.events.EventAdapter;
import com.example.eventus.ui.events.UserEventDisplay;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserDiscoverFragment extends Fragment {

    private EditText searchEditText;
    private Button searchButton;
    private RecyclerView eventsRecyclerView;
    private EventAdapter eventAdapter;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public UserDiscoverFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_discover, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchEditText = view.findViewById(R.id.searchEditText);
        searchButton = view.findViewById(R.id.searchButton);
        eventsRecyclerView = view.findViewById(R.id.eventsList);

        // Set up click listeners for buttons
        view.findViewById(R.id.profile).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_userDiscoverFragment_to_profileFragment, createNavigationBundle()));

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
        bundle.putString("userId", getArguments().getString("userId", ""));
        bundle.putString("userName", getArguments().getString("userName", ""));
        return bundle;
    }

    private void performEventSearch() {
        String searchQuery = searchEditText.getText().toString();

        // Create a HashMap with the search query
        HashMap<String, Object> searchParams = new HashMap<>();
        searchParams.put("name", searchQuery);

        // Execute the search in the background using ExecutorService
        executorService.execute(() -> {
            try {
                UserEventDisplay[] searchResults = Database.searchEvents(searchParams);

                // Update UI on the main thread with the search results
                requireActivity().runOnUiThread(() -> {
                    eventAdapter = new EventAdapter(Arrays.asList(searchResults));
                    eventsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                    eventsRecyclerView.setAdapter(eventAdapter);
                });

            } catch (Exception e) {
                // Handle the exception, e.g., show an error message
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Shutdown the ExecutorService when the fragment is destroyed
        executorService.shutdown();
    }
}
