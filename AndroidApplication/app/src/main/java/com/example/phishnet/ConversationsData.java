package com.example.phishnet;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Type;
import java.util.Stack;
import java.util.UUID;

public class ConversationsData {
    public static Stack<Conversation> conversationStack;
    private static final String filePath = "conversations.json";

    public static void saveConversations(Context context){
        System.out.println("Saving data");
        try {
            Gson gson = new Gson();
            String json = gson.toJson(conversationStack);

            FileOutputStream fileOutputStream = context.openFileOutput(filePath, Context.MODE_PRIVATE);
            PrintStream stream = new PrintStream(fileOutputStream, true, "UTF-8");
            stream.println(json);
            stream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadConversations(Context context) {
        System.out.println("Loading data");
        try {
            Gson gson = new Gson();
            FileInputStream fileInputStream = context.openFileInput(filePath);
            JsonReader reader = new JsonReader(new InputStreamReader(fileInputStream));
            Type REVIEW_TYPE = new TypeToken<Stack<Conversation>>() {}.getType();
            conversationStack = gson.fromJson(reader, REVIEW_TYPE);
            reader.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Conversation getConversationById(UUID id){
        for (Conversation conversation : conversationStack){
            if (conversation.getId().equals(id))
                return conversation;
        }
        return null;
    }
}
