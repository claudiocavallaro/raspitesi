package com.unisa.raspitesi;

import com.mashape.unirest.http.Unirest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;

@EnableSwagger2
@SpringBootApplication
public class Application {

    public static void main(String[] args){

        //SpringApplication.run(Application.class, args);

        SpringApplication springApplication = new SpringApplication(Application.class);
        springApplication.run(args);


        Application app = new Application();
    }


    public Application(){

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
