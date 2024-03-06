package com.example.eventus.ui.screens.Messages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.eventus.R;
import com.example.eventus.data.Database;
import com.example.eventus.data.ServerSideException;
import com.example.eventus.data.model.UserDisplay;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/*TODO: Fix send message part, after sending a message, user doesn't get moved back user doesn't get moved back
       (got exception from the Database function sendMessage, but it still send the message)
*/
public class CreateMessageFragment extends Fragment {
    private EditText subjectEditText;
    private EditText messageEditText;
    private CreateMessageActivity holder;
    public CreateMessageFragment(CreateMessageActivity holder){
        this.holder = holder;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView recipientsText = view.findViewById(R.id.recipients);
        subjectEditText = view.findViewById(R.id.subjectEditText);
        messageEditText = view.findViewById(R.id.messageEditText);
        Button sendButton = view.findViewById(R.id.sendButton);
        sendButton.setOnClickListener(this::onSendButtonClick);

        if(!this.holder.getDefaultTitle().equals("")){
            subjectEditText.setText(holder.getDefaultTitle());
            subjectEditText.setFocusable(false);
        }
        String usersNames = Arrays.stream(holder.getOtherUsers())
                .map(UserDisplay::getName)
                .collect(Collectors.joining(", "));

        recipientsText.setText(usersNames);

    }

    void onSendButtonClick(View view) {
        int subjectLen = this.subjectEditText.getText().toString().length();
        int contentLen = this.messageEditText.getText().toString().length();

        //TODO handle constraints on message subject and content lengths
        if(subjectLen == 0 && contentLen == 0){
            Toast.makeText(requireContext(), "Message missing subject and content", Toast.LENGTH_SHORT).show();
            return;
        }
        if(subjectLen == 0){
            Toast.makeText(requireContext(), "Message missing subject", Toast.LENGTH_SHORT).show();
            return;
        }
        if(contentLen == 0){
            Toast.makeText(requireContext(), "Message missing content", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent res = holder.sendMessage(this.subjectEditText.getText().toString(), this.messageEditText.getText().toString());

        if(res.getIntExtra("code",Activity.RESULT_CANCELED) == Activity.RESULT_OK) {
            subjectEditText.setText("");
            messageEditText.setText("");
            // Prints success message
            Toast.makeText(requireContext(), "Message sent successfully", Toast.LENGTH_SHORT).show();
            holder.success();
        }
        else{
            //TODO handle caught exception
        }

    }

}
