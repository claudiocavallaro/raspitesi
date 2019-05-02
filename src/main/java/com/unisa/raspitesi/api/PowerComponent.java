package com.unisa.raspitesi.api;

import com.jayway.jsonpath.JsonPath;
import com.unisa.raspitesi.configuration.EventPublisherService;
import com.unisa.raspitesi.model.Power;
import com.unisa.raspitesi.model.PowerEvent;
import org.eclipse.paho.client.mqttv3.*;

public class PowerComponent implements MqttCallback {

    MqttClient client;


    public PowerComponent(){
        try {
            client = new MqttClient("tcp://192.168.1.111:1883", "Sending");
            client.connect();
            client.setCallback(this);
            client.subscribe("tele/spow/SENSOR");

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

        String pathEnergy = "$.ENERGY.Voltage";
        String pathCurrent = "$.ENERGY.Current";
        String pathPower = "$.ENERGY.Power";

        int energy = JsonPath.read(json, pathEnergy);
        double current = JsonPath.read(json, pathCurrent);
        int power = JsonPath.read(json,pathPower);

        Power powerObj = new Power(energy, current, power);

        PowerEvent powerEvent = new PowerEvent(powerObj);
        EventPublisherService.eventPublisherService.publishEvent(powerEvent);

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
