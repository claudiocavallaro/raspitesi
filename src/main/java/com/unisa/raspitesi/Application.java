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

    }




}
