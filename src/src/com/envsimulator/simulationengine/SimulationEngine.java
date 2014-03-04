// Module Author: Eric Carver
// Mon Mar  3 18:16:19 EST 2014

package com.envsimulator.simulationengine;

import java.util.HashMap;

abstract class Organism {
    public Organism(int x, int y) {
        xLocation = x;
        yLocation = y;
    }
    int xLocation;
    int yLocation;
    public int x() { return xLocation; }
    public int y() { return yLocation; }
    protected String species;
    @Override public String toString() { return species; }
}

class Plant extends Organism {
    public Plant(int x, int y, float maxFood, float growthRate, float food) {
        super(x, y);
        this.maxFood = maxFood;
        this.growthRate = growthRate;
        this.food = food;
    }
    public Plant(int x, int y, float maxFood, float growthRate) {
        this(x, y, maxFood, growthRate, 0.0f);
    }
    float growthRate;
    float food;
    float maxFood;
}

class Location {
    public Location() {
        this.x = -1;
        this.y = -1;
    }
    int x;
    int y;
    public int distance(int x, int y) {
        if (this.x == -1 || this.y == -1) {
            return 0;
        }
        return (int)hypot((double)x, (double)y);
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
}

enum AnimalSpecies {
    public AnimalType(String species, float waterCapacity, float foodCapacity, float metabolicRate,
                      float speed, float agingRate, float size, Boolean isCarnivore, Boolean isHerbivore) {
        this.species = species;
        this.waterCapacity = waterCapacity;
        this.foodCapacity = foodCapacity;
        this.metabolicRate = metabolicRate;
        this.speed = speed;
        this.agingRate = agingRate;
        this.size = size;
        this.isCarnivore = isCarnivore;
        this.isHerbivore = isHerbivore;
    }
    //      Species water  food   met   spd  entrp   size  carn    herb
    BEAR(    "Bear", 2.0f, 9.0f, 0.5f, 0.3f, 0.03f, 10.0f, true,  false);
    RABBIT("Rabbit", 1.0f, 1.0f, 0.2f, 0.5f, 0.07f,  1.0f, false, true);

    private float waterCapacity;
    private float foodCapacity;
    private float metabolicRate;
    private float speed;
    private float agingRate;
    private Boolean isCarnivore;
    private Boolean isHerbivore;
}

class Animal extends Organism implements Comparable {
    public Animal(int x, int y, float evolutionaryFitness, Boolean isMale) {
        super(x, y);
        this.evolutionaryFitness = evolutionaryFitness;
        this.isMale = isMale;
        this.health = 1.0f;
        this.thirst = 0.0f;
        this.hunger = 0.0f;
        this.age = 0.0f;
    }
    // public Animal(int x, int y) {
    //     this(x, y);
    // }
    private float health; // This represents injury. The health value used upstream depends on
                          // several factors
    private float thirst;
    private float hunger;
    private float evolutionaryFitness;
    private float age;
    private Boolean isMale;
    private AnimalSpecies attributes;

    public float health() { return health; } // Need a formula to calculate this
    public float size() { return size; } // Need a formula to calculate this

    //Reproduction is not a critical feature; it will be added later
    //static Animal reproduce(Animal male, Animal female) { return new Animal(x(), y()); }

    // This method decides who eats/drinks first
    public int compareTo(Animal other) {
        if (this.size() + this.evolutionaryFitness < other.size() + other.evolutionaryFitness) {
            return -1;
        }
        if (this.size() + this.evolutionaryFitness > other.size() + other.evolutionaryFitness) {
            return 1;
        }
        return 0;
    }
}

public class SimulationEngine {
    public SimulationEngine() {
        this.organisms = new HashMap();
    }

    public void step() {}
    public void simulateOneEvent(SimulationEvent event) {}

    // This method adds an organism of the specified species to a random location with random attributes
    public void addOrganism(String species) {} // probably want an enum type for species eventually

    HashMap organisms;
    Grid grid;
}
