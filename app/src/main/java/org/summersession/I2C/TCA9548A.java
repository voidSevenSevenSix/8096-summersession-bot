package org.summersession.I2C;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfig;
import com.pi4j.io.i2c.I2CProvider;

public class TCA9548A {
    private final I2C tca9548a;
    private Context pi4j = Pi4J.newAutoContext();

    public TCA9548A(int addr){
        I2CConfig config = I2C.newConfigBuilder(pi4j)
            .bus(1) 
            .device(addr)
            .build();
        I2CProvider i2cProvider = pi4j.provider("linuxfs-i2c");
        tca9548a = i2cProvider.create(config);
        tca9548a.write((byte) (1 << 0));
    }

    public void setActiveChannel(int channel){
        tca9548a.write((byte) (1 << channel));
    }
}
