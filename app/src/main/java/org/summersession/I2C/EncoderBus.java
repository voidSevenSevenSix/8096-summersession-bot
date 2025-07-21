package org.summersession.I2C;

public class EncoderBus {
    private final AS5600 as5600 = new AS5600();
    private final TCA9548A tca9548a;
    
    public EncoderBus(TCA9548A tca9548a){
        this.tca9548a = tca9548a;
    }

    public double readEncoderDegrees(int busId){
        tca9548a.setActiveChannel(busId);
        try{
            return as5600.readAngleDegrees();
        }
        catch(Exception e){
            System.out.println("[EncoderBus]: Reading encoder failed: " + e);
            return 0;
        }
    }
}
