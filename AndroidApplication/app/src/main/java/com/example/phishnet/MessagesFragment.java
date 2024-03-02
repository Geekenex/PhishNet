package com.example.phishnet;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.phishnet.databinding.FragmentMessagesBinding;

import java.util.ArrayList;
import java.util.Stack;


public class MessagesFragment extends Fragment implements SMSReceiver.MessageListenerInterface {

    public FragmentMessagesBinding binding;
    private Stack<Conversation> conversationStack;
    private ArrayList<SMSMessage> convo1 = new ArrayList<SMSMessage>();
    private RecyclerView recyclerView;
    private MessagesRecyclerAdapter messagesRecyclerAdapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        SMSReceiver.bindListener(this);
        binding = FragmentMessagesBinding.inflate(inflater, container, false);

        return binding.getRoot();

    }
    private void setAdapter(){
        messagesRecyclerAdapter = new MessagesRecyclerAdapter(conversationStack);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(messagesRecyclerAdapter);
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = getView().findViewById(R.id.messagesRecyclerView);
        conversationStack = new Stack<Conversation>();

        CreateList();
        setAdapter();
        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(MessagesFragment.this)
                        .navigate(R.id.action_messagesFragment_to_FirstFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void CreateList() {
        convo1.add(new SMSMessage("514-625-5276", "Hey want free cash"));
        convo1.add(new SMSMessage("514-625-5276", "Hey want free cash"));
        convo1.add(new SMSMessage("514-625-5276", "Hey want free cash"));
        convo1.add(new SMSMessage("514-625-5276", "Hey want free cash :)"));

        conversationStack.push(new Conversation(convo1, "514-625-5276"));
    }

    @Override
    public void messageReceived(SMSMessage message) {
        // Send to server first before adding to recycler
        displayMessage(message);
    }

    public void displayMessage(SMSMessage message){
        boolean convoExists = false;
        for (Conversation convo: conversationStack) {
            if (convo.getPhoneNumber().equals(message.getPhoneNumber())){
                convo.getSmsMessages().add(message);
                convoExists = true;
                Conversation temp = conversationStack.get(conversationStack.size() - 1);
                int index = conversationStack.indexOf(convo);
                conversationStack.set(conversationStack.size() - 1, convo);
                conversationStack.set(index, temp);

            }
        }
        // If the conversation did not exist before, create a new one.
        if (!convoExists){
            ArrayList<SMSMessage> tempMessages = new ArrayList<>();
            tempMessages.add(message);
            Conversation convo = new Conversation(tempMessages, message.getPhoneNumber());
            conversationStack.push(convo);
        }

        Toast.makeText(getActivity().getApplicationContext(), message.getId() + ": " +  message.getMessage(), Toast.LENGTH_LONG).show();
        messagesRecyclerAdapter.notifyDataSetChanged();
    }
}