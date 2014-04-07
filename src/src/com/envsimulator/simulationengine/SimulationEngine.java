// Module Author: Eric Carver
// Mon Mar  3 18:16:19 EST 2014

package com.envsimulator.simulationengine;

import java.util.HashMap;
import java.util.Random;

import com.envsimulator.eventqueue.Event;
import com.envsimulator.eventqueue.SimulationEvent;

abstract class Organism {
    public Organism(int x, int y, String action) {
        this.location = new Location(x, y);
        this.lastAction = action;
    }
    abstract Boolean increase_age();
    Location location;
    protected String species;
    @Override public String toString() { return species; }
    protected int id;
    protected String lastAction;

    public String getLastAction() {
        return lastAction;
    }
}

class Plant extends Organism {
    public Plant(int x, int y, float maxFood, float growthRate, float food) {
        super(x, y, "Grew a little");
        this.maxFood = maxFood;
        this.growthRate = growthRate;
        this.food = food;
    }
    public Plant(int x, int y, float maxFood, float growthRate) {
        this(x, y, maxFood, growthRate, 0.0f);
    }
    Boolean increase_age() {
        this.food += this.growthRate;
        if (this.food >= this.maxFood) {
            this.food = this.maxFood;
        }
        return false;
    }
    float growthRate;
    float food;
    float maxFood;
    
    //***************Begin Properties*******************
    public float GetGrowthRate()
    {
    	return growthRate;
    }
    
    public float GetFood()
    {
    	return food;
    }
    
    public float GetMaxFood()
    {
    	return maxFood;
    }
    //****************End Properties*******************
}

enum AnimalSpecies {
    //      Species water  food   met   spd  entrp   size  aggr   carn    herb
    BEAR(    "Bear", 2.0f, 9.0f, 0.5f, 0.3f, 0.03f, 10.0f, 0.2f,  true,  false),
    RABBIT("Rabbit", 1.0f, 1.0f, 0.2f, 0.5f, 0.07f,  1.0f, 0.0f, false,   true);

