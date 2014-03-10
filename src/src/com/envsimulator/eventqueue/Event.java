// Module Author: Eric Carver
// Mon Mar  3 18:16:56 EST 2014

package com.envsimulator.eventqueue;

public abstract class Event {
    public Event(int priority) {
        this.priority = priority;
    }
    public Event() { this(6); }

    public int priority;
    public static final enum TypePriority {
        RENDER(0), INTERACT(2), EAT_DRINK(3), MOVE(4), DEFERRED_MOVE(5), AGE(7);
    }
}
