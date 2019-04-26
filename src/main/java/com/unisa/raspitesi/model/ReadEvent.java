package com.unisa.raspitesi.model;


import lombok.Getter;

public class ReadEvent {

    @Getter
    private Read read;

    public ReadEvent(Read read){
        this.read = read;
    }
}
