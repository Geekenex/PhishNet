package com.example.phishnet;

import static com.example.phishnet.ConversationsData.conversationStack;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
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

        SMSMessageListener.callbacks.add(new Runnable() {
            @Override
            public void run() {
                messagesRecyclerAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(mConversation.getSmsMessages().size() - 1);
            }
        });
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
        EditText inputField = getView().findViewById(R.id.edit_text_message);
        Button submitButton = getView().findViewById(R.id.button_send);
        InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputString = inputField.getText().toString();
                if (!inputString.isEmpty() || !inputString.equals("")){
                    ConversationsData.addMessageToConversation(new SMSMessage(mConversation.getPhoneNumber(), inputString));

                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(mConversation.getPhoneNumber(), null, inputString, null, null);

                    ConversationsData.saveConversations(getContext());
                    messagesRecyclerAdapter.notifyDataSetChanged();
                    inputField.setText("");
                    recyclerView.scrollToPosition(mConversation.getSmsMessages().size() - 1);
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                }

            }
        });
        setAdapter();
        recyclerView.scrollToPosition(mConversation.getSmsMessages().size() - 1);
    }


}