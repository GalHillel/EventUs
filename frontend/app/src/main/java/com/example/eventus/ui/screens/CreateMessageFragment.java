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
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.eventus.R;
import com.example.eventus.data.Database;
import com.example.eventus.data.ServerSideException;
import com.example.eventus.data.model.UserDisplay;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CreateMessageFragment extends Fragment {

    private TextView recipientsText;
    private EditText subjectEditText;
    private EditText messageEditText;
    private Button sendButton;
    private ImageButton backButton;
    private UserDisplay user;
    private UserDisplay[] other_users;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recipientsText = view.findViewById(R.id.recipients);
        subjectEditText = view.findViewById(R.id.subjectEditText);
        messageEditText = view.findViewById(R.id.messageEditText);
        sendButton = view.findViewById(R.id.sendButton);
        backButton = view.findViewById(R.id.backButton);

        sendButton.setOnClickListener(this::onSendButtonClick);

        backButton.setOnClickListener(this::onBackButtonClick);
        String defaultTitle = "";
        if (getArguments() != null) {
            this.user = (UserDisplay) getArguments().getSerializable("user");
            this.other_users = (UserDisplay[]) getArguments().getSerializable("other_users");
            defaultTitle = getArguments().getString("title","");
            String usersNames = Arrays.stream(other_users)
                    .map(UserDisplay::getName)
                    .collect(Collectors.joining(", "));
            this.recipientsText.setText(usersNames);
        }
        if(defaultTitle.length() != 0){
            subjectEditText.setText(defaultTitle);
            subjectEditText.setFocusable(false);
        }
    }

    void onSendButtonClick(View view){
        if(this.subjectEditText.getText().toString().length()>0 && this.messageEditText.getText().toString().length()>0){
            try{
                List<String> ids = Arrays.stream(other_users).map(UserDisplay::get_id).collect(Collectors.toList());
                Database.sendMessage(user.get_id(),ids,this.subjectEditText.getText().toString(),this.messageEditText.getText().toString());
                onBackButtonClick(view);//TODO fix this part, after sending a message, user doesnt get moved back
            } catch (ServerSideException e) {
                // Handle the exception (e.g., show an error message)
                e.printStackTrace();
            } catch (Exception e) {
                // Handle other exceptions
                e.printStackTrace();
            }
        }

    }

    public void onBackButtonClick(View view) {
        // Navigate back
        getParentFragmentManager().popBackStack();
    }
}
