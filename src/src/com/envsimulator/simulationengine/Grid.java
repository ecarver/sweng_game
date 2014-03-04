// Module Author: Eric Carver
// Tue Mar  4 13:17:47 EST 2014

package com.envsimulator.simulationengine;

import java.util.Set;
import java.util.List;

public static final int animalCapacity = 2;
public static final int plantCapacity = 2;

public class Grid {
    public Grid(int xSize, int ySize) {
        this.tileMatrix = new Tile[xSize][ySize];
        this.xSize = xSize;
        this.ySize = ySize;
    }

    Tile[][] tileMatrix;
    private int xSize;
    private int ySize;

    // Gets simulation events for all tiles
    List<SimulationEvent> getEvents() {
        List<SimulationEvent> events = new List<SimulationEvent>
        for (int y = 0; y < ySize; y++) {
            for (int x = 0; x < xSize; x++) {
                events.addAll(tileMatrix[x][y].getEvents());
            }
        }
        return events;
    }
}

enum TileEnvironmentType {
    DESERT(false), FOREST(true);
    public TileEnvironmentType(Boolean hasWater) {
        this.hasWater = hasWater;
    }
    public Boolean hasWater;
}

class Tile {
    public Tile() {
        this((TileEnvironmentType)0);
    }
    public Tile(TileEnvironmentType environmentType) {
        this.environmentType = environmentType;
        animals = new TreeSet<Animal>;
        plants = new TreeSet<Plant>;
    }

    TileEnvironmentType environmentType;

    private Set<Animal> animals;
    private Set<Plant> plants;

    public Boolean fullAnimals() { return animals.size() >= animalCapacity; }
    public Boolean fullPlants() { return plants.size() >= plantCapacity; }

    void addAnimal(Animal animal) {
        if (this.animals.size() >= animalCapacity) {
            throw new IndexOutOfBoundsException();
        }
        this.animals.add(animal);
    }

    void addPlant(Plant plant) {
        if (this.plants.size() >= plantCapacity) {
            throw new IndexOutOfBoundsException();
        }
        this.plants.add(plant);
    }

    void removeAnimal(Animal animal) {
        this.animals.remove(animal);
    }
    void removePlant(Plant plant) {
        this.plants.remove(plant);
    }

    List<SimulationEvent> getEvents() { return null; }
}
