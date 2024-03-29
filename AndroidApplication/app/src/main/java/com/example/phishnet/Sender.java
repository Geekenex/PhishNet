package com.example.phishnet;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.UUID;

public class Sender {

    public static void sendMessageAsync(String message, UUID conversationId, UUID messageId) {
        new Thread(() -> send(message, conversationId, messageId)).start();
    }

    public static void send(String message, UUID conversationId, UUID messageId) {
        MqttClient mqttClient;

        

        try {
            mqttClient = new MqttClient(SolaceCredentials.SOLACE_HOST, "HelloWorldPub_" + messageId.toString(), new MemoryPersistence());
            MqttConnectOptions connectionOptions = new MqttConnectOptions();
            connectionOptions.setUserName(SolaceCredentials.SOLACE_USERNAME);
            connectionOptions.setPassword(SolaceCredentials.SOLACE_PASSWORD.toCharArray());
            mqttClient.connect(connectionOptions);

            String topicName = "Messages";
            MqttMessage mqttMessage = new MqttMessage(String.format("{\"message\":\"%s\",\"messageId\":\"%s\",\"conversationId\":\"%s\"}",message,messageId,conversationId).getBytes());
            mqttMessage.setQos(2);
            mqttClient.publish(topicName, mqttMessage);
            System.out.println("Message published");
            mqttClient.disconnect();

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}