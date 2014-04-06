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
	
	@SuppressLint({ "InlinedApi", "NewApi" }) @Override
	protected void onCreate(Bundle savedInstanceState) {
				
		// Turn off the window's title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        // Super
		super.onCreate(savedInstanceState);
		
		// Fullscreen mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);

        
        // We create our Surfaceview for our OpenGL here.
		
		glSurf = new GLSurf(this);
        glSurfaceView = glSurf;
        
        // Set our view.	
		setContentView(R.layout.activity_main);
		
		// Retrieve our Relative layout from our main layout we just set to our view.
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.gamelayout);
        
        // Attach our surfaceview to our relative layout from our main layout.
        RelativeLayout.LayoutParams glParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layout.addView(glSurfaceView, glParams);
        
        text = new TextQueue();
        text.addText(findViewById(R.id.gamelayout), "YOLO", 10, 10, 20, 
			10, 10, 10, 10);
        

	}

	@Override
	protected void onPause() {
		super.onPause();
		glSurfaceView.onPause();
		//text.removeText("YOLO");
	}

	@Override
	protected void onResume() {
		super.onResume();
		glSurfaceView.onResume();
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) 
	{
		
		glSurf.KeyDown(keyCode,event);
		return true;
	}

	

}
