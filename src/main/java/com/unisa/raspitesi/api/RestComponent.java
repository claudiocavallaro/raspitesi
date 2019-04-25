package com.unisa.raspitesi.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.Unirest;
import com.unisa.raspitesi.Application;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestComponent {


    @GetMapping("/api/restart")
    public String get(){

        Gson gson = new GsonBuilder().create();
        String result = gson.toJson("Restart");

        Nfc nfc = new Nfc();
        return result;

    }

}
