package com.example.eventus.ui.recycleViews;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventus.R;
import com.example.eventus.data.model.UserDisplay;
import com.example.eventus.data.model.UserEvent;

import java.util.List;
import java.util.Map;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final List<UserDisplay> userList;
    private final String mode;
    private Map<String,Boolean> userStatus;
    private boolean isPrivate;

    private ButtonListener kickListener;
    private ButtonListener acceptListener;
    private ButtonListener messageListener;
    private ButtonListener userItemListener;

    public UserAdapter(List<UserDisplay> userList,UserEvent event, String mode) {
        this.userList = userList;
        this.mode = mode;
        this.userStatus = event.getAttendents();
        this.isPrivate = event.getIsPrivate();
    }

    public interface ButtonListener {
        void onKickClick(int position);

        void onMessageClick(int position);

        void onUserItemClick(int position);
        void onAcceptClick(int position);
    }

    public void setOnKickClickListener(ButtonListener listener) {
        this.kickListener = listener;
    }

    public void setOnMessageClickListener(ButtonListener listener) {
        this.messageListener = listener;
    }

    public void setOnUserItemClickListener(ButtonListener listener) {
        this.userItemListener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserDisplay user = userList.get(position);
        if(user==null){
            return;
        }
        if(user.getUser_type().equals("Organizer")){
            holder.userName.setText(user.getName() + " (Host)");
        }
        else {
            holder.userName.setText(user.getName());
        }

        holder.kickButton.setOnClickListener(v -> {
            if (kickListener != null) {
                kickListener.onKickClick(position);
            }
        });
        holder.messageButton.setOnClickListener(v -> {
            if (messageListener != null) {
                messageListener.onMessageClick(position);
            }
        });
        holder.userItem.setOnClickListener(v -> {
            if (userItemListener != null) {
                userItemListener.onUserItemClick(position);
            }
        });
        holder.acceptButton.setOnClickListener(v->{
            if(acceptListener != null){
                acceptListener.onAcceptClick(position);
            }
        });


        Boolean status = this.userStatus.get(user.get_id());

        // Hide kick and message buttons if not in Organizer mode or if the user is an Organizer
        if (!mode.equals("Organizer") || user.getUser_type().equals("Organizer")) {
            holder.kickButton.setVisibility(View.GONE);
            holder.messageButton.setVisibility(View.GONE);
        }
        else{
            if(this.isPrivate && status != null && status){
                holder.setKickButtonMode(status);
            }
        }
        if(!mode.equals("Organizer") && this.isPrivate && status != null && status){
            holder.userItem.setVisibility(View.GONE);
        }
        // Set click listeners for kick and message buttons



    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        LinearLayout userItem;
        TextView userName;
        Button kickButton;
        Button acceptButton;
        Button messageButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userNameTextView);
            kickButton = itemView.findViewById(R.id.kickButton);
            messageButton = itemView.findViewById(R.id.messageButton);
            userItem = itemView.findViewById(R.id.userItemLayout);
            acceptButton = itemView.findViewById(R.id.acceptButton);
        }

        /**
         * sets the kick button as an accept button if flg=false
         * @param flg
         */
        @SuppressLint("ResourceAsColor")
        public void setKickButtonMode(Boolean flg){
            if(flg){
                this.kickButton.setVisibility(View.VISIBLE);
                this.acceptButton.setVisibility(View.GONE);
                return;
            }

            this.kickButton.setVisibility(View.GONE);
            this.acceptButton.setVisibility(View.VISIBLE);


        }
    }
}
