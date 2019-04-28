package com.unisa.raspitesi;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class Application {

    public static void main(String[] args){
        SpringApplication springApplication = new SpringApplication(Application.class);
        springApplication.run(args);
    }




}
