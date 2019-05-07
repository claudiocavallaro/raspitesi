package com.unisa.raspitesi.api;

import com.unisa.raspitesi.model.Camera;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Component;

@Component
public class CameraComponent implements MqttCallback {

    public CameraComponent(MqttComponent component){
        try {
            MqttClient client = component.getClient();
            client.setCallback(this);
            client.subscribe("camera");


        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {

        System.out.println(" ----- " + s + " ------ " + mqttMessage.toString());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
