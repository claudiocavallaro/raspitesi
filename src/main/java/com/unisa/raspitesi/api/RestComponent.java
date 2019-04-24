package com.unisa.raspitesi.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.Unirest;
import com.unisa.raspitesi.Application;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestComponent {


    @RequestMapping(value = "/restart", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String get(){

        Gson gson = new GsonBuilder().create();
        String result = gson.toJson("Restart");

        return result;

    }

}
