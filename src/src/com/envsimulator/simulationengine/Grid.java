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

    // This makes a list of events that should be added to the priority queue for the next step
    // This method should be called at the beginning of every step
    List<SimulationEvent> getEvents() {
        List<SimulationEvent> eventList = new List<SimulationEvent>;
        // It is possible to implement interactions among three or more animals, but this can be
        // implemented later. We will need to enqueue interaction events between each combination
        // of two animals in the set.
        if (animalCapacity != 2) {
            throw new UnsupportedOperationException;
        }
        // When two animals occupy the same tile, they must interact
        if (animals.size == 2) {
            eventList.add(new SimulationEvent(animals.first(), animals.second()));
        }
        // Next, we queue up individual events
        // Note that omnivores have the ability to eat twice in a step: once by killing another
        // animal and once by eating a plant. This is probably a bad thing, but we don't have any
        // omnivorous species yet.
        Set<Plant> localPlants = new TreeSet<Plant>(this.plants);
        for (Animal animal : this.animals) {
            // Herbivores will try to eat the largest plant
            if (animal.isHerbivore) {
                Plant biggestPlant = localPlants.first();
                for (Plant currentPlant : localPlants) {
                    if (currentPlant.food > biggestPlant.food) {
                        biggestPlant = currentPlant;
                    }
                }
                if (!localPlants.remove(biggestPlant)) {
                    throw new IndexOutOfBoundsException();
                }
                eventList.add(animal.id, biggestPlant);
            }
            // Also, every animal will try to drink
            eventList.add(animal.id, this.environmentType.hasWater);
            // And while we're at it, let's set up move events for each animal that is eligible
            if (animal.movement >= 1.0f) {
                Location goal = animal.movementGoal();
                eventList.add(new SimulationEvent(animal.id,
                                  Integer.signum(animal.location.xDirection(goal.x())),
                                  Integer.signum(animal.location.yDirection(goal.y()))));
            }
            // Finally, every animal's stats must be updated for the passing of time.
            // We call this "aging" the animal
            eventList.add(animal.id);
        }
        // We must also age all plants
        for (Plant plant : this.plants) {
            eventList.add(plant.id);
        }
        return eventList;
    }
}
