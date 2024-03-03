package com.example.phishnet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SMSReceiver extends BroadcastReceiver {
    public static String SMS_RECEIVE = "android.provider.Telephony.SMS_RECEIVED";
    private static MessageListenerInterface mListener;
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (intent.getAction().equals(SMS_RECEIVE)){
            SmsMessage[] msgs;
            String strMessage = "";
            String format = bundle.getString("format");

            // Retrieve the SMS message received.
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus != null) {
                // Fill the msgs array.
                msgs = new SmsMessage[pdus.length];
                for (int i = 0; i < msgs.length; i++) {
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                    // Build the message to show.
                    strMessage += "SMS from " + msgs[i].getOriginatingAddress();
                    strMessage += " :" + msgs[i].getMessageBody() + "\n";

                    String unprocessedNumber = msgs[i].getOriginatingAddress();
                    String formattedString = formatPhoneNumber(unprocessedNumber);
                    SMSMessage message = new SMSMessage(formattedString, msgs[i].getMessageBody());
                    mListener.messageReceived(message);
                }
            }
        }
    }

    // With help from ChatGPT and StackOverflow
    public static String formatPhoneNumber(String phoneNumber) {
        // Define regex patterns for different phone number formats
        String patternWithCountryCode = "(\\+\\d{1,2})?(\\d{3})(\\d{3})(\\d{4})";
        String patternWithoutCountryCode = "(\\d{3})(\\d{3})(\\d{4})";

        // Create Pattern objects
        Pattern countryCodePattern = Pattern.compile(patternWithCountryCode);
        Pattern noCountryCodePattern = Pattern.compile(patternWithoutCountryCode);

        // Match the input phone number with the patterns
        Matcher countryCodeMatcher = countryCodePattern.matcher(phoneNumber);
        Matcher noCountryCodeMatcher = noCountryCodePattern.matcher(phoneNumber);

        if (countryCodeMatcher.matches()) {
            // Phone number with country code
            String countryCode = countryCodeMatcher.group(1) != null ? countryCodeMatcher.group(1) + " " : "";
            String areaCode = countryCodeMatcher.group(2);
            String firstPart = countryCodeMatcher.group(3);
            String secondPart = countryCodeMatcher.group(4);
            return countryCode + "(" + areaCode + ") " + firstPart + "-" + secondPart;
        } else if (noCountryCodeMatcher.matches()) {
            // Phone number without country code
            String areaCode = noCountryCodeMatcher.group(1);
            String firstPart = noCountryCodeMatcher.group(2);
            String secondPart = noCountryCodeMatcher.group(3);
            return "(" + areaCode + ") " + firstPart + "-" + secondPart;
        } else {
            // If the phone number doesn't match any pattern, return the original string
            return phoneNumber;
        }
    }

    public interface MessageListenerInterface {
        // creating an interface method for messages received.
        void messageReceived(SMSMessage message);
    }
    public static void bindListener(MessageListenerInterface listener) {
        mListener = listener;
    }
}