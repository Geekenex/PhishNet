package com.example.phishnet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Stack;

public class MessagesRecyclerAdapter extends RecyclerView.Adapter<MessagesRecyclerAdapter.ViewHolder> {
    private ArrayList<SMSMessage> messageArrayList;
    public MessagesRecyclerAdapter(ArrayList<SMSMessage> messageArrayList) {
        this.messageArrayList = messageArrayList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView message;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.messageText);
        }
    }
    @NonNull
    @Override
    public MessagesRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_view_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesRecyclerAdapter.ViewHolder holder, int position) {
        SMSMessage messageObject = messageArrayList.get(position);
        String messageText = messageObject.getMessage();
        holder.message.setText(messageText);
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }
}
