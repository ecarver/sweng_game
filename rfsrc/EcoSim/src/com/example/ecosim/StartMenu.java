//Author: Carlo Perottino

package com.example.ecosim;

import java.io.*;
import java.util.*;
import android.app.Activity;

public final class StartMenu extends Activity
{
	static final String FileName = "SWENG_GAME";
  
  public SimulationEngine LoadSimulation() throws IOException
  {
  	String Data; 
  	String[] DataArry;
  	String tmp;
  	String[] tmpArry;
  	int grid_x;
  	int grid_y;
  	int i;
  	int j;
  	SimulationEngine NewEngine;
  	FileInputStream fin;

  	try
  	{
      fin = new FileInputStream(FileName);
  	}
  	catch(Exception e)
  	{
  		return null;
  	}

      BufferedReader reader = new BufferedReader(new InputStreamReader(fin));
      StringBuilder sb = new StringBuilder();
      String line = null;
     
      while ((line = reader.readLine()) != null) 
      {
        sb.append(line).append("\n");
      }
      
      reader.close();
      fin.close(); 

      Data = sb.toString();
      
      DataArry = Data.split("\\r?\\n");
      
      tmp = DataArry[0];
      
      tmp.replaceAll("GridSize:", "");
      
      tmpArry = tmp.split("x");
      
      grid_x = Integer.parseInt(tmpArry[0]);
      
      grid_y = Integer.parseInt(tmpArry[1]);
    		  
      NewEngine = new SimulationEngine(grid_x, grid_y);
     
      for(i = 1 ; i < DataArry.length ; i++)
      {
    	  if(DataArry[i].contains("Tile:"))
    	  {
    		  tmp = DataArry[i].replaceAll("Tile:", "");
    		  tmpArry = tmp.split("x");
    	      grid_x = Integer.parseInt(tmpArry[0]);
    	      grid_y = Integer.parseInt(tmpArry[1]);
    	      
    	      if(DataArry[i+1] == "DESSERT")
    	      {
    	    	  NewEngine.grid.tiles[grid_x][grid_y].environmentType.hasWater = false;
    	      }
    	      else if(DataArry[i+1] == "FOREST")
    	      {
    	    	  NewEngine.grid.tiles[grid_x][grid_y].environmentType.hasWater = true;
    	      }
    	      
    	      for(j = 1 ; j < j + 1 ; j++)
    	      {
    	    	  if(i+1+j >= DataArry.length)
    	    	  {
    	    		  break;
    	    	  }
    	    	  else if(DataArry[i+1+j].contains("Tile:"))
    	    	  {
    	    		  break;
    	    	  }
    	    	  else if(DataArry[i+1+j].contains("ANIMAL:"))
    	    	  {
    	    		  NewEngine.grid.tiles[grid_x][grid_y].addAnimal(ParseAnimalData(DataArry[i+1+j], grid_x, grid_y));
    	    	  }
    	    	  else if(DataArry[i+1+j].contains("PLANT:"))
    	    	  {
    	    		  NewEngine.grid.tiles[grid_x][grid_y].addPlant(ParsePlantData(DataArry[i+1+j], grid_x, grid_y));
    	    	  }
    	      }
    	       
    	  }
      }
      
      return NewEngine;
  }

  private Animal ParseAnimalData(String Data, int x, int y)
  {
	  Animal NewAnimal;
	  String[] DataArry;
	  
	  Data.replaceAll("ANIMAL:", "");
	  DataArry = Data.split(",");
	  
	  NewAnimal = new Animal(x, y, AnimalSpecies.BEAR , 1.0f, true, Integer.parseInt(DataArry[11]) );
	  
	  NewAnimal.SetHealth(DataArry[0]);
	  NewAnimal.SetThirst(DataArry[1]);
	  NewAnimal.SetHunger(DataArry[2]);
	  NewAnimal.SetEvolutionaryFitness(DataArry[3]);
	  NewAnimal.SetAge(DataArry[4]);
	  NewAnimal.SetMovement(DataArry[5]);
	  NewAnimal.SetGender(DataArry[6]);
	  NewAnimal.SetSpecies(DataArry[7]);
	  NewAnimal.SetLastFood(DataArry[8]);
	  NewAnimal.SetLastWater(DataArry[9]);
	  NewAnimal.Setrng();
	  
	  return NewAnimal;
  }
  
  private Plant ParsePlantData(String Data, int x, int y)
  {
	  Plant NewPlant;
	  String[] DataArry;
	  
	  Data.replaceAll("PLANT:", "");
	  DataArry = Data.split(",");
	  
	  NewPlant = new Plant(x, y, Float.parseFloat(DataArry[2]), Float.parseFloat(DataArry[0]), Float.parseFloat(DataArry[1]), Integer.parseInt(DataArry[3]));
	  	  
	  return NewPlant;
  }
  
  public void SaveSimulation(SimulationEngine Engine) throws IOException
  {
  	String SimulationData = GetSimulationData(Engine);
  	FileOutputStream fos; 		
  	
  	fos = openFileOutput(FileName, MODE_PRIVATE);
  	fos.write(SimulationData.getBytes());
  	fos.close();

  }
  
  private String GetSimulationData(SimulationEngine Engine)
  {
      StringBuilder Data = new StringBuilder();
      Set<Animal> AnimalData;
      Set<Plant> PlantData;
  	int MaxGridX;
  	int MaxGridY;
  	int j;
  	int i;
  	
  	MaxGridX = Engine.grid.GetXSize();
  	MaxGridY = Engine.grid.GetYSize();
  	
  	Data.append("GridSize:").append(MaxGridX).append("x").append(MaxGridY).append("\n");
  	
  	Data.append("\n");
  	
  	for(i = 0 ; i < MaxGridY ; i++)
  	{
      	for(j = 0 ; j < MaxGridX ; j++)
      	{
      		Data.append("Tile:").append(j).append("x").append(i).append("\n");
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
  
  private String GetAnimalData(Set<Animal> AnimalSet)
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
  	int id;
  	Animal[] AnimalData = new Animal[AnimalSet.size()];

  	AnimalSet.toArray(AnimalData);
  	
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
      	id = AnimalData[k].id;
      	
      	Data.append("ANIMAL:").append(health).append(",").append(thirst).append(",").append(hunger).append(",").append(evolutionaryFitness).append(",").append(movement).append(",").append(isMale).append(",").append(Species).append(",").append(LastFood).append(",").append(LastWater).append(",").append(rng).append(",").append(id).append("\n");
  	}
  	
 		return Data.toString();
 	}
  
  private String GetPlantData(Set<Plant> PlantSet)
  {
  	StringBuilder Data = new StringBuilder();
  	int k;
  	String growthRate;
  	String food;
  	String maxFood;
  	int id;
  	Plant[] PlantData = new Plant[PlantSet.size()];

  	PlantSet.toArray(PlantData);
  	
  	for(k = 0 ; k < PlantData.length ; k++)
  	{
      	growthRate = String.valueOf(PlantData[k].GetGrowthRate());
      	food = String.valueOf(PlantData[k].GetFood());
      	maxFood = String.valueOf(PlantData[k].GetMaxFood());
      	id = PlantData[k].id;
      	
      	Data.append("PLANT:").append(growthRate).append(",").append(food).append(",").append(maxFood).append(",").append(id).append("\n");
  	}
  	
 		return Data.toString();
 	}
}
