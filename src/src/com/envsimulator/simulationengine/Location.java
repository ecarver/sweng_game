// Module Author: Eric Carver
// Mon Mar 10 01:12:39 EDT 2014

package com.envsimulator.simulationengine;

public class Location {
    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Location() {
        this(-1,-1);
    }
    private int x;
    private int y;
    public int distance(int x, int y) {
        if (this.x == -1 || this.y == -1) {
            return 0;
        }
        return (int)hypot((double)x, (double)y);
    }
    public int distance(Location other) {
        return this.distance(other.x(), other.y());
    }
    public int xDirection(int x) {
        if (this.x == -1) {
            return 0;
        }
        return x - this.x;
    }
    public int yDirection(int y) {
        if (this.y == -1) {
            return 0;
        }
        return y - this.y;
    }
    public void memorize(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Boolean hasMemory() {
        return (this.x != -1 && this.y != -1);
    }
    public int x() { return this.x; }
    public int y() { return this.y; }
}
