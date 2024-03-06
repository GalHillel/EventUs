package com.example.eventus.ui.screens.Messages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.fragment.NavHostFragment;

import com.example.eventus.R;
import com.example.eventus.data.BaseActivity;
import com.example.eventus.data.Database;
import com.example.eventus.data.ServerSideException;
import com.example.eventus.data.model.UserDisplay;
import com.example.eventus.data.model.UserMessage;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class MessageActivity extends BaseActivity {
    FragmentContainerView frag;
    private UserDisplay user,sender;
    private UserMessage message;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_message);

        this.user = (UserDisplay) args.getSerializable("user");
        String message_id = args.getString("message_id","");
        try {
            this.message = Database.loadMessage(message_id, this.user.get_id());
            this.sender = Database.userDisplay(this.message.getSender_id());
        } catch (ServerSideException e) {
            Intent res = new Intent();
            res.putExtra("message", e.getMessage());
            setResult(e.getReturnCode(), res);
            this.finish();
            return;
        } catch (Exception e) {
            Intent res = new Intent();
            res.putExtra("message", e.getMessage());
            setResult(Activity.RESULT_CANCELED, res);
            this.finish();
            return;
        }

        MessageFragment messageFragment = new MessageFragment(this);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_message, messageFragment)
                .commit();

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(this::backButtonClick);
    }



    public UserDisplay getUser() {
        return user;
    }

    public UserDisplay getSender() {
        return sender;
    }

    public UserMessage getMessage() {
        return message;
    }

    public void backButtonClick(View view) {
        // Navigate back
        Intent i = new Intent();
        Bundle args = new Bundle();
        args.putSerializable("user",this.user);
        i.putExtra("testing",args);
        this.setResult(Activity.RESULT_OK,i);
        this.finish();
    }

    @Override
    public Set<String> getRequiredArgs() {
        return new HashSet<String>(Arrays.asList("user","message_id"));
    }


}
