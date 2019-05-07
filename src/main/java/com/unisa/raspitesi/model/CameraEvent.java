package com.unisa.raspitesi.model;

import lombok.Getter;

public class CameraEvent {

    @Getter
    private Camera camera;


    private CameraEvent(Camera camera){
        this.camera = camera;
    }
}
