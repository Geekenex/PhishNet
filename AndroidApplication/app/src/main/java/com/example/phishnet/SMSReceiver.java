package com.example.phishnet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

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

                    SMSMessage message = new SMSMessage(msgs[i].getOriginatingAddress(), msgs[i].getMessageBody());
                    mListener.messageReceived(message);
                }
            }
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