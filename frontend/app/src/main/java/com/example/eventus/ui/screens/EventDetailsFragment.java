package com.example.eventus.ui.screens;

import android.app.DatePickerDialog;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class EventDetailsFragment extends Fragment implements UserAdapter.ButtonListener {
    private UserEvent userEvent;
    private Button editEventButton, saveEventButton, pickDateButton;

    private Calendar calendar;
    private final List<UserDisplay> users = new ArrayList<>();

    private UserDisplay currentUser;

    private UserAdapter userAdapter;

    private EditText eventNameView, eventDateView, eventLocationview, eventDescription;
    private Button exitEventButton, joinEventButton;

    private CardView descriptionCard, participantsCard;
    private RecyclerView userListRecyclerView;

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
        backButton.setOnClickListener(v -> {
            // Navigate back to the previous fragment
            getParentFragmentManager().popBackStack();
        });

        // Inflate the layout for this fragment
        return view;
    }
    /*
        TODO:
            1. Add the option for an Organizer to accept a new user
            2. Add the option for an Organizer to send a message to all users in event
            3. Add the option to rate an event and implement the rating algorithm
            4. (optional) show profile pictures of users
            5. Move Organizer to the top of the list and show that he is the Organizer
     */
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventNameView = view.findViewById(R.id.eventNameTextView);
        eventDateView = view.findViewById(R.id.eventDateTextView);
        eventLocationview = view.findViewById(R.id.eventLocationTextView);
        eventDescription = view.findViewById(R.id.eventDescriptionTextView);
        this.joinEventButton = view.findViewById(R.id.joinEventButton);
        this.exitEventButton = view.findViewById(R.id.leaveEventButton);
        //eventDateView = view.findViewById(R.id.eventDateTextView);
        this.calendar = Calendar.getInstance();
        this.editEventButton = view.findViewById(R.id.editEventButton);
        this.saveEventButton = view.findViewById(R.id.saveEventButton);
        this.pickDateButton = view.findViewById(R.id.pickDateButton);
        this.descriptionCard = view.findViewById(R.id.descriptionCard);
        this.participantsCard = view.findViewById(R.id.participantCard);
        this.userListRecyclerView = view.findViewById(R.id.eventListRecycleView);

        this.pickDateButton.setOnClickListener(this::onPickDateClick);
        this.joinEventButton.setOnClickListener(this::onJoinEventClick);
        this.exitEventButton.setOnClickListener(this::onLeaveEventClick);
        this.editEventButton.setOnClickListener(this::onEditEventClick);
        this.saveEventButton.setOnClickListener(this::onSaveEventClick);
        this.descriptionCard.setOnClickListener(this::onDescriptionCardClick);
        this.participantsCard.setOnClickListener(this::onParticipantsCardClick);




        if (getArguments() != null) {

            this.currentUser = (UserDisplay) getArguments().getSerializable("user");
            String eventId = getArguments().getString("eventId", "");
            try {
                userEvent = Database.loadEvent(eventId);
                UserDisplay[] tmp = Database.getUserList(eventId);
                this.users.clear();
                this.users.addAll(Arrays.asList(tmp));


                updateFields();


                userAdapter = new UserAdapter(this.users, (this.currentUser.get_id().equals(this.userEvent.getCreator_id())) ? "Organizer" : "Participant");
                userAdapter.setOnKickClickListener(this);
                userAdapter.setOnMessageClickListener(this);
                userAdapter.setOnUserItemClickListener(this);
                userListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                userListRecyclerView.setAdapter(userAdapter);
                if (this.currentUser.get_id().equals(this.userEvent.getCreator_id())) {
                    this.exitEventButton.setText("Delete Event");

                }
                toggleEditableMode(false);
                if (this.userEvent.getAttendents().containsKey(this.currentUser.get_id())) {
                    this.joinEventButton.setVisibility(View.GONE);
                    this.exitEventButton.setVisibility(View.VISIBLE);
                } else {
                    this.exitEventButton.setVisibility(View.GONE);
                    this.joinEventButton.setVisibility(View.VISIBLE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.userListRecyclerView.setVisibility(View.GONE);
        this.eventDescription.setVisibility(View.GONE);

    }

    private void onDescriptionCardClick(View view) {
        if(this.userListRecyclerView.getVisibility() == View.VISIBLE){
            this.userListRecyclerView.setVisibility(View.GONE);
        }

        if(this.eventDescription.getVisibility()==View.VISIBLE){
            this.eventDescription.setVisibility(View.GONE);
            return;
        }
        this.eventDescription.setVisibility(View.VISIBLE);

    }

    private void onParticipantsCardClick(View view) {
        if(this.eventDescription.getVisibility() == View.VISIBLE){
            this.eventDescription.setVisibility(View.GONE);
        }
        if(this.userListRecyclerView.getVisibility()==View.VISIBLE){
            this.userListRecyclerView.setVisibility(View.GONE);
            return;
        }
        this.userListRecyclerView.setVisibility(View.VISIBLE);

    }

    private void updateFields() {
        eventNameView.setText(this.userEvent.getName());
        eventDateView.setText(this.userEvent.getDate().toString());
        eventLocationview.setText(this.userEvent.getLocation());
        eventDescription.setText(this.userEvent.getDescription());
    }

    public void onBackButtonClick(View view) {
        // Navigate back
        getParentFragmentManager().popBackStack();
    }

    // This method will be called when the "Join Event" button is clicked
    public void onJoinEventClick(View view) {
        try {
            Database.joinEvent(this.currentUser.get_id(), this.userEvent.getId());
            this.joinEventButton.setVisibility(View.GONE);
            this.exitEventButton.setVisibility(View.VISIBLE);
            this.users.add(new UserDisplay(currentUser.get_id(), currentUser.getName(), (this.currentUser.get_id().equals(this.userEvent.getCreator_id())) ? "Organizer" : "Participant"));
            this.userAdapter.notifyItemInserted(this.users.size() - 1);
        } catch (Exception e) {
            //handle
        }
    }

    // This method will be called when the "Leave Event" button is clicked
    public void onLeaveEventClick(View view) {
        if (this.currentUser.get_id().equals(this.userEvent.getCreator_id())) {
            try {
                Database.delEvent(this.userEvent.getId());
                onBackButtonClick(this.getView());
            } catch (Exception e) {
                //handle
            }
        } else {
            UserDisplay u = new UserDisplay(currentUser.get_id(), currentUser.getName(), (this.currentUser.get_id().equals(this.userEvent.getCreator_id())) ? "Organizer" : "Participant");
            removeUser(u);
            this.exitEventButton.setVisibility(View.GONE);
            this.joinEventButton.setVisibility(View.VISIBLE);
        }
    }

    private void removeUser(UserDisplay user) {
        try {
            Database.exitEvent(user.get_id(), this.userEvent.getId());
            int idx = this.users.indexOf(user);
            this.users.remove(idx);
            this.userAdapter.notifyItemRemoved(idx);
        } catch (Exception e) {
            //handle
        }
    }

    @Override
    public void onKickClick(int position) {
        removeUser(this.users.get(position));
    }

    @Override
    public void onMessageClick(int position) {
        Bundle args = new Bundle();
        args.putSerializable("user", this.currentUser);

        UserDisplay[] others = {this.users.get(position)};
        args.putSerializable("other_users", others);

        NavHostFragment.findNavController(EventDetailsFragment.this)
                .navigate(R.id.createMessageFragment, args);
    }

    @Override
    public void onUserItemClick(int position) {
        Bundle args = new Bundle();
        args.putSerializable("user", this.currentUser);
        args.putSerializable("other_user", this.users.get(position));
        NavHostFragment.findNavController(EventDetailsFragment.this)
                .navigate(R.id.userProfileFragment, args);
    }

    // This method will be called when the "Edit Event" button is clicked
    public void onEditEventClick(View view) {
        toggleEditableMode(true);
        pickDateButton.setVisibility(View.VISIBLE);
    }

    // This method will be called when the "Save Event" button is clicked
    public void onSaveEventClick(View view) {
        // Get the updated event details and call the editEvent function
        HashMap<String, Object> updatedEventParams = new HashMap<>();
        updatedEventParams.put("name", eventNameView.getText().toString());
        updatedEventParams.put("date", eventDateView.getText().toString());
        updatedEventParams.put("location", eventLocationview.getText().toString());
        updatedEventParams.put("description", eventDescription.getText().toString());

        try {
            Database.editEvent(userEvent.getId(), updatedEventParams);
            toggleEditableMode(false);

            this.userEvent.setName(eventNameView.getText().toString());
            this.userEvent.setDate(calendar.getTime());
            this.userEvent.setLocation(eventLocationview.getText().toString());
            this.userEvent.setDescription(eventDescription.getText().toString());
            updateFields();

        } catch (Exception e) {
            // Handle exception
        }


    }

    private void toggleEditableMode(boolean isEdit) {

        // Enable or disable editing of TextViews based on the editable flag
        setAsTextView(eventNameView, isEdit);
        setAsTextView(eventDateView, isEdit);
        setAsTextView(eventLocationview, isEdit);
        setAsTextView(eventDescription, isEdit);
        // Show or hide the buttons based on the editable flag
        if (isEdit) {
            editEventButton.setVisibility(View.GONE);
            saveEventButton.setVisibility(View.VISIBLE);
            pickDateButton.setVisibility(View.VISIBLE);
        } else {
            //enable the edit button only for creator
            if (this.currentUser.get_id().equals(this.userEvent.getCreator_id())) {
                editEventButton.setVisibility(View.VISIBLE);
            } else {
                editEventButton.setVisibility(View.GONE);
            }
            saveEventButton.setVisibility(View.GONE);
            pickDateButton.setVisibility(View.GONE);
        }


    }

    private void setAsTextView(EditText tv, boolean flg) {
        if (flg) {
            tv.setInputType(EditorInfo.TYPE_CLASS_TEXT);
            tv.setBackground(new AppCompatEditText(tv.getContext()).getBackground());
        } else {
            tv.setInputType(EditorInfo.TYPE_NULL);
            tv.setBackground(null);
        }

    }

    public void onPickDateClick(View view) {
        showDatePickerDialog(view);
    }

    public void showDatePickerDialog(View view) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                updateDateTextView();
            }
        };

        new DatePickerDialog(
                requireContext(),
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void updateDateTextView() {
        String dateFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        eventDateView.setText(calendar.getTime().toString());
    }


}