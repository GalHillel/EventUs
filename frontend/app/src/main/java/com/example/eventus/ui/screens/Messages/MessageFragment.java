package com.example.eventus.ui.screens.Messages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.eventus.R;
import com.example.eventus.data.model.UserDisplay;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

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
        MaterialAutoCompleteTextView contentTextView = view.findViewById(R.id.messageContentTextView);
        titleTextView.setText(this.holder.getMessage().getTitle());
        senderTextView.setText("From: " + this.holder.getSender().getName());
        contentTextView.setText(this.holder.getMessage().getContent());


        Button replyButton = view.findViewById(R.id.replyButton);
        replyButton.setOnClickListener(this::onReplyButtonClick);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == R.id.activity_create_messages) {
            if (resultCode == Activity.RESULT_OK) {
                this.holder.success();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(requireContext(), data.getStringExtra("error"), Toast.LENGTH_LONG).show();
            }
        }
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
        startActivityForResult(i, R.id.activity_create_messages);

    }


}

