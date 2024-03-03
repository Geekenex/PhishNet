package com.example.phishnet;


import static com.example.phishnet.ConversationsData.conversationStack;
import static com.example.phishnet.ConversationsData.updateMessageFlag;

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

import java.util.ArrayList;
import java.util.Stack;
import java.util.UUID;


public class MessagesFragment extends Fragment {

    public FragmentMessagesBinding binding;

    private final ArrayList<SMSMessage> convo1 = new ArrayList<>();
    private RecyclerView recyclerView;
    private ConversationsRecyclerAdapter conversationsRecyclerAdapter;
    private final String filePath = "conversations.json";

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentMessagesBinding.inflate(inflater, container, false);
        SMSMessageListener.callbacks.add(new Runnable() {
            @Override
            public void run() {
                displayMessage(SMSMessageListener.newestMessage);
            }
        });
        return binding.getRoot();

    }
    private void setAdapter(){
        conversationsRecyclerAdapter = new ConversationsRecyclerAdapter(conversationStack);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(conversationsRecyclerAdapter);
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = getView().findViewById(R.id.messagesRecyclerView);
        ConversationsData.loadConversations(getContext());
        if (conversationStack == null || conversationStack.size() == 0){
            conversationStack = new Stack<Conversation>();
            //createList();
            TextView emptyMessages = getView().findViewById(R.id.empty_messages);
            emptyMessages.setVisibility(View.VISIBLE);
        }

        setAdapter();
        conversationsRecyclerAdapter.setOnClickListener(new ConversationsRecyclerAdapter.OnClickListener(){
            @Override
            public void onClick(int position, Conversation conversation) {
                // Passing the data to the
                Toast.makeText(requireActivity().getApplicationContext(), conversation.getPhoneNumber(), Toast.LENGTH_LONG).show();
              //  intent.putExtra(NEXT_SCREEN, conversation);
              //  startActivity(intent);
                Bundle bundle = new Bundle();
                bundle.putString("conversationID", String.valueOf(conversation.getId()));
                NavHostFragment.findNavController(MessagesFragment.this)
                        .navigate(R.id.action_messagesFragment_to_conversationFragment, bundle);
            }
        });
    }

    private void displayMessage(SMSMessage message){
        //Sender.sendMessageAsync(message.getMessage(), 1, message.getId());
        message.setReceived(true);
        ConversationsData.addMessageToConversation(message);
        Toast.makeText(getActivity().getApplicationContext(), message.getId() + ": " +  message.getMessage(), Toast.LENGTH_LONG).show();
        conversationsRecyclerAdapter.notifyDataSetChanged();
        ConversationsData.saveConversations(getContext());
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




}

