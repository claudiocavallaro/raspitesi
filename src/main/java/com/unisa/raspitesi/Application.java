package com.unisa.raspitesi;

import com.mashape.unirest.http.Unirest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class Application {

    public static void main(String[] args){

        //SpringApplication.run(Application.class, args);

        Application app = new Application();
    }


    public Application(){
        sendGet("http://localhost:8080/api/entrance");
    }

    private String sendGet(String getUrl) {

        String noresult = "no results from sendGet on " + getUrl + " - check logs";

        int numTry = 5;

        while(numTry > 0) {

            try{
                com.mashape.unirest.http.HttpResponse<String> R = Unirest.get(getUrl)
                        .header("Content-Type", "application/json")
                        .header("Cache-Control", "no-cache")
                        .queryString("uid", "uid1")
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
