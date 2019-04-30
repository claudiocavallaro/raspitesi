package com.unisa.raspitesi.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;

import javax.print.DocFlavor;

@Data
public class User {

    private String name;
    private String bornDate;
    private int ingressi;
    private boolean inside;

    private String uid;

    @JsonCreator
    public User(){}

    private long timeStamp;

    public User(String name, String bornDate, int ingressi, String uid){
        this.name = name;
        this.bornDate = bornDate;
        this.ingressi = ingressi;
        this.uid = uid;
        this.inside = false;
    }

    public String toString(){
        return this.name + " " + this.bornDate + "\nUID: " + this.uid;
    }

}