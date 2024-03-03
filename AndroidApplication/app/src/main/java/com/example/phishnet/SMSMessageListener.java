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
        ConversationsData.addMessageToConversation(message);
        message.setReceived(true);
        Sender.sendMessageAsync(message.getMessage(), message.getConversationId() , message.getId());
        for (Runnable callback : callbacks){
            callback.run();
        }
    }


}
