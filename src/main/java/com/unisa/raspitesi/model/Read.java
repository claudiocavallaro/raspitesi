package com.unisa.raspitesi.model;

import lombok.Data;

@Data
public class Read {

    private long timestamp;
    private String uid;


    public Read(String uid){
        this.uid = uid;
        this.timestamp = System.currentTimeMillis();
    }

    public Read(){}


    public String toString(){
        return "UID: " + uid + " timestamp : " + timestamp;
    }
}
