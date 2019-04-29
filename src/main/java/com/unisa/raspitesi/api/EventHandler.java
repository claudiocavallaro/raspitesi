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

    private User appoggio = null;
    private long timeStamp = 0;

    @Async
    @EventListener
    public void readEvent(ReadEvent event){

        if(appoggio != null){
            if((appoggio.getUid().equals(event.getRead().getUid())) && (event.getRead().getTimestamp() - timeStamp < Math.abs(60000))){
                System.out.println("no get to send");
            } else {
                System.out.println(event.getRead() + "----- SENDING GET ------");
                String result = sendGet("http://192.168.1.92:8080/api/entrance", event.getRead().getUid());

                ObjectMapper mapper = new ObjectMapper();
                try{
                    if (result.equals("Nothing")){
                        System.out.println("No user to display");
                    } else {
                        User user = mapper.readValue(result, User.class);

                        if (user.isInside()){
                            appoggio = user;
                            timeStamp = event.getRead().getTimestamp();
                        }
                        System.out.println(user.toString());
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
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
