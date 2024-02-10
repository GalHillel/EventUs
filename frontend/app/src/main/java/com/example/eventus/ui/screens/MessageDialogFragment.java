package com.example.eventus.ui.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.Navigation;

import com.example.eventus.R;

public class MessageDialogFragment extends DialogFragment {

    private TextView titleTextView;
    private TextView senderTextView;
    private TextView contentTextView;
    private EditText replyEditText;
    private Button sendButton;
    private ImageButton backButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titleTextView = view.findViewById(R.id.messageTitleTextView);
        senderTextView = view.findViewById(R.id.messageSenderTextView);
        contentTextView = view.findViewById(R.id.messageContentTextView);
        replyEditText = view.findViewById(R.id.replyEditText);
        sendButton = view.findViewById(R.id.sendButton);
        backButton = view.findViewById(R.id.backButton);

        // TODO: Change with actual data
        titleTextView.setText("Message Title");
        senderTextView.setText("From: Sender");
        contentTextView.setText("Message Content");

        sendButton.setOnClickListener(v -> {
            // TODO: Implement send functionality here
            dismiss();
        });

        backButton.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

    }
}

