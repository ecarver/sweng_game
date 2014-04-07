package com.example.ecosim;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.KeyEvent;
//import android.view.MotionEvent;
import android.widget.Toast;

public class GLSurf extends GLSurfaceView {
	private Context _context;
	private final GLRenderer mRenderer;
	private RenderList _renderList;
	private TextQueue _textQueue;
	
	public GLSurf(Context context, RenderList renderList, TextQueue textQueue) {
        super(context);
        _context = context;

        setEGLContextClientVersion(2);

        _renderList = renderList;
        _textQueue = textQueue;
        mRenderer = new GLRenderer(context, _renderList, _textQueue);
        setRenderer(mRenderer);

        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

	@Override
	public void onPause() {
		super.onPause();
		mRenderer.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		mRenderer.onResume();
	}
	
	public boolean KeyDown(int keyCode, KeyEvent event) 
	{
		//Toast.makeText(_context.getApplicationContext(), "Key: " + keyCode, 
		//		Toast.LENGTH_LONG).show(); 
		mRenderer.keyEvent(keyCode);
		return true;
	}
	

}
