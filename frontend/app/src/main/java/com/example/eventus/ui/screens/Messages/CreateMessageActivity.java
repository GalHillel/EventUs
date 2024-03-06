package com.example.eventus.ui.screens.Messages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import com.example.eventus.R;
import com.example.eventus.data.BaseActivity;
import com.example.eventus.data.Database;
import com.example.eventus.data.ServerSideException;
import com.example.eventus.data.model.LoggedInUser;
import com.example.eventus.data.model.UserDisplay;
import com.example.eventus.data.model.UserMessage;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CreateMessageActivity extends BaseActivity {

    private UserDisplay[] other_users;

    private String defaultTitle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_message);
        this.user = (LoggedInUser) args.getSerializable("user");
        this.other_users = (UserDisplay[]) args.getSerializable("other_users");
        this.defaultTitle = args.getString("title", "");

        CreateMessageFragment createMessageFragment = new CreateMessageFragment(this);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_create_message, createMessageFragment)
                .commit();

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(this::backButtonClick);


    }

    public UserDisplay getUser() {
        return user;
    }

    public UserDisplay[] getOtherUsers() {
        return other_users;
    }

    public String getDefaultTitle() {
        return this.defaultTitle;
    }
    public Intent sendMessage(String title, String content){
        Intent res = new Intent();
        try {
            List<String> ids = Arrays.stream(this.other_users).map(UserDisplay::get_id).collect(Collectors.toList());
            Database.sendMessage(this.user.get_id(), ids, title, content);
            res.putExtra("code",Activity.RESULT_OK);

        } catch (ServerSideException e) {
            res.putExtra("code",e.getReturnCode());
            res.putExtra("message",e.getMessage());


        } catch (Exception e) {
            res.putExtra("code",Activity.RESULT_CANCELED);
            res.putExtra("message",e.getMessage());
        }

        return res;
    }


    @Override
    public Set<String> getRequiredArgs() {
        return new HashSet<String>(Arrays.asList("user","other_users"));
    }

    public void backButtonClick(View view) {
        // Navigate back
        success();
    }
}

