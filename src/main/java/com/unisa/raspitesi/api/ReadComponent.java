package com.unisa.raspitesi.api;

import com.unisa.raspitesi.configuration.EventPublisherService;
import com.unisa.raspitesi.model.Read;
import com.unisa.raspitesi.model.ReadEvent;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
public class ReadComponent implements DisposableBean, Runnable {

    private Thread thread;
    private volatile boolean flag = true;


    public ReadComponent(){
        this.thread = new Thread(this);
        this.thread.start();
    }


    @Override
    public void run() {
        //String lastValue = "";
        //long time = 0;
        while(flag == true){

            Read read = null;
            String line = "";
            ProcessBuilder pb = new ProcessBuilder("python", this.getClass().getResource("/readNoT.py").getPath());
            try {
                Process p = pb.start();

                BufferedReader bfr = new BufferedReader(new InputStreamReader(p.getInputStream()));
                line = bfr.readLine();

                int exit = 1;
                while ((line = bfr.readLine()) != null) {
                    System.out.println(line);
                    if (!line.equals("No card")) {
                        read = new Read(line);
                        read.setTimestamp(System.currentTimeMillis());
                    }
                }


            } catch (Exception e){
                e.printStackTrace();
            }

            //Se il tag l'ho passato meno di 60 secondi prima non fa nessuna richiesta
            //Successivamente si può aumentare questo tempo

            if(read != null){
                //if(read.getUid().equals(lastValue) && read.getTimestamp() - time < 60000){
                //    System.out.println("Nothing to publish");
                //} else {
                    ReadEvent event = new ReadEvent(read);
                    EventPublisherService.eventPublisherService.publishEvent(event);
                    //lastValue = read.getUid();
                    //time = read.getTimestamp();
                //}
            }


            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void destroy() throws Exception {

    }
}
