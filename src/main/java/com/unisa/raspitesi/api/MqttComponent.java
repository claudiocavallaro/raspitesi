package com.unisa.raspitesi.api;

import com.jayway.jsonpath.JsonPath;
import com.unisa.raspitesi.configuration.EventPublisherService;
import com.unisa.raspitesi.model.Camera;
import com.unisa.raspitesi.model.CameraEvent;
import com.unisa.raspitesi.model.Power;
import com.unisa.raspitesi.model.PowerEvent;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Component;

@Component
public class MqttComponent implements MqttCallback {

    private MqttClient client;

    public MqttComponent(){
        try {
            client = new MqttClient("tcp://localhost:1883", "Sending");
            client.connect();
            client.setCallback(this);
            client.subscribe("tele/spow/SENSOR");

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

        String json = mqttMessage.toString();


        //System.out.println(json);


        if (json.contains("{")){
            String pathEnergy = "$.ENERGY.Voltage";
            String pathCurrent = "$.ENERGY.Current";
            String pathPower = "$.ENERGY.Power";

            int energy = JsonPath.read(json, pathEnergy);
            double current = JsonPath.read(json, pathCurrent);
            int power = JsonPath.read(json,pathPower);

            Power object = new Power(energy, current, power);
            PowerEvent event = new PowerEvent(object);

            EventPublisherService.eventPublisherService.publishEvent(event);
        } else {
            Camera object = new Camera("area1", Integer.valueOf(json));
            CameraEvent event = new CameraEvent(object);

            EventPublisherService.eventPublisherService.publishEvent(event);
        }




    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
