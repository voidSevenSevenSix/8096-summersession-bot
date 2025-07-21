package org.summersession;

public class Utils {
    public static float applyDeadband(float value, double band){
        if(Math.abs(value) < band){
            return 0;
        }
        return value;
    }
}
