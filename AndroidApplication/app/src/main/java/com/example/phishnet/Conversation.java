package com.example.phishnet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Conversation implements Serializable {
    private ArrayList<SMSMessage> smsMessages;
    private String phoneNumber;
    private UUID id;

    public Conversation (ArrayList<SMSMessage> smsMessages, String phoneNumber) {
        this.smsMessages = smsMessages;
        if (this.smsMessages == null){
            this.smsMessages = new ArrayList<SMSMessage>();
        }
        this.phoneNumber = phoneNumber;
        id = UUID.randomUUID();
    }

    public ArrayList<SMSMessage> getSmsMessages() {
        return smsMessages;
    }

    public void setSmsMessages(ArrayList<SMSMessage> smsMessages) {
        this.smsMessages = smsMessages;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
