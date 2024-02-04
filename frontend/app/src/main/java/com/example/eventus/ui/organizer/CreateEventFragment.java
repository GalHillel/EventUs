package com.example.eventus.ui.organizer;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.eventus.R;
import com.example.eventus.data.Database;
import com.example.eventus.data.model.UserEvent;
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

        // Set up click listener for the create event button
        createEventButton.setOnClickListener(v -> createEvent());
    }

    private void createEvent() {
        // Retrieve data from UI fields
        String eventName = eventNameEditText.getText().toString();
        String eventDate = eventDateEditText.getText().toString();
        String eventLocation = eventLocationEditText.getText().toString();
        String eventDescription = eventDescriptionEditText.getText().toString();

        try {
            // Call the Database method to add the event
            UserEvent userEvent = Database.addEvent("65b91bfb1800bf3124779178", eventName, eventDate, eventLocation, eventDescription);

            // Convert UserEvent to UserEventDisplay for display purposes
            UserEventDisplay userEventDisplay = new UserEventDisplay(
                    userEvent.getId(),
                    userEvent.getName(),
                    userEvent.getDate(),
                    userEvent.getLocation()
            );

            // Display information about the created event using UserEventDisplay
            displayEventDetails(userEventDisplay);

        } catch (Exception e) {
            // Handle the exception, e.g., show an error message
            Toast.makeText(requireContext(), "Error creating event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void displayEventDetails(UserEventDisplay userEventDisplay) {
        // Example: Displaying event details in a Toast message
        String message = "Event created:\n" +
                "Name: " + userEventDisplay.getEventName() + "\n" +
                "Date: " + userEventDisplay.getEventDate() + "\n" +
                "Location: " + userEventDisplay.getEventLocation();

        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();

        // You can also perform other actions, such as updating UI elements or navigating to another fragment
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
