package org.summersession.controlSystem;

import java.util.LinkedList;
import java.util.Optional;

/*
 * Scheduler for queueing runnables to run for specific numbers of cycles
 */

public class TimedCycleScheduler {
    public LinkedList<Runnable> scheduledRunnables = new LinkedList<Runnable>();
    public LinkedList<Integer> scheduledCycleTimes = new LinkedList<Integer>();

    public TimedCycleScheduler(){
        System.out.println("[TimedCycleScheduler]: Initialized.");
    }

    public void addTimedCycleScheduledRunnable(Runnable runnable, int cycles){
        scheduledRunnables.add(runnable);
        scheduledCycleTimes.add(cycles);
    }

    public Optional<Runnable> getCurrentTimedCycleScheduledRunnable(){
        if(scheduledRunnables.size() == 0){
            return Optional.empty();
        }
        Runnable current = scheduledRunnables.get(0);
        scheduledCycleTimes.set(0, scheduledCycleTimes.get(0) - 1);
        if(scheduledCycleTimes.get(0) == 0){
            scheduledCycleTimes.remove(0);
            scheduledRunnables.remove(0);
        }
        return Optional.of(current);
    }
}
