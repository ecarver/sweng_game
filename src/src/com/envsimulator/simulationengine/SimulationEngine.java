// Module Author: Eric Carver
// Mon Mar  3 18:16:19 EST 2014

package com.envsimulator.simulationengine;

import java.util.HashMap;

abstract class Organism {
    public Organism(int x, int y) {
        this.location = new Location(x, y);
    }
    abstract int age();
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
    int age() {
        this.food += this.growthRate;
        if (this.food >= this.maxFood) {
            this.food = this.maxFood;
        }
    }
    float growthRate;
    float food;
    float maxFood;
}

enum AnimalSpecies {
    public AnimalSpecies(String species, float waterCapacity, float foodCapacity, float metabolicRate,
                         float speed, float agingRate, float size, float aggressiveness,
                         Boolean isCarnivore, Boolean isHerbivore) {
        this.species = species;
        this.waterCapacity = waterCapacity;
        this.foodCapacity = foodCapacity;
        this.metabolicRate = metabolicRate;
        this.speed = speed;
        this.agingRate = agingRate;
        this.size = size;
        this.isCarnivore = isCarnivore;
        this.isHerbivore = isHerbivore;
        this.aggressiveness = aggressiveness;
    }
    //      Species water  food   met   spd  entrp   size  aggr   carn    herb
    BEAR(    "Bear", 2.0f, 9.0f, 0.5f, 0.3f, 0.03f, 10.0f, 0.2f,  true,  false);
    RABBIT("Rabbit", 1.0f, 1.0f, 0.2f, 0.5f, 0.07f,  1.0f, 0.0f, false,   true);

    private float waterCapacity;
    private float foodCapacity;
    private float metabolicRate;
    private float speed;
    private float agingRate;
    private Boolean isCarnivore;
    private Boolean isHerbivore;
    private float aggressiveness;
}

