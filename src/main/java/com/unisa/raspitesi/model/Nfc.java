package com.unisa.raspitesi.model;

import com.mashape.unirest.http.Unirest;
import com.unisa.raspitesi.api.EventPublisherService;
import com.unisa.raspitesi.model.Read;
import com.unisa.raspitesi.model.ReadEvent;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class Nfc {



    /*public Nfc(){

        String line = "";
        boolean flag = false;
        while (flag == false) {
            ProcessBuilder pb = new ProcessBuilder("python", this.getClass().getResource("/readNoT.py").getPath());
            try {
                Process p = pb.start();

                BufferedReader bfr = new BufferedReader(new InputStreamReader(p.getInputStream()));
                line = bfr.readLine();

                int exit = 1;
                while ((line = bfr.readLine())!= null) {
                    System.out.println(line);
                    if (!line.equals("No card")){
                        //sendGet("http://192.168.1.92:8080/api/entrance", line);
                        exit = 0;
                        break;
                    }
                }

                if(exit == 0) {
                    flag = true;
                }

                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        sendGet("http://192.168.1.92:8080/api/entrance", line);
    }*/


    /*public Nfc(){

        System.out.println("Arrivo");
        Read read = new Read("lettura");

        System.out.println(read.getUid());
        ReadEvent event = new ReadEvent(read);
        EventPublisherService.eventPublisherService.publishEvent(event);

    }*/

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
