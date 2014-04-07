/**package com.example.ecosim;

class Statistics {
    public Statistics (int xGridSize, int yGridSize, int waterTiles) {
        this.xGridSize = xGridSize;
        this.yGridSize = yGridSize;
        
        this.numTiles = this.xGridSize*this.yGridSize;
        this.waterTiles = waterTiles;
        this.landTiles = numTiles - waterTiles;
    }
    
    private int xGridSize;
    private int yGridSize;
    
    private int numTiles;
    private int waterTiles;
    private int landTiles;
    
    private int bearsDiedInTile[xGridSize][yGridSize];
    private int rabbitsDiedInTile[xGridSize][yGridSize];
    private int plantsDiedInTile[xGridSize][yGridSize];//
    
    private int bearsLivingInTile[xGridSize][yGridSize];
    private int rabbitsLivingInTile[xGridSize][yGridSize];
    private int plantsLivingInTile[xGridSize][yGridSize];
    
    public void recordLife (int xTile, int yTile, boolean isAnimal, AnimalSpecies species) {
        if (isAnimal) {
            switch (species) {
                case AnimalSpecies.BEAR:
                    bearsLivingInTile[xTile][yTile]++;
                    break;
                case AnimalSpecies.RABBIT:
                    rabbitsLivingInTile[xTile][yTile]++;
                    break;
                default:
                    break
            }
        }
        else
            plantsLivingInTile[xTile][yTile]++;
    }
    
    public void recordDeath (int xTile, int yTile, boolean isAnimal, AnimalSpecies species) {
        if (isAnimal) {
            switch (species) {
                case AnimalSpecies.BEAR:
                    bearsLivingInTile[xTile][yTile]--;
                    break;
                case AnimalSpecies.RABBIT:
                    rabbitsLivingInTile[xTile][yTile]--;
                    break;
                default:
                    break
            }
        }
        else
            plantsLivingInTile[xTile][yTile]--;
    }
    
    public int totalOrganisms () {
        return totalPlants() + totalAnimals();
    }
    
    public int liveOrganisms() {
        return liveAnimals() + livePlants();
    }
    
    public int deadOrganisms() {
        return deadAnimals() + deadPlants();
    }
    
    public int totalPlants() {
        return livePlants() + deadPlants();
    }
    
    public int totalAnimals() {
        return liveAnimals() + deadAnimals();
    }
    
    public int livePlants() {
        int livePlants = 0;
        for (int i = 0; i < xGridSize; i++) {
            for (int j = 0; j < yGridSize; j++) {
                livePlants += plantsLivingInTile[i][j];
            }
        }
        return livePlants;
    }
    
    public int deadPlants() {
        int deadPlants = 0;
        for (int i = 0; i < xGridSize; i++) {
            for (int j = 0; j < yGridSize; j++) {
                deadPlants += plantsDiedInTile[i][j];
            }
        }
        return livePlants;
    }
    
    public int liveAnimals() {
        return liveBears() + liveRabbits();
    }
    
    public int deadAnimals() {
        return deadBears() + deadRabbits();
    }
    
    public int liveBears() {
        int liveBears = 0;
        for (int i = 0; i < xGridSize; i++) {
            for (int j = 0; j < yGridSize; j++) {
                liveBears += bearsLivingInTile[i][j];
            }
        }
        return liveBears;
    }
    
    public int liveRabbits() {
        int liveRabbits = 0;
        for (int i = 0; i < xGridSize; i++) {
            for (int j = 0; j < yGridSize; j++) {
                liveRabbits += rabbitsLivingInTile[i][j];
            }
        }
        return liveRabbits;
    }
    
    public int deadBears() {
        int deadBears = 0;
        for (int i = 0; i < xGridSize; i++) {
            for (int j = 0; j < yGridSize; j++) {
                deadBears += bearsDiedInTile[i][j];
            }
        }
        return deadBears;
    }
    
    public int deadRabbits() {
        int deadRabbits = 0;
        for (int i = 0; i < xGridSize; i++) {
            for (int j = 0; j < yGridSize; j++) {
                deadRabbits += rabbitsDiedInTile[i][j];
            }
        }
        return deadRabbits;
    }
    
    public void switchTile(int x, int y, int xMovement, int yMovement, boolean isAnimal, AnimalSpecies species) {
        if (isAnimal) {
            switch (species) {
                case AnimalSpecies.BEAR:
                    bearsLivingInTile[x][y]--;
                    bearsLivingInTile[x+xMovement][y+yMovement]++;
                    break;
                case AnimalSpecies.RABBIT:
                    rabbitsLivingInTile[x][y]--;
                    rabbitsLivingInTile[x+xMovement][y+yMovement]++;
                    break;
                default:
                    break
            }
        }
        else
            plantsLivingInTile[x][y]--;
            plantsLivingInTile[x+xMovement][y+yMovement]++;
    }
}**/