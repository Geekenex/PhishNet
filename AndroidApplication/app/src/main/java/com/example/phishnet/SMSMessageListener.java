package com.example.phishnet;

import java.util.ArrayList;
import java.util.UUID;

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
        // TODO conversationId
        //Sender.sendMessageAsync(message.getMessage(), UUID.randomUUID() , message.getId());
        for (Runnable callback : callbacks){
            callback.run();
        }
        //displayMessage(message);
        //TextView emptyMessages = requireView().findViewById(R.id.empty_messages);
        //emptyMessages.setVisibility(View.INVISIBLE);
    }


}
