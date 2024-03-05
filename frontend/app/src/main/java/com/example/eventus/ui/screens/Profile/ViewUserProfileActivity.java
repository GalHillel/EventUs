package com.example.eventus.ui.screens.Profile;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventus.R;
import com.example.eventus.data.Database;
import com.example.eventus.data.ServerSideException;
import com.example.eventus.data.model.LoggedInUser;
import com.example.eventus.data.model.UserDisplay;
import com.example.eventus.data.model.UserEventDisplay;
import com.example.eventus.data.model.UserProfile;
import com.example.eventus.ui.screens.Messages.CreateMessageActivity;
import com.example.eventus.ui.screens.UserEvents.ViewEventsActivity;

import java.util.List;

public class ViewUserProfileActivity extends AppCompatActivity {
    LoggedInUser user;
    UserProfile profile;

    UserEventDisplay[] eventList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_profile);

        if (getIntent() == null) {
            Intent res = new Intent();
            res.putExtra("message", "null intent given");
            setResult(Activity.RESULT_CANCELED, res);
            this.finish();
            return;
        }

        Intent intent = getIntent();

        if (intent.getExtras() == null) {
            Intent res = new Intent();
            res.putExtra("message", "intent missing bundle");
            setResult(Activity.RESULT_CANCELED, res);
            this.finish();
            return;
        }

        Bundle args = intent.getExtras();

        if (!args.containsKey("user") || !args.containsKey("other_user_id")) {
            Intent res = new Intent();
            res.putExtra("message", "args missing 'user' or 'other_user_id' fields");
            setResult(Activity.RESULT_CANCELED, res);
            this.finish();
            return;
        }
        this.user = (LoggedInUser) args.getSerializable("user");
        String _id  = args.getString("other_user_id");

        try {
            this.profile = Database.userProfile(_id);
        } catch (ServerSideException e) {
            Intent res = new Intent();
            res.putExtra("message",e.getMessage());
            setResult(e.getReturnCode(),res);
            this.finish();
            return;
        } catch (Exception e) {
            Intent res = new Intent();
            res.putExtra("message",e.getMessage());
            setResult(Activity.RESULT_CANCELED,res);
            this.finish();
            return;
        }


        ImageButton backButton = findViewById(R.id.backButton);
        Button messageButton = findViewById(R.id.sendMessageButton);
        Button viewPastUserEventsButton = findViewById(R.id.viewPastUserEventsButton);
        Button viewUpcomingUserEventsButton = findViewById(R.id.viewUpcomingUserEventsButton);

        backButton.setOnClickListener(this::backButtonClick);
        messageButton.setOnClickListener(this::onSendMessageButtonClick);
        viewPastUserEventsButton.setOnClickListener(this::onViewPastEventsClick);
        viewUpcomingUserEventsButton.setOnClickListener(this::onViewUpcomingEventsClick);

        if(!profile.getUser_type().equals("Organizer")){
            viewPastUserEventsButton.setVisibility(View.GONE);
            viewUpcomingUserEventsButton.setVisibility(View.GONE);
        }

        BaseUserProfileFragment userProfileFragment = new BaseUserProfileFragment(profile);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_base_user_profile, userProfileFragment)
                .commit();

    }




    public void backButtonClick(View view) {
        // Navigate back
        this.setResult(Activity.RESULT_OK);
        this.finish();
    }
    public void success(){
        this.setResult(Activity.RESULT_OK);
        this.finish();
    }
    private void onSendMessageButtonClick(View view) {
        Bundle args = new Bundle();
        args.putSerializable("user", this.user);

        UserDisplay[] others = {this.profile};
        args.putSerializable("other_users", others);

        //TODO handle activity fail
        Intent i = new Intent(view.getContext(), CreateMessageActivity.class);
        i.putExtras(args);
        startActivity(i);
    }
    private void onViewUpcomingEventsClick(View view) {
        viewEvents(view,"upcoming");
    }

    private void onViewPastEventsClick(View view) {
        viewEvents(view,"past");
    }


    private void viewEvents(View view, String mode) {
        Bundle args = new Bundle();
        args.putSerializable("user", this.user);
        args.putSerializable("other_user_profile",this.profile);
        args.putSerializable("eventArr",this.eventList);
        args.putSerializable("mode",mode);

        //TODO handle activity fail
        Intent i = new Intent(view.getContext(), ViewEventsActivity.class);
        i.putExtras(args);
        startActivity(i);
    }




}
