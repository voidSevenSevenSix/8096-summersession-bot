package org.summersession.controlSystem;

public class ControllerState {
    public boolean leftTrigger;
    public boolean rightTrigger;
    public boolean leftBumper;
    public boolean rightBumper;
    public boolean a;
    public boolean b;
    public boolean x;
    public boolean y;
    public boolean up;
    public boolean down;
    public boolean left;
    public boolean right;
    public boolean start;
    public float leftX;
    public float leftY;
    public float rightX;
    public float rightY;

    public ControllerState(boolean leftTrigger, boolean rightTrigger, boolean leftBumper, boolean rightBumper, boolean a, boolean b, boolean x, boolean y, boolean up, boolean down, boolean left, boolean right, boolean start, float leftX, float leftY, float rightX, float rightY){
        this.leftTrigger = leftTrigger;
        this.rightTrigger = rightTrigger;
        this.leftBumper = leftBumper;
        this.rightBumper = rightBumper;
        this.a = a;
        this.b = b;
        this.x = x;
        this.y = y;
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.leftX = leftX;
        this.leftY = leftY;
        this.rightX = rightX;
        this.rightY = rightY;
        this.start = start;
    }
}

