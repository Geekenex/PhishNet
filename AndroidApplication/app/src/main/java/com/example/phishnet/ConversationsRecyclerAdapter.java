package com.example.phishnet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Stack;

public class ConversationsRecyclerAdapter extends RecyclerView.Adapter<ConversationsRecyclerAdapter.ViewHolder> {
    private ArrayList<SMSMessage> messageArrayList;
    private Stack<Conversation> conversationStack;
    private OnClickListener listener;
    public ConversationsRecyclerAdapter(Stack<Conversation> conversationStack) {
        this.conversationStack = conversationStack;
    }

    public interface OnClickListener {
        void onClick(int position, Conversation conversation);
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
    public ConversationsRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationsRecyclerAdapter.ViewHolder holder, int position) {
        Conversation conversation = conversationStack.get(position);
        int tempPos = position;
        String number = conversationStack.get(position).getPhoneNumber();
        holder.phoneNumber.setText(number);
        ArrayList<SMSMessage> temp = conversationStack.get(position).getSmsMessages();
        String messagePreview = temp.get(temp.size() - 1).getMessage();
        holder.messagePreview.setText(messagePreview);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(tempPos, conversation);
                }
            }
        });
    }
    public void setOnClickListener(OnClickListener onClickListener) {
        this.listener = onClickListener;
    }
    @Override
    public int getItemCount() {
        return conversationStack.size();
    }
}
