package com.unisa.raspitesi.api;


import com.unisa.raspitesi.model.ReadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EventHandler {

    @Autowired
    private TaskExecutor taskExecutor;

    public EventHandler(){}

    @Async
    @EventListener
    public void readEvent(ReadEvent event){
        System.out.println(event.getRead());
    }
}
