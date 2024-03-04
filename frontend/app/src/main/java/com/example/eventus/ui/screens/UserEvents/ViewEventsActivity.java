package com.example.eventus.ui.screens.UserEvents;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventus.R;
import com.example.eventus.data.Database;
import com.example.eventus.data.ServerSideException;
import com.example.eventus.data.model.LoggedInUser;
import com.example.eventus.data.model.UserEventDisplay;
import com.example.eventus.ui.screens.UserEvents.EventListFragment;

import java.util.Arrays;
import java.util.List;

public class ViewEventsActivity extends AppCompatActivity {

    LoggedInUser user;
    List<UserEventDisplay> userEvents;

    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_events);

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
            UserEventDisplay[] tmp = Database.getEventList(user.get_id());
            userEvents = Arrays.asList(tmp);
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
        TextView title = findViewById(R.id.user_events_title);
        title.setText(this.user.getName()+"'s events");
        backButton.setOnClickListener(this::backButtonClick);

        EventListFragment userEventListFragment = new EventListFragment(this.user,this.userEvents);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_event_list, userEventListFragment)
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
}
