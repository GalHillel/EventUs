package com.example.eventus.ui.screens.UserEvents;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventus.R;
import com.example.eventus.data.model.LoggedInUser;
import com.example.eventus.data.model.UserDisplay;
import com.example.eventus.data.model.UserEventDisplay;
import com.example.eventus.ui.recycleViews.EventAdapter;
import com.example.eventus.ui.screens.EventDetails.EventDetailsActivity;

import java.util.Comparator;
import java.util.List;

public class EventListFragment extends Fragment implements EventAdapter.OnShowMoreDetailsClickListener {
    private List<UserEventDisplay> lst;
    private LoggedInUser user;
    public EventListFragment(LoggedInUser user, List<UserEventDisplay> lst){
        this.lst = lst;
        this.user = user;
        lst.sort(Comparator.comparing(UserEventDisplay::getDate));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_list, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        RecyclerView upcomingEventsRecyclerView = view.findViewById(R.id.eventListRecycleView);
        EventAdapter upcomingEventAdapter = new EventAdapter(lst);
        upcomingEventAdapter.setOnShowMoreDetailsClickListener(this);
        upcomingEventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        upcomingEventsRecyclerView.setAdapter(upcomingEventAdapter);

        if(this.lst.size() == 0 ){
            view.setVisibility(View.GONE);
        }

    }

    @Override
    public void onShowMoreDetailsClick(UserEventDisplay clickedEvent) {
        Bundle args = new Bundle();
        args.putSerializable("user", this.user);

        args.putString("eventId", clickedEvent.getId());
        //TODO handle activity fail
        Intent i = new Intent(this.getContext(), EventDetailsActivity.class);
        i.putExtras(args);
        startActivity(i);

    }
}
