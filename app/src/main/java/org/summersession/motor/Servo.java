package org.summersession.motor;

import org.summersession.I2C.PCA9685;

public class Servo{
    private PCA9685 controller;
    private int channel;

    public Servo(int channel, PCA9685 pca9685){
        this.channel = channel;
        this.controller = pca9685;
    }

    public void setPosition(double deg){
        controller.setServoAngle(channel, deg);
    }
}
