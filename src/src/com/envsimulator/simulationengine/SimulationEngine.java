// Module Author: Eric Carver
// Mon Mar  3 18:16:19 EST 2014

package com.envsimulator.simulationengine;

import java.util.HashMap;

abstract class Organism {
    public Organism(int x, int y) {
        this.location = new Location(x, y);
    }
    Location location;
    protected String species;
    @Override public String toString() { return species; }
    protected int id;
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

enum AnimalSpecies {
    public AnimalSpecies(String species, float waterCapacity, float foodCapacity, float metabolicRate,
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
    public Animal(int x, int y, AnimalSpecies attributes, float evolutionaryFitness, Boolean isMale) {
        super(x, y);
        this.evolutionaryFitness = evolutionaryFitness;
        this.isMale = isMale;
        this.health = 1.0f;
        this.thirst = 0.0f;
        this.hunger = 0.0f;
        this.age = 0.0f;
        this.movement = 0.0f;
        this.attributes = attributes;
        this.lastFood = new Location();
        this.lastWater = new Location();
        this.rng = new Random();
    }
    // public Animal(int x, int y) {
    //     this(x, y);
    // }
    private float health; // This represents injury. The health value used upstream depends on
                          // several factors
    private float thirst;
    float hunger;
    private float evolutionaryFitness;
    private float age;
    private float movement;
    private Boolean isMale;
    private AnimalSpecies attributes;

    private Location lastFood;
    private Location lastWater;

    private Random rng;

    public float health() { return health; } // Need a formula to calculate this
    public float size() { return size; } // Need a formula to calculate this

    // The follwing two methods calculate how much the animal wants to go for water/food
    private float foodDesire() {
        if (!lastFood.hasMemory()) {
            return -1.0f;
        }
        return (hunger/attributes.foodCapacity)*(float)location.distance(lastFood);
    }

    private float waterDesire() {
        if (!lastWater.hasMemory()) {
            return -1.0f;
        }
        return (thirst/attributes.waterCapacity)*(float)location.distance(lastWater);
    }

    public void completeMovement() {
        this.movement -= 1.0f;
    }

    //Reproduction is not a critical feature; it will be added later
    //static Animal reproduce(Animal male, Animal female) { return new Animal(x(), y()); }

    // public int xMovement() {
    //     if (hunger > thirst) {
    //         if (foodX != -1) {
    //             return 
    // }
    // public int yMovement() {}

    public Location movementGoal {
        float foodDesire = this.foodDesire();
        float waterDesire = this.waterDesire();
        if (foodDesire > waterDesire) {
            return new Location(lastFood);
        }
        if (foodDesire < waterDesire) {
            return new Location(lastWater);
        }
        if (foodDesire < 0.0f && waterDesire < 0.0f) {
            return new Location(rng.nextInt(), rng.nextInt());
        }
        // If all else is equal, then we'll prioritize water
        return lastWater;
    }

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
    public SimulationEngine(int gridSizeX, int gridSizeY) {
        this.organisms = new HashMap();
        this.grid = new Grid(gridSizeX, gridSizeY);
        this.animalIdCount = 1;
        this.plantIdCount = -1;
        this.rng = new Random();
    }

    // public void step() {
        
    // }

    private int maxAnimals;
    private int maxPlants;
    private int animalIdCount;
    private int plantIdCount;

    private Random rng;

    private void simulateInteraction(Animal first, Animal second) {
        throw new UnsupportedOperationException();
    }

    private int simulateMovement(Animal candidate, int x, int y) {
        int xGoal = candidate.location.x() + x;
        int yGoal = candidate.location.y() + y;
        // Prevent the organism from walking off the map
        if (xGoal < 0 || xGoal > grid.xSize) {
            xGoal = candidate.location.x();
        }
        if (yGoal < 0 || yGoal > grid.ySize) {
            yGoal = candidate.location.y();
        }
        // Try to move to goal tile
        if (!this.grid.tiles[xGoal][yGoal].fullAnimals()) {
            this.grid.tiles[candidate.location.x()][candidate.location.y()]
                .removeAnimal(candidate);
            candidate.location.move(x, y);
        }
        else if (!this.grid.tiles[xGoal][candidate.location.y()].fullAnimals()) {
            this.grid.tiles[candidate.location.x()][candidate.location.y()]
                .removeAnimal(candidate);
            candidate.location.move(x, 0);
        }
        else if (!this.grid.tiles[candidate.location.x()][yGoal].fullAnimals()) {
            this.grid.tiles[candidate.location.x()][candidate.location.y()]
                .removeAnimal(candidate);
            candidate.location.move(0, y);
        }
        else {
            return 1;
        }
        this.grid.tiles[candidate.location.x()][candiate.location.y()].addAnimal(candidate);
        return 0;
    }

    public int simulateOneEvent(SimulationEvent event) {
        switch (event.priority) {
        case Event.TypePriority.RENDER:
            return -1;
        case Event.TypePriority.INTERACT:
            this.simulateInteraction(organisms.get(event.firstOrganism),
                                     organisms.get(event.secondOrganism));
            return 0;
        case Event.TypePriority.EAT:
            Animal eater = organisms.get(event.firstOrganism);
            Plant plant = organisms.get(event.secondOrganism);
            if (eater.hunger > plant.food) {
                eater.hunger -= plant.food;
                plant.food = 0;
            }
            else {
                plant.food -= eater.hunger;
                eater.hunger = 0;
            }
            return 0;
        case Event.TypePriority.DRINK:
            Animal drinker = organisms.get(event.firstOrganism);
            // For now, we will just assume an unlimited water supply
            drinker.thirst = 0;
            return 0;
        case Event.TypePriority.MOVE:
            Animal mover = organisms.get(event.firstOrganism);
            if (!this.simulateMove(mover, event.x, event.y)) {
                mover.completeMovement();
                return 0;
            }
            // TODO: Add deferred moves
            event.priority++;
            return 1;
        case Event.TypePriority.DEFERRED_MOVE:
            Animal mover = organisms.get(event.firstOrganism);
            if (!this.simulateMove(mover, event.x, event.y)) {
                mover.completeMovement();
            }
            // If the animal still can't move, tough shit. Try again next step.
            return 0;
        case Event.TypePriority.AGE:
            throw new UnsupportedOperationException();
            return 0;
        default:
            return (int)event.priority;
    }

    // This method adds an organism of the specified species to a random location
    public void addAnimal(AnimalSpecies species) {
        organisms.put(animalIdCount++, new Animal(this.rng.nextInt(grid.xSize)+1,
                                                  this.rng.nextInt(grid.ySize)+1, species));
    }

    public static Boolean isAnimal(int organismId) {
        return (organismId > 0);
    }
    public static Boolean isPlant(int organismId) {
        return (organismId < 0);
    }
    HashMap organisms;
    Grid grid;
}
