package org.summersession.motor;

import org.summersession.I2C.PCA9685;

public class Servo{
    private PCA9685 controller;
    private int channel;
    private double curDeg = 0;

    public Servo(int channel, PCA9685 pca9685){
        this.channel = channel;
        this.controller = pca9685;
    }

    public void setPosition(double deg){
        controller.setServoAngle(channel, deg);
        curDeg = deg;
    }

    public void moveByDeg(double deg){
        curDeg += deg;
        if(curDeg > 180){
            curDeg = 180;
        }
        if(curDeg < 0){
            curDeg = 0;
        }
        setPosition(curDeg);
    }
}
