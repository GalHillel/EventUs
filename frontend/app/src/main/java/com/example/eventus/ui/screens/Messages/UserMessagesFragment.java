package com.example.eventus.ui.screens.Messages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventus.R;
import com.example.eventus.data.Database;
import com.example.eventus.data.ServerSideException;
import com.example.eventus.data.model.LoggedInUser;
import com.example.eventus.data.model.UserDisplay;
import com.example.eventus.data.model.UserMessageDisplay;
import com.example.eventus.ui.recycleViews.MessageAdaptor;
import com.example.eventus.ui.screens.EventDetails.EventDetailsActivity;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class UserMessagesFragment extends Fragment implements MessageAdaptor.onMessageClickListener {
    private LoggedInUser user;
    private final List<UserMessageDisplay> messageList = new ArrayList<>();

    public UserMessagesFragment() {
        // Required empty public constructor
        this.user = null;
    }

    public UserMessagesFragment(LoggedInUser user){
        this.user = user;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_messages, container, false);
    }
    /*
        TODO:
            1. Make this page much prettier
     */
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        BottomNavigationView bottomNavigationView = view.findViewById(R.id.navigation);
//        Menu navMenu = bottomNavigationView.getMenu();

        if (user == null && getArguments() != null) {
            this.user = (LoggedInUser) getArguments().getSerializable("user");

//            if (user != null && user.getUser_type().equals("Organizer")) {
//                navMenu.findItem(R.id.discover).setVisible(false);
//            } else {
//                navMenu.findItem(R.id.newEvent).setVisible(false);
//            }

        }

//        // Set up click listeners for buttons
//        view.findViewById(R.id.discover).setOnClickListener(v ->
//                Navigation.findNavController(v).navigate(R.id.action_userMessagesFragment_to_userDiscoverFragment, createNavigationBundle()));
//
//        view.findViewById(R.id.profile).setOnClickListener(v ->
//                Navigation.findNavController(v).navigate(R.id.action_userMessagesFragment_to_userProfileFragment, createNavigationBundle()));
//
//        view.findViewById(R.id.myevents).setOnClickListener(v ->
//                Navigation.findNavController(v).navigate(R.id.action_userMessagesFragment_to_userEventsFragment, createNavigationBundle()));
//
//        view.findViewById(R.id.newEvent).setOnClickListener(v ->
//                Navigation.findNavController(v).navigate(R.id.action_userMessagesFragment_to_createEventFragment, createNavigationBundle()));


        try {

            UserMessageDisplay[] newList = Database.getMessageInbox(this.user.get_id());
            Map<String, Boolean> inboxRead = Database.getMessageInboxStatus(this.user.get_id());
            messageList.clear();
            Collections.addAll(messageList, newList);

            Collections.sort(messageList, (message1, message2) -> {
                if (Boolean.TRUE.equals(inboxRead.getOrDefault(message1.get_id(), false)) && Boolean.FALSE.equals(inboxRead.getOrDefault(message2.get_id(), false))) {
                    return 1;
                } else if (Boolean.FALSE.equals(inboxRead.getOrDefault(message1.get_id(), false)) && Boolean.TRUE.equals(inboxRead.getOrDefault(message2.get_id(), false))) {
                    return -1;
                } else {
                    return message2.getDate_sent().compareTo(message1.getDate_sent());
                }
            });

            RecyclerView messagesRecyclerView = view.findViewById(R.id.userMessagesList);
            MessageAdaptor messageAdaptor = new MessageAdaptor(messageList, inboxRead);
            messageAdaptor.setOnMessageClickListener(this);
            messagesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            messagesRecyclerView.setAdapter(messageAdaptor);

        } catch (ServerSideException e) {
            // Handle the exception (e.g., show an error message)
            e.printStackTrace();
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
        }

    }


    // Method to create a common bundle for navigation
    private Bundle createNavigationBundle() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        return bundle;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == R.id.activity_user_message){
            if(resultCode == Activity.RESULT_OK){
                //TODO handle message read
                Toast.makeText(requireContext(), "returned from read message", Toast.LENGTH_SHORT).show();

                //Log.w("UserMessageFragment", "intent keys: "+data.getExtras().keySet().toString());

            }
        }
    }

    @Override
    public void onMessageClick(int position) {

        UserMessageDisplay messageClicked = messageList.get(position);
        Bundle args = createNavigationBundle();
        args.putString("message_id", messageClicked.get_id());

        //TODO handle activity fail
        Intent i = new Intent(this.getContext(), MessageActivity.class);
        i.putExtras(args);
        startActivityForResult(i,R.id.activity_user_message);
    }
}
