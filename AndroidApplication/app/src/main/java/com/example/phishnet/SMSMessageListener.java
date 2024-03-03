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


        for (Runnable callback : callbacks){
            callback.run();
        }

        ConversationsData.addMessageToConversation(message);

        Sender.sendMessageAsync(message.getMessage(), message.getConversationId() , message.getId());

    }
    public static void RunCallbacks(){
        for (Runnable callback : callbacks){
            callback.run();
        }
    }

}
