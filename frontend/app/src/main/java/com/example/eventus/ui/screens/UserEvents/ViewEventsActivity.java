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
import com.example.eventus.data.model.User;
import com.example.eventus.data.model.UserEventDisplay;
import com.example.eventus.data.model.UserProfile;
import com.example.eventus.ui.screens.UserEvents.EventListFragment;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ViewEventsActivity extends AppCompatActivity {

    LoggedInUser user;
    List<UserEventDisplay> userEvents;
    UserProfile userProfile;

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

        if (!args.containsKey("user") || !args.containsKey("other_user_profile")) {
            Intent res = new Intent();
            res.putExtra("message", "args missing 'user' or 'other_user_profile' fields");
            setResult(Activity.RESULT_CANCELED, res);
            this.finish();
            return;
        }

        this.user = (LoggedInUser) args.getSerializable("user");
        this.userProfile  = (UserProfile) args.getSerializable("other_user_profile");
        String mode = args.getString("mode","");

        try {
            UserEventDisplay[] tmp = Database.getEventList(userProfile.get_id());
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
        Date d = new Date();
        if(mode.equals("upcoming")){
            userEvents = userEvents.stream().filter(u->u.getDate().after(d)).collect(Collectors.toList());
        }
        else{
            userEvents = userEvents.stream().filter(u->!u.getDate().after(d)).collect(Collectors.toList());
        }
        if(mode.length()>0){
            mode = mode+" ";
        }

        ImageButton backButton = findViewById(R.id.backButton);
        TextView title = findViewById(R.id.user_events_title);
        title.setText(this.userProfile.getName()+"'s "+mode+"events");
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
