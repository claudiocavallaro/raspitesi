package com.unisa.raspitesi.model;

import lombok.Getter;

public class PowerEvent {

    @Getter
    private Power power;

    public PowerEvent(Power power){
        this.power = power;
    }
}
