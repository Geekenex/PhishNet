package com.example.phishnet;

public class SMSMessage {
    private String phoneNumber;
    private String message;
    private int id;
    private static int count = 0;

    public SMSMessage(String phoneNumber, String message){
        this.phoneNumber = phoneNumber;
        this.message = message;
        id = ++count;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
