// Module Author: Eric Carver
// Mon Mar  3 18:29:31 EST 2014

package com.envsimulator.eventqueue;

import java.util.Comparator;
import java.util.PriorityQueue;

public class EventQueue {
    Comparator<Event> prioritizer = new EventComparator();
    public EventQueue() {
        queue = new PriorityQueue<Event>(32, prioritizer);
    }

    private PriorityQueue<Event> queue;
    public Event getEvent() {
        return queue.remove();
    }
    public void postEvent(Event event) {
        queue.add(event);
    }
}

// This class orders events such that the highest priority is 0 assuming priorities are unsigned
class EventComparator implements Comparator<Event> {
    @Override public int compare(Event left, Event right) {
        if (left.priority < right.priority) {
            return 1;
        }
        if (left.priority > right.priority) {
            return -1;
        }
        return 0;
    }
}