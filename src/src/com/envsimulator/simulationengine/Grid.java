// Module Author: Eric Carver
// Tue Mar  4 13:17:47 EST 2014

package com.envsimulator.simulationengine;

import java.util.ArrayList;
import java.util.Set;
import java.util.List;
import java.util.TreeSet;

import com.envsimulator.eventqueue.Event;
import com.envsimulator.eventqueue.SimulationEvent;

public class Grid {
    public Grid(int xSize, int ySize) {
        this.tiles = new Tile[xSize][ySize];
        this.xSize = xSize;
        this.ySize = ySize;
    }

    public Tile[][] tiles;
    int xSize;
    int ySize;

 //***************Begin Properties*******************
    
    public int GetXSize()
    {
        return xSize;
    }
    
    public int GetYSize()
    {
        return ySize;
    }
    
    //***************End Properties*******************
    
    // Gets simulation events for all tiles
    List<SimulationEvent> getEvents() {
        List<SimulationEvent> events = new ArrayList<SimulationEvent>();
        for (int y = 0; y < ySize; y++) {
            for (int x = 0; x < xSize; x++) {
                events.addAll(tiles[x][y].getEvents());
            }
        }
        return events;
    }
}

enum TileEnvironmentType {
    DESERT(false), FOREST(true);
    TileEnvironmentType(Boolean hasWater) {
        this.hasWater = hasWater;
    }
    public Boolean hasWater;
}

class Tile {

    public static final int ANIMAL_CAPACITY = 2;
    public static final int PLANT_CAPACITY = 2;

    public Tile() {
        this(TileEnvironmentType.DESERT);
    }
    public Tile(TileEnvironmentType environmentType) {
        this.environmentType = environmentType;
        animals = new TreeSet<Animal>();
        plants = new TreeSet<Plant>();
    }

    TileEnvironmentType environmentType;

    //***************Begin Properties*******************
    
    public TileEnvironmentType GetEnvironmentType()
    {
        return environmentType;
    }
    
    public Set<Animal> GetAnimals()
    {
        return animals;
    }
    
    public Set<Plant> GetPlants()
    {
        return plants;
    }
    
    //***************End Properties*******************
    
    private Set<Animal> animals;
    private Set<Plant> plants;

    public Boolean fullAnimals() { return animals.size() >= ANIMAL_CAPACITY; }
    public Boolean fullPlants() { return plants.size() >= PLANT_CAPACITY; }

    public ArrayList<Integer> getOrganismIds() {
        ArrayList<Integer> ret = new ArrayList<Integer>();
        for (Animal animal : this.animals) {
            ret.add(animal.id);
        }
        for (int i = animals.size(); i < ANIMAL_CAPACITY; i++) {
            red.add(i, 0);
        }
        for (Plant plant : this.plants) {
            ret.add(plant.id);
        }
        for (int i = plants.size(); i < PLANT_CAPACITY; i++) {
            red.add(i, 0);
        }
        return ret;
    }

    void addAnimal(Animal animal) {
        if (this.animals.size() >= ANIMAL_CAPACITY) {
            throw new IndexOutOfBoundsException();
        }
        this.animals.add(animal);
    }

    void addPlant(Plant plant) {
        if (this.plants.size() >= PLANT_CAPACITY) {
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
        List<SimulationEvent> eventList = new ArrayList<SimulationEvent>();
        // It is possible to implement interactions among three or more animals, but this can be
        // implemented later. We will need to enqueue interaction events between each combination
        // of two animals in the set.
        if (ANIMAL_CAPACITY != 2) {
            throw new UnsupportedOperationException();
        }
        // When two animals occupy the same tile, they must interact
        if (animals.size() == 2) {
            int[] ids = new int[animals.size()];
            int i = 0;
            for (Animal animal : animals) {
                ids[i++] = animal.id;
            }
            eventList.add(new SimulationEvent(ids[0], ids[1], Event.INTERACT, true));
        }
        // Next, we queue up individual events
        // Note that omnivores have the ability to eat twice in a step: once by killing another
        // animal and once by eating a plant. This is probably a bad thing, but we don't have any
        // omnivorous species yet.
        Set<Plant> localPlants = new TreeSet<Plant>(this.plants);
        for (Animal animal : this.animals) {
            // Herbivores will try to eat the largest plant
            if (animal.attributes.isHerbivore) {
                Plant biggestPlant = (Plant)localPlants.toArray()[0];
                for (Plant currentPlant : localPlants) {
                    if (currentPlant.food > biggestPlant.food) {
                        biggestPlant = currentPlant;
                    }
                }
                if (!localPlants.remove(biggestPlant)) {
                    throw new IndexOutOfBoundsException();
                }
                eventList.add(new SimulationEvent(animal.id, biggestPlant.id, Event.EAT, true));
            }
            // Also, every animal will try to drink
            eventList.add(new SimulationEvent(animal.id, this.environmentType.hasWater));
            // And while we're at it, let's set up move events for each animal that is eligible
            if (animal.movement >= 1.0f) {
                Location goal = animal.movementGoal();
                eventList.add(new SimulationEvent(animal.id,
                                  Integer.signum(animal.location.xDirection(goal.x())),
                                  Integer.signum(animal.location.yDirection(goal.y()))));
            }
            // Finally, every animal's stats must be updated for the passing of time.
            // We call this "aging" the animal
            eventList.add(new SimulationEvent(animal.id));
        }
        // We must also age all plants
        for (Plant plant : this.plants) {
            eventList.add(new SimulationEvent(plant.id));
        }
        return eventList;
    }
}
