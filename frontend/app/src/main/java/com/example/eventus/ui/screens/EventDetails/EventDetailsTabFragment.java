package com.example.eventus.ui.screens.EventDetails;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import com.example.eventus.ui.screens.Messages.CreateMessageActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class EventDetailsTabFragment extends Fragment {

    private Button editEventButton, saveEventButton, pickDateButton, contactUserButton;
    private Calendar calendar;
    private EditText eventNameView, eventDateView, eventLocationview, eventDescription;

    EventDetailsActivity holder;

    public EventDetailsTabFragment(EventDetailsActivity myContext) {
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

        View view = inflater.inflate(R.layout.fragment_event_details_tab, container, false);

        // Inflate the layout for this fragment
        return view;
    }
    /*
        TODO:
            1. Add the option for an Organizer to send a message to all users in event - DONE
            2. Add the option to rate an event and implement the rating algorithm
     */
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventNameView = view.findViewById(R.id.eventNameTextView);
        eventDateView = view.findViewById(R.id.eventDateTextView);
        eventLocationview = view.findViewById(R.id.eventLocationTextView);
        eventDescription = view.findViewById(R.id.eventDescriptionTextView);
        //eventDateView = view.findViewById(R.id.eventDateTextView);
        this.calendar = Calendar.getInstance();
        this.editEventButton = view.findViewById(R.id.editEventButton);
        this.saveEventButton = view.findViewById(R.id.saveEventButton);
        this.pickDateButton = view.findViewById(R.id.pickDateButton);
        this.contactUserButton = view.findViewById(R.id.contanctUserButton);

        if("Organizer".equals(this.holder.getUser().getUser_type())){
            this.contactUserButton.setText("Contact all participants");
            this.contactUserButton.setOnClickListener(this::onContactAllParticipantsClick);
        }
        else{
            this.contactUserButton.setOnClickListener(this::onContactOrganizerClick);
        }

        this.pickDateButton.setOnClickListener(this::onPickDateClick);
        this.editEventButton.setOnClickListener(this::onEditEventClick);
        this.saveEventButton.setOnClickListener(this::onSaveEventClick);

        updateFields();

        toggleEditableMode(false);



    }

    private void updateFields() {
        eventNameView.setText(this.holder.getEvent().getName());
        eventDateView.setText(this.holder.getEvent().getDate().toString());
        eventLocationview.setText(this.holder.getEvent().getLocation());
        eventDescription.setText(this.holder.getEvent().getDescription());
    }


    // This method will be called when the "Edit Event" button is clicked
    public void onEditEventClick(View view) {
        toggleEditableMode(true);
        pickDateButton.setVisibility(View.VISIBLE);
    }

    public void onContactAllParticipantsClick(View view) {
        Bundle args = new Bundle();
        args.putSerializable("user", this.holder.getUser());

        UserDisplay[] others = this.holder.getUsers().toArray(new UserDisplay[0]);
        args.putSerializable("other_users", others);

        //TODO handle activity fail
        Intent i = new Intent(this.getContext(), CreateMessageActivity.class);
        i.putExtras(args);
        startActivity(i);

    }
    public void onContactOrganizerClick(View view) {
        Bundle args = new Bundle();
        args.putSerializable("user", this.holder.getUser());

        Optional<UserDisplay> org;
        org = this.holder.getUsers().stream().filter(e->e.get_id().equals(this.holder.getEvent().getCreator_id())).findFirst();
        if(org.isPresent()){
            UserDisplay[] others = new UserDisplay[]{org.get()};
            args.putSerializable("other_users", others);

            //TODO handle activity fail
            Intent i = new Intent(this.getContext(), CreateMessageActivity.class);
            i.putExtras(args);
            startActivity(i);
        }


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
            Database.editEvent(this.holder.getEvent().getId(), updatedEventParams);
            toggleEditableMode(false);

            this.holder.getEvent().setName(eventNameView.getText().toString());
            this.holder.getEvent().setDate(calendar.getTime());
            this.holder.getEvent().setLocation(eventLocationview.getText().toString());
            this.holder.getEvent().setDescription(eventDescription.getText().toString());
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
            if (this.holder.getUser().get_id().equals(this.holder.getEvent().getCreator_id())) {
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
