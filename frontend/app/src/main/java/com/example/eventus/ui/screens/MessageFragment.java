package com.example.eventus.ui.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
//import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.eventus.R;
import com.example.eventus.data.Database;
import com.example.eventus.data.ServerSideException;
import com.example.eventus.data.model.UserDisplay;
import com.example.eventus.data.model.UserMessage;

public class MessageFragment extends Fragment {

    //private EditText replyEditText;
    private UserDisplay user;
    private UserDisplay sender;

    private UserMessage message;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView titleTextView = view.findViewById(R.id.messageTitleTextView);
        TextView senderTextView = view.findViewById(R.id.messageSenderTextView);
        TextView contentTextView = view.findViewById(R.id.messageContentTextView);

        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(this::onBackButtonClicked);

        Button replyButton = view.findViewById(R.id.replyButton);
        replyButton.setOnClickListener(this::onReplyButtonClick);

        if (getArguments() != null) {
            this.user = (UserDisplay) getArguments().getSerializable("user");
            String message_id = getArguments().getString("message_id");
            String sender_id = getArguments().getString("sender_id");

            try {
                this.message = Database.loadMessage(message_id, user.get_id());
                this.sender = Database.userDisplay(sender_id);
                titleTextView.setText(message.getTitle());
                senderTextView.setText(sender.getName());
                contentTextView.setText(message.getContent());

            } catch (ServerSideException e) {
                // Handle the exception (e.g., show an error message)
                e.printStackTrace();
            } catch (Exception e) {
                // Handle other exceptions
                e.printStackTrace();
            }

        }
    }

    public void onBackButtonClicked(View view) {
        // Navigate back
        getParentFragmentManager().popBackStack();
    }

    public void onReplyButtonClick(View view) {
        Bundle args = new Bundle();
        args.putSerializable("user", this.user);
        UserDisplay[] others = {sender};
        args.putSerializable("other_users", others);
        args.putString("title", "re: " + this.message.getTitle());
        NavHostFragment.findNavController(MessageFragment.this)
                .navigate(R.id.createMessageFragment, args);
    }

}

