package com.example.phishnet;

import java.util.ArrayList;

public class Conversation {
    private ArrayList<SMSMessage> smsMessages;
    private String phoneNumber;

    public Conversation (ArrayList<SMSMessage> smsMessages, String phoneNumber) {
        this.smsMessages = smsMessages;
        if (this.smsMessages == null){
            this.smsMessages = new ArrayList<SMSMessage>();
        }
        this.phoneNumber = phoneNumber;
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
}
