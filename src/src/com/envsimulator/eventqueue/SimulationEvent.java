// Module Author: Eric Carver
// Mon Mar  3 18:16:56 EST 2014

package com.envsimulator.eventqueue;

public class SimulationEvent extends Event {
    public SimulationEvent(int firstOrganism, int secondOrganism, int priority, Boolean spec) {
        super(priority);
        this.firstOrganism = firstOrganism;
        this.secondOrganism = secondOrganism;
    }
    public SimulationEvent(int organism, Boolean water) {
        super(Event.DRINK);
        this.firstOrganism = organism;
        this.secondOrganism = -1;
    }
    public SimulationEvent(int organism, int x, int y) {
        super(Event.MOVE);
        this.firstOrganism = organism;
        this.secondOrganism = -1;
        this.x = x;
        this.y = y;
    }
    public SimulationEvent(int organism) {
        super(Event.AGE);
        this.firstOrganism = organism;
        this.secondOrganism = -1;
    }

    public int firstOrganism;
    public int secondOrganism;
    public int x;
    public int y;
}
