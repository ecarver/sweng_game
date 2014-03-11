//package com.example.renderertesting

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.LinkedList

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.RectF;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;

public class RenderLink {
	private String _tag;
	private String _filename;
	private int _drawLayer;
	private float _positionX;
	private float _positionY;
	private RenderLink _next;

	public RenderLink(tag, filename, drawLayer, positionX, positionY) {
		_tag = tag;
		_filename = filename;
		_drawLayer = drawLayer;
		_positionX = positionX;
		_positionY = positionY;
		_next = null;
	}

	public String getTag() {
		return _tag;
	}

	public String getFilename() {
		return _filename;
	}

	public int getDrawLayer() {
		return _drawLayer;
	}

	public RenderLink getNext() {
		return _next;
	}

	public void setNext(RenderLink next) {
		_next = next;
	}
}

public class RenderList {
	private RenderLink _head;

	public RenderList() {
		_head = null;
	}

	public RenderList(RenderLink head) {
		_head = head;
	}

	public void addRenderLink(RenderLink add) {
		if(this.isEmpty){
			_head = add;
		} else {
			RenderLink loop = head;
			bool check = false;
			while(loop.getNext() != null && !check) {
				if(loop.getNext().getDrawLayer() >= add.getDrawLayer()) {
					check = true;
				}
				else {
					loop = loop.getNext();
				}
			}
			
			RenderLink temp = loop.getNext();
			loop.setNext(add);
			loop.getNext = temp;
		}
	}

	public void deleteRenderLink(String tag) {
		//To implement;
	}

	public boolean isEmpty() {
		return (head == null);
	}
}

public class SimulationRenderer implements Renderer {
	
}