class RenderLink {
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

class RenderList {
	private RenderLink _head;

	public RenderList() {
		_head = null;
	}

	public RenderList(RenderLink head) {
		_head = head;
	}

	public void addRenderLink(RenderLink add) {
		if(this.isEmpty()){
			_head = add;
		} else {			
			RenderLink temp = _head;
			for( ; temp != null; temp = temp.getNext()) {
				if(temp.getDrawLayer() >= add.getDrawLayer()) {
					if(temp == _head) {
						RenderLink next = new RenderLink(_head);
						_head.setAll(add);
						_head.setNext(next);
						next.setPrev(_head);
						return;
					} else {
						RenderLink next = new RenderLink(temp);
						temp.setAll(add);
						temp.setPrev(next.getPrev());
						temp.setNext(next);
						next.setPrev(temp);
						return;
					}
				}
				
				if(temp.getNext() == null) {
					RenderLink next = new RenderLink(add);
					next.setPrev(temp);
					temp.setNext(next);
					return;
				}
				
			}
		}
	}

	public void deleteRenderLink(String tag) {
		if(this.isEmpty()) {
			return;
		} else {
			RenderLink temp = _head;
			for( ; temp != null; temp = temp.getNext()) {
				String temp_tag = temp.getTag();
				if(temp_tag.equals(tag)) {
					if(temp == _head) {
						_head = _head.getNext();
						return;
					} else if(temp.getNext() == null) {
						temp.getPrev().setNext(temp.getNext());
					} else {
						temp.getPrev().setNext(temp.getNext());
						temp.getNext().setPrev(temp.getPrev());
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
		_head = null;
	}
}

public class SimulationRenderer {
	
}