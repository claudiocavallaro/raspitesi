package com.unisa.raspitesi.model;

import lombok.Getter;

public class CameraEvent {

    @Getter
    private Camera camera;


    public CameraEvent(Camera camera){
        this.camera = camera;
    }
}
