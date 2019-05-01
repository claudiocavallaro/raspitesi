package com.unisa.raspitesi.api;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.unisa.raspitesi.model.Read;
import com.unisa.raspitesi.model.ReadEvent;
import com.unisa.raspitesi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EventHandler {


    public EventHandler() {
    }


    private ConcurrentHashMap<String, Long> recordList = new ConcurrentHashMap<>();
    private Read lastExit = null;

    @Async
    @EventListener
    public void readEvent(ReadEvent event) {

        User user = null;

        System.out.println(recordList.keySet());

        /*
        * */
        if (recordList.isEmpty()) {
            if(lastExit == null){
                user = completeSend(event);
                if(user != null){
                    recordList.put(event.getRead().getUid(), event.getRead().getTimestamp());
                }
            } else {

                Read now = event.getRead();
                System.out.println("----NOW----" + now.toString());
                System.out.println("----LAST FROM ENTER----" + lastExit.toString());
                if (now.getUid().equals(lastExit.getUid())){
                    if (Math.abs(now.getTimestamp() - lastExit.getTimestamp()) < 6000){
                        System.out.println("can't enter");
                    } else {
                        user = completeSend(event);
                        if(user != null){
                            recordList.put(event.getRead().getUid(), event.getRead().getTimestamp());
                        }
                    }
                } else {
                    user = completeSend(event);
                    if(user != null){
                        recordList.put(event.getRead().getUid(), event.getRead().getTimestamp());
                    }
                }

            }

        } else {

            /*
             *
             *
             */

            if (recordList.containsKey(event.getRead().getUid())) {

                //System.out.println("Valore già registrato " + recordList.get(event.getRead().getUid()));
                long timeArrive = recordList.get(event.getRead().getUid());
                long recordValue = event.getRead().getTimestamp();
                //System.out.println("Valore di confronto " + event.getRead().getTimestamp());

                if (Math.abs(timeArrive - recordValue) < 6000) {
                    System.out.println("no get to send");
                } else {
                    user = completeSend(event);
                    recordList.remove(event.getRead().getUid());
                    lastExit = new Read(event.getRead().getUid());
                    lastExit.setTimestamp(event.getRead().getTimestamp());
                    System.out.println("----LAST-----" + lastExit.toString());
                    System.out.println("Utente uscito");
                }

            } else {

                if(lastExit == null){
                    user = completeSend(event);
                    if(user != null){
                        recordList.put(event.getRead().getUid(), event.getRead().getTimestamp());
                    }
                } else {

                    Read now = event.getRead();
                    if (now.getUid().equals(lastExit.getUid())){
                        if (Math.abs(now.getTimestamp() - lastExit.getTimestamp()) < 6000){
                            System.out.println("can't enter");
                        } else {
                            user = completeSend(event);
                            if(user != null){
                                recordList.put(event.getRead().getUid(), event.getRead().getTimestamp());
                            }
                        }
                    } else {
                        user = completeSend(event);
                        if(user != null){
                            recordList.put(event.getRead().getUid(), event.getRead().getTimestamp());
                        }
                    }

                }

            }
        }

        //if (user != null) {
        //    System.out.println("Apro tornello");
        //}

    }

    public User completeSend(ReadEvent event) {
        System.out.println(event.getRead() + "----- SENDING GET ------");
        String result = sendGet("http://192.168.1.92:8080/api/entrance", event.getRead().getUid());
        User user = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (result.equals("Nothing")) {
                System.out.println("No user to display");
            } else if (result.equals("Non ci sono ingressi disponibili")) {
                System.out.println("Non ci sono ingressi disponibili");
            } else {
                user = mapper.readValue(result, User.class);
                System.out.println(user.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    private String sendGet(String getUrl, String line) {
        String noresult = "no results from sendGet on " + getUrl + " - check logs";
        int numTry = 5;
        while (numTry > 0) {
            try {
                com.mashape.unirest.http.HttpResponse<String> R = Unirest.get(getUrl)
                        .header("Content-Type", "application/json")
                        .header("Cache-Control", "no-cache")
                        .queryString("uid", line)
                        .asString();
                return R.getBody().toString();

            } catch (Exception e) {
                numTry--;
                if (numTry == 0) {
                    e.printStackTrace();
                }
            }
        }
        return noresult;
    }
}
