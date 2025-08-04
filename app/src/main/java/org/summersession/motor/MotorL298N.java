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
        Context pi4j = Pi4J.newAutoContext();
        var in1Config = DigitalOutput.newConfigBuilder(pi4j)
                .address(d1) //gpio id
                .build();

        var in2Config = DigitalOutput.newConfigBuilder(pi4j)
                .address(d2) //gpio id
                .build();

        DigitalOutput in1 = pi4j.create(in1Config);
        DigitalOutput in2 = pi4j.create(in2Config);
        in1.low();
        in2.low();
    }

    public void set(double speed){
        speed = Math.max(-1.0, Math.min(1.0, speed));
        int pwmScaled = (int)(Math.abs(speed) * 4095);
        if (speed > 0) {
            in1.high();
            in2.low();
        } else if (speed < 0) {
            in1.low();
            in2.high();
        } else if(brake){
            in1.high();
            in2.high();
            speed = 1.0; //cause the l298n is RETARDED
        }
        else{
            in1.low();
            in2.low();
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
