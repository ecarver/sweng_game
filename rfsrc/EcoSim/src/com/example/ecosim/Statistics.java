package com.example.ecosim;

class Statistics {
    public Statistics (int xGridSize, int yGridSize, int waterTiles) {
        this.xGridSize = xGridSize;
        this.yGridSize = yGridSize;
        
        this.numTiles = this.xGridSize*this.yGridSize;
        this.waterTiles = waterTiles;
        this.landTiles = numTiles - waterTiles;
        
        bearsDiedInTile = new int[xGridSize][yGridSize];
        rabbitsDiedInTile = new int[xGridSize][yGridSize];
        plantsDiedInTile = new int[xGridSize][yGridSize];
        
        bearsLivingInTile = new int[xGridSize][yGridSize];
        rabbitsLivingInTile = new int[xGridSize][yGridSize];
        plantsLivingInTile = new int[xGridSize][yGridSize];
    }
    
    private int xGridSize;
    private int yGridSize;
    
    private int numTiles;
    private int waterTiles;
    private int landTiles;
    
    private int bearsDiedInTile[][];
    private int rabbitsDiedInTile[][];
    private int plantsDiedInTile[][];
    
    private int bearsLivingInTile[][];
    private int rabbitsLivingInTile[][];
    private int plantsLivingInTile[][];
    
    public void recordLife (int xTile, int yTile, boolean isAnimal, AnimalSpecies species) {
    	
    	if(isAnimal) {
    		if(species.species.equals("Rabbit")) {
    			bearsLivingInTile[xTile][yTile]++;
    		} else if(species.species.equals("Bear")) {
    			rabbitsLivingInTile[xTile][yTile]++;
    		}
    	} else {
    		plantsLivingInTile[xTile][yTile]++;
    	}
    }
    
    public void recordDeath (int xTile, int yTile, boolean isAnimal, AnimalSpecies species) {
    	if(isAnimal) {
    		if(species.species.equals("Rabbit")) {
    			bearsLivingInTile[xTile][yTile]--;
    		} else if(species.species.equals("Bear")) {
    			rabbitsLivingInTile[xTile][yTile]--;
    		}
    	} else {
    		plantsLivingInTile[xTile][yTile]--;
    	}
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
                livePlants = livePlants += plantsLivingInTile[i][j];
            }
        }
        return livePlants;
    }
    
    public int deadPlants() {
        int deadPlants = 0;
        for (int i = 0; i < xGridSize; i++) {
            for (int j = 0; j < yGridSize; j++) {
                deadPlants = deadPlants += plantsDiedInTile[i][j];
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
    
    public void switchTile(int x, int y, int xMovement, int yMovement, boolean isAnimal, AnimalSpecies species) {
        if (isAnimal) {
        	if(species.species.equals("Bear")) {
                bearsLivingInTile[x][y]--;
                bearsLivingInTile[x+xMovement][y+yMovement]++;
        	} else if(species.species.equals("Rabbit")) {
        		rabbitsLivingInTile[x][y]--;
                rabbitsLivingInTile[x+xMovement][y+yMovement]++;
        	} 
        } else {
            plantsLivingInTile[x][y]--;
            plantsLivingInTile[x+xMovement][y+yMovement]++;
        }
    }
}