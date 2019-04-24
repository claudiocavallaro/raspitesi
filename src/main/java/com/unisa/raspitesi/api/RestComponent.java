package com.unisa.raspitesi.api;

import com.mashape.unirest.http.Unirest;
import com.unisa.raspitesi.Application;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestComponent {



    @RequestMapping(value = "/restart", method = RequestMethod.GET)
    public void reStart(){

        Application app = new Application();
    }

}
