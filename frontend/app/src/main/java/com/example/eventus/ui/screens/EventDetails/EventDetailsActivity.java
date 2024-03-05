package com.example.eventus.ui.screens.EventDetails;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.eventus.R;
import com.example.eventus.data.Database;
import com.example.eventus.data.ServerSideException;
import com.example.eventus.data.model.LoggedInUser;
import com.example.eventus.data.model.UserDisplay;
import com.example.eventus.data.model.UserEvent;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventDetailsActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    LoggedInUser user;
    private UserEvent userEvent;
    private List<UserDisplay> users = new ArrayList<>();
    private boolean passed;
    BadgeDrawable badge;

    private Map<String,Bitmap> profilePic_lookup = new HashMap<>();


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
                this.user = (LoggedInUser) args.getSerializable("user");
                String eventId = args.getString("eventId", "");
                try {
                    this.userEvent = Database.loadEvent(eventId);
                    UserDisplay[] tmp = Database.getUserList(eventId);
                    this.users.clear();
                    this.users.addAll(Arrays.asList(tmp));
                    //TODO fast load profile pictures
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        Date d = new Date();
        this.passed = !userEvent.getDate().after(d);
        if(passed && !users.contains(user)){
            tabLayout.removeTabAt(1);
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
        this.badge = null;
        if(!passed && user.get_id().equals(userEvent.getCreator_id())) {
            this.badge = tabLayout.getTabAt(1).getOrCreateBadge();
            updateBadge();
        }
    }

    LoggedInUser getUser(){
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
        if(this.badge != null){
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

    public Bitmap getProfilePicBitmap(String userId) {

        if(profilePic_lookup.containsKey(userId)){

            return profilePic_lookup.get(userId);
        }
        Bitmap profile_icon = null;
        try {
            profile_icon = Database.getProfilePic(userId);
            int width = getResources().getInteger(R.integer.participant_list_profilePicSize);
            int height = getResources().getInteger(R.integer.participant_list_profilePicSize);
            profile_icon = Bitmap.createScaledBitmap(profile_icon,width,height,false);

        } catch (ServerSideException e) {
            // Handle the exception (e.g., show an error message)
            e.printStackTrace();
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
        }
        return profile_icon;
    }

    public boolean hasPassed(){return this.passed;}
}
