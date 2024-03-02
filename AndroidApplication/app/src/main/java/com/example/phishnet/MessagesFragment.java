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


public class MessagesFragment extends Fragment implements SMSReceiver.MessageListenerInterface {

    public FragmentMessagesBinding binding;
    private ArrayList<SMSMessage> messageArrayList;
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
        messagesRecyclerAdapter = new MessagesRecyclerAdapter(messageArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(messagesRecyclerAdapter);
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = getView().findViewById(R.id.messagesRecyclerView);
        messageArrayList = new ArrayList<SMSMessage>();

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
        messageArrayList.add(new SMSMessage("514-625-5276", "Hey want free cash"));
        messageArrayList.add(new SMSMessage("514-625-5276", "Hey want free cash"));
        messageArrayList.add(new SMSMessage("514-625-5276", "Hey want free cash"));
        messageArrayList.add(new SMSMessage("514-625-5276", "Hey want free cash"));
        messageArrayList.add(new SMSMessage("514-625-5276", "Hey want free cash"));
    }

    @Override
    public void messageReceived(SMSMessage message) {
        // Send to server
        messageArrayList.add(message);
        Toast.makeText(getActivity().getApplicationContext(), message.getMessage(), Toast.LENGTH_LONG).show();
        messagesRecyclerAdapter.notifyDataSetChanged();
    }

}