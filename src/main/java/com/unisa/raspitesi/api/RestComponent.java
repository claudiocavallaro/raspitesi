package com.unisa.raspitesi.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pi4j.io.gpio.*;
import com.unisa.raspitesi.model.Read;
import com.unisa.raspitesi.model.ReadEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestComponent {

    private static GpioPinDigitalOutput pin;


    @GetMapping("/api/restart")
    public String get(){

        Gson gson = new GsonBuilder().create();
        String result = gson.toJson("Restart");

        return result;

    }


    @RequestMapping("/api/confirm")
    public String getConfirm(){

        if (pin == null) {
            GpioController gpio = GpioFactory.getInstance();
            pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_17, "LED", PinState.LOW);
        }

        pin.toggle();

        return "OK";
    }

}
