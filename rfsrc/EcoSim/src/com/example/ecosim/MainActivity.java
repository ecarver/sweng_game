package com.example.ecosim;

import java.util.ArrayList;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	// Our OpenGL Surfaceview
	private OrganismInfo organismData[][][];
	private SimulationEngine engine;
	private GLSurfaceView glSurfaceView;
	private GLSurf glSurf;
	private RenderList _renderList;
	private TextQueue _textQueue;
	private CurrentTileInfo _tileInfo;
	private enum StartMenuState { START_SIMULATION_SELECTED, LOAD_SIMULATION_SELECTED, HOW_TO_PLAY_SELECTED, CREDITS_SELECTED };
	private StartMenuState _startMenuState;
	private final String credits = "CREDITS\nEric Carver\nCarlo Perottino\nClay Wagner\nDoug Weber";
    private static final String HOW_TO_PLAY = "Press \"Start Simulation\" to begin a new simulation at a predetermined starting point.\n" +
             "Press \"Load Simulation\" to resume the last simulation.\n" +
            "Step the simulation forward by pressing \"Step.\"\n" +
            "Move the cursor over a tile to show information on its occupants.\n";
    private String sideMenuInfo = "";
    private int verifyExit;
	
	@SuppressLint({ "InlinedApi", "NewApi" }) @Override
	protected void onCreate(Bundle savedInstanceState) {
				
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
		super.onCreate(savedInstanceState);
		
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);

		_startMenuState = StartMenuState.START_SIMULATION_SELECTED;
		
		_renderList = new RenderList();

		engine = new SimulationEngine(4,4);
		
        //_renderList.clearList();
		glSurf = new GLSurf(this, _renderList, _textQueue);
        glSurfaceView = glSurf;
        
		setContentView(R.layout.activity_main);
		
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.gamelayout);
        
        RelativeLayout.LayoutParams glParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layout.addView(glSurfaceView, glParams);
        // 
        verifyExit = 0;
        
        _textQueue = new TextQueue(findViewById(R.id.gamelayout));
        _textQueue.addText("BETA", 1100, 10, 20, 
			10, 10, 10, 10, false);
        
        _tileInfo = new CurrentTileInfo(0,0);
		initStartMenu();
        

	}

	@Override
	protected void onPause() {
		super.onPause();
		glSurfaceView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		glSurfaceView.onResume();
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) 
	{
		if(_renderList.getScreen() == _renderList.START_MENU) {
			if(keyCode == 19) {
				if(_startMenuState == StartMenuState.START_SIMULATION_SELECTED) {
					_renderList.deleteRenderLink("startmenuselect");
					_renderList.addRenderLink(new RenderLink("startmenuselect", "startmenuselect", 2, 482.f, 42.f));
					_startMenuState = StartMenuState.CREDITS_SELECTED;
				} else if(_startMenuState == StartMenuState.LOAD_SIMULATION_SELECTED) {
					_renderList.deleteRenderLink("startmenuselect");
					_renderList.addRenderLink(new RenderLink("startmenuselect", "startmenuselect", 2, 482.f, 303.f));
					_startMenuState = StartMenuState.START_SIMULATION_SELECTED;
				} else if(_startMenuState == StartMenuState.HOW_TO_PLAY_SELECTED) {
					_renderList.deleteRenderLink("startmenuselect");
					_renderList.addRenderLink(new RenderLink("startmenuselect", "startmenuselect", 2, 482.f, 216.f));
					_startMenuState = StartMenuState.LOAD_SIMULATION_SELECTED;
				} else if(_startMenuState == StartMenuState.CREDITS_SELECTED) {
					_renderList.deleteRenderLink("startmenuselect");
					_renderList.addRenderLink(new RenderLink("startmenuselect", "startmenuselect", 2, 482.f, 129.f));
					_startMenuState = StartMenuState.HOW_TO_PLAY_SELECTED;
				}
			} else 	if(keyCode == 20) {
				if(_startMenuState == StartMenuState.START_SIMULATION_SELECTED) {
					_renderList.deleteRenderLink("startmenuselect");
					_renderList.addRenderLink(new RenderLink("startmenuselect", "startmenuselect", 2, 482.f, 216.f));
					_startMenuState = StartMenuState.LOAD_SIMULATION_SELECTED;
				} else if(_startMenuState == StartMenuState.LOAD_SIMULATION_SELECTED) {
					_renderList.deleteRenderLink("startmenuselect");
					_renderList.addRenderLink(new RenderLink("startmenuselect", "startmenuselect", 2, 482.f, 129.f));
					_startMenuState = StartMenuState.HOW_TO_PLAY_SELECTED;
				} else if(_startMenuState == StartMenuState.HOW_TO_PLAY_SELECTED) {
					_renderList.deleteRenderLink("startmenuselect");
					_renderList.addRenderLink(new RenderLink("startmenuselect", "startmenuselect", 2, 482.f, 42.f));
					_startMenuState = StartMenuState.CREDITS_SELECTED;
				} else if(_startMenuState == StartMenuState.CREDITS_SELECTED) {
					_renderList.deleteRenderLink("startmenuselect");
					_renderList.addRenderLink(new RenderLink("startmenuselect", "startmenuselect", 2, 482.f, 303.f));
					_startMenuState = StartMenuState.START_SIMULATION_SELECTED;
				}
			} else if(keyCode == 66) {
				if(_startMenuState == StartMenuState.START_SIMULATION_SELECTED) {
					//Toast.makeText(this.getApplicationContext(), "Start Simulation", 
					//		Toast.LENGTH_LONG).show();
					_renderList.setScreen(_renderList.ECO_MAP);
					_textQueue.removeText("Up Arrow: Move Up | Down Arrow: Move Down | Enter Key: Select");
					initStartSimulation();
				} else if(_startMenuState == StartMenuState.LOAD_SIMULATION_SELECTED) {
					Toast.makeText(this.getApplicationContext(), "Load Simulation", 
							Toast.LENGTH_LONG).show(); 
				} else if(_startMenuState == StartMenuState.HOW_TO_PLAY_SELECTED) {
					//Toast.makeText(this.getApplicationContext(), "How to Play", 
					//		Toast.LENGTH_LONG).show(); 
					_textQueue.removeText("Up Arrow: Move Up | Down Arrow: Move Down | Enter Key: Select");
					_renderList.setScreen(_renderList.HOW_TO_PLAY_SCREEN);
					initHowToPlay();
					
				} else if(_startMenuState == StartMenuState.CREDITS_SELECTED) {
					//Toast.makeText(this.getApplicationContext(), "Credits", 
					//		Toast.LENGTH_LONG).show(); 
					//_renderList.clearList();
					_textQueue.removeText("Up Arrow: Move Up | Down Arrow: Move Down | Enter Key: Select");
					_renderList.setScreen(_renderList.CREDITS_SCREEN);
					initCredits();
					
				}
			}
		} else if(_renderList.getScreen() == _renderList.CREDITS_SCREEN) {
			if(keyCode == 67) {
				_textQueue.removeText(credits);
				_textQueue.removeText("Backspace: Return to Start Menu");
				_renderList.setScreen(_renderList.START_MENU);
				initStartMenu();
			}
		} else if(_renderList.getScreen() == _renderList.HOW_TO_PLAY_SCREEN) {
			if(keyCode == 67) {
				_textQueue.removeText(HOW_TO_PLAY);
				_textQueue.removeText("Backspace: Return to Start Menu");
				_renderList.setScreen(_renderList.START_MENU);
				initStartMenu();
			}
		} else if(_renderList.getScreen() == _renderList.ECO_MAP) {
			if(keyCode == 19) {
				_textQueue.removeText("Tile(" + _tileInfo.getTileX() + "," + _tileInfo.getTileY() + ")");
				_renderList.deleteRenderLink("tileselect");
				_tileInfo.moveUp();
				_textQueue.addText("Tile(" + _tileInfo.getTileX() + "," + _tileInfo.getTileY() + ")", 410, 10, 11, 
						10, 10, 10, 10, false);
				_renderList.addRenderLink(new RenderLink("tileselect", "tileselect", 3, _tileInfo.getXLocation(), _tileInfo.getYLocation()));
			} else if(keyCode == 20) {
				_textQueue.removeText("Tile(" + _tileInfo.getTileX() + "," + _tileInfo.getTileY() + ")");
				_renderList.deleteRenderLink("tileselect");
				_tileInfo.moveDown();
				_textQueue.addText("Tile(" + _tileInfo.getTileX() + "," + _tileInfo.getTileY() + ")", 410, 10, 11, 
						10, 10, 10, 10, false);
				_renderList.addRenderLink(new RenderLink("tileselect", "tileselect", 3, _tileInfo.getXLocation(), _tileInfo.getYLocation()));
			} else if(keyCode == 21) {
				_textQueue.removeText("Tile(" + _tileInfo.getTileX() + "," + _tileInfo.getTileY() + ")");
				_renderList.deleteRenderLink("tileselect");
				_tileInfo.moveLeft();
				_textQueue.addText("Tile(" + _tileInfo.getTileX() + "," + _tileInfo.getTileY() + ")", 410, 10, 11, 
						10, 10, 10, 10, false);
				_renderList.addRenderLink(new RenderLink("tileselect", "tileselect", 3, _tileInfo.getXLocation(), _tileInfo.getYLocation()));
			} else if(keyCode == 22) {
				_textQueue.removeText("Tile(" + _tileInfo.getTileX() + "," + _tileInfo.getTileY() + ")");
				_renderList.deleteRenderLink("tileselect");
				_tileInfo.moveRight();
				_textQueue.addText("Tile(" + _tileInfo.getTileX() + "," + _tileInfo.getTileY() + ")", 410, 10, 11, 
						10, 10, 10, 10, false);
				_renderList.addRenderLink(new RenderLink("tileselect", "tileselect", 3, _tileInfo.getXLocation(), _tileInfo.getYLocation()));
			} else if(keyCode == 66) {
				Toast.makeText(this.getApplicationContext(), "Step", 
					Toast.LENGTH_LONG).show();
				
				//TODO: Step here
			} else if (keyCode == 62) {
				_renderList.setScreen(_renderList.STAT_SCREEN);
				_textQueue.removeText("Name: bear1\nLife Remaining: 55\nLast Action: Fight\n\nName: bear2\nLife Remaining: 55\nLast Action: Fight\n\n"
						+ "Name: bear3\nLife Remaining: 55\nLast Action: Fight\n\nName: bear4\nLife Remaining: 55\nLast Action: Fight\n\n");
					_textQueue.removeText("Tile(" + _tileInfo.getTileX() + "," + _tileInfo.getTileY() + ")");
					_textQueue.removeText("Directional Arrows: Move | Enter Key: Step | Space Key: Stats | Escape Key: Save and Exit");
				initStatsMenu();
					
					//TODO: Step here			
			} else if (keyCode == 4) {
				if(verifyExit > 0) {
					//TODO: Save
					_textQueue.removeText("Name: bear1\nLife Remaining: 55\nLast Action: Fight\n\nName: bear2\nLife Remaining: 55\nLast Action: Fight\n\n"
						+ "Name: bear3\nLife Remaining: 55\nLast Action: Fight\n\nName: bear4\nLife Remaining: 55\nLast Action: Fight\n\n");
					_textQueue.removeText("Tile(" + _tileInfo.getTileX() + "," + _tileInfo.getTileY() + ")");
					_textQueue.removeText("Directional Arrows: Move | Enter Key: Step | Space Key: Stats | Escape Key: Save and Exit");
					_renderList.setScreen(_renderList.START_MENU);
					initStartMenu();
				} else {
					Toast.makeText(this.getApplicationContext(), "Press escape one more time to save and exit.", 
							Toast.LENGTH_LONG).show();
					verifyExit++;
				}
			}
		} else if(_renderList.getScreen() == _renderList.STAT_SCREEN) {
			if(keyCode == 67) {
				_textQueue.removeText("Backspace: Return to Start Menu");
				_textQueue.removeText("Some text");
				_renderList.deleteRenderLink("background");
				_renderList.setScreen(_renderList.ECO_MAP);
				initStartSimulation();
			}
		}
		glSurf.KeyDown(keyCode,event);
		return true;
	}
	
	public void initStartMenu() {
		_renderList.clearList();
        _renderList.addRenderLink(new RenderLink("logo", "logo", 1, 520.f, 469.f));
        _renderList.addRenderLink(new RenderLink("title", "title", 1, 457.f, 383.f));
        _renderList.addRenderLink(new RenderLink("startgamebutton", "startgamebutton", 1, 482.f, 303.f));
        _renderList.addRenderLink(new RenderLink("loadgamebutton", "loadgamebutton", 1, 482.f, 216.f));
        _renderList.addRenderLink(new RenderLink("howtoplaygamebutton", "howtoplaygamebutton", 1, 482.f, 129.f));
        _renderList.addRenderLink(new RenderLink("creditsgamebutton", "creditsgamebutton", 1, 482.f, 42.f));
        
		if(_startMenuState == StartMenuState.START_SIMULATION_SELECTED) {
			_renderList.addRenderLink(new RenderLink("startmenuselect", "startmenuselect", 2, 482.f, 303.f));
		} else if(_startMenuState == StartMenuState.LOAD_SIMULATION_SELECTED) {
			_renderList.addRenderLink(new RenderLink("startmenuselect", "startmenuselect", 2, 482.f, 216.f));
		} else if(_startMenuState == StartMenuState.HOW_TO_PLAY_SELECTED) {
			_renderList.addRenderLink(new RenderLink("startmenuselect", "startmenuselect", 2, 482.f, 129.f)); 
		} else if(_startMenuState == StartMenuState.CREDITS_SELECTED) {
			_renderList.addRenderLink(new RenderLink("startmenuselect", "startmenuselect", 2, 482.f, 42.f));
		}
		
		_textQueue.addText("Up Arrow: Move Up | Down Arrow: Move Down | Enter Key: Select", 1, 670, 12, 
				10, 10, 10, 10, false);
	}

	public void initCredits() {
		_renderList.clearList();
		_textQueue.removeText("Up Arrow: Move Up | Down Arrow: Move Down | Enter Key: Select");
		_renderList.addRenderLink(new RenderLink("logo", "logo", 1, 520.f, 469.f));
        _renderList.addRenderLink(new RenderLink("title", "title", 1, 457.f, 383.f));
		_textQueue.addText(credits, 510, 350, 20, 
			10, 10, 10, 10, true);
		_textQueue.addText("Backspace: Return to Start Menu", 1, 670, 12, 
				10, 10, 10, 10, false);
	}
	
	public void initHowToPlay() {
		_renderList.clearList();
		_textQueue.removeText("Up Arrow: Move Up | Down Arrow: Move Down | Enter Key: Select");
		_renderList.addRenderLink(new RenderLink("logo", "logo", 1, 520.f, 469.f));
        _renderList.addRenderLink(new RenderLink("title", "title", 1, 457.f, 383.f));
        _textQueue.addText(HOW_TO_PLAY, 80, 360, 15, 
    			1, 1, 1, 1, true);
		_textQueue.addText("Backspace: Return to Start Menu", 1, 670, 12, 
				10, 10, 10, 10, true);
	}
	
	public void initStartSimulation() {
		int tempx = _tileInfo.getTileX();
		int tempy = _tileInfo.getTileY();
		engine = new SimulationEngine(4,4);
		
		//TODO: Change hardcode
		organismData = new OrganismInfo[engine.grid.xSize][engine.grid.ySize][4];
		
		for(int x = 0; x < engine.grid.xSize; x++) {
			for(int y = 0; y < engine.grid.ySize; y--) {
				for(int z = 0; z < 4; z++)  {
					organismData[x][y][z] = new OrganismInfo("null","null","null");
				}
			}
		}
		
		_renderList.clearList();
		//Log.v("engine", Integer.toString(engine.grid.xSize));
		engine.genRandomGrid();
		for(int x = 0; x < engine.grid.xSize; x++) {
			for(int y = 0; y < engine.grid.ySize; y++) {
				//Log.v("engine", "x: " + Integer.toString(x) + "y: " + Integer.toString(y));
				//Log.v("engine", engine.grid.tiles[x][y].GetEnvironmentType().toString());
				//Log.v("engine", Integer.toString(engine.grid.tiles[x][y].ANIMAL_CAPACITY));
				//Log.v("engine", "YOLO" +TileEnvironmentType.DESERT.toString());
				Log.v("engine", engine.grid.tiles[x][y].GetEnvironmentTypeToString());
				if(engine.grid.tiles[x][y].GetEnvironmentTypeToString().equals("DESERT")) {
					Log.v("engine", "here");
					_tileInfo.setLocation(x, y);
					_renderList.addRenderLink(new RenderLink("tile" + Integer.toString((y*4) + x), "grasstile", 1, 
						_tileInfo.getXLocation(), _tileInfo.getYLocation()));
				} else if(engine.grid.tiles[x][y].GetEnvironmentTypeToString().equals("FOREST")) {
					_tileInfo.setLocation(x, y);
					_renderList.addRenderLink(new RenderLink("tile" + Integer.toString((y*4) + x), "watertile", 1, 
						_tileInfo.getXLocation(), _tileInfo.getYLocation()));
				}
				
				ArrayList<Animal> animals = new ArrayList<Animal>();
				ArrayList<Plant> plants = new ArrayList<Plant>();
				
				animals.addAll(engine.grid.tiles[x][y].GetAnimals());
				
				plants.addAll(engine.grid.tiles[x][y].GetPlants());

				ArrayList<OrganismInfo>	tempOrganismInfo = new ArrayList<OrganismInfo>();
				
				for(int i = 0; i < animals.size(); i++) {
					tempOrganismInfo.add(new OrganismInfo(animals.get(i).species + Integer.toString(animals.get(i).id), 
						Float.toString(animals.get(i).GetHealth()),  animals.get(i).getLastAction()));
				}
				
				for(int i = 0; i < plants.size(); i++) {
					tempOrganismInfo.add(new OrganismInfo(plants.get(i).species + Integer.toString(plants.get(i).id), 
						"N/A",  plants.get(i).getLastAction()));
				}
				
				for(int i = 0; i < tempOrganismInfo.size(); i++) {
					organismData[x][y][i] = tempOrganismInfo.get(i);
				}

			}
		}
		
		_tileInfo.setLocation(tempx,tempy);
		// TODO: Load simulation from save
		//_renderList.clearList();
		_textQueue.removeText("Up Arrow: Move Up | Down Arrow: Move Down | Enter Key: Select");
		/*
		_renderList.addRenderLink(new RenderLink("sidemenu", "sidemenu", 1, -9.0f, -1.1f));
		_renderList.addRenderLink(new RenderLink("tile0", "watertile", 1, 575f, 488f));
		_renderList.addRenderLink(new RenderLink("tile1", "grasstile", 1, 703f, 488f));
		_renderList.addRenderLink(new RenderLink("tile2", "grasstile", 1, 831f, 488f));
		_renderList.addRenderLink(new RenderLink("tile3", "grasstile", 1, 959f, 488f));
		_renderList.addRenderLink(new RenderLink("tile4", "grasstile", 1, 575f, 368f));
		_renderList.addRenderLink(new RenderLink("tile5", "grasstile", 1, 703f, 368f));
		_renderList.addRenderLink(new RenderLink("tile6", "watertile", 1, 831f, 368f));
		_renderList.addRenderLink(new RenderLink("tile7", "grasstile", 1, 959, 368f));
		_renderList.addRenderLink(new RenderLink("tile8", "grasstile", 1, 575f, 248f));
		_renderList.addRenderLink(new RenderLink("tile9", "grasstile", 1, 703f, 248f));
		_renderList.addRenderLink(new RenderLink("tile10", "grasstile", 1, 831f, 248f));
		_renderList.addRenderLink(new RenderLink("tile11", "grasstile", 1, 959f, 248f));
		_renderList.addRenderLink(new RenderLink("tile12", "watertile", 1, 575f, 128f));
		_renderList.addRenderLink(new RenderLink("tile13", "grasstile", 1, 703f, 128f));
		_renderList.addRenderLink(new RenderLink("tile14", "grasstile", 1, 831f, 128f));
		_renderList.addRenderLink(new RenderLink("tile15", "grasstile", 1, 959f, 128f));
		*/
		//fake code
		_renderList.addRenderLink(new RenderLink("rabbit1", "rabbit", 2, 959f, 128f));
		_renderList.addRenderLink(new RenderLink("rabbit2", "rabbit", 2, 959f, 192f));
		_renderList.addRenderLink(new RenderLink("flower1", "flower", 2, 1023f, 128f));
		_renderList.addRenderLink(new RenderLink("bear1", "bear", 2, 1023f, 192f));
		_renderList.addRenderLink(new RenderLink("flower2", "flower", 2, getAnimalLocationX(0,2), getAnimalLocationY(0,2)));
		_textQueue.addText("Name: bear1\nLife Remaining: 55\nLast Action: Fight\n\nName: bear2\nLife Remaining: 55\nLast Action: Fight\n\n"
				+ "Name: bear3\nLife Remaining: 55\nLast Action: Fight\n\nName: bear4\nLife Remaining: 55\nLast Action: Fight\n\n", 0, 0, 18, 
				10, 10, 10, 10, false);
		//Actual code
		_renderList.addRenderLink(new RenderLink("tileselect", "tileselect", 3, _tileInfo.getXLocation(), _tileInfo.getYLocation()));
		_textQueue.addText("Directional Arrows: Move | Enter Key: Step | Space Key: Stats | Escape Key: Save and Exit", 410, 670, 10, 
				10, 10, 10, 10, false);
		//_textQueue.addText("Tile(0,0)", 410, 10, 11, 
		//		10, 10, 10, 10, false);
	}
	
	public void initStatsMenu() {
		_renderList.addRenderLink(new RenderLink("background", "background", 10, -30.f, 0));
		_textQueue.addText("Some text", 0, 0, 18, 
				10, 10, 10, 10, false);
		_textQueue.addText("Backspace: Return to Start Menu", 1, 670, 12, 
				10, 10, 10, 10, true);
	}
	
	public float getAnimalLocationX(int tileX, int spot) {
		float locationX = 0.f;
		switch (tileX) {
			case 0:  locationX = 575f; break;
			case 1:  locationX = 703f; break;
			case 2:  locationX = 831f; break;
			case 3:  locationX = 959f; break;
		}
		
		switch(spot) {
			case 0: break;
			case 1: locationX = locationX + 64; break;
			case 2: break;
			case 3: locationX = locationX + 64; break;			
		}
		
		return locationX;	
	}
	
	public float getAnimalLocationY(int tileY, int spot) {
		float locationY = 0.f;
		switch (tileY) {
			case 0:  locationY = 488f; break;
			case 1:  locationY = 368f; break;
			case 2:  locationY = 248f; break;
			case 3:  locationY = 128f; break;
		}
		
		switch(spot) {
			case 0: locationY = locationY - 64; break;
			case 1: locationY = locationY - 64; break;
			case 2: break;
			case 3: break;		
		}
		
		return locationY;	
	}

}

