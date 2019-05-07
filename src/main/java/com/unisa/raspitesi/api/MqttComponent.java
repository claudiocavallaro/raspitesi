package com.unisa.raspitesi.api;

import lombok.Getter;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MqttComponent {

    @Autowired
    private MqttClient client;

    public MqttComponent() throws MqttException {
        client = new MqttClient("tcp://localhost:1883", "Sending");
        client.connect();

    }
}
