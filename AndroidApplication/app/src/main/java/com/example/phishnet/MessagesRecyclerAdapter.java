package com.example.phishnet;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MessagesRecyclerAdapter.ViewHolder holder, int position) {
        SMSMessage messageObject = messageArrayList.get(position);
        String messageText = messageObject.getMessage();
        holder.message.setText(messageText);



        if (messageObject.isReceived()){
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.gray));
           // holder.itemView.setForegroundGravity(Gravity.START);
            ((LinearLayout) holder.itemView).setGravity(Gravity.START);
        }
        else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.blue));
            ((LinearLayout) holder.itemView).setGravity(Gravity.END);
        }


    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }
}
