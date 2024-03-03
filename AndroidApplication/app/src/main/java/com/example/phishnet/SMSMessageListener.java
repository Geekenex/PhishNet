package com.example.phishnet;

import androidx.arch.core.util.Function;

import java.util.ArrayList;

public class SMSMessageListener implements SMSReceiver.MessageListenerInterface {
    public SMSMessageListener(){
        SMSReceiver.bindListener(this);

    }

    public static ArrayList<Runnable> callbacks = new ArrayList<>();
    public static SMSMessage newestMessage;
    @Override
    public void messageReceived(SMSMessage message) {
        newestMessage = message;
        message.setReceived(true);
        // Send to server first before adding to recycler
        for (Runnable callback : callbacks){
            callback.run();
        }
        //displayMessage(message);
        //TextView emptyMessages = requireView().findViewById(R.id.empty_messages);
        //emptyMessages.setVisibility(View.INVISIBLE);
    }


}
