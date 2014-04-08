// Module Author: Eric Carver
// Mon Mar 10 01:12:39 EDT 2014

package com.example.ecosim;

public class Location {
    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Location() {
        this(-1,-1);
    }
    public Location(Location other) {
        this.x = other.x;
        this.y = other.y;
    }
    private int x;
    private int y;
    public int distance(int x, int y) {
        if (this.x == -1 || this.y == -1) {
            return 0;
        }
        return (int)Math.hypot((double)x, (double)y);
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
    public void memorize(Location other) {
        this.x = other.x;
        this.y = other.y;
    }
    public void forget() {
        this.x = -1;
        this.y = -1;
    }
    public void move(int x, int y) {
        this.x = this.x + x;
        this.y = this.y + y;
    }
    public Boolean hasMemory() {
        return (this.x != -1 && this.y != -1);
    }
    public int x() { return this.x; }
    public int y() { return this.y; }
}
