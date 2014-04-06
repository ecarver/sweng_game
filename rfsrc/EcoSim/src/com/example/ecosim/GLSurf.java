package com.example.ecosim;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.KeyEvent;
//import android.view.MotionEvent;
import android.widget.Toast;

public class GLSurf extends GLSurfaceView {
	private Context _context;
	private final GLRenderer mRenderer;
	
	public GLSurf(Context context) {
        super(context);
        _context = context;
        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new GLRenderer(context);
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mRenderer.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mRenderer.onResume();
	}
	
	/*@Override
    public boolean onTouchEvent(MotionEvent e) {
		//mRenderer.processTouchEvent(e);
		return true;
	}*/
	
	public boolean KeyDown(int keyCode, KeyEvent event) 
	{
		//Toast.makeText(_context.getApplicationContext(), "Key: " + keyCode, 
		//		Toast.LENGTH_LONG).show(); 
		//System.out.println("hello!!!");
		//System.out.println(keyCode);
		mRenderer.keyEvent(keyCode);
		return true;
	}

}
