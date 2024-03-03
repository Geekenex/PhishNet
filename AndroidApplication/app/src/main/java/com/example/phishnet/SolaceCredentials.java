package com.example.phishnet;

import android.content.Context;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SolaceCredentials {
    public static String SOLACE_HOST = "";
    public static String SOLACE_USERNAME = "";
    public static String SOLACE_PASSWORD = "";
    public static String SOLACE_RECEIVING_QUEUE_NAME = "";
    public static String SOLACE_SENDING_QUEUE_NAME = "";

    public static void getCredentials(Context context) {
        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.env);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.replaceAll("\"","").split("=");
                if (parts.length == 2) { // Ensure there are exactly two parts
                    switch (parts[0]) {
                        case "SOLACE_HOST":
                            SOLACE_HOST = parts[1];
                            break;
                        case "SOLACE_USERNAME":
                            SOLACE_USERNAME = parts[1];
                            break;
                        case "SOLACE_PASSWORD":
                            SOLACE_PASSWORD = parts[1];
                            break;
                        case "SOLACE_RECEIVING_QUEUE_NAME":
                            SOLACE_RECEIVING_QUEUE_NAME = parts[1];
                            break;
                        case "SOLACE_SENDING_QUEUE_NAME":
                            SOLACE_SENDING_QUEUE_NAME = parts[1];
                            break;
                    }
                }
            }
            reader.close();
        } catch (Exception e) {
            System.err.println("Error reading credentials: " + e.getMessage());
        }
    }
}
