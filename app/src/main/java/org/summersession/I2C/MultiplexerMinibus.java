package org.summersession.I2C;

public class MultiplexerMinibus {
    public int chan = 0;
    public TCA9548A multiplexer;
    public AS5600 encoder;
    
    public MultiplexerMinibus(TCA9548A tca9548a, AS5600 as5600){
        multiplexer = tca9548a;
        encoder = as5600;
    }
    
    public double getActiveAngleDeg(){
        try{
            return encoder.readAngleDegrees();
        }
        catch(Exception e){
            System.out.println("Warning: MultiplexerMinibus failed to read encoder data from encoder on channel " + chan);
            return 0.0;
        }
    }

    public void setChannel(int c){
        chan = c;
    }

    public void incrementChannel(){
        chan++;
    }
}
