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
	
	@SuppressLint({ "InlinedApi", "NewApi" }) @Override
	protected void onCreate(Bundle savedInstanceState) {
				
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
		super.onCreate(savedInstanceState);
		
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);

		_startMenuState = StartMenuState.START_SIMULATION_SELECTED;
		
		_renderList = new RenderList();
		
		initStartMenu();

        //_renderList.clearList();
		glSurf = new GLSurf(this, _renderList, _textQueue);
        glSurfaceView = glSurf;
        
		setContentView(R.layout.activity_main);
		
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.gamelayout);
        
        RelativeLayout.LayoutParams glParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layout.addView(glSurfaceView, glParams);
        // 
        
        _textQueue = new TextQueue();
        _textQueue.addText(findViewById(R.id.gamelayout), "ALPHA", 10, 10, 20, 
			10, 10, 10, 10);
        

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
			} else 	if(keyCode == 66) {
				if(_startMenuState == StartMenuState.START_SIMULATION_SELECTED) {
					Toast.makeText(this.getApplicationContext(), "Start Simulation", 
							Toast.LENGTH_LONG).show(); 
				} else if(_startMenuState == StartMenuState.LOAD_SIMULATION_SELECTED) {
					Toast.makeText(this.getApplicationContext(), "Load Simulation", 
							Toast.LENGTH_LONG).show(); 
				} else if(_startMenuState == StartMenuState.HOW_TO_PLAY_SELECTED) {
					Toast.makeText(this.getApplicationContext(), "How to Play", 
							Toast.LENGTH_LONG).show(); 
					_startMenuState = StartMenuState.CREDITS_SELECTED;
				} else if(_startMenuState == StartMenuState.CREDITS_SELECTED) {
					Toast.makeText(this.getApplicationContext(), "Credits", 
							Toast.LENGTH_LONG).show(); 
					_renderList.clearList();
					_renderList.setScreen(_renderList.CREDITS_SCREEN);
					
				}
			}
		}
		glSurf.KeyDown(keyCode,event);
		return true;
	}
	
	public void initStartMenu() {
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

	}

	//public void
	

}
