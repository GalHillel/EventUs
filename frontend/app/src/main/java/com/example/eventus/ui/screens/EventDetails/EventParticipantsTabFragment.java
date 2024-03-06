package com.example.eventus.ui.screens.EventDetails;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventus.R;
import com.example.eventus.data.Database;
import com.example.eventus.data.model.UserDisplay;
import com.example.eventus.data.model.UserEvent;
import com.example.eventus.ui.recycleViews.UserAdapter;
import com.example.eventus.ui.screens.Messages.CreateMessageActivity;
import com.example.eventus.ui.screens.Messages.MessageActivity;
import com.example.eventus.ui.screens.Profile.ViewUserProfileActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class EventParticipantsTabFragment extends Fragment implements UserAdapter.ButtonListener,UserAdapter.Wrappers {

    private UserAdapter userAdapter;

    private Button exitEventButton, joinEventButton;

    private RecyclerView userListRecyclerView;

    private EventDetailsActivity holder;

    public EventParticipantsTabFragment(EventDetailsActivity myContext) {
        this.holder = myContext;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Additional initialization or setup can be done here
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_participants_tab, container, false);

        // Inflate the layout for this fragment
        return view;
    }
    /*
        TODO:
            1. Add the option for an Organizer to accept a new user - NEEDS CHECKING
            4. (optional) show profile pictures of users
            5. Move Organizer to the top of the list and show that he is the Organizer - DONE
            6. Fix bug where if a user leaves an event and then goes back to event list,
             the event is still there until the user refreshes
            7. Fix bug where on user click, the app crashes
     */
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.joinEventButton = view.findViewById(R.id.joinEventButton);
        this.exitEventButton = view.findViewById(R.id.leaveEventButton);

        this.userListRecyclerView = view.findViewById(R.id.eventListRecycleView);


        this.joinEventButton.setOnClickListener(this::onJoinEventClick);
        this.exitEventButton.setOnClickListener(this::onLeaveEventClick);

        userAdapter = new UserAdapter(this.holder.getUsers(),this.holder.getEvent(), (this.holder.getUser().get_id().equals(this.holder.getEvent().getCreator_id())) ? "Organizer" : "Participant");
        userAdapter.setOnKickClickListener(this);
        userAdapter.setOnMessageClickListener(this);
        userAdapter.setOnUserItemClickListener(this);
        userAdapter.setProfileWrapper(this);

        if (this.holder.getUser().get_id().equals(this.holder.getEvent().getCreator_id())) {
            this.exitEventButton.setText("Delete Event");
        }
        if (this.holder.getEvent().getAttendents().containsKey(this.holder.getUser().get_id())) {
            this.joinEventButton.setVisibility(View.GONE);
            this.exitEventButton.setVisibility(View.VISIBLE);
            if(this.holder.getEvent().getIsPrivate()){
                if(Boolean.FALSE.equals(this.holder.getEvent().getAttendents().get(this.holder.getUser().get_id()))) {
                    this.exitEventButton.setText("Cancel Request");
                    this.userListRecyclerView.setVisibility(View.INVISIBLE);
                }
                else{
                    this.exitEventButton.setText("Leave Event");
                }
            }


        } else {
            this.exitEventButton.setVisibility(View.GONE);
            this.joinEventButton.setVisibility(View.VISIBLE);
            if(this.holder.getEvent().getIsPrivate()){
                this.userListRecyclerView.setVisibility(View.INVISIBLE);

            }
        }

        if(this.holder.hasPassed()){
            exitEventButton.setVisibility(View.GONE);
            joinEventButton.setVisibility(View.GONE);
        }

        userListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userListRecyclerView.setAdapter(userAdapter);

    }

    // This method will be called when the "Join Event" button is clicked
    public void onJoinEventClick(View view) {
        try {
            Database.joinEvent(this.holder.getUser().get_id(), this.holder.getEvent().getId());
            this.joinEventButton.setVisibility(View.GONE);
            this.exitEventButton.setVisibility(View.VISIBLE);
            this.holder.getUsers().add(this.holder.getUser());
            this.holder.getEvent().getAttendents().put(this.holder.getUser().get_id(),false);
            if(this.holder.getEvent().getIsPrivate()) {
                if (Boolean.FALSE.equals(this.holder.getEvent().getAttendents().get(this.holder.getUser().get_id()))) {
                    this.exitEventButton.setText("Cancel Request");
                    this.userListRecyclerView.setVisibility(View.INVISIBLE);
                } else {
                    this.exitEventButton.setText("Leave Event");
                }
            }

            this.userAdapter.notifyItemInserted(this.holder.getUsers().size() - 1);
        } catch (Exception e) {
            //handle
        }
    }

    // This method will be called when the "Leave Event" button is clicked
    public void onLeaveEventClick(View view) {
        if (this.holder.getUser().get_id().equals(this.holder.getEvent().getCreator_id())) {
            try {
                //TODO maybe change this?
                Database.delEvent(this.holder.getEvent().getId());
                this.holder.success();
            } catch (Exception e) {
                //handle
            }
        } else {
            boolean removed = removeUser(holder.getUser());
            if(removed) {
                this.exitEventButton.setVisibility(View.GONE);
                this.joinEventButton.setVisibility(View.VISIBLE);
                this.holder.getUser().getEvents().remove(this.holder.getEvent().getId());
            }
        }
    }

    private boolean removeUser(UserDisplay user) {
        try {
            Database.exitEvent(user.get_id(), this.holder.getEvent().getId());
            int idx = this.holder.getUsers().indexOf(user);
            this.holder.getUsers().remove(idx);
            this.userAdapter.notifyItemRemoved(idx);
            return true;
        } catch (Exception e) {
            return false;
            //handle
        }
    }
    private void acceptUser(UserDisplay user){
        try {
            Database.acceptUser(user.get_id(), this.holder.getEvent().getId());
            int idx = this.holder.getUsers().indexOf(user);
            this.holder.getEvent().getAttendents().put(user.get_id(),true);
            this.userAdapter.notifyItemChanged(idx);
        } catch (Exception e) {
            //handle
        }
    }

    @Override
    public void onKickClick(int position) {
        removeUser(this.holder.getUsers().get(position));
    }

    @Override
    public void onAcceptClick(int position) {
        acceptUser(this.holder.getUsers().get(position));
        holder.updateBadge();
    }

    @Override
    public void onMessageClick(int position) {
        Bundle args = new Bundle();
        args.putSerializable("user", this.holder.getUser());

        UserDisplay[] others = {this.holder.getUsers().get(position)};
        args.putSerializable("other_users", others);

        //TODO handle activity fail
        Intent i = new Intent(this.getContext(), CreateMessageActivity.class);
        i.putExtras(args);
        startActivity(i);

    }

    @Override
    public void onUserItemClick(int position) {
        Bundle args = new Bundle();
        args.putSerializable("user", this.holder.getUser());
        args.putSerializable("other_user_id", this.holder.getUsers().get(position).get_id());

        //TODO handle activity fail
        Intent i = new Intent(this.getContext(), ViewUserProfileActivity.class);
        i.putExtras(args);
        startActivity(i);
    }


    @Override
    public Bitmap getUserProfile(String profile_pic_id) {
        return this.holder.getProfilePicBitmap(profile_pic_id);
    }
}
