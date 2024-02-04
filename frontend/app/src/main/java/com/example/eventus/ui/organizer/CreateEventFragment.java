package com.example.eventus.ui.organizer;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.eventus.R;
import com.example.eventus.ui.events.UserEventDisplay;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateEventFragment extends Fragment {

    private EditText eventNameEditText;
    private EditText eventDateEditText;
    private EditText eventLocationEditText;
    private EditText eventDescriptionEditText;
    private Button createEventButton;

    private Calendar calendar;

    public CreateEventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventNameEditText = view.findViewById(R.id.eventNameEditText);
        eventDateEditText = view.findViewById(R.id.eventDateEditText);
        eventLocationEditText = view.findViewById(R.id.eventLocationEditText);
        eventDescriptionEditText = view.findViewById(R.id.eventDescriptionEditText);
        createEventButton = view.findViewById(R.id.createEventButton);

        // Set up click listeners for buttons
        view.findViewById(R.id.messages).setOnClickListener(v -> {
            // Navigate to OrganizerMessagesFragment
            Navigation.findNavController(v).navigate(R.id.action_createEventFragment_to_organizerMessages);
        });

        view.findViewById(R.id.profile).setOnClickListener(v -> {
            // Navigate to OrganizerProfileFragment
            Navigation.findNavController(v).navigate(R.id.action_createEventFragment_to_organizerProfileFragment);
        });

        view.findViewById(R.id.myevents).setOnClickListener(v -> {
            // Navigate to OrganizerProfileFragment
            Navigation.findNavController(v).navigate(R.id.action_createEventFragment_to_organizerEvents);
        });

        // Initialize Calendar
        calendar = Calendar.getInstance();

        view.findViewById(R.id.pickDateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
    }

    private void createEvent() {
        // Retrieve data from UI fields
        String eventName = eventNameEditText.getText().toString();
        String eventDate = eventDateEditText.getText().toString();
        String eventLocation = eventLocationEditText.getText().toString();
        String eventDescription = eventDescriptionEditText.getText().toString();

        // Create an EventModel object with the entered data
        UserEventDisplay newEvent = new UserEventDisplay(eventName, eventDate, eventLocation, eventDescription);

        // TODO: Add logic to save the new event to the database or perform any other necessary actions
    }

    public void showDatePickerDialog(View view) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                updateDateEditText();
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

    private void updateDateEditText() {
        String dateFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        eventDateEditText.setText(sdf.format(calendar.getTime()));
    }
}
