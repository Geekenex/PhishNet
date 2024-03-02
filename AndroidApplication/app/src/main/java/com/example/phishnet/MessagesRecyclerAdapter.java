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
    private Stack<Conversation> conversationStack;
    public MessagesRecyclerAdapter(Stack<Conversation> conversationStack) {
        this.conversationStack = conversationStack;
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

        String number = conversationStack.get(position).getPhoneNumber();
        holder.phoneNumber.setText(number);
        ArrayList<SMSMessage> temp = conversationStack.get(position).getSmsMessages();
        String messagePreview = temp.get(temp.size() - 1).getMessage();
        holder.messagePreview.setText(messagePreview);
    }

    @Override
    public int getItemCount() {
        return conversationStack.size();
    }
}
