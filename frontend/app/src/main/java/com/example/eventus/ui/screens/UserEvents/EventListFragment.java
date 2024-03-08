package com.example.eventus.ui.screens.UserEvents;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventus.R;
import com.example.eventus.data.model.LoggedInUser;
import com.example.eventus.data.model.UserEventDisplay;
import com.example.eventus.ui.recycleViews.EventAdapter;
import com.example.eventus.ui.screens.EventDetails.EventDetailsActivity;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class EventListFragment extends Fragment implements EventAdapter.OnShowMoreDetailsClickListener {
    private final List<UserEventDisplay> lst;
    private LoggedInUser user;

    public EventListFragment(LoggedInUser user, List<UserEventDisplay> lst) {
        this.lst = lst;
        this.user = user;
        Date d = new Date();
        this.lst.sort(Comparator.comparingLong(o -> Math.abs(d.getTime() - o.getDate().getTime())));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_list, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView upcomingEventsRecyclerView = view.findViewById(R.id.eventListRecycleView);
        EventAdapter upcomingEventAdapter = new EventAdapter(lst);
        upcomingEventAdapter.setOnShowMoreDetailsClickListener(this);
        upcomingEventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        upcomingEventsRecyclerView.setAdapter(upcomingEventAdapter);

        if (this.lst.isEmpty()) {
            view.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == R.id.showMoreDetails) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null && data.getExtras() != null) {
                    LoggedInUser newUser = (LoggedInUser) data.getExtras().getSerializable("user");
                    if (newUser != null) {
                        this.user = newUser;
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(requireContext(), "an error has occurred", Toast.LENGTH_LONG).show();
            }
        }
        if (getParentFragment() != null) requireParentFragment().onActivityResult(requestCode, resultCode, data);
        else {
            requireActivity().setResult(resultCode, data);
        }
    }

    @Override
    public void onShowMoreDetailsClick(UserEventDisplay clickedEvent) {
        Bundle args = new Bundle();
        args.putSerializable("user", this.user);

        args.putString("eventId", clickedEvent.getId());

        Intent i = new Intent(getContext(), EventDetailsActivity.class);
        i.putExtras(args);
        startActivityForResult(i, R.id.showMoreDetails);

    }
}
