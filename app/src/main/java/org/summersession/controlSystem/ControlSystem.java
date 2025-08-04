package org.summersession.controlSystem;

import java.util.LinkedList;
import java.util.Optional;

import org.summersession.controlSystem.interfaces.Subsystem;

import com.google.gson.Gson;

public class ControlSystem {
    /* General */
    private Comms comms;
    private Gson gson = new Gson();
    /* Drivetrain */
    public interface QuadConsumer<T, U, V, W> {
        void accept(T t, U u, V v, W w);
    }
    private Optional<QuadConsumer<Float, Float, Float, Float>> drivetrain = Optional.empty(); //left x, left y, right x, right y
    /* Bindings */
    public Runnable leftTrigger = ()->{};
    public Runnable rightTrigger = ()->{};
    public Runnable leftBumper = ()->{};
    public Runnable rightBumper = ()->{};
    public Runnable a = ()->{};
    public Runnable b = ()->{};
    public Runnable x = ()->{};
    public Runnable y = ()->{};
    public Runnable up = ()->{};
    public Runnable down = ()->{};
    public Runnable left = ()->{};
    public Runnable right = ()->{};
    public float leftX = 0;

    private LinkedList<Subsystem> registeredSubsystems = new LinkedList<Subsystem>();

    private Runnable controlLoop = ()->{
        System.out.println("Control loop started.");
        while(true){
            Optional<String> dataOptional = comms.readComms();
            if (dataOptional.isPresent()) {
                String data = dataOptional.get();
                ControllerState state = gson.fromJson(data, ControllerState.class);
                /* control loop */
                for(Subsystem s : registeredSubsystems){
                    s.periodic();
                }
                /* drivetrain */
                if(drivetrain.isPresent()){
                    drivetrain.get().accept(state.leftX, state.leftY, state.rightX, state.rightY);
                }
                /* i can't be bothered to think of a better way of doing this w/ discrete fields, it's too late */
                if(state.leftTrigger){
                    leftTrigger.run();
                }
                if (state.rightTrigger) {
                    rightTrigger.run();
                }
                if (state.leftBumper) {
                    leftBumper.run();
                }
                if (state.rightBumper) {
                    rightBumper.run();
                }
                if (state.a) {
                    a.run();
                }
                if (state.b) {
                    b.run();
                }
                if (state.x) {
                    x.run();
                }
                if (state.y) {
                    y.run();
                }
                if (state.up) {
                    up.run();
                }
                if (state.down) {
                    down.run();
                }
                if (state.left) {
                    left.run();
                }
                if (state.right) {
                    right.run();
                }
                if (state.start) {
                    System.out.println("[ControlSystem.java]: Exiting.");
                    break;
                }
            }
        }
    };

    public ControlSystem(){
        comms = new Comms();
        if (!comms.connected()) {
            System.out.println("[ControlSystem.java]: Comms failed to connect.");
            return;
        }
        System.out.println("[ControlSystem.java]: Comms connected.");
        new Thread(controlLoop).start();
    }

    public void registerSubsystem(Subsystem s){
        registeredSubsystems.add(s);
    }

    public void setDrivetrain(QuadConsumer<Float, Float, Float, Float> consumer){
        this.drivetrain = Optional.of(consumer);
    }

    /* could i use a map? yes. but i am trying to be consistent w/ how wpilib does it incase anyone else ever uses this so we are doing discrete methods for now */
    public void onLeftTrigger(Runnable r){
        leftTrigger = r;
    }

    public void onRightTrigger(Runnable r) {
        rightTrigger = r;
    }

    public void onLeftBumper(Runnable r) {
        leftBumper = r;
    }

    public void onRightBumper(Runnable r) {
        rightBumper = r;
    }

    public void onA(Runnable r) {
        a = r;
    }

    public void onB(Runnable r) {
        b = r;
    }

    public void onX(Runnable r) {
        x = r;
    }

    public void onY(Runnable r) {
        y = r;
    }

    public void onUp(Runnable r) {
        up = r;
    }

    public void onDown(Runnable r) {
        down = r;
    }

    public void onLeft(Runnable r) {
        left = r;
    }

    public void onRight(Runnable r) {
        right = r;
    }
}
