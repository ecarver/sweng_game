package com.example.ecosim;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.util.Log;

public class GLRenderer implements Renderer {

	// Our matrices
	private final float[] mtrxProjection = new float[16];
	private final float[] mtrxView = new float[16];
	private final float[] mtrxProjectionAndView = new float[16];
	
	// Geometric variables
	public static float vertices[];
	public static short indices[];
	public static float uvs[];
	public FloatBuffer vertexBuffer;
	public ShortBuffer drawListBuffer;
	public FloatBuffer uvBuffer; 
	// Our screenresolution
	float	mScreenWidth = 1280;
	float	mScreenHeight = 720;
	float 	ssu = 1.0f;
	float 	ssx = 1.0f;
	float 	ssy = 1.0f;
	float 	swp = 1280.0f;
	float 	shp = 720.0f;

	// Misc
	Context mContext;
	long mLastTime;
	int mProgram;
	RenderList _renderList;
	TextQueue _textQueue;
	
	private EventQueue eventQueue;
	
	public GLRenderer(Context c, RenderList renderList, TextQueue textQueue)
	{
		mContext = c;
		mLastTime = System.currentTimeMillis() + 100;
		_renderList = renderList;
		_textQueue = textQueue;
		//sprite = new Sprite();
		
		eventQueue = new EventQueue();
	}
	
	public void onPause()
	{
		/* Do stuff to pause the renderer */
	}
	
	public void onResume()
	{
		/* Do stuff to resume the renderer */
		mLastTime = System.currentTimeMillis();
	}
	
	@Override
	public void onDrawFrame(GL10 unused) {
		
		// Get the current time
    	long now = System.currentTimeMillis();
    	
    	// We should make sure we are valid and sane
    	if (mLastTime > now) return;
        
    	
    	if(_renderList.getChange()) {
    		_renderList.setChangeToFalse();
    		SetupTriangle();
    		// Create the image information
    		SetupImage();    		
    	}
		
		// Render our example
		Render(mtrxProjectionAndView);
		
		// Save the current time to see how long it took :).
        mLastTime = now;
		
	}
	
