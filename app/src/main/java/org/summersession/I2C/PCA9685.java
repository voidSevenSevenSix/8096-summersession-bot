package org.summersession.I2C;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfig;
import com.pi4j.io.i2c.I2CProvider;

public class PCA9685 {
    private final I2C pca9685;
    private Context pi4j = Pi4J.newAutoContext();

    public PCA9685(int addr){
        I2CConfig config = I2C.newConfigBuilder(pi4j)
            .bus(1) 
            .device(addr)
            .build();
        I2CProvider i2cProvider = pi4j.provider("linuxfs-i2c");
        pca9685 = i2cProvider.create(config);

        pca9685.writeRegister(0x00, 0x00); //reset before starting
        setPWMFrequency(20000);
        pca9685.writeRegister(0x00, 0x20); //turns on writing to multiple registers or smth
    }

    /* Taken from online */
    public void setPWMFrequency(double freq) {
        // Calculate prescale value
        double prescaleval = 25000000.0; // 25MHz
        prescaleval /= 4096.0; // 12-bit
        prescaleval /= freq;
        prescaleval -= 1.0;
        int prescale = (int)(prescaleval + 0.5);
        
        // Save current mode
        int oldmode = pca9685.readRegister(0x00);
        int newmode = (oldmode & 0x7F) | 0x10; // sleep
        
        // Go to sleep
        pca9685.writeRegister(0x00, newmode);
        
        // Set prescale
        pca9685.writeRegister(0xFE, prescale);
        
        // Wake up
        pca9685.writeRegister(0x00, oldmode);
        try { Thread.sleep(5); } catch (InterruptedException e) {}
        
        // Restart
        pca9685.writeRegister(0x00, oldmode | 0x80);
    }

    public void setPWM(int channel, int on, int off){
        int reg = 0x06 + (4 * channel); //calc the first of the 4 registers
        pca9685.writeRegister(reg, 0);
        pca9685.writeRegister(reg + 1, on >> 8);
        pca9685.writeRegister(reg + 2, 0);
        pca9685.writeRegister(reg + 3, off >> 8);
    }
}
