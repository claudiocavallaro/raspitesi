package com.unisa.raspitesi.api;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.pi4j.io.gpio.*;
import com.unisa.raspitesi.model.*;
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

    //*************************** READ EVENT **************************************

    private static GpioPinDigitalOutput pin;


    private ConcurrentHashMap<String, Long> recordList = new ConcurrentHashMap<>();
    private Read lastExit = null;

    private long waitTime = 60 * 1000;

    @Async
    @EventListener
    public void readEvent(ReadEvent event) {

        User user = null;

        //System.out.println(recordList.keySet());

        /* Se la lista dei record è vuota vuol dire che nella stanza non c'è nessuno.
           Se non c'è nessuno quindi controllo l'ultimo andato via,
           se l'ultimo che è andato via è quello che ora sta arrivando controllo
           quanto tempo fa è andato via
        * */

        if (recordList.isEmpty()) {
            if(lastExit == null){
                user = completeSend(event);
                if(user != null){
                    recordList.put(event.getRead().getUid(), event.getRead().getTimestamp());
                }
            } else {

                Read now = event.getRead();
                //System.out.println("----NOW----" + now.toString());
                //System.out.println("----LAST FROM ENTER----" + lastExit.toString());
                if (now.getUid().equals(lastExit.getUid())){
                    if (Math.abs(now.getTimestamp() - lastExit.getTimestamp()) < waitTime){
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

            /*  Se la palestra non è vuota e la lettura proviene da uno all'interno lo faccio uscire
             *  ed aggiorno l'oggetto lastExit.
             *
             *  Se la lettura non proviene da uno all'interno controllo lastExit come nel metodo precedente.
             *
             */

            if (recordList.containsKey(event.getRead().getUid())) {

                //System.out.println("Valore già registrato " + recordList.get(event.getRead().getUid()));
                long timeArrive = recordList.get(event.getRead().getUid());
                long recordValue = event.getRead().getTimestamp();
                //System.out.println("Valore di confronto " + event.getRead().getTimestamp());

                if (Math.abs(timeArrive - recordValue) < waitTime) {
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
                        if (Math.abs(now.getTimestamp() - lastExit.getTimestamp()) < waitTime){
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

        if (user != null) {
            System.out.println("Apro tornello");
            if (pin == null) {
                GpioController gpio = GpioFactory.getInstance();
                pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "LED", PinState.LOW);
            }

            pin.toggle();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pin.toggle();
        }

    }

    public User completeSend(ReadEvent event) {
        System.out.println(event.getRead() + "----- SENDING GET ------");
        String result = sendGet("http://192.168.1.57:8080/api/entrance", event.getRead().getUid());
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



    //*************************** POWER EVENT ***************************

    @Async
    @EventListener
    public void powerPusblish(PowerEvent powerEvent){
        Power power = powerEvent.getPower();
        System.out.println(power.toString());

        // Qui posso anche fare in modo che se la palestra è vuota non manda alcuna get, giusto per risparmiare risorse

        System.out.println("----- SENDING GET ------");
        String result = sendGetPower("http://192.168.1.57:8080/api/power", powerEvent.getPower());
    }

    private String sendGetPower(String s, Power power) {
        String voltage = String.valueOf(power.getVoltage());
        String current = String.valueOf(power.getCurrent());
        String powerString = String.valueOf(power.getPower());

        String noresult = "no results from sendGet on " + s + " - check logs";
        int numTry = 5;
        while (numTry > 0) {
            try {
                com.mashape.unirest.http.HttpResponse<String> R = Unirest.get(s)
                        .header("Content-Type", "application/json")
                        .header("Cache-Control", "no-cache")
                        .queryString("voltage", voltage)
                        .queryString("current", current)
                        .queryString("power", powerString)
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



    //************************* CAMERA EVENT *****************************

    @Async
    @EventListener
    public void cameraPublish(CameraEvent cameraEvent){
        Camera camera = cameraEvent.getCamera();
        System.out.println(camera.toString());

        System.out.println("----- SENDING GET ------");
        String result = sendGetCamera("http://192.168.1.57:8080/api/camera", cameraEvent.getCamera());
    }

    private String sendGetCamera(String s, Camera camera) {

        String area = camera.getArea();
        String number = String.valueOf(camera.getNumber());

        String noresult = "no results from sendGet on " + s + " - check logs";
        int numTry = 5;
        while (numTry > 0) {
            try {
                com.mashape.unirest.http.HttpResponse<String> R = Unirest.get(s)
                        .header("Content-Type", "application/json")
                        .header("Cache-Control", "no-cache")
                        .queryString("area", area)
                        .queryString("number", number)
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
