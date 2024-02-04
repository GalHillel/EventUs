package com.example.eventus.ui.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.eventus.R;

public class MinimumDataToShow  extends Fragment {


    public MinimumDataToShow(){
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_event, container, false);
        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {}
    private Bundle createEventDetailsBundle() {
        Bundle bundle = new Bundle();
        // Add event details to the bundle
        // For example:
        bundle.putString("eventName", "Event Name");
        bundle.putString("eventDate", "Event Date");
        bundle.putString("eventLocation", "Event Location");
        return bundle;
    }

}
