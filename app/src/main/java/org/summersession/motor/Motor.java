package org.summersession.motor;

import org.summersession.I2C.PCA9685;

public class Motor {
    private PCA9685 controller;
    private int forwardChannel;
    private int reverseChannel;
    private boolean brake = true;

    public Motor(int channel, PCA9685 pca9685){
        forwardChannel = channel * 2;
        reverseChannel = forwardChannel + 1;
        this.controller = pca9685;
    }

    public void set(double speed){
        speed = Math.max(-1.0, Math.min(1.0, speed));
        int pwmScaled = (int)(Math.abs(speed) * 4095);
        if (speed > 0) {
            controller.setPWM(forwardChannel, 0, pwmScaled);
            controller.setPWM(reverseChannel, 0, 0);
        } else if (speed < 0) {
            controller.setPWM(forwardChannel, 0, 0);
            controller.setPWM(reverseChannel, 0, pwmScaled);
        } else {
            controller.setPWM(forwardChannel, brake?4096:0, 0); 
            controller.setPWM(reverseChannel, brake?4096:0, 0);  
        }
    }

    public void stop(){
        set(0.0);
    }

    public void setBrakeMode(boolean brake){
        this.brake = brake;
    }
}
