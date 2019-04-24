package com.unisa.raspitesi;

import com.mashape.unirest.http.Unirest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@EnableSwagger2
@SpringBootApplication
public class Application {

    public static void main(String[] args){

        //SpringApplication.run(Application.class, args);

        Application app = new Application();
    }


    public Application(){

/*        while (true) {
            ProcessBuilder pb = new ProcessBuilder("python","readNoT.py");
            try {
                Process p = pb.start();

                BufferedReader bfr = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = "";
                line = bfr.readLine();

                while ((line = bfr.readLine())!= null) {
                    System.out.println(line);
                }

                if (!(line.equals("No card"))){
                    break;
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

        sendGet("http://192.168.1.92.:8080/api/entrance");
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
