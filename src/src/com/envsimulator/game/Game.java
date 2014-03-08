package com.envsimulator.game;

import com.envsimulator.userinterface.userinterface;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.opengl.GLSurfaceView;
import android.view.Window;
import android.view.WindowManager;
import tv.ouya.console.api.OuyaController;

public class Game extends Activity {
	private UserInterface userInterface;
	private Render render;
	
	private final int maxTileX = 5;
	private final int maxTileY = 5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// requesting to turn the title OFF
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	// making it full screen
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
    			 WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	
        userInterface = new UserInterface(this);
        setContentView(userInterface);
    }
    
    // Not really sure if these are needed or not
    @Override
    protected void onResume() {
        super.onResume();
        userInterface.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        userInterface.onPause();
    }
}
