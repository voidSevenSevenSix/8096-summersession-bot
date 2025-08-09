package org.summersession.motor;

import org.summersession.I2C.PCA9685;
import org.summersession.controlSystem.interfaces.MotorSpeed;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;

public class MotorL298N implements MotorSpeed{
    private PCA9685 controller;
    private int speedChannel;
    private int directionChannel1;
    private int directionChannel2;
    private boolean brake = true;
    private DigitalOutput in1;
    private DigitalOutput in2;

    public MotorL298N(int channel, int d1, int d2, PCA9685 pca9685){
        speedChannel = channel;
        directionChannel1 = d1;
        directionChannel2 = d2;
        this.controller = pca9685;
        /*Context pi4j = Pi4J.newAutoContext();
        var in1Config = DigitalOutput.newConfigBuilder(pi4j)
                .address(d1) //gpio id
                .provider("linuxfs-digital-output")
                .build();

        var in2Config = DigitalOutput.newConfigBuilder(pi4j)
                .address(d2) //gpio id
                .provider("linuxfs-digital-output")
                .build();

        in1 = pi4j.create(in1Config);
        in2 = pi4j.create(in2Config);
        in1.high();
        in2.high();*/
    }

    public void set(double speed){
        speed = Math.max(-1.0, Math.min(1.0, speed));
        int pwmScaled = (int)(Math.abs(speed) * 4095);
        if (speed > 0) {
            /*in1.high();
            in2.low();*/
            controller.setPWM(directionChannel1, 0, 4095);
            controller.setPWM(directionChannel2, 0, 0);
        } else if (speed < 0) {
            /*in1.low();
            in2.high();*/
            controller.setPWM(directionChannel2, 0, 4095);
            controller.setPWM(directionChannel1, 0, 0);
        } else if(brake){
            /*in1.high();
            in2.high();*/
            controller.setPWM(directionChannel1, 0, 4095);
            controller.setPWM(directionChannel2, 0, 4095);
            speed = 1.0; //cause the l298n is RETARDED
        }
        else{
            /*in1.low();
            in2.low();*/
            controller.setPWM(directionChannel1, 0, 0);
            controller.setPWM(directionChannel2, 0, 0);
        }
        controller.setPWM(speedChannel, 0, pwmScaled);
        
    }

    public void stop(){
        set(0.0);
    }

    public void setBrakeMode(boolean brake){
        this.brake = brake;
    }
}
