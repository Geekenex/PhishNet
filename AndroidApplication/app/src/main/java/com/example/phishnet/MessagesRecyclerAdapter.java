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
import java.util.stream.Collectors;

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
        int flag = messageObject.getFlag();
        boolean received = messageObject.isReceived();
        String messageText = messageObject.getMessage();
        if (flag == 0 || !received){
            holder.itemView.setVisibility(View.VISIBLE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            holder.message.setText(messageText);

            LinearLayout layout = (LinearLayout) holder.itemView.findViewById(R.id.innerLayout);

            if (messageObject.isReceived()){
                layout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.shape));
                ((LinearLayout) holder.itemView).setGravity(Gravity.START);
            }
            else {
                layout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.blue_shape));
                ((LinearLayout) holder.itemView).setGravity(Gravity.END);
            }
        }
        else {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }

    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }
}