    AnimalSpecies(String species, float waterCapacity, float foodCapacity, float metabolicRate,
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

    float waterCapacity;
    float foodCapacity;
    float metabolicRate;
    float speed;
    float agingRate;
    Boolean isCarnivore;
    Boolean isHerbivore;
    float aggressiveness;
    float size;
    String species;
}

class Animal extends Organism implements Comparable<Animal> {
    public Animal(int x, int y, AnimalSpecies attributes, float evolutionaryFitness, Boolean isMale) {
        super(x, y, "Did nothing");
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
    float injury_health; // This represents injury. The health value used upstream depends on
                         // several factors
    float thirst;
    float hunger;
    float evolutionaryFitness;
    float age;
    float movement;
    Boolean isMale;
    AnimalSpecies attributes;

    Location lastFood;
    Location lastWater;

    Random rng;

    void fight(Animal other) {
        this.lastAction = "Started a fight";
        if (other.lastAction != "Started a fight") {
            other.lastAction = "Defended itself";
        }
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

    Boolean increase_age() {
        hunger += attributes.metabolicRate;
        thirst += attributes.metabolicRate;
        if (hunger > attributes.foodCapacity) {
            injury_health -= hunger - attributes.foodCapacity;
        }
        if (thirst > attributes.waterCapacity) {
            injury_health -= thirst - attributes.waterCapacity;
        }

        movement += attributes.speed;
        age += attributes.agingRate;

        if (injury_health <= 0.0f || age > 1.0f) {
            // The animal died
            return true;
        }
        else {
            // The animal heals a bit
            injury_health += attributes.metabolicRate/10.0f;
            if (injury_health > 1.0f) {
                injury_health = 1.0f;
            }
        }
        movement += attributes.speed;
        return false;
    }

    public float health() { return injury_health; } // Need a formula to calculate this
    public float size() {
        // Let's assume that an animal is at its prime in the middle of life,
        // and its size increases steadily for the first half of its life
        // and decreases steadily for the second half of its life
        // Additionally, I'll assume the minimum size of an animal is 50% of its nominal size
        float ageScalingFactor = 1-Math.abs(age-0.5f);
        return attributes.size*ageScalingFactor;
    }

    //***************Begin Properties*******************
    
    public float GetHealth()
    {
    	return injury_health;
    }
    
    public float GetThirst()
    {
    	return thirst;
    }
    
    public float GetHunger()
    {
    	return hunger;
    }
    
    public float GetEvolutionaryFitness()
    {
    	return evolutionaryFitness;
    }

    public float GetAge()
    {
    	return age;
    }
    
    public float GetMovement()
    {
    	return movement;
    }

    public Boolean GetGender()
    {
    	return isMale;
    }

    public AnimalSpecies GetSpecies()
    {
    	return attributes;
    }

    public String GetLastFood()
    {
    	return Integer.toString(lastFood.x()) + "x" + Integer.toString(lastFood.y()) ;
    }

    public String GetLastWater()
    {
    	return Integer.toString(lastWater.x()) + "x" + Integer.toString(lastWater.y()) ;
    }

    public Random Getrng()
    {
    	return rng;
    }
    
    //****************End Properties********************
    
    // The following two methods calculate how much the animal wants to go for water/food
    float foodDesire() {
        if (!lastFood.hasMemory() || hunger < attributes.foodCapacity/2) {
            return -1.0f;
        }
        return (hunger/attributes.foodCapacity)*(float)location.distance(lastFood);
    }

    float waterDesire() {
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

    public Location movementGoal() {
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
    @Override public int compareTo(Animal other) {
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
        this.organisms = new HashMap<Integer,Organism>();
        this.grid = new Grid(gridSizeX, gridSizeY);
        this.animalIdCount = 1;
        this.plantIdCount = -1;
        this.rng = new Random();
        this.stats = new Statistics(gridSizeX, gridSizeY);
    }

    // Generates a random grid. Must be updated if more animal, plant, or tile types are added
    public void genRandomGrid() {
        for (int i = 1; i < grid.xSize; i++) {
            for (int j = 1; j < grid.ySize; j++) {
                // Generate a random tile environment type
                if (rng.nextInt(2) == 0) {
                    grid.tiles[i][j] = new Tile(TileEnvironmentType.DESERT);
                }
                else {
                    grid.tiles[i][j] = new Tile(TileEnvironmentType.FOREST);
                }
                // Add 0-2 random animals
                for (int k = rng.nextInt(Tile.ANIMAL_CAPACITY+1); k < Tile.ANIMAL_CAPACITY; k++) {
                    if (rng.nextInt(2) == 0) {
                        this.addRandomAnimal(AnimalSpecies.BEAR, i, j);
                    }
                    else {
                        this.addRandomAnimal(AnimalSpecies.RABBIT, i, j);
                    }
                }
                // Add 0-2 random plants
                for (int k = rng.nextInt(Tile.PLANT_CAPACITY+1); k < Tile.PLANT_CAPACITY; k++) {
                    this.addRandomPlant(i, j);
                }
            }
        }
    }

    public void step() {
        for (int i = 1; i < grid.xSize; i++) {
            for (int j = 1; j < grid.ySize; j++) {
                for (Animal animal : grid.tiles[i][j].animals) {
                    animal.lastAction = "Did nothing";
                }
            }
        }
    }

    private int maxAnimals;
    private int maxPlants;
    private int animalIdCount;
    private int plantIdCount;

    private Random rng;

    private Statistics stats;
    
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
            if (first.attributes.isCarnivore) {
                if (second.attributes.isHerbivore) {
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

            if (second.attributes.isCarnivore) {
                if (first.attributes.isHerbivore) {
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
    }

    private Boolean simulateMovement(Animal candidate, int x, int y) {
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
            return false;
        }
        this.grid.tiles[candidate.location.x()][candidate.location.y()].addAnimal(candidate);
        return true;
    }

    public int simulateOneEvent(SimulationEvent event) {
        switch (event.priority) {
        case Event.RENDER:
            return -1;
        case Event.INTERACT:
            this.simulateInteraction((Animal)organisms.get(event.firstOrganism),
                                     (Animal)organisms.get(event.secondOrganism));
            return 0;
        case Event.EAT:
            Animal eater = (Animal)organisms.get(event.firstOrganism);
            Plant plant = (Plant)organisms.get(event.secondOrganism);
            if (eater.hunger > plant.food) {
                eater.hunger -= plant.food;
                plant.food = 0;
            }
            else {
                plant.food -= eater.hunger;
                eater.hunger = 0;
            }
            eater.lastFood.memorize(eater.location);
            if (eater.lastAction.equals("Did nothing")) {
                eater.lastAction = "Ate a plant";
            }
            return 0;
        case Event.DRINK:
            Animal drinker = (Animal)organisms.get(event.firstOrganism);
            // For now, we will just assume an unlimited water supply
            drinker.thirst = 0;
            drinker.lastWater.memorize(drinker.location);
            if (drinker.lastAction.equals("Did nothing")) {
                drinker.lastAction = "Drank some water";
            }
            return 0;
        case Event.MOVE:
            Animal mover = (Animal)organisms.get(event.firstOrganism);
            if (!this.simulateMovement(mover, event.x, event.y)) {
            	stats.switchTile(mover.location.x()-event.x, mover.location.y()-event.y, event.x, event.y, mover.GetSpecies());
                mover.completeMovement();
                if (mover.lastAction.equals("Did nothing")) {
                    mover.lastAction = "Moved";
                }
                return 0;
            }
            event.priority++;
            return 1;
        case Event.DEFERRED_MOVE:
            Animal deferred_mover = (Animal)organisms.get(event.firstOrganism);
            if (!this.simulateMovement(deferred_mover, event.x, event.y)) {
            	stats.switchTile(deferred_mover.location.x()-event.x, deferred_mover.location.y()-event.y, event.x, event.y, deferred_mover.GetSpecies());
                deferred_mover.completeMovement();
                if (deferred_mover.lastAction.equals("Did nothing")) {
                    deferred_mover.lastAction = "Moved";
                }
            }
            // If the animal still can't move, tough shit. Try again next step.
            return 0;
        case Event.AGE:
            Organism organism = organisms.get(event.firstOrganism);
            if (organism.increase_age()) {
                // The animal died
                if ( isAnimal(event.firstOrganism) ) {
                	stats.recordDeath(organism.location.x(), organism.location.y(), true, ((Animal)organism).GetSpecies());
                    grid.tiles[organism.location.x()][organism.location.y()]
                        .removeAnimal((Animal)organism);
                }
                else {
                    stats.recordDeath(organism.location.x(), organism.location.y(), false, AnimalSpecies.BEAR);
                    grid.tiles[organism.location.x()][organism.location.y()]
                        .removePlant((Plant)organism);
                }
                organisms.remove(organism);
            }
            return 0;
        default:
            return (int)event.priority;
        }
    }

    // This method adds an organism of the specified species to a random location
    public void addAnimal(AnimalSpecies species) {
        organisms.put(animalIdCount++, new Animal(this.rng.nextInt(grid.xSize)+1,
                                                  this.rng.nextInt(grid.ySize)+1, species,
                                                  this.rng.nextFloat(), true));
        Organism org = organisms.get(animalIdCount-1);
        stats.recordLife(org.location.x(), org.location.y(), true, ((Animal)org).GetSpecies());
    }
    public void addPlant() {
        organisms.put(plantIdCount--, new Plant(this.rng.nextInt(grid.xSize)+1,
                                                this.rng.nextInt(grid.ySize)+1,
                                                this.rng.nextFloat()*2+0.5f,
                                                this.rng.nextFloat()/2));
        Organism org = organisms.get(plantIdCount+1);
        stats.recordLife(org.location.x(), org.location.y(), false, AnimalSpecies.BEAR);
    }
    public void addRandomAnimal(AnimalSpecies species, int x, int y) {
        Animal animal = new Animal(x, y, species, this.rng.nextFloat(), true);
        organisms.put(animalIdCount++, animal);
        grid.tiles[x][y].addAnimal(animal);
        stats.recordLife(x, x, true, species);
    }
    public void addRandomPlant(int x, int y) {
        Plant plant = new Plant(x, y, this.rng.nextFloat()*2+0.5f, this.rng.nextFloat()/2);
        organisms.put(plantIdCount--, plant);
        grid.tiles[x][y].addPlant(plant);
        stats.recordLife(x, y, true, AnimalSpecies.BEAR); // Species is ignored
    }

    public void addExistingAnimal(Animal animal) throws IndexOutOfBoundsException {
        organisms.put(animal.id, animal);
        grid.tiles[animal.location.x()][animal.location.y()].addAnimal(animal);
    }

    public void addExistingPlant(Plant plant) throws IndexOutOfBoundsException {
        organisms.put(plant.id, plant);
        grid.tiles[plant.location.x()][plant.location.y()].addPlant(plant);
    }

    public static Boolean isAnimal(int organismId) {
        return (organismId > 0);
    }
    public static Boolean isPlant(int organismId) {
        return (organismId < 0);
    }
    public HashMap<Integer,Organism> organisms;
    public Grid grid;
}
