package com.unisa.raspitesi.api;

import com.mashape.unirest.http.Unirest;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestComponent {



    private String sendGet(String getUrl) {

        String noresult = "no results from sendGet on " + getUrl + " - check logs";

        int numTry = 5;

        while(numTry > 0) {

            try{
                com.mashape.unirest.http.HttpResponse<String> R = Unirest.get(getUrl)
                        .header("Content-Type", "application/json")
                        .header("Cache-Control", "no-cache")
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
