// Module Author: Eric Carver
// Mon Mar  3 18:16:56 EST 2014

package com.envsimulator.eventqueue;

public abstract class Event {
    public Event(int priority) {
        this.priority = priority;
    }
    public Event() { this(AGE+1); }

    public int priority;
    public static enum TypePriority {
        RENDER, INTERACT, DRINK, EAT, MOVE, DEFERRED_MOVE, AGE;
    }
    public static final int RENDER = 0;
    public static final int INTERACT = 2;
    public static final int DRINK = 3;
    public static final int EAT = 4;
    public static final int MOVE = 5;
    public static final int DEFERRED_MOVE = 6;
    public static final int AGE = 7;
}
