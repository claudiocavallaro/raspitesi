package com.unisa.raspitesi.model;

import lombok.Data;

@Data
public class Power {


    private int voltage;
    private int power;
    private double current;

    public Power(int voltage, double current, int power){
        this.voltage = voltage;
        this.current = current;
        this.power = power;
    }

    public Power(){}

    public String toString(){
        return " ---VOLTAGE : " + voltage + " ---CURRENT : " + current + " ---POWER : " + power;
    }

}
