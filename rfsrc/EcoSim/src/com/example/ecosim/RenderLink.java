package com.example.ecosim;

public class RenderLink {
	private String _tag;
	private String _filename;
	private int _drawLayer;
	private float _positionX;
	private float _positionY;
	private RenderLink _prev;
	private RenderLink _next;

	public RenderLink(String tag, String filename, int drawLayer, float positionX, float positionY) {
		_tag = tag;
		_filename = filename;
		_drawLayer = drawLayer;
		_positionX = positionX;
		_positionY = positionY;
		_next = null;
		_prev = null;
	}
	
	public RenderLink(RenderLink copy) {
		_tag = copy.getTag();
		_filename = copy.getFilename();
		_drawLayer = copy.getDrawLayer();
		_positionX = copy.getPositionX();
		_positionY = copy.getPositionY();
		_next = copy.getNext();	
		_prev = copy.getPrev();
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
	
	public float getPositionX() {
		return _positionX;
	}
	
	public float getPositionY() {
		return _positionY;
	}
	
	public RenderLink getPrev() {
		return _prev;
	}

	public RenderLink getNext() {
		return _next;
	}

	public void setAll(RenderLink copy) {
		_tag = copy.getTag();
		_filename = copy.getFilename();
		_drawLayer = copy.getDrawLayer();
		_positionX = copy.getPositionX();
		_positionY = copy.getPositionY();
		_next = copy.getNext();
		_prev = copy.getPrev();
	}

	public void setPrev(RenderLink prev) {
		_prev = prev;
	}
	
	public void setNext(RenderLink next) {
		_next = next;
	}
}
