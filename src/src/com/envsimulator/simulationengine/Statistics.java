package com.envsimulator.simulationengine;

class Statistics {
    public Statistics (int xGridSize, int yGridSize) {
        this.xGridSize = xGridSize;
        this.yGridSize = yGridSize;
    }
    
    private int xGridSize;
    private int yGridSize;
    
    private int[][] bearsDiedInTile = new int[xGridSize][yGridSize];
    private int[][] rabbitsDiedInTile = new int[xGridSize][yGridSize];
    private int[][] plantsDiedInTile = new int[xGridSize][yGridSize];
    
    private int[][] bearsLivingInTile = new int[xGridSize][yGridSize];
    private int[][] rabbitsLivingInTile = new int[xGridSize][yGridSize];
    private int[][] plantsLivingInTile = new int[xGridSize][yGridSize];
    
    public void recordLife (int xTile, int yTile, boolean isAnimal, AnimalSpecies species) {
        if (isAnimal) {
            switch (species) {
                case BEAR:
                    bearsLivingInTile[xTile][yTile]++;
                    break;
                case RABBIT:
                    rabbitsLivingInTile[xTile][yTile]++;
                    break;
                default:
                    break;
            }
        }
        else
            plantsLivingInTile[xTile][yTile]++;
    }
    
    public void recordDeath (int xTile, int yTile, boolean isAnimal, AnimalSpecies species) {
        if (isAnimal) {
            switch (species) {
                case BEAR:
                    bearsLivingInTile[xTile][yTile]--;
                    break;
                case RABBIT:
                    rabbitsLivingInTile[xTile][yTile]--;
                    break;
                default:
                    break;
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
        return deadPlants;
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
    
    public void switchTile(int x, int y, int xMovement, int yMovement, AnimalSpecies species) {
        switch (species) {
            case BEAR:
                bearsLivingInTile[x][y]--;
                bearsLivingInTile[x+xMovement][y+yMovement]++;
                break;
            case RABBIT:
                rabbitsLivingInTile[x][y]--;
                rabbitsLivingInTile[x+xMovement][y+yMovement]++;
                break;
            default:
                break;
        }
     }
}