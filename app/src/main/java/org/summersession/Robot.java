package org.summersession;

import org.summersession.I2C.AS5600;
import org.summersession.I2C.BNO055;
import org.summersession.I2C.MultiplexerMinibus;
import org.summersession.I2C.PCA9685;
import org.summersession.I2C.TCA9548A;
import org.summersession.controlSystem.ControlSystem;
import org.summersession.controlSystem.TimedCycleScheduler;
import org.summersession.motor.Motor;
import org.summersession.motor.MotorL298N;
import org.summersession.motor.Servo;

public class Robot {
    /* Initialize and register subsystems and bind controls here. */

    /************************* IMPORTANT **************************
     * If you have not wired one of the i2c devices and would like to test out the rest, you MUST comment out
     * the objects corresponding to the unwired device and all of its uses. The i2c bus will encounter errors
     * if it expects a device that is not wired (note: it may be possible to run with those errors, and you are
     * free to try this, but if it fails definitely follow the above instructions).
    */

    public Robot() {
        /* Do not alter */
        ControlSystem robotController = new ControlSystem();
        TimedCycleScheduler tcs = new TimedCycleScheduler();
        /* End do not alter */

        /* Begin various objects on i2c bus declarations */
        System.out.println("Begin i2c bus declarations.");
        PCA9685 hat0 = new PCA9685(0x40); //this is the pwm hat that does NOT have any soldered connections for address changes
        // PCA9685 hat1 = new PCA9685(0x41); //this is the pwm hat that DOES have a soldered connection for the address change - i put 0x41 but that may not be the correct address
        // TCA9548A multiplexer = new TCA9548A(0x70); //this is the i2c multiplexer, do NOT alter its i2c address. it goes DIRECTLY ON THE i2c BUS (sda and scl pins)
        // AS5600 encoder = new AS5600(); //this is all of the as5600 encoders, they all go ON THE MULTIPLEXER SDA and SCL PINS (each one for one pair). because of how they work with the multiplexer they all "share" an object, which is retarded as fuck but is the only way to make it work, which means the multiplexer selects the encoder and this object actually gets it. idk if you actaully need to understand this
        // MultiplexerMinibus multibus = new MultiplexerMinibus(multiplexer, encoder);
        // BNO055 gyro = new BNO055(); //this is the gyroscope, right now it just prints cool stuff, it goes DIRECTLY ON THE i2c BUS (sda and scl pins)
        System.out.println("End i2c bus declarations.");

        /************* DRIVE CONFIG *************/
        int driveEN = 0; //either ENA or ENB
        int driveIN1 = 1; //either IN1 or IN3
        int driveIN2 = 2; //either IN2 or IN4
        MotorL298N allDriveMotors = new MotorL298N(driveEN, driveIN1, driveIN2, hat0);
    
        int steeringEN = 3; //either ENA or ENB
        int steeringIN1 = 4; //either IN1 or IN3
        int steeringIN2 = 5; //IN2 or IN4
        MotorL298N allSteeringMotors = new MotorL298N(steeringEN, steeringIN1, steeringIN2, hat0);

        ControlSystem.QuadConsumer<Float, Float, Float, Float> driveControl = new ControlSystem.QuadConsumer<Float, Float, Float, Float>() {
            public void accept(Float leftX, Float leftY, Float rightX, Float rightY){
                allDriveMotors.set(leftY);
                allSteeringMotors.set(rightY);
            }
        };
        robotController.setDrivetrain(driveControl);

        /************ SERVO CONFIG ***********/
        int servoPair1Pin = 6;
        int servoPair2Pin = 7;
        int servoIndivPin = 8;
        Servo servoPair1 = new Servo(servoPair1Pin, hat0);
        Servo servoPair2 = new Servo(servoPair2Pin, hat0);
        Servo servoIndiv = new Servo(servoIndivPin, hat0);

        robotController.onA(
            ()->{
                servoPair1.moveByDeg(5.0);
                servoPair2.moveByDeg(5.0);
            }
        );
        robotController.onB(
            ()->{
                servoPair1.moveByDeg(-5.0);
                servoPair2.moveByDeg(-5.0);
            }
        );
        robotController.onX(
            ()->{
                servoIndiv.setPosition(15.0);
            }
        );
        robotController.onY(
            ()->{
                servoIndiv.setPosition(15.0);
            }
        );
        


        /* Begin motor objects declarations */
        /* The DRIVE motors on the L298Ns will go on the hat that did NOT have its address changed (hat0), and should be connected as follows:
         * Motor0: EN -> 0, IN -> 1, IN -> 2
         * Motor1: EN -> 3, IN -> 4, IN -> 5
         * Motor2: EN -> 6, IN -> 7, IN -> 8
         * Motor3: EN -> 9, IN -> 10, IN -> 11
         * Switching which IN pin is connected to which PWM hat pin will determine relative direction of motor, Misha should know about this setup
         */
        // System.out.println("Begin L298N declarations.");
        // MotorL298N driveMotor0 = new MotorL298N(0, 1, 2, hat0);
        // MotorL298N driveMotor1 = new MotorL298N(3, 4, 5, hat0);
        // MotorL298N driveMotor2 = new MotorL298N(6, 7, 8, hat0);
        // MotorL298N driveMotor3 = new MotorL298N(9, 10, 11, hat0);
        // System.out.println("End L298N declarations.");

        /* The STEERING motors on the DRV8833s will go on the hat that DID have its address changed (hat1), and should be connected as follows:
         * Motor0: IN -> 0, IN -> 1
         * Motor1: IN -> 2, IN -> 3
         * Motor2: IN -> 4, IN -> 5
         * Motor3: IN -> 6, IN -> 7
         */
        // System.out.println("Begin DRV8833 declarations.");
        // Motor steeringMotor0 = new Motor(0, hat1);
        // Motor steeringMotor1 = new Motor(1, hat1);
        // Motor steeringMotor2 = new Motor(2, hat1);
        // Motor steeringMotor3 = new Motor(3, hat1);
        // System.out.println("End DRV8833 declarations.");

        /* The SERVOS for testing will go on the hat that DID have its address changed (hat1), and should be connected as follows:
         * Servo0: Control pin -> 8
         */
        // Servo servo0 = new Servo(8, hat1);

        /* Motor control system declaration
         * The left joystick Y axis is a THROTTLE for the DRIVING motors
         * The right joystick X axis is a THROTTLE for the STEERING motors
         * This is by no means a drivetrain - it would SUCK to control and does not use PID or anything to maintain heading, but it will do for just testing the motors
        */
        // DriveManager drvmgr = new DriveManager();
        // System.out.println("Begin motor control system declaration.");
        // ControlSystem.QuadConsumer<Float, Float, Float, Float> driveMotor = new ControlSystem.QuadConsumer<Float, Float, Float, Float>() {
        //     public void accept(Float leftX, Float leftY, Float rightX, Float rightY){
        //         if(leftX > 0 || leftY > 0){
        //             //steering
        //             int cycles = drvmgr.calcHeadingAndReturnCycles(leftX, leftY);
        //             int sig = Integer.signum(cycles);
        //             cycles = cycles * sig;
        //             if(cycles > 0){
        //                 tcs.addTimedCycleScheduledRunnable(
        //                 ()->{
        //                     steeringMotor0.set(sig);
        //                     steeringMotor1.set(sig);
        //                     steeringMotor2.set(sig);
        //                     steeringMotor3.set(sig);
        //                 }, cycles);
        //                 tcs.addTimedCycleScheduledRunnable(
        //                 ()->{
        //                     steeringMotor0.set(0);
        //                     steeringMotor1.set(0);
        //                     steeringMotor2.set(0);
        //                     steeringMotor3.set(0);
        //                 }, 1);
        //             }
        //         }
        //         //drive
        //         double power = Math.sqrt(Math.pow(leftX, 2) + Math.pow(leftY, 2))/(Math.sqrt(2));
        //         double directional = power*Math.signum(leftY);
        //         driveMotor0.set(directional);
        //         driveMotor1.set(directional);
        //         driveMotor2.set(directional);
        //         driveMotor3.set(directional);
        //     }
        // };

        /* Other bindings 
         * A -> Dumps gyroscope data
         * B -> Increment the active encoder channel
         * X -> Dump encoder data from active encoder
         * Dpad Left -> Set servo to 0deg
         * Dpad Right -> Set servo to 180deg
        */
        // robotController.onA(
        //     ()->{
        //         gyro.printData();
        //     }
        // );
        // robotController.onB(
        //     ()->{
        //         multibus.incrementChannel();
        //         System.out.println("The active encoder channel is now " + multibus.chan);
        //     }
        // );
        // robotController.onX(
        //     ()->{
        //         System.out.println("The value of the encoder on channel " + multibus.chan + " is " + multibus.getActiveAngleDeg());
        //     }
        // );
        // robotController.onLeft(
        //     ()->{
        //         servo0.setPosition(0);
        //         System.out.println("The position of servo0 has been set to 0deg.");
        //     }
        // );
        // robotController.onRight(
        //     ()->{
        //         servo0.setPosition(180);
        //         System.out.println("The position of servo0 has been set to 180deg.");
        //     }
        // );

        // robotController.setDrivetrain(driveMotor);
        // System.out.println("Motors are now all active.");

        // System.out.println("End motor control system declaration.");
        // System.out.println("The current selected encoder channel is " + multibus.chan);
        // System.out.println("Initialization thread ended.");
    }
}
