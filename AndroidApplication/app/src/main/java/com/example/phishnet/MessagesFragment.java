package com.example.phishnet;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phishnet.databinding.FragmentMessagesBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Stack;


public class MessagesFragment extends Fragment implements SMSReceiver.MessageListenerInterface {

    public FragmentMessagesBinding binding;
    private Stack<Conversation> conversationStack;
    private final ArrayList<SMSMessage> convo1 = new ArrayList<>();
    private RecyclerView recyclerView;
    private MessagesRecyclerAdapter messagesRecyclerAdapter;
    private final String filePath = "conversations.json";

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
        if (conversationStack == null || conversationStack.size() == 0){
            conversationStack = new Stack<Conversation>();
            //createList();
            TextView emptyMessages = getView().findViewById(R.id.empty_messages);
            emptyMessages.setVisibility(View.VISIBLE);
        }


        setAdapter();
        messagesRecyclerAdapter.setOnClickListener(new MessagesRecyclerAdapter.OnClickListener(){
            @Override
            public void onClick(int position, Conversation conversation) {
                // Passing the data to the
                Toast.makeText(requireActivity().getApplicationContext(), conversation.getPhoneNumber(), Toast.LENGTH_LONG).show();
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
        message.setReceived(true);
        // Send to server first before adding to recycler
        Sender.send(message.getMessage(), 1, message.getId());
        displayMessage(message);
        TextView emptyMessages = requireView().findViewById(R.id.empty_messages);
        emptyMessages.setVisibility(View.INVISIBLE);
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
            Gson gson = new Gson();
            String json = gson.toJson(conversationStack);

            FileOutputStream fileOutputStream = requireContext().openFileOutput(filePath, Context.MODE_PRIVATE);
            PrintStream stream = new PrintStream(fileOutputStream, true, "UTF-8");
            stream.println(json);
            stream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConversations() {
        try {
            Gson gson = new Gson();
            FileInputStream fileInputStream = requireContext().openFileInput(filePath);
            JsonReader reader = new JsonReader(new InputStreamReader(fileInputStream));
            Type REVIEW_TYPE = new TypeToken<Stack<Conversation>>() {}.getType();
            conversationStack = gson.fromJson(reader, REVIEW_TYPE);
            reader.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

