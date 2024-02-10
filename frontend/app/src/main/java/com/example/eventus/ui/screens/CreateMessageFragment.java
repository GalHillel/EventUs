package com.example.eventus.ui.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.eventus.R;

public class CreateMessageFragment extends Fragment {

    private EditText recipientEditText;
    private EditText subjectEditText;
    private EditText messageEditText;
    private Button sendButton;
    private ImageButton backButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recipientEditText = view.findViewById(R.id.recipientEditText);
        subjectEditText = view.findViewById(R.id.subjectEditText);
        messageEditText = view.findViewById(R.id.messageEditText);
        sendButton = view.findViewById(R.id.sendButton);
        backButton = view.findViewById(R.id.backButton);

        sendButton.setOnClickListener(v -> {
            // TODO: Implement send functionality here
        });

        backButton.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
    }
}
