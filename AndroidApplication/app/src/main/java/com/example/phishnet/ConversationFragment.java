package com.example.phishnet;

import static com.example.phishnet.ConversationsData.conversationStack;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConversationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConversationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CONVERSATION = "conversationID";
    private RecyclerView recyclerView;
    private MessagesRecyclerAdapter messagesRecyclerAdapter;

    // TODO: Rename and change types of parameters
    private static Conversation mConversation;

    public ConversationFragment() {
        // Required empty public constructor
    }

    private void setAdapter(){
        messagesRecyclerAdapter = new MessagesRecyclerAdapter(mConversation.getSmsMessages());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(messagesRecyclerAdapter);
    }

    public static ConversationFragment newInstance(Conversation conversation) {
        ConversationFragment fragment = new ConversationFragment();
        mConversation = conversation;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String conversationID = getArguments().getString(ARG_CONVERSATION);
        mConversation = ConversationsData.getConversationById(UUID.fromString(conversationID));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_conversation, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = getView().findViewById(R.id.conversation_messages_recycler);
        TextView phoneNumber = getView().findViewById(R.id.phoneNumberConversationTitle);
        phoneNumber.setText(mConversation.getPhoneNumber());
        setAdapter();

    }
}