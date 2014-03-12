// Module Author: Eric Carver
// Mon Mar  3 18:16:56 EST 2014

package com.envsimulator.eventqueue;

public class SimulationEvent extends Event {
    public SimulationEvent(int firstOrganism, int secondOrganism) {
        if (SimulationEngine.isAnimal(secondOrganism)) {
            super((int)Event.TypePriority.INTERACT);
        }
        else {
            super((int)Event.TypePriority.EAT);
        }
        this.firstOrganism = firstOrganism;
        this.secondOrganism = secondOrganism;
    }
    public SimulationEvent(int organism, bool water) {
        super((int)Event.TypePriority.DRINK);
        this.firstOrganism = organism;
        this.secondOrganism = -1;
    }
    public SimulationEvent(int organism, int x, int y) {
        super((int)Event.TypePriority.MOVE);
        this.firstOrganism = organism;
        this.secondOrganism = -1;
        this.x = x;
        this.y = y;
    }
    public SimulationEvent(int organism) {
        super((int)Event.TypePriority.AGE);
        this.firstOrgansim = organism;
        this.secondOrganism = -1;
    }

    int firstOrganism;
    int secondOrganism;
    public int x;
    public int y;
}
