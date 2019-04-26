package com.unisa.raspitesi.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unisa.raspitesi.model.Read;
import com.unisa.raspitesi.model.ReadEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestComponent {


    @GetMapping("/api/restart")
    public String get(){

        Gson gson = new GsonBuilder().create();
        String result = gson.toJson("Restart");

        return result;

    }

}
