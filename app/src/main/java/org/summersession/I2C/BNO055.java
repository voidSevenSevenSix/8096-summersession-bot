package org.summersession.I2C;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfig;
import com.pi4j.io.i2c.I2CProvider;

public class BNO055 {

    private final I2C i2c;
    private Context pi4j = Pi4J.newAutoContext();
    private byte[] buffer = new byte[6];

    public BNO055() {
        I2CProvider provider = pi4j.provider("linuxfs-i2c");

        I2CConfig config = I2C.newConfigBuilder(pi4j)
            .bus(1)
            .device(0x28)
            .build();

        this.i2c = provider.create(config);
        try{
            writeCalibrationProfile();
        }
        catch(Exception e){
            System.out.println("Warn: Failed to write calibration profile to gyro");
        }
        i2c.writeRegister((byte)0x3D, (byte)0x0C);
    }

    public void printData() {
        i2c.write((byte)0x1A); // Start at Euler H LSB
        i2c.read(buffer, 0, 6);
        int heading = (buffer[1] << 8 | (buffer[0] & 0xFF)) / 16;
        int roll = (buffer[3] << 8 | (buffer[2] & 0xFF)) / 16;
        int pitch = (buffer[5] << 8 | (buffer[4] & 0xFF)) / 16;
        System.out.printf("Heading: %d, Roll: %d, Pitch: %d\n", heading, roll, pitch);
    }

    public void calibration(){
        byte[] buffer1 = new byte[1];
        i2c.write((byte) 0x35);
        i2c.read(buffer1, 0, 1);
        int calibStat = buffer1[0] & 0xFF;
        int sysCal = (calibStat >> 6) & 0x03;
        int gyroCal = (calibStat >> 4) & 0x03;
        int accelCal = (calibStat >> 2) & 0x03;
        int magCal = calibStat & 0x03;
        System.out.printf("SYS: %d, GYRO: %d, ACCEL: %d, MAG: %d\n",
                sysCal, gyroCal, accelCal, magCal);
    }

    public void readCalOffsets(){
        byte[] calibData = new byte[22];
        i2c.write((byte) 0x55); // Start address of calibration profile
        i2c.read(calibData, 0, 22);
        System.out.println("Calibration profile read: ");
        for(byte b : calibData){
            System.out.println(b);
        }
    }

    public void linearAcceleration(){
        byte[] buffer1 = new byte[6];
        i2c.write((byte) 0x28); // Linear Acceleration X LSB
        i2c.read(buffer1, 0, 6);
        int x = (short) ((buffer1[1] << 8) | (buffer1[0] & 0xFF));
        int y = (short) ((buffer1[3] << 8) | (buffer1[2] & 0xFF));
        int z = (short) ((buffer1[5] << 8) | (buffer1[4] & 0xFF));
        double accelX = x / 100.0; 
        double accelY = y / 100.0;
        double accelZ = z / 100.0;
        System.out.println("Acceleration m/ss: " + accelX + " " + accelY + " " + accelZ);
    }

    public void writeCalibrationProfile() throws InterruptedException {
        byte[] calibData = new byte[]{
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-32,1
        };
        i2c.writeRegister((byte) 0x3D, (byte) 0x00);
        Thread.sleep(25);
        for (int i = 0; i < 22; i++) {
            i2c.writeRegister((byte)(0x55 + i), calibData[i]);
        }
        i2c.writeRegister((byte) 0x3D, (byte) 0x0C);
        Thread.sleep(25);
        System.out.println("Calibration profile restored.");
    }
}

