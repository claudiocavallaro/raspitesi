package com.unisa.raspitesi;


import com.unisa.raspitesi.api.EventPublisherService;
import com.unisa.raspitesi.model.Nfc;

import com.unisa.raspitesi.model.Read;
import com.unisa.raspitesi.model.ReadEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class Application {

    public static void main(String[] args){
        SpringApplication springApplication = new SpringApplication(Application.class);
        springApplication.run(args);
    }




}
