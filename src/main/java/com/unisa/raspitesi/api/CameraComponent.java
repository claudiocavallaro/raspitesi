package com.unisa.raspitesi.api;

import com.unisa.raspitesi.configuration.EventPublisherService;
import com.unisa.raspitesi.model.Camera;
import com.unisa.raspitesi.model.CameraEvent;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Component;

@Component
public class CameraComponent implements MqttCallback {

    
    public CameraComponent(){
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

        //System.out.println(" ----- " + s + " ------ " + mqttMessage.toString());

        Camera camera = new Camera("area1", Integer.valueOf(mqttMessage.toString()));

        CameraEvent event = new CameraEvent(camera);
        EventPublisherService.eventPublisherService.publishEvent(event);

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
