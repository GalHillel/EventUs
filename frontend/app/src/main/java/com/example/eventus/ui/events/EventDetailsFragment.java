package com.example.eventus.ui.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventus.R;
import com.example.eventus.data.Database;
import com.example.eventus.data.ServerSideException;
import com.example.eventus.data.model.User;
import com.example.eventus.data.model.UserDisplay;
import com.example.eventus.data.model.UserEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventDetailsFragment extends Fragment implements UserAdaptor.ButtonListener{
    private UserEvent userEvent;
    private List<UserDisplay> users = new ArrayList<>();
    private String userId;
    private String userName;
    private String eventId;
    private RecyclerView userListRecyclerView;
    private UserAdaptor userAdaptor;

    private TextView eventNameView, eventDateView, eventLocationview, eventDescription;
    private Button exitEventButton, joinEventButton;

    @Nullable
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Additional initialization or setup can be done here
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event_details, container, false);

        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the previous fragment
                getParentFragmentManager().popBackStack();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        eventNameView = view.findViewById(R.id.eventNameTextView);
        eventDateView = view.findViewById(R.id.eventDateTextView);
        eventLocationview = view.findViewById(R.id.eventLocationTextView);
        eventDescription = view.findViewById(R.id.eventDescriptionTextView);
        this.joinEventButton = view.findViewById(R.id.joinEventButton);
        this.exitEventButton = view.findViewById(R.id.leaveEventButton);
        this.joinEventButton.setOnClickListener(this::onJoinEventClick);
        this.exitEventButton.setOnClickListener(this::onLeaveEventClick);

        if (getArguments() != null) {
            this.userId = getArguments().getString("userId", "");
            this.userName = getArguments().getString("userName", "");
            this.eventId = getArguments().getString("eventId", "");
            try{
                userEvent = Database.loadEvent(this.eventId);
                UserDisplay[] tmp = Database.getUserList(this.eventId);
                this.users.clear();
                this.users.addAll(Arrays.asList(tmp));

                eventNameView.setText(this.userEvent.getName());
                eventDateView.setText(this.userEvent.getDate().toString());
                eventLocationview.setText(this.userEvent.getLocation());
                eventDescription.setText(this.userEvent.getDescription());

                userListRecyclerView = view.findViewById(R.id.eventListRecycleView);
                userAdaptor = new UserAdaptor(this.users,(this.userId.equals(this.userEvent.getId()))? "Organizer": "Participant");
                userAdaptor.SetonKickClickListener(this);
                userListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                userListRecyclerView.setAdapter(userAdaptor);


                if(this.userEvent.getAttendents().contains(this.userId)){
                    this.joinEventButton.setVisibility(View.GONE);
                    this.exitEventButton.setVisibility(View.VISIBLE);
                }
                else{
                    this.exitEventButton.setVisibility(View.GONE);
                    this.joinEventButton.setVisibility(View.VISIBLE);
                }


            }catch(Exception e){
                e.printStackTrace();
            }



        }

    }

    public void onBackButtonClick(View view) {
        // Navigate back
        //NavHostFragment.findNavController(this).popBackStack(R.id.eventDetailsFragment, false);
    }

    // This method will be called when the "Join Event" button is clicked
    public void onJoinEventClick(View view) {
        try{
            Database.joinEvent(this.userId,this.userEvent.getId());
            this.joinEventButton.setVisibility(View.GONE);
            this.exitEventButton.setVisibility(View.VISIBLE);
            this.users.add(new UserDisplay(userId,userName,(this.userId.equals(this.userEvent.getId()))? "Organizer": "Participant"));
            this.userAdaptor.notifyItemInserted(this.users.size()-1);
        }catch(Exception e){
            //handle
        }
    }

    // This method will be called when the "Leave Event" button is clicked
    public void onLeaveEventClick(View view) {
        if(this.userId.equals(this.userEvent.getId())){
            //delete event
        }
        else{
            try{
                Database.exitEvent(this.userId,this.userEvent.getId());
                this.exitEventButton.setVisibility(View.GONE);
                this.joinEventButton.setVisibility(View.VISIBLE);
                int idx = this.users.indexOf(new UserDisplay(userId,userName,(this.userId.equals(this.userEvent.getId()))? "Organizer": "Participant"));
                this.users.remove(idx);
                this.userAdaptor.notifyItemRemoved(idx);
            }catch(Exception e){
                //handle
            }

        }
    }

    @Override
    public void onKickClick(int position) {
        //handle kick button click
    }

    @Override
    public void onMessageClick(int position) {
        //handle message button click
    }
}