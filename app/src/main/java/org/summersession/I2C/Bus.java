/* Note: class written specifically for our configuration on the i2c bus, not designed to be reused or implementable for other configurations */

/* 
 * Bus configuration
 * ID 0: BNO055 IMU
 * IDs 1-4: DRV8833 controllers
 * IDs 5-12: L298N controllers
 * IDs 13-16: AS5600 encoders
 */

package org.summersession.I2C;

import org.summersession.controlSystem.interfaces.MotorSpeed;
import org.summersession.motor.Motor;
import org.summersession.motor.MotorL298N;

public class Bus {
    private AS5600 as5600; //single object encoder, access different ones with multiplexer
    private BNO055 bno055; //imu
    private TCA9548A tca9548a; //multiplexer
    private MotorSpeed motors[] = new MotorSpeed[12]; //first 4 are drv, last 8 are l298n
    
    public Bus(TCA9548A tca9548a, BNO055 bno055, AS5600 as5600){
        this.tca9548a = tca9548a;
        this.bno055 = bno055;
        this.as5600 = as5600;
    }

    public void initializeMotor(MotorSpeed m, int id){
        motors[id-1] = m;
    }

    public double readEncoderDegrees(int id){
        tca9548a.setActiveChannel(id-13);
        try{
            return as5600.readAngleDegrees();
        }
        catch(Exception e){
            System.out.println("Warn: Failed to read angle from ID " + id + " channel " + (id-13));
            return 0.0;
        }
    }

    public void setMotorSpeed(int id, int speed){
        motors[id-1].set(speed);
    }

    public void setMotorBrakeMode(int id, boolean brakeModeEnabled){
        motors[id-1].setBrakeMode(brakeModeEnabled);
    }

    //TODO actually do useful stuff w/ the imu
    public void dumpImuInfo(){
        bno055.printData();
        bno055.linearAcceleration();
    }
}
