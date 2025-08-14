package org.summersession;

public class DriveManager {
    public double currentCommandedHeading = 0;
    public double degPerCycle = 5; //how many degrees does it turn per 50ms cycle? TODO tune. this is unreliable as hell but is the only way to make this work

    public DriveManager(){

    }

    public int calcHeadingAndReturnCycles(float x, float y){ //number of cycles (directional based on sign) to run for
        double deg = Math.atan2(y, x) + (Math.atan2(y, x)<0?360:0);
        double diff = deg - currentCommandedHeading;
        currentCommandedHeading = deg;
        return (int)(diff/degPerCycle);
    }

}
