package org.summersession;

import org.summersession.I2C.AS5600;
import org.summersession.I2C.BNO055;
import org.summersession.I2C.PCA9685;
import org.summersession.I2C.TCA9548A;
import org.summersession.controlSystem.ControlSystem;
import org.summersession.motor.Motor;
import org.summersession.motor.MotorL298N;

public class Robot {
    /* Initialize and register subsystems and bind controls here. */

    public Robot() {
        /* Do not alter */
        ControlSystem robotController = new ControlSystem();
        /* End do not alter */

        /* Begin bus declaration*/
        PCA9685 motorBus0 = new PCA9685(0x40);

        /* Begin subsystem declaration and registration */
        /*Motor motor = new Motor(0, motorBus0);
        Motor motor1 = new Motor(1, motorBus0);
        Motor motor2 = new Motor(2, motorBus0);
        Motor motor3 = new Motor(3, motorBus0);*/
        MotorL298N motorL = new MotorL298N(0, 1, 2, motorBus0);
        MotorL298N motorL1 = new MotorL298N(3, 4, 5, motorBus0);
        MotorL298N motorL2 = new MotorL298N(6, 7, 8, motorBus0);
        MotorL298N motorL3 = new MotorL298N(9, 10, 11, motorBus0);

        //AS5600 as5600 = new AS5600();
        //BNO055 bno055 = new BNO055();
        
        /* Begin controller bindings */
        robotController.onX(
            ()->{
                
            }
        );

        ControlSystem.QuadConsumer<Float, Float, Float, Float> driveMotor = new ControlSystem.QuadConsumer<Float, Float, Float, Float>() {
            public void accept(Float leftX, Float leftY, Float rightX, Float rightY){
                /*motor.set(leftX.floatValue());
                motor1.set(leftX.floatValue());
                motor2.set(leftX.floatValue());
                motor3.set(leftX.floatValue());*/
                motorL.set(leftX.floatValue());
                motorL1.set(leftX.floatValue());
                motorL2.set(leftX.floatValue());
                motorL3.set(leftX.floatValue());
            }
        };

        robotController.setDrivetrain(driveMotor);


    }
}
