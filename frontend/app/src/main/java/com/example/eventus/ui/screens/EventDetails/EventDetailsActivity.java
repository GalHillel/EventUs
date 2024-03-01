package com.example.eventus.ui.screens.EventDetails;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.eventus.R;
import com.example.eventus.data.Database;
import com.example.eventus.data.model.UserDisplay;
import com.example.eventus.data.model.UserEvent;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventDetailsActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    UserDisplay user;
    private UserEvent userEvent;
    private List<UserDisplay> users = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_event_details);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(this::backButtonClick);

        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        //TODO close activity on fail and return the response

        if(getIntent() != null){
            Intent intent = getIntent();
            if(intent.getExtras() != null){
                Bundle args = intent.getExtras();
                this.user = (UserDisplay) args.getSerializable("user");
                String eventId = args.getString("eventId", "");
                try {
                    this.userEvent = Database.loadEvent(eventId);
                    UserDisplay[] tmp = Database.getUserList(eventId);
                    this.users.clear();
                    this.users.addAll(Arrays.asList(tmp));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        EventDetailsPagerAdaptor myAdaptor = new EventDetailsPagerAdaptor(this,getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(myAdaptor);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    UserDisplay getUser(){
        return this.user;
    }
    UserEvent getEvent(){
        return this.userEvent;
    }
    List<UserDisplay> getUsers(){
        return this.users;
    }

    public void setUserEvent(UserEvent userEvent) {
        this.userEvent = userEvent;
    }

    public void setUsers(List<UserDisplay> users) {
        this.users = users;
    }

    public void backButtonClick(View view) {
        // Navigate back
        this.setResult(Activity.RESULT_OK);
        this.finish();

    }
}