class CurrentTileInfo {
	private int _tileX;
	private int _tileY;
	
	public CurrentTileInfo(int x, int y) {
		_tileX = x;
		_tileY = y;
	}
	
	public int getTileX() {
		return _tileX;
	}
	
	public int getTileY() {
		return _tileY;
	}
	
	public String getTileArrayName() {
		
		//x = index % Width;

		//y = (index - x) / Width;
		
		return "tile" + Integer.toString((_tileY*4) + _tileX);
	}
	
	public float getXLocation() {
		switch (_tileX) {
			case 0:  return 575f;
			case 1:  return 703f;
			case 2:  return 831f;
			case 3:  return 959f;
		}
		
		return -1f;
	}
	
	public float getYLocation() {
		switch (_tileY) {
			case 0:  return 488f;
			case 1:  return 368f;
			case 2:  return 248f;
			case 3:  return 128f;
		}
		
		return -1f;
	}
	
	public void moveUp() {
		switch (_tileY) {
			case 0:  _tileY = 0; break;
			case 1:  _tileY = 0; break;
			case 2:  _tileY = 1; break;
			case 3:  _tileY = 2; break;
		}		
	}
	
	public void moveDown() {
		switch (_tileY) {
			case 0:  _tileY = 1; break;
			case 1:  _tileY = 2; break;
			case 2:  _tileY = 3; break;
			case 3:  _tileY = 3; break;
		}		
	}
	
	public void moveLeft() {
		switch (_tileX) {
			case 0:  _tileX = 0; break;
			case 1:  _tileX = 0; break;
			case 2:  _tileX = 1; break;
			case 3:  _tileX = 2; break;
		}		
	}
	
	public void moveRight() {
		switch (_tileX) {
			case 0:  _tileX = 1; break;
			case 1:  _tileX= 2; break;
			case 2:  _tileX = 3; break;
			case 3:  _tileX = 3; break;
		}		
	}
	
	public void setLocation(int x, int y) {
		_tileX = x;
		_tileY = y;
	}
	
}

class OrganismInfo {
	private String _name;
	private String _health;
	private String _lastAction;
	
	public OrganismInfo(String name, String health, String lastAction) {
		_name = name;
		_health = health;
		_lastAction = lastAction;
	}
	
	public String getName() {
		return _name;
	}
	
	public String getHealth() {
		return _health;
	}
	
	public String getLastAction() {
		return _lastAction;
	}
}
