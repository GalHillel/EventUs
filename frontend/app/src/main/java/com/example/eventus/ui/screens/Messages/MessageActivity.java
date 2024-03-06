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
import com.example.eventus.data.Database;
import com.example.eventus.data.ServerSideException;
import com.example.eventus.data.model.UserDisplay;
import com.example.eventus.data.model.UserMessage;

public class MessageActivity extends AppCompatActivity {
    FragmentContainerView frag;
    private UserDisplay user,sender;
    private UserMessage message;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_message);
        if(getIntent() == null){
            Intent res = new Intent();
            res.putExtra("message","null intent given");
            setResult(Activity.RESULT_CANCELED,res);
            this.finish();
            return;
        }

        Intent intent = getIntent();

        if(intent.getExtras() == null){
            Intent res = new Intent();
            res.putExtra("message","intent missing bundle");
            setResult(Activity.RESULT_CANCELED,res);
            this.finish();
            return;
        }

        Bundle args = intent.getExtras();

        if(!args.containsKey("user") || !args.containsKey("message_id")){
            Intent res = new Intent();
            res.putExtra("message","args missing 'user' or 'message_id' fields");
            setResult(Activity.RESULT_CANCELED,res);
            this.finish();
            return;
        }

        this.user = (UserDisplay) args.getSerializable("user");
        String message_id = args.getString("message_id");

        try {
            this.message = Database.loadMessage(message_id, this.user.get_id());
            this.sender = Database.userDisplay(this.message.getSender_id());
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


}
