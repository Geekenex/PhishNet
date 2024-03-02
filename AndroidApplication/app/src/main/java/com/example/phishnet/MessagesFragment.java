package com.example.phishnet;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.phishnet.databinding.FragmentMessagesBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Stack;


public class MessagesFragment extends Fragment implements SMSReceiver.MessageListenerInterface {

    public FragmentMessagesBinding binding;
    private Stack<Conversation> conversationStack;
    private ArrayList<SMSMessage> convo1 = new ArrayList<SMSMessage>();
    private RecyclerView recyclerView;
    private MessagesRecyclerAdapter messagesRecyclerAdapter;
    public static final String NEXT_SCREEN = "conversation_screen";

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
        loadConversations();
        if (conversationStack.size() == 0){
            conversationStack = new Stack<Conversation>();
            createList();
        }



        setAdapter();
        messagesRecyclerAdapter.setOnClickListener(new MessagesRecyclerAdapter.OnClickListener(){
            @Override
            public void onClick(int position, Conversation conversation) {
                Intent intent = new Intent(getActivity(), Conversation.class);
                // Passing the data to the
                Toast.makeText(getActivity().getApplicationContext(), conversation.getPhoneNumber(), Toast.LENGTH_LONG).show();
              //  intent.putExtra(NEXT_SCREEN, conversation);
              //  startActivity(intent);
                Bundle bundle = new Bundle();
                bundle.putSerializable("conversation", conversation);
                NavHostFragment.findNavController(MessagesFragment.this)
                        .navigate(R.id.action_messagesFragment_to_conversationFragment, bundle);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void createList() {
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
        saveConversations();
    }

    public void saveConversations(){
        try {
            FileOutputStream fileOutputStream = getContext().openFileOutput("conversations.csv", Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(conversationStack);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConversations() {
        try {
            FileInputStream fileInputStream = getContext().openFileInput("conversations.csv");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            conversationStack = (Stack<Conversation>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

