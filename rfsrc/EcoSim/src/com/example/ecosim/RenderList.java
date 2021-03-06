package com.example.ecosim;

public class RenderList {
	private RenderLink _head;
	private int size;
	private boolean change;
	public final int START_MENU = 1;
	public final int HOW_TO_PLAY_SCREEN = 2;
	public final int CREDITS_SCREEN = 3;
	public final int ECO_MAP = 4;
	public final int STAT_SCREEN = 5;
	private int _screen;

	public RenderList() {
		_screen = START_MENU;
		size = 0;
		_head = null;
		change = false;
	}

	public RenderList(RenderLink head) {
		_screen = START_MENU;
		size = 1;
		_head = head;
		change = true;
	}

	public void addRenderLink(RenderLink add) {
		if(this.isEmpty()){
			_head = add;
			size++;
			change = true;
			return;
		} else {			
			RenderLink temp = _head;
			for( ; temp != null; temp = temp.getNext()) {
				if(temp.getDrawLayer() >= add.getDrawLayer()) {
					if(temp == _head) {
						RenderLink next = new RenderLink(_head);
						_head.setAll(add);
						_head.setNext(next);
						next.setPrev(_head);
						size++;
						change = true;
						return;
					} else {
						RenderLink next = new RenderLink(temp);
						temp.setAll(add);
						temp.setPrev(next.getPrev());
						temp.setNext(next);
						next.setPrev(temp);
						size++;
						change = true;
						return;
					}
				}
				
				if(temp.getNext() == null) {
					RenderLink next = new RenderLink(add);
					next.setPrev(temp);
					temp.setNext(next);
					size++;
					change = true;
					return;
				}
				
			}
		}
	}

	public void deleteRenderLink(String tag) {
		if(this.isEmpty()) {
			size--;
			change = true;
			return;
		} else {
			RenderLink temp = _head;
			for( ; temp != null; temp = temp.getNext()) {
				String temp_tag = temp.getTag();
				if(temp_tag.equals(tag)) {
					if(temp == _head) {
						_head = _head.getNext();
						size--;
						change = true;
						return;
					} else if(temp.getNext() == null) {
						temp.getPrev().setNext(null);
						size--;
						change = true;
						return;
					} else {
						temp.getPrev().setNext(temp.getNext());
						temp.getNext().setPrev(temp.getPrev());
						size--;
						change = true;
						return;
					}
				}
			}
		}
	}

	public boolean isEmpty() {
		return (_head == null);
	}
	
	public void clearList() {
		size = 0;
		_head = null;
		change = true;
	}

	public RenderLink getHead() {
		return _head;
	}

	public void setHead(RenderLink head) {
		_head = head;
		change = true;
	}
	
	public int getSize() {
		return size;
	}
	
	public boolean getChange() {
		return change;
	}
	public void setChangeToFalse() {
		change = false;
	}
	
	public void setScreen(int screen) {
		_screen = screen;
	}

	
	public int getScreen() {
		return _screen;
	}
}

