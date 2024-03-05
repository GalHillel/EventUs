package com.example.eventus.ui.screens.UserMainScreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventus.R;
import com.example.eventus.data.model.LoggedInUser;
import com.example.eventus.ui.screens.Messages.UserMessagesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class UserMainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener{

    LoggedInUser user;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main_menu);
        BottomNavigationView navigationView = findViewById(R.id.mainMenuUserNavigation);
        navigationView.setOnItemSelectedListener(this);
        navigationView.setSelectedItemId(R.id.myevents);


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

        if (!args.containsKey("user")) {
            Intent res = new Intent();
            res.putExtra("message", "args missing 'user' field");
            setResult(Activity.RESULT_CANCELED, res);
            this.finish();
            return;
        }
        this.user = (LoggedInUser) args.getSerializable("user");

        if (user != null && user.getUser_type().equals("Organizer")) {

            navigationView.getMenu().findItem(R.id.discover).setVisible(false);
        } else {
            navigationView.getMenu().findItem(R.id.newEvent).setVisible(false);
        }

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.newEvent) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.userMainMenuFrame, new CreateEventFragment(this.user))
                    .commit();
            return true;
        } else if (itemId == R.id.discover) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.userMainMenuFrame, new UserDiscoverFragment(this.user))
                    .commit();
            return true;
        } else if (itemId == R.id.messages) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.userMainMenuFrame, new UserMessagesFragment(this.user))
                    .commit();
            return true;
        } else if (itemId == R.id.myevents) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.userMainMenuFrame, new UserEventsFragment(this.user))
                    .commit();
            return true;
        } else if (itemId == R.id.profile) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.userMainMenuFrame, new UserProfileFragment(this.user))
                    .commit();
            return true;
        }
        return false;
    }
}
