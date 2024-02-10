package com.example.eventus.ui.recycleViews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventus.R;
import com.example.eventus.data.model.UserDisplay;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<UserDisplay> userList;
    private String mode;
    private ButtonListener kickListener;
    private ButtonListener messageListener;
    private ButtonListener userItemListener;

    public UserAdapter(List<UserDisplay> userList, String mode) {
        this.userList = userList;
        this.mode = mode;
    }

    public interface ButtonListener {
        void onKickClick(int position);
        void onMessageClick(int position);
        void onUserItemClick(int position);
    }

    public void setOnKickClickListener(ButtonListener listener) {
        this.kickListener = listener;
    }

    public void setOnMessageClickListener(ButtonListener listener) {
        this.messageListener = listener;
    }
    public void setOnUserItemClickListener(ButtonListener listener){
        this.userItemListener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserDisplay user = userList.get(position);

        holder.userName.setText(user.getName());

        // Set click listeners for kick and message buttons
        holder.kickButton.setOnClickListener(v -> {
            if (kickListener != null) {
                kickListener.onKickClick(position);
            }
        });

        holder.messageButton.setOnClickListener(v -> {
            if (messageListener != null) {
                messageListener.onMessageClick(position);
            }
            // TODO: Add bundel for transform data
            //  Navigation.findNavController(v).navigate(R.id.createMessageFragment);
        });
        holder.userItem.setOnClickListener(v-> {
            if (userItemListener != null) {
                userItemListener.onUserItemClick(position);
            }
        });

        // Hide kick and message buttons if not in Organizer mode or if the user is an Organizer
        if (!mode.equals("Organizer") || user.getUser_type().equals("Organizer")) {
            holder.kickButton.setVisibility(View.GONE);
            holder.messageButton.setVisibility(View.GONE);
        }

        // Set click listener for the whole item to navigate to the user profile
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("viewedUser", user); // Pass the user whose profile is being viewed
            Navigation.findNavController(v).navigate(R.id.userProfileFragment, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        LinearLayout userItem;
        TextView userName;
        Button kickButton;
        Button messageButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userNameTextView);
            kickButton = itemView.findViewById(R.id.kickButton);
            messageButton = itemView.findViewById(R.id.messageButton);
            userItem = itemView.findViewById(R.id.userItemLayout);
        }
    }
}
