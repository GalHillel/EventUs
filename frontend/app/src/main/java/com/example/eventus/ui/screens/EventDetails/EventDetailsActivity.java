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
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class EventDetailsActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    UserDisplay user;
    private UserEvent userEvent;
    private List<UserDisplay> users = new ArrayList<>();
    BadgeDrawable badge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

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

        this.users.sort(Comparator.comparing(UserDisplay::getUser_type));

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
        this.badge = tabLayout.getTabAt(1).getOrCreateBadge();
        updateBadge();
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
    public void success(){
        this.setResult(Activity.RESULT_OK);
        this.finish();
    }

    public void updateBadge() {
        int badgeNum = 0;
        if(this.userEvent.getIsPrivate()){
            badgeNum = (int) this.userEvent.getAttendents().entrySet().stream().filter(Map.Entry::getValue).count();
        }

        if(badgeNum > 0) {
            badge.setVisible(true);
            badge.setNumber(badgeNum);
            return;
        }
        badge.setNumber(0);
        badge.setVisible(false);

    }
}
