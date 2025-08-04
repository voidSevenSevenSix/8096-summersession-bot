package org.summersession;

import org.summersession.I2C.AS5600;
import org.summersession.I2C.BNO055;
import org.summersession.I2C.PCA9685;
import org.summersession.I2C.TCA9548A;
import org.summersession.controlSystem.ControlSystem;
import org.summersession.motor.Motor;

public class Robot {
    /* Initialize and register subsystems and bind controls here. */

    public Robot() {
        /* Do not alter */
        ControlSystem robotController = new ControlSystem();
        /* End do not alter */

        /* Begin bus declaration*/
        //PCA9685 motorBus0 = new PCA9685(0x40);

        /* Begin subsystem declaration and registration */
        //Motor motor = new Motor(0, motorBus0);
        //AS5600 as5600 = new AS5600();
        //BNO055 bno055 = new BNO055();
        
        /* Begin controller bindings */
        robotController.onX(
            ()->{
                
            }
        );

        /*ControlSystem.QuadConsumer<Float, Float, Float, Float> driveMotor = new ControlSystem.QuadConsumer<Float, Float, Float, Float>() {
            public void accept(Float leftX, Float leftY, Float rightX, Float rightY){
                motor.set(leftX.floatValue());
            }
        };

        robotController.setDrivetrain(driveMotor);*/


    }
}
