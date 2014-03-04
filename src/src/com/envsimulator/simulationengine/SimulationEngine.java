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

class Animal extends Organism {
    public Animal(int x, int y, float waterCapacity, float foodCapacity, float metabolicRate,
                  float evolutionaryFitness, float speed, float agingRate, float size,
                  Boolean isCarnivore, Boolean isHerbivore, Boolean isMale) {
        super(x, y);
        this.waterCapacity = waterCapacity;
        this.foodCapacity = foodCapacity;
        this.metabolicRate = metabolicRate;
        this.evolutionaryFitness = evolutionaryFitness;
        this.speed = speed;
        this.agingRate = agingRate;
        this.size = size;
        this.isCarnivore = isCarnivore;
        this.isHerbivore = isHerbivore;
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
    private float waterCapacity;
    private float thirst;
    private float foodCapacity;
    private float hunger;
    private float metabolicRate;
    private float evolutionaryFitness;
    private float speed;
    private float age;
    private float agingRate;
    private float size;
    private Boolean isCarnivore;
    private Boolean isHerbivore;
    private Boolean isMale;

    public float health() { return health; } // Need a formula to calculate this

    //Reproduction is not a critical feature; it will be added later
    //static Animal reproduce(Animal male, Animal female) { return new Animal(x(), y()); }
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
