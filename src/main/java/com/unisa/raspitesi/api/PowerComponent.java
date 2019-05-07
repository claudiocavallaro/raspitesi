package com.unisa.raspitesi.api;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.unisa.raspitesi.configuration.EventPublisherService;
import com.unisa.raspitesi.model.Power;
import com.unisa.raspitesi.model.PowerEvent;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Component;

@Component
public class PowerComponent implements MqttCallback {

    private MqttClient client;

    public PowerComponent(){
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

        System.out.println(json);

        String pathEnergy = "";
        String pathCurrent = "";
        String pathPower = "";

        try {
            pathEnergy = "$.ENERGY.Voltage";
            pathCurrent = "$.ENERGY.Current";
            pathPower = "$.ENERGY.Power";
        } catch (PathNotFoundException pne){
            System.out.println(pne);
        }


        int energy = JsonPath.read(json, pathEnergy);
        double current = JsonPath.read(json, pathCurrent);
        int power = JsonPath.read(json,pathPower);

        Power powerObj = new Power(energy, current, power);

        //System.out.println("--- FROM COMPONENT " + powerObj);

        PowerEvent powerEvent = new PowerEvent(powerObj);
        EventPublisherService.eventPublisherService.publishEvent(powerEvent);

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
