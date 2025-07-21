package org.summersession.I2C;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfig;
import com.pi4j.io.i2c.I2CProvider;

public class AS5600 {

    private final I2C i2c;
    private Context pi4j = Pi4J.newAutoContext();

    public AS5600() {
        I2CProvider provider = pi4j.provider("linuxfs-i2c");

        I2CConfig config = I2C.newConfigBuilder(pi4j)
            .bus(1)
            .device(0x36)
            .build();

        this.i2c = provider.create(config);
    }

    public int readRawAngle() throws Exception {
        byte[] buffer = new byte[2];
        i2c.readRegister(0x0C, buffer);
        return ((buffer[0] & 0x0F) << 8) | (buffer[1] & 0xFF);
    }

    public double readAngleDegrees() throws Exception {
        return readRawAngle() * (360.0 / 4096.0);
    }
}

