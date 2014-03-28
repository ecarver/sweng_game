//Author: Carlo Perottino

package com.envsimulator.startmenu;

import com.envsimulator.simulationengine.*;
import java.io.*;

import android.app.Activity;
import android.os.Bundle;

public final class StartMenu extends Activity
{
	static final String FileName = "SWENG_GAME";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public SimulationEngine LoadSimulation() 
    {
    	String Data; 
    	int grid_x;
    	int grid_y;
    	SimulationEngine NewEngine;
    	
        FileInputStream fin = new FileInputStream(FileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fin));
        StringBuilder sb = new StringBuilder();
        String line = null;
        
        while ((line = reader.readLine()) != null) 
        {
          sb.append(line).append("\n");
        }
        
        reader.close();
        fin.close(); 
        
        
        
        NewEngine = new SimulationEngine(grid_x, grid_y);
             
        
        return NewEngine;
    }

    public void SaveSimulation(SimulationEngine Engine) 
    {
    	String SimulationData = GetSimulationData(Engine);
    	
    	FileOutputStream fos = openFileOutput(FileName, MODE_PRIVATE);
    	fos.write(SimulationData.getBytes());
    	fos.close();    	
    }
    
    private String GetSimulationData(SimulationEngine Engine)
    {
        StringBuilder Data = new StringBuilder();
        Animal[] AnimalData;
        Plant[] PlantData;
    	int MaxGridX;
    	int MaxGridY;
    	int j;
    	int i;
    	
    	MaxGridX = Engine.grid.GetXSize();
    	MaxGridY = Engine.grid.GetYSize();
    	
    	for(i = 0 ; i < MaxGridY ; i++)
    	{
        	for(j = 0 ; j < MaxGridX ; j++)
        	{
        		Data.append(j).append("x").append(i).append("\n");
        		Data.append(Engine.grid.tiles[j][i].GetEnvironmentType().toString()).append("\n");
        		
        		AnimalData = Engine.grid.tiles[j][i].GetAnimals();
        		
        		Data.append(GetAnimalData(AnimalData));
        		
        		PlantData = Engine.grid.tiles[j][i].GetPlants();
        		
        		Data.append(GetPlantData(PlantData));
        		
        		Data.append("\n");
        	}
    	}
    	
    	return Data.toString(); 
    }    	
    
    private String GetAnimalData(Animal[] AnimalData)
    {
    	StringBuilder Data = new StringBuilder();
    	int k;
    	String health;
    	String thirst;
    	String hunger;
    	String evolutionaryFitness;
    	String movement;
    	String isMale;
    	String Species;
    	String LastFood;
    	String LastWater;
    	String rng;
    	
    	for(k = 0 ; k < AnimalData.length ; k++)
    	{
    		health = String.valueOf(AnimalData[k].GetHealth());
    		thirst = String.valueOf(AnimalData[k].GetThirst());
        	hunger = String.valueOf(AnimalData[k].GetHunger());
        	evolutionaryFitness = String.valueOf(AnimalData[k].GetEvolutionaryFitness());
        	movement = String.valueOf(AnimalData[k].GetMovement());
        	isMale = AnimalData[k].GetGender().toString();
        	Species = AnimalData[k].GetSpecies().name().toString();
        	LastFood = AnimalData[k].GetLastFood();
        	LastWater = AnimalData[k].GetLastWater();
        	rng = AnimalData[k].Getrng().toString();
        	
        	Data.append("ANIMAL:").append(health).append(",").append(thirst).append(",").append(hunger).append(",").append(evolutionaryFitness).append(",").append(movement).append(",").append(isMale).append(",").append(Species).append(",").append(LastFood).append(",").append(LastWater).append(",").append(rng).append("\n");
    	}
    	
   		return Data.toString();
   	}
    
    private String GetPlantData(Plant[] PlantData)
    {
    	StringBuilder Data = new StringBuilder();
    	int k;
    	String growthRate;
    	String food;
    	String maxFood;

    	
    	for(k = 0 ; k < PlantData.length ; k++)
    	{
        	growthRate = String.valueOf(PlantData[k].GetGrowthRate());
        	food = String.valueOf(PlantData[k].GetFood());
        	maxFood = String.valueOf(PlantData[k].GetMaxFood());
        	
        	Data.append("PLANT:").append(growthRate).append(",").append(food).append(",").append(maxFood).append("\n");
    	}
    	
   		return Data.toString();
   	}
}
