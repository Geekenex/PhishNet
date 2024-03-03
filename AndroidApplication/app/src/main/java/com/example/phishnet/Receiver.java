package com.example.phishnet;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Receiver implements MqttCallback {

    private static MqttClient mqttClient;
    private static final ConcurrentLinkedQueue<String> messageQueue = new ConcurrentLinkedQueue<>();

    public static void startReceiving() {
        try {
            String clientId = "HelloWorldSub_" + UUID.randomUUID().toString();
            mqttClient = new MqttClient(SolaceCredentials.SOLACE_HOST, clientId, new MemoryPersistence());
            MqttConnectOptions connectionOptions = new MqttConnectOptions();
            connectionOptions.setUserName(SolaceCredentials.SOLACE_USERNAME);
            connectionOptions.setPassword(SolaceCredentials.SOLACE_PASSWORD.toCharArray());

            mqttClient.setCallback(new Receiver());
            mqttClient.connect(connectionOptions);
            mqttClient.subscribe("Verdicts", 2);

            System.out.println(clientId + " subscribed to Verdicts");

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("Connection lost: " + cause.toString());
        // Implement reconnection logic as needed
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        // Add the incoming message to the queue
        messageQueue.add(new String(message.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // Not used in this receiver example
    }

    public static String getNextMessage() {
        // Retrieves and removes the head of the message queue, or returns null if the queue is empty
        return messageQueue.poll();
    }
}
