/**package com.example.phishnet;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Receiver implements MqttCallback {

    private MqttClient mqttClient;

    public Receiver() {
        String clientId = "HelloWorldSub_" + UUID.randomUUID().toString();
        try {
            mqttClient = new MqttClient(SolaceCredentials.SOLACE_HOST, clientId, new MemoryPersistence());
            MqttConnectOptions connectionOptions = new MqttConnectOptions();
            connectionOptions.setUserName(SolaceCredentials.SOLACE_USERNAME);
            connectionOptions.setPassword(SolaceCredentials.SOLACE_PASSWORD.toCharArray());
            mqttClient.setCallback(this);
            mqttClient.connect(connectionOptions);

            mqttClient.subscribe("Verdicts", 2);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("Connection lost: " + cause.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("Message arrived. Topic: " + topic + ", Message: " + new String(message.getPayload()));
    }

}**/