	private void Render(float[] m) {
		
		// clear Screen and Depth Buffer, we have set the clear color as black.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        
        // get handle to vertex shader's vPosition member and add vertices
	    int mPositionHandle = GLES20.glGetAttribLocation(EcoShaderInfo.sp_Image, "vPosition");
	    GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
	    GLES20.glEnableVertexAttribArray(mPositionHandle);
	    
	    // Get handle to texture coordinates location and load the texture uvs
	    int mTexCoordLoc = GLES20.glGetAttribLocation(EcoShaderInfo.sp_Image, "a_texCoord" );
	    GLES20.glVertexAttribPointer ( mTexCoordLoc, 2, GLES20.GL_FLOAT, false, 0, uvBuffer);
	    GLES20.glEnableVertexAttribArray ( mTexCoordLoc );
	    
	    // Get handle to shape's transformation matrix and add our matrix
        int mtrxhandle = GLES20.glGetUniformLocation(EcoShaderInfo.sp_Image, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, m, 0);
        
        // Get handle to textures locations
        int mSamplerLoc = GLES20.glGetUniformLocation (EcoShaderInfo.sp_Image, "s_texture" );
        
        // Set the sampler texture unit to 0, where we have saved the texture.
        GLES20.glUniform1i ( mSamplerLoc, 0);

        // Draw the triangle
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);
        	
	}
	

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		
		// We need to know the current width and height.
		mScreenWidth = width;
		mScreenHeight = height;
		
		// Redo the Viewport, making it fullscreen.
		GLES20.glViewport(0, 0, (int)mScreenWidth, (int)mScreenHeight);
		
		// Clear our matrices
	    for(int i=0;i<16;i++)
	    {
	    	mtrxProjection[i] = 0.0f;
	    	mtrxView[i] = 0.0f;
	    	mtrxProjectionAndView[i] = 0.0f;
	    }
	    
	    // Setup our screen width and height for normal sprite translation.
	    Matrix.orthoM(mtrxProjection, 0, 0f, mScreenWidth, 0.0f, mScreenHeight, 0, 50);
	    
	    // Set the camera position (View matrix)
        Matrix.setLookAtM(mtrxView, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mtrxProjectionAndView, 0, mtrxProjection, 0, mtrxView, 0);
        
        // Setup our scaling system
		SetupScaling();
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		
		// Setup our scaling system
		SetupScaling();
		// Create the triangles
		SetupTriangle();
		// Create the image information
		SetupImage();
		
		// Set the clear color to black
		GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1);	
		
		GLES20.glEnable(GLES20.GL_BLEND);
	    GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
	    
	    // Create the shaders, images
	    int vertexShader = EcoShaderInfo.loadShader(GLES20.GL_VERTEX_SHADER, EcoShaderInfo.vs_Image);
	    int fragmentShader = EcoShaderInfo.loadShader(GLES20.GL_FRAGMENT_SHADER, EcoShaderInfo.fs_Image);

	    EcoShaderInfo.sp_Image = GLES20.glCreateProgram();             // create empty OpenGL ES Program
	    GLES20.glAttachShader(EcoShaderInfo.sp_Image, vertexShader);   // add the vertex shader to program
	    GLES20.glAttachShader(EcoShaderInfo.sp_Image, fragmentShader); // add the fragment shader to program
	    GLES20.glLinkProgram(EcoShaderInfo.sp_Image);                  // creates OpenGL ES program executables
	    
	    // Set our shader programm
		GLES20.glUseProgram(EcoShaderInfo.sp_Image);
	}
	
	public void SetupScaling()
	{
		// The screen resolutions
		swp = (int) (mContext.getResources().getDisplayMetrics().widthPixels);
		shp = (int) (mContext.getResources().getDisplayMetrics().heightPixels);
		
		// Orientation is assumed portrait
		ssx = swp / 1280.0f;
		ssy = shp / 720.0f;
		
		/**
		// Get our uniform scaler
		if(ssx > ssy)
    		ssu = ssy;
    	else
    		ssu = ssx;
    	*/
	}
	
	
	public void SetupImage()
	{
		// We will use a randomizer for randomizing the textures from texture atlas.
		// This is strictly optional as it only effects the output of our app,
		// Not the actual knowledge.
		
		// 30 imageobjects times 4 vertices times (u and v)
		uvs = new float[_renderList.getSize()*4*2];
		
		RenderLink loop = _renderList.getHead();
		
		// We will make 30 randomly textures objects
		for(int i=0; i<_renderList.getSize(); i++)
		{
			float x_start_pos = 0;
			float x_end_pos = 0.46040515653775f;
			float y_start_pos = 0;
			float y_end_pos = 0.28132387706856f;
			
			if(loop.getFilename().equals("logo")) {
				x_start_pos = 0;
				x_end_pos = 0.46040515653775f;
				y_start_pos = 0;
				y_end_pos = 0.28132387706856f;
			}

			else if(loop.getFilename().equals("title")) {
				x_start_pos = 0;
				x_end_pos = 0.68692449355433f;
				y_start_pos = 0.28723404255319f;
				y_end_pos = 0.38770685579196f;
			}
			
			else if(loop.getFilename().equals("startgamebutton")) {
				x_start_pos = 0;
				x_end_pos = 0.60036832412523f;
				y_start_pos = 0.39125295508274f;
				y_end_pos = 0.47399527186761f;
			}
			
			else if(loop.getFilename().equals("loadgamebutton")) {
				x_start_pos = 0;
				x_end_pos = 0.60036832412523f;
				y_start_pos = 0.5709219858156f;
				y_end_pos = 0.65602836879433f;
			}
			
			else if(loop.getFilename().equals("howtoplaygamebutton")) {
				x_start_pos = 0;
				x_end_pos = 0.60036832412523f;
				y_start_pos = 0.67139479905437f;
				y_end_pos = 0.7565011820331f;
			}
			
			else if(loop.getFilename().equals("creditsgamebutton")) {
				x_start_pos = 0;
				x_end_pos = 0.60036832412523f;
				y_start_pos = 0.76004728132388f;
				y_end_pos = 0.8451536643026f;
			}
			
			else if(loop.getFilename().equals("startmenuselect")) {
				x_start_pos = 0;
				x_end_pos = 0.60036832412523f;
				y_start_pos = 0.47872340425532f;
				y_end_pos = 0.5661938534279f;
			}
			
			else if(loop.getFilename().equals("sidemenu")) {
				x_start_pos = 0;
				x_end_pos = 0.98488664987406f;
				y_start_pos = 0.00199866755496f;
				y_end_pos = 0.48434377081945f;
			}
			
			else if(loop.getFilename().equals("tileselect")) {
				x_start_pos = 0;
				x_end_pos = 0.35012594458438f;
				y_start_pos = 0.50299800133245f;
				y_end_pos = 0.59093937375083f;
			}
			
			else if(loop.getFilename().equals("watertile")) {
				x_start_pos = 0;
				x_end_pos = 0.35012594458438f;
				y_start_pos = 0.61034043970686f;
				y_end_pos = 0.69828181212525f;
			}
			
			else if(loop.getFilename().equals("grasstile")) {
				x_start_pos = 0;
				x_end_pos = 0.35012594458438f;
				y_start_pos = 0.71730979347102f;
				y_end_pos = 0.80525116588941f;
			}
			
			else if(loop.getFilename().equals("rabbit")) {
				x_start_pos = 0;
				x_end_pos = 0.18891687657431f;
				y_start_pos = 0.82427848101266f;
				y_end_pos = 0.86958161225849f;
			}

			else if(loop.getFilename().equals("bear")) {
				x_start_pos = 0;
				x_end_pos = 0.18891687657431f;
				y_start_pos = 0.88860959360426f;
				y_end_pos =  0.9339127248501f;
			}
			
			else if(loop.getFilename().equals("flower")) {
				x_start_pos = 0;
				x_end_pos = 0.18891687657431f;
				y_start_pos = 0.95294003997335f;
				y_end_pos = 0.99824317121919f;
			}
			
			//397 1501
			uvs[(i*8) + 0] = x_start_pos;
			uvs[(i*8) + 1] = y_start_pos;
			uvs[(i*8) + 2] = x_start_pos;
			uvs[(i*8) + 3] = y_end_pos;
			uvs[(i*8) + 4] = x_end_pos;
			uvs[(i*8) + 5] = y_end_pos;
			uvs[(i*8) + 6] = x_end_pos;
			uvs[(i*8) + 7] = y_start_pos;
			
			
			
			loop = loop.getNext();
		}
		
		// The texture buffer
		ByteBuffer bb = ByteBuffer.allocateDirect(uvs.length * 4);
		bb.order(ByteOrder.nativeOrder());
		uvBuffer = bb.asFloatBuffer();
		uvBuffer.put(uvs);
		uvBuffer.position(0);
		
		// Generate Textures, if more needed, alter these numbers.
		int[] texturenames = new int[1];
		GLES20.glGenTextures(1, texturenames, 0);
		
		// Retrieve our image from resources
		int id = 1;
		if(_renderList.getScreen() == _renderList.START_MENU || _renderList.getScreen() == _renderList.CREDITS_SCREEN ||
				_renderList.getScreen() == _renderList.HOW_TO_PLAY_SCREEN) {
			id = mContext.getResources().getIdentifier("drawable/ecosimtextureatlas", null, mContext.getPackageName());
		} else if(_renderList.getScreen() == _renderList.ECO_MAP) {
			id = mContext.getResources().getIdentifier("drawable/simulationatlas", null, mContext.getPackageName());
		}
		
		// Temporary create a bitmap
		Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), id);
		
		// Bind texture to texturename
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[0]);
		
		// Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        
        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);
        
        // We are done using the bitmap so we should recycle it.
		bmp.recycle();

	}
	
	public void SetupTriangle()
	{
		// We will need a randomizer
		Random rnd = new Random();
		
		// Our collection of vertices
		vertices = new float[_renderList.getSize()*4*3];
		
		RenderLink loop = _renderList.getHead();
		
		// Create the vertex data
		for(int i=0;i<_renderList.getSize();i++)
		{
			Log.i("Loop", loop.getFilename());

			int offset_x = 0;
			int offset_y = 0;
			float scale_x = 543.0f;
			float scale_y = 846.0f;
			//System.out.println(loop.getFilename());
			
			if(loop.getFilename().equals("logo")) {
				offset_x = (int)loop.getPositionX();
				offset_y = (int)loop.getPositionY();
				scale_x = 250.0f;
				scale_y = 237.0f;
			}
			
			else if(loop.getFilename().equals("title")) {
				offset_x = (int)loop.getPositionX();
				offset_y = (int)loop.getPositionY();
				scale_x = 375.0f;
				scale_y = 81.0f;
			}
			
			else if(loop.getFilename().equals("startgamebutton")) {
				offset_x = (int)loop.getPositionX();
				offset_y = (int)loop.getPositionY();
				scale_x = 326.0f;
				scale_y = 68.0f;
			}
			
			else if(loop.getFilename().equals("loadgamebutton")) {
				offset_x = (int)loop.getPositionX();
				offset_y = (int)loop.getPositionY();
				scale_x = 326.0f;
				scale_y = 68.0f;
			}
			
			else if(loop.getFilename().equals("howtoplaygamebutton")) {
				offset_x = (int)loop.getPositionX();
				offset_y = (int)loop.getPositionY();
				scale_x = 326.0f;
				scale_y = 68.0f;
			}
			
			else if(loop.getFilename().equals("creditsgamebutton")) {
				offset_x = (int)loop.getPositionX();
				offset_y = (int)loop.getPositionY();
				scale_x = 326.0f;
				scale_y = 68.0f;
			}
			
			else if(loop.getFilename().equals("startmenuselect")) {
				offset_x = (int)loop.getPositionX();
				offset_y = (int)loop.getPositionY();
				scale_x = 326.0f;
				scale_y = 68.0f;
			}
			
			else if(loop.getFilename().equals("sidemenu")) {
				offset_x = (int)loop.getPositionX();
				offset_y = (int)loop.getPositionY();
				scale_x = 393.0f;
				scale_y = 722.0f;
			}
			
			else if(loop.getFilename().equals("tileselect")) {
				offset_x = (int)loop.getPositionX();
				offset_y = (int)loop.getPositionY();
				scale_x = 139.0f;
				scale_y = 128.0f;
			}

			else if(loop.getFilename().equals("watertile")) {
				offset_x = (int)loop.getPositionX();
				offset_y = (int)loop.getPositionY();
				scale_x = 139.0f;
				scale_y = 128.0f;
			}
			
			else if(loop.getFilename().equals("grasstile")) {
				offset_x = (int)loop.getPositionX();
				offset_y = (int)loop.getPositionY();
				scale_x = 139.0f;
				scale_y = 128.0f;
			}
			
			else if(loop.getFilename().equals("rabbit")) {
				offset_x = (int)loop.getPositionX();
				offset_y = (int)loop.getPositionY();
				scale_x = 75.0f;
				scale_y = 64.0f;
			}
			
			else if(loop.getFilename().equals("bear")) {
				offset_x = (int)loop.getPositionX();
				offset_y = (int)loop.getPositionY();
				scale_x = 75.0f;
				scale_y = 64.0f;
			}
			
			else if(loop.getFilename().equals("flower")) {
				offset_x = (int)loop.getPositionX();
				offset_y = (int)loop.getPositionY();
				scale_x = 75.0f;
				scale_y = 64.0f;
			}

			// Create the 2D parts of our 3D vertices, others are default 0.0f
			vertices[(i*12) + 0] = offset_x;
			vertices[(i*12) + 1] = offset_y + (scale_y);
			vertices[(i*12) + 2] = 0f;
			vertices[(i*12) + 3] = offset_x;
			vertices[(i*12) + 4] = offset_y;
			vertices[(i*12) + 5] = 0f;
			vertices[(i*12) + 6] = offset_x + (scale_x);
			vertices[(i*12) + 7] = offset_y;
			vertices[(i*12) + 8] = 0f;
			vertices[(i*12) + 9] = offset_x + (scale_x);
			vertices[(i*12) + 10] = offset_y + (scale_y);
			vertices[(i*12) + 11] = 0f;
			//
			loop = loop.getNext();
		}
		
		// The indices for all textured quads
		indices = new short[_renderList.getSize()*6]; 
		int last = 0;
		for(int i=0;i<_renderList.getSize();i++)
		{
			// We need to set the new indices for the new quad
			indices[(i*6) + 0] = (short) (last + 0);
			indices[(i*6) + 1] = (short) (last + 1);
			indices[(i*6) + 2] = (short) (last + 2);
			indices[(i*6) + 3] = (short) (last + 0);
			indices[(i*6) + 4] = (short) (last + 2);
			indices[(i*6) + 5] = (short) (last + 3);
			
			// Our indices are connected to the vertices so we need to keep them
			// in the correct order.
			// normal quad = 0,1,2,0,2,3 so the next one will be 4,5,6,4,6,7
			last = last + 4;
		}

		// The vertex buffer.
		ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
		bb.order(ByteOrder.nativeOrder());
		vertexBuffer = bb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		// initialize byte buffer for the draw list
		ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2);
		dlb.order(ByteOrder.nativeOrder());
		drawListBuffer = dlb.asShortBuffer();
		drawListBuffer.put(indices);
		drawListBuffer.position(0);
	}
	
	private Screen screen;
    private StartOption startOption;
    private int xTile;
    private int yTile;
    private final int xTileMax = 4;
    private final int yTileMax = 4;
    Boolean simMenuOpen;
	
    enum Screen {
        START_MENU,
        GAME_MENU,
        SIMULATION
    };

    enum StartOption {
        NEW_SIM,
        LOAD_SIM,
        CREDITS,
        GUIDE
    };
	
	public void keyEvent(int keyCode) {
	    boolean handled = false;

        //Handle the input
        switch(keyCode){
        case OuyaController.BUTTON_O:
            if (this.screen == Screen.SIMULATION) { // Back
            	this.screen = Screen.START_MENU;
            }
            else if (this.screen == Screen.GAME_MENU) { // Back
            	this.screen = Screen.SIMULATION;
            }
            handled = true;
            break;
        case OuyaController.BUTTON_U:
            if (this.screen == Screen.SIMULATION) { // Game Menu
            	this.screen = Screen.GAME_MENU;
            }
            else if (this.screen == Screen.GAME_MENU) { // Exit Menu
            	this.screen = Screen.SIMULATION;
            }
            handled = true;
            break;
        case OuyaController.BUTTON_Y:
            if (this.screen == Screen.SIMULATION) { // Step
                StepSimulation();
            }
            handled = true;
            break;
        case OuyaController.BUTTON_A:
            if (this.screen == Screen.START_MENU) { // Select
                if (startOption == StartOption.LOAD_SIM) {
                    LoadSimulation();
                }
                else if (startOption == StartOption.NEW_SIM) {
                    NewSimulation();
                }
            }
            else if (this.screen == Screen.SIMULATION) { // Select
                simMenuOpen = !simMenuOpen;
            }
            else if (this.screen == Screen.GAME_MENU) { // Select
            	this.screen = Screen.SIMULATION;
            }
            handled = true;
            break;
        case OuyaController.BUTTON_DPAD_UP:
            if (this.screen == Screen.START_MENU) {
                if (startOption == StartOption.NEW_SIM)
                    startOption = StartOption.GUIDE;
                else if (startOption == StartOption.LOAD_SIM)
                    startOption = StartOption.NEW_SIM;
                else if (startOption == StartOption.CREDITS)
                	startOption = StartOption.LOAD_SIM;
                else
                	startOption = StartOption.CREDITS;
            }
            else if (this.screen == Screen.SIMULATION) {
                if (yTile > 1) yTile--;
            }
            handled = true;
            break;
        case OuyaController.BUTTON_DPAD_DOWN:
            if (this.screen == Screen.START_MENU) {
                if (startOption == StartOption.NEW_SIM)
                    startOption = StartOption.LOAD_SIM;
                else if (startOption == StartOption.LOAD_SIM)
                    startOption = StartOption.CREDITS;
                else if (startOption == StartOption.CREDITS)
                	startOption = StartOption.GUIDE;
                else
                	startOption = StartOption.NEW_SIM;
            }
            else if (this.screen == Screen.SIMULATION) {
                if (yTile < yTileMax) yTile++;
            }
            handled = true;
            break;
        case OuyaController.BUTTON_DPAD_LEFT:
            if (this.screen == Screen.SIMULATION) {
                if (xTile > 1) xTile--;
            }
            handled = true;
            break;
        case OuyaController.BUTTON_DPAD_RIGHT:
            if (this.screen == Screen.SIMULATION) {
                if (xTile < xTileMax) xTile++;
            }
            handled = true;
            break;
        }
        return handled || super.onKeyDown(keyCode, event);
	}
	
    private void StepSimulation() {
        engine.step();
        while (!eventQueue.isEmpty()) {
        	Event event = eventQueue.getEvent();
        	if (event instanceof SimulationEvent) {
        		if (!simulationEngine.simulateOneEvent((SimulationEvent)event)) {
        			eventQueue.popEvent();
        		}
        	}
        }
    }

    private void LoadSimulation() {
        engine = startMenu.LoadSimulation();
    }

    private void SaveSimulation() {
        startMenu.SaveSimulation();
    }

    private void NewSimulation() {
        engine = new SimulationEngine();
    }
	
	class Sprite
	{
		float angle;
		float scale;
		RectF base;
		PointF translation;
		
		public Sprite()
		{
			// Initialise our intital size around the 0,0 point
			base = new RectF(-50f*ssu,50f*ssu, 50f*ssu, -50f*ssu);
			
			// Initial translation
			translation = new PointF(50f*ssu,50f*ssu);
			
			// We start with our inital size
			scale = 1f;
			
			// We start in our inital angle
			angle = 0f;
		}
		
		
		public void translate(float deltax, float deltay)
		{
			// Update our location.
			translation.x += deltax;
			translation.y += deltay;
		}
		
		public void scale(float deltas)
		{
			scale += deltas;
		}
		
		public void rotate(float deltaa)
		{
			angle += deltaa;
		}
		
		public float[] getTransformedVertices()
		{
			// Start with scaling
			float x1 = base.left * scale;
			float x2 = base.right * scale;
			float y1 = base.bottom * scale;
			float y2 = base.top * scale;
			
			// We now detach from our Rect because when rotating, 
			// we need the seperate points, so we do so in opengl order
			PointF one = new PointF(x1, y2);
			PointF two = new PointF(x1, y1);
			PointF three = new PointF(x2, y1);
			PointF four = new PointF(x2, y2);
			
			// We create the sin and cos function once, 
			// so we do not have calculate them each time.
			float s = (float) Math.sin(angle);
			float c = (float) Math.cos(angle);
			
			// Then we rotate each point
			one.x = x1 * c - y2 * s;
			one.y = x1 * s + y2 * c;
			two.x = x1 * c - y1 * s;
			two.y = x1 * s + y1 * c;
			three.x = x2 * c - y1 * s;
			three.y = x2 * s + y1 * c;
			four.x = x2 * c - y2 * s;
			four.y = x2 * s + y2 * c;
			
			// Finally we translate the sprite to its correct position.
			one.x += translation.x;
			one.y += translation.y;
			two.x += translation.x;
			two.y += translation.y;
			three.x += translation.x;
			three.y += translation.y;
			four.x += translation.x;
			four.y += translation.y;
			
			// We now return our float array of vertices.
			return new float[]
	        {
					one.x, one.y, 0.0f,
	       			two.x, two.y, 0.0f,
	       			three.x, three.y, 0.0f,
	       			four.x, four.y, 0.0f,
	        };
		}
	}
}