class Animal extends Organism implements Comparable {
    public Animal(int x, int y, AnimalSpecies attributes, float evolutionaryFitness, Boolean isMale) {
        super(x, y);
        this.evolutionaryFitness = evolutionaryFitness;
        this.isMale = isMale;
        this.injury_health = 1.0f;
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
    private float injury_health; // This represents injury. The health value used upstream depends on
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

    void fight(Animal other) {
        // This animal is the aggressor. The aggressor gets a slight bonus in the fight
        float damageToOther = this.health()*(this.size()-other.size())*
            (this.evolutionaryFitness-other.evolutionaryFitness) + 0.05f;
        float damageToThis = other.health()*(other.size()-this.size())*
            (other.evolutionaryFitness-this.evolutionaryFitness);

        if (damageToOther > 0) {
            other.injury_health -= damageToOther;
        }
        if (damageToThis > 0) {
            this.injury_health -= damageToThis;
        }
    }

    int age() {
        hunger += attributes.metabolic_rate;
        thirst += attributes.metabolic_rate;
        if (hunger > attributes.foodCapacity) {
            injury_health -= hunger - attributes.foodCapacity;
        }
        if (thirst > attributes.waterCapacity) {
            injury_health -= thirst - attributes.waterCapcity;
        }

        movement += attributes.speed;
        age += attributes.agingRate;

        if (injury_health <= 0.0f || age > 1.0f) {
            // The animal died
            return 1;
        }
        else {
            // The animal heals a bit
            injury_health += attributes.metabolism/10.0f;
            if (injury_health > 1.0f) {
                injury_health = 1.0f;
            }
        }
        movement += attributes.speed;
        return 0;
    }

    public float health() { return injury_health; } // Need a formula to calculate this
    public float size() {
        // Let's assume that an animal is at its prime in the middle of life,
        // and its size increases steadily for the first half of its life
        // and decreases steadily for the second half of its life
        // Additionally, I'll assume the minimum size of an animal is 50% of its nominal size
        float ageScalingFactor = 1-abs(age-0.5f);
        return attributes.size*ageScalingFactor;
    }

    // The follwing two methods calculate how much the animal wants to go for water/food
    private float foodDesire() {
        if (!lastFood.hasMemory() || hunger < attributes.foodCapacity/2) {
            return -1.0f;
        }
        return (hunger/attributes.foodCapacity)*(float)location.distance(lastFood);
    }

    private float waterDesire() {
        if (!lastWater.hasMemory() || thirst < attributes.waterCapacity/2) {
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
        // Either the animals are the same species or different species
        if ( first.attributes.species.equals(second.attributes.species) ) {
            // We'll start by excluding cannibalism
            // So there can be a peaceful meeting or a territorial fight
            // Let's say that territorial fights are not very likely, but can be instigated by
            //   either animal
            // Some animals have no aggressiveness, so they will never fight each other
            // Others are encouraged/discouraged by the size of the other

            // We won't have any animals die during the fight. Assume they die later from blood loss
            //   or something. However, their injury level does affect their effectiveness

            // TODO: Push "fight!" messages up
            if (first.rng.nextFloat() >
                1-first.attributes.aggressiveness - second.size() + first.size()) {
                // This one wants to fight
                first.fight(second);
            }
            if (second.rng.nextFloat() >
                     1-second.attributes.aggressiveness - first.size() + second.size()) {
                second.fight(first);
            }
            // Otherwise, peaceful meeting so do nothing
        }
        else {
            // The animals are different species, so one may be able to eat the other
            // Let's exclude carnivores eating other carnivores for now
            // An animal can only eat a smaller animal
            // The carnivore's ability to catch and eat an herbivore depends mostly on evolutionary
            //   fitness and random chance
            if (first.attributes.carnivore) {
                if (second.attributes.herbivore) {
                    if (first.size() > second.size() && first.foodDesire() > 0.0f) {
                        // The first will try to eat the second
                        if (first.evolutionaryFitness + first.rng.nextFloat() >
                            second.evolutionaryFitness + second.rng.nextFloat()) {
                            // The second dies
                            second.injury_health -= 10.0f;
                            // The first eats
                            first.hunger -= second.size();
                            if ( first.hunger < 0.0f ) {
                                first.hunger = 0.0f;
                            }
                        }
                    }
                }
            }

            if (second.attributes.carnivore) {
                if (first.attributes.herbivore) {
                    if (second.size() > first.size() && second.foodDesire() > 0.0f) {
                        // The second will try to eat the first
                        if (second.evolutionaryFitness + second.rng.nextFloat() >
                            first.evolutionaryFitness + first.rng.nextFloat()) {
                            // The first dies. It will be "cleaned up" by the aging step
                            first.injury_health -= 10.0f;
                            // The second eats
                            second.hunger -= first.size();
                            if ( second.hunger < 0.0f ) {
                                second.hunger = 0.0f;
                            }
                        }
                    }
                }
            }

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
            eater.lastFood.memorize(eater.location);
            return 0;
        case Event.TypePriority.DRINK:
            Animal drinker = organisms.get(event.firstOrganism);
            // For now, we will just assume an unlimited water supply
            drinker.thirst = 0;
            eater.lastWater.memorize(eater.location);
            return 0;
        case Event.TypePriority.MOVE:
            Animal mover = organisms.get(event.firstOrganism);
            if (!this.simulateMove(mover, event.x, event.y)) {
                mover.completeMovement();
                return 0;
            }
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
            Organism organism = organisms.get(event.firstOrganism);
            if (organism.age()) {
                // The animal died
                if ( isAnimal(event.firstOrganism) ) {
                    grid.tiles[organism.location.x()][organism.location.y()]
                        .removeAnimal((Animal)organism);
                }
                else {
                    grid.tiles[organism.location.x()][organism.location.y()]
                        .removePlant((Plant)organism);
                }
                organisms.remove(organism);
            }
            return 0;
        default:
            return (int)event.priority;
    }

    // This method adds an organism of the specified species to a random location
    public void addAnimal(AnimalSpecies species) {
        organisms.put(animalIdCount++, new Animal(this.rng.nextInt(grid.xSize)+1,
                                                  this.rng.nextInt(grid.ySize)+1, species,
                                                  this.rng.nextFloat(), true));
    }
    public void addPlant() {
        organisms.put(plantIdCount--, new Plant(this.rng.nextInt(grid.xSize)+1,
                                                this.rng.nextInt(grid.ySize)+1,
                                                this.rng.nextFloat()*2+0.5f,
                                                this.rng.nextFloat()/2));
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
