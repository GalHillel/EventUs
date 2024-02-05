package com.example.eventus.ui.recycleViews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventus.R;
import com.example.eventus.data.model.UserDisplay;

import java.util.List;

public class UserAdaptor extends RecyclerView.Adapter<UserAdaptor.UserViewHolder> {
    public interface ButtonListener {
        void onKickClick(int position);
        void onMessageClick(int position);
    }


    private List<UserDisplay> userList;
    private String mode;
    private ButtonListener kickListener;
    private ButtonListener messageListener;


    public UserAdaptor(List<UserDisplay> userList,String mode) {
        this.userList = userList;
        this.mode = mode;
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

        //kick and message only for organizer
        if(this.mode.equals("Organizer") && !this.userList.get(position).getUser_type().equals("Organizer")){
            holder.kickButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Notify the listener that the button was clicked
                    if (kickListener != null) {
                        kickListener.onKickClick(holder.getAdapterPosition());
                    }
                }
            });
            holder.messageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Notify the listener that the button was clicked
                    if (messageListener != null) {
                        messageListener.onMessageClick(holder.getAdapterPosition());
                    }
                }
            });
        }else{
            holder.messageButton.setVisibility(View.GONE);
            holder.kickButton.setVisibility(View.GONE);
        }

    }


    public void SetonKickClickListener(ButtonListener listener) {
        this.kickListener = listener;
    }
    public void SetonMessageClickListener(ButtonListener listener) {
        this.messageListener = listener;
    }

    @Override
    public int getItemCount() {
        return this.userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        Button kickButton;
        Button messageButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userNameTextView);
            kickButton = itemView.findViewById(R.id.kickButton);
            messageButton = itemView.findViewById(R.id.messageButton);
        }
    }
}
