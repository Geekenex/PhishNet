package com.example.phishnet;


import static com.example.phishnet.ConversationsData.conversationStack;

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


public class MessagesFragment extends Fragment implements SMSReceiver.MessageListenerInterface {

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
        SMSReceiver.bindListener(this);
        binding = FragmentMessagesBinding.inflate(inflater, container, false);

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
        conversationsRecyclerAdapter.notifyDataSetChanged();
        ConversationsData.saveConversations(getContext());
    }
}

