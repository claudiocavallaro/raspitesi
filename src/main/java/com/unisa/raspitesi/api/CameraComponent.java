package com.unisa.raspitesi.api;

import org.eclipse.paho.client.mqttv3.*;

public class CameraComponent implements MqttCallback {

    MqttClient client;


    public CameraComponent(){
        try {
            client = new MqttClient("tcp://localhost:1883", "Sending");
            client.connect();
            client.setCallback(this);
            client.subscribe("api/camera");

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {

        String json = mqttMessage.toString();
        System.out.println(json);

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
