package com.example.phishnet;

import java.io.Serializable;
import java.util.UUID;

public class SMSMessage implements Serializable {
    private String phoneNumber;
    private String message;
    private UUID id;
    private UUID conversationId;
    private boolean received = false;
    private int flag;


    public SMSMessage(String phoneNumber, String message){
        this.phoneNumber = phoneNumber;
        this.message = message;
        id = UUID.randomUUID();
        setFlag(1);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public boolean isReceived() {
        return received;
    }

    public void setReceived(boolean received) {
        this.received = received;
        if (received)
            this.setFlag(0);
    }

    public UUID getConversationId() {
        return conversationId;
    }

    public void setConversationId(UUID conversationId) {
        this.conversationId = conversationId;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
