package com.example.eventus.ui.recycleViews;


import android.graphics.Typeface;
import android.text.Layout;
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
import com.example.eventus.data.model.UserEventDisplay;
import com.example.eventus.data.model.UserMessageDisplay;

import java.util.List;
import java.util.Map;

public class MessageAdaptor extends RecyclerView.Adapter<MessageAdaptor.MessageViewHolder> {


    public interface onMessageClickListener {
        void onMessageClick(int position);
    }

    private List<UserMessageDisplay> messageList;
    private Map<String, Boolean> userRead;
    private onMessageClickListener listener;

    public MessageAdaptor(List<UserMessageDisplay> messageList, Map<String,Boolean> userRead) {
        this.messageList = messageList;
        this.userRead = userRead;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

        UserMessageDisplay msg = messageList.get(position);
        boolean hasRead = userRead.containsKey(msg.get_id()) && Boolean.TRUE.equals(userRead.get(msg.get_id()));

        holder.userMessageTitleView.setText(msg.getTitle());
        holder.userMessageDateView.setText(msg.getDateString());
        holder.userMessageSenderView.setText("from: "+msg.getSenderName());
        if(hasRead){
            holder.userMessageItem.setBackgroundResource(R.color.message_read);
            holder.userMessageSenderView.setTypeface(null, Typeface.NORMAL);
            holder.userMessageDateView.setTypeface(null, Typeface.NORMAL);
            holder.userMessageTitleView.setTypeface(null, Typeface.NORMAL);
        }else{
            holder.userMessageItem.setBackgroundResource(R.color.message_unread);
            holder.userMessageSenderView.setTypeface(null, Typeface.BOLD);
            holder.userMessageDateView.setTypeface(null, Typeface.BOLD);
            holder.userMessageTitleView.setTypeface(null, Typeface.BOLD);
        }

        holder.userMessageItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Notify the listener that the button was clicked
                if (listener != null) {
                    listener.onMessageClick(holder.getAdapterPosition());
                }
            }
            // TODO: Add bundel for transform data and move to dialog fragment
            });
    }
    public void setOnMessageClickListener(onMessageClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView userMessageTitleView;
        TextView userMessageDateView;
        TextView userMessageSenderView;
        LinearLayout userMessageItem;


        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            userMessageTitleView = itemView.findViewById(R.id.userMessageTitleTextView);
            userMessageDateView = itemView.findViewById(R.id.userMessageDateTextView);
            userMessageSenderView = itemView.findViewById(R.id.userMessageSenderTextView);
            userMessageItem = itemView.findViewById(R.id.userMessageItem);
        }
    }
}
