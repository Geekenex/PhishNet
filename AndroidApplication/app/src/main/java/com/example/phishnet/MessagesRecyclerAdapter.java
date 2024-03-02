package com.example.phishnet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MessagesRecyclerAdapter extends RecyclerView.Adapter<MessagesRecyclerAdapter.ViewHolder> {
    private ArrayList<SMSMessage> messageArrayList;

    public MessagesRecyclerAdapter(ArrayList<SMSMessage> messageArrayList) {
        this.messageArrayList = messageArrayList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView phoneNumber;
        private TextView messagePreview;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            phoneNumber = itemView.findViewById(R.id.phoneNumberTitle);
            messagePreview = itemView.findViewById(R.id.messagePreviewText);
        }
    }
    @NonNull
    @Override
    public MessagesRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesRecyclerAdapter.ViewHolder holder, int position) {
        String number = messageArrayList.get(position).getPhoneNumber();
        holder.phoneNumber.setText(number);
        String messagePreview = messageArrayList.get(position).getMessage();
        holder.messagePreview.setText(messagePreview);
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }
}
