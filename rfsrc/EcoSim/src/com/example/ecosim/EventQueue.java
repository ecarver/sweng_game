// Module Author: Eric Carver
// Mon Mar  3 18:29:31 EST 2014

package com.example.ecosim;


import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class EventQueue {
    Comparator<Event> prioritizer = new EventComparator();
    public EventQueue() {
        queue = new PriorityQueue<Event>(32, prioritizer);
    }

    private PriorityQueue<Event> queue;
    public Event popEvent() {
        return queue.remove();
    }
    public Event peekEvent() {
        return queue.peek();
    }
    public void postEvent(Event event) {
        queue.add(event);
    }
    public void postSimulationEvents(List<SimulationEvent> events) {
        for (SimulationEvent event : events) {
            queue.add((Event)event);
        }
    }
    public Boolean isEmpty() { return queue.peek() == null; }
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