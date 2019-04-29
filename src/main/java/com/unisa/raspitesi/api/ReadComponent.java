package com.unisa.raspitesi.api;

import com.unisa.raspitesi.configuration.EventPublisherService;
import com.unisa.raspitesi.model.Read;
import com.unisa.raspitesi.model.ReadEvent;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

@Component
public class ReadComponent implements DisposableBean, Runnable {

    private Thread thread;
    private volatile boolean flag = true;

    private ArrayList<Read> lista = new ArrayList<>();


    public ReadComponent(){
        this.thread = new Thread(this);
        this.thread.start();
    }


    @Override
    public void run() {
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

            lista.add(read);

            if(read != null && lista.size() > 0 && condition(read) == true){
                ReadEvent event = new ReadEvent(read);
                EventPublisherService.eventPublisherService.publishEvent(event);
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean condition(Read read) {
        boolean cond = false;
        for(Read r : lista){
            if (read.getUid().equals(r.getUid())){
                if (read.getTimestamp() - r.getTimestamp() < 60000){
                    cond =  false;
                } else {
                    cond = true;
                }
            }
        }
        return cond;
    }


    @Override
    public void destroy() throws Exception {

    }
}
