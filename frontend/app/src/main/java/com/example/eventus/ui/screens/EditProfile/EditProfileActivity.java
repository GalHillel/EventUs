package com.example.eventus.ui.screens.EditProfile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventus.R;
import com.example.eventus.data.Database;
import com.example.eventus.data.model.LoggedInUser;
import com.example.eventus.ui.screens.Profile.BaseUserProfileFragment;

import java.util.HashMap;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {
    LoggedInUser user;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

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

        if (!args.containsKey("user")){
            Intent res = new Intent();
            res.putExtra("message", "args missing 'user' field");
            setResult(Activity.RESULT_CANCELED, res);
            this.finish();
            return;
        }
        this.user = (LoggedInUser) args.getSerializable("user");

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(this::backButtonClick);

        EditProfileFragment userProfileFragment = new EditProfileFragment(this);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.EditProfileFrame, userProfileFragment)
                .commit();

    }

    public LoggedInUser getUser(){
        return this.user;
    }

    public boolean updateParams(HashMap<String, Object> updatedUserParams){
        try {
            Database.editUser(this.user.get_id(), updatedUserParams);
            if (updatedUserParams.containsKey("name")) {
                this.user.setName((String) updatedUserParams.get("name"));
            }
            if (updatedUserParams.containsKey("bio")) {
                this.user.setBio((String) updatedUserParams.get("bio"));
            }
            if(updatedUserParams.containsKey("profile_pic")){
                this.user.setProfile_pic((String) updatedUserParams.get("profile_pic"));
            }
            return true;


        } catch (Exception e) {
            Log.e("Err", Objects.requireNonNull(e.getMessage()));
        }
        return false;
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
