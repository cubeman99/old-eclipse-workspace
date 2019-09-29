package cmg.ui;

import cmg.graphics.Draw;
import cmg.math.geometry.Point;
import cmg.math.geometry.Rect;

public class Component {
	protected Container container;
	protected Rect rect;
	
	
	
	// ================== CONSTRUCTORS ================== //

	public Component() {
		rect = new Rect(0, 0, 20, 20);
	}
	
	

	// =============== DO-NOTHING METHODS =============== //
	
	public void update() {}
	
	public void draw() {}
	
	
	
	// =================== ACCESSORS =================== //
	
	public boolean mouseOver() {
		return rect.contains(Mouse.current.position());
	}
	
	public Point getPosition() {
		return new Point(rect.corner);
	}
	
	public Point getSize() {
		return new Point(rect.size);
	}
	
	public int getWidth() {
		return rect.getWidth();
	}
	
	public int getHeight() {
		return rect.getHeight();
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void setRect(int x, int y, int width, int height) {
		rect.set(x, y, width, height);
	}
	
	public void setPosition(int x, int y) {
		rect.corner.set(x, y);
	}
	
	public void setPosition(Point pos) {
		rect.corner.set(pos);
	}
	
	public void setSize(int width, int height) {
		rect.size.set(width, height);
	}

	public void setSize(Point size) {
		rect.size.set(size);
	}
	
	public void setWidth(int width) {
		rect.size.x = width;
	}
	
	public void setHeight(int height) {
		rect.size.y = height;
	}
	
	public void step() {
		update();
	}
	
	public void render() {
		draw();
	}
}
