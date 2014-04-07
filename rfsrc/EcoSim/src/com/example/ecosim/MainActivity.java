package com.example.ecosim;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;
public class MainActivity extends Activity {
	
	TextQueue text;
	
	// Our OpenGL Surfaceview
	private GLSurfaceView glSurfaceView;
	private GLSurf glSurf;
	private RenderList _renderList;
	private TextQueue _textQueue;
	private enum StartMenuState { START_SIMULATION_SELECTED, LOAD_SIMULATION_SELECTED, HOW_TO_PLAY_SELECTED, CREDITS_SELECTED };
	private StartMenuState _startMenuState;
	private final String credits = "     CREDITS\n   Eric Carver\nCarlo Perottino\n  Clay Wagner\n  Doug Weber";
    private static final String HOW_TO_PLAY = "Press \"Start Simulation\" to begin a new simulation at a predetermined starting point.\n" +
            "Press \"Load Simulation\" to resume the last simulation.\n" +
            "Step the simulation forward by pressing \"Step.\"\n" +
            "Move the cursor over a tile to show information on its occupants.\n";
	
	@SuppressLint({ "InlinedApi", "NewApi" }) @Override
	protected void onCreate(Bundle savedInstanceState) {
				
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
		super.onCreate(savedInstanceState);
		
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);

		_startMenuState = StartMenuState.START_SIMULATION_SELECTED;
		
		_renderList = new RenderList();

        //_renderList.clearList();
		glSurf = new GLSurf(this, _renderList, _textQueue);
        glSurfaceView = glSurf;
        
		setContentView(R.layout.activity_main);
		
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.gamelayout);
        
        RelativeLayout.LayoutParams glParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layout.addView(glSurfaceView, glParams);
        // 
        
        _textQueue = new TextQueue(findViewById(R.id.gamelayout));
        _textQueue.addText("ALPHA", 10, 10, 20, 
			10, 10, 10, 10);
        //
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
		//19
		//20
		//66 neter
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
					Toast.makeText(this.getApplicationContext(), "Start Simulation", 
							Toast.LENGTH_LONG).show(); 
				} else if(_startMenuState == StartMenuState.LOAD_SIMULATION_SELECTED) {
					Toast.makeText(this.getApplicationContext(), "Load Simulation", 
							Toast.LENGTH_LONG).show(); 
				} else if(_startMenuState == StartMenuState.HOW_TO_PLAY_SELECTED) {
					Toast.makeText(this.getApplicationContext(), "How to Play", 
							Toast.LENGTH_LONG).show(); 
					_textQueue.removeText("Backspace: Return to Start Menu");
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
		} else if(_renderList.getScreen() == _renderList.HOW_TO_PLAY_SCREEN); {
			if(keyCode == 67) {
				_textQueue.removeText(HOW_TO_PLAY);
				_textQueue.removeText("Backspace: Return to Start Menu");
				_renderList.setScreen(_renderList.START_MENU);
				initStartMenu();
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
				10, 10, 10, 10);

	}

	public void initCredits() {
		_renderList.clearList();
		_textQueue.removeText("Up Arrow: Move Up | Down Arrow: Move Down | Enter Key: Select");
		_textQueue.addText("Backspace: Return to Start Menu", 1, 670, 12, 
				10, 10, 10, 10);
		_renderList.addRenderLink(new RenderLink("logo", "logo", 1, 520.f, 469.f));
        _renderList.addRenderLink(new RenderLink("title", "title", 1, 457.f, 383.f));
		_textQueue.addText(credits, 510, 350, 20, 
			10, 10, 10, 10);
	}
	
	public void initHowToPlay() {
		_renderList.clearList();
		_renderList.addRenderLink(new RenderLink("logo", "logo", 1, 520.f, 469.f));
        _renderList.addRenderLink(new RenderLink("title", "title", 1, 457.f, 383.f));
        _textQueue.addText(HOW_TO_PLAY, 0, 350, 15, 
    			10, 10, 10, 10);
		_textQueue.addText("Backspace: Return to Start Menu", 1, 670, 12, 
				10, 10, 10, 10);
	}
	

}
