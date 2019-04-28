package com.unisa.raspitesi.api;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.unisa.raspitesi.model.ReadEvent;
import com.unisa.raspitesi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class EventHandler {


    public EventHandler(){}


    @Async
    @EventListener
    public void readEvent(ReadEvent event){
        System.out.println(event.getRead() + "----- SENDING GET ------");
        String result = sendGet("http://192.168.1.92:8080/api/entrance", event.getRead().getUid());

        ObjectMapper mapper = new ObjectMapper();
        try{
            if (result.equals("Nothing")){
                System.out.println("No user to display");
            } else {
                User user = mapper.readValue(result, User.class);
                System.out.println(user.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private String sendGet(String getUrl, String line) {
        String noresult = "no results from sendGet on " + getUrl + " - check logs";
        int numTry = 5;
        while(numTry > 0) {
            try{
                com.mashape.unirest.http.HttpResponse<String> R = Unirest.get(getUrl)
                        .header("Content-Type", "application/json")
                        .header("Cache-Control", "no-cache")
                        .queryString("uid", line)
                        .asString();
                return R.getBody().toString();

            } catch (Exception e) {
                numTry --;
                if(numTry == 0) {
                    e.printStackTrace();
                }
            }
        }
        return noresult;
    }
}
