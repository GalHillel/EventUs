package com.example.eventus.ui.screens.Messages;

import android.content.Intent;
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
import com.example.eventus.ui.screens.EventDetails.EventDetailsActivity;

public class MessageFragment extends Fragment {

    //private EditText replyEditText;
    MessageActivity holder;

    public MessageFragment(MessageActivity initiator) {
        this.holder = initiator;
    }


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

        titleTextView.setText(this.holder.getMessage().getTitle());
        senderTextView.setText(this.holder.getSender().getName());
        contentTextView.setText(this.holder.getMessage().getContent());

        Button replyButton = view.findViewById(R.id.replyButton);
        replyButton.setOnClickListener(this::onReplyButtonClick);

//        ImageButton backButton = view.findViewById(R.id.backButton);
//        backButton.setOnClickListener(v -> {
//            this.holder.backButtonClick(v);
//        });
    }
    public void onReplyButtonClick(View view) {
        Bundle args = new Bundle();
        args.putSerializable("user", this.holder.getUser());
        UserDisplay[] others = {this.holder.getSender()};
        args.putSerializable("other_users", others);
        args.putString("title", "re: " + this.holder.getMessage().getTitle());

        //TODO handle activity fail
        Intent i = new Intent(this.getContext(), CreateMessageActivity.class);
        i.putExtras(args);
        startActivity(i);

    }



}

