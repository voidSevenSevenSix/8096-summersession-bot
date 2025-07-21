package org.summersession;

import org.summersession.I2C.EncoderBus;
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
        PCA9685 motorBus0 = new PCA9685(0x40);
        EncoderBus encoderBus0 = new EncoderBus(new TCA9548A(0x70));

        /* Begin subsystem declaration and registration */
        Motor motor = new Motor(0, motorBus0);
        
        /* Begin controller bindings */
        

    }
}
