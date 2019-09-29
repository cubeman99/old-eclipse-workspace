package tp.planner.hud;

import tp.common.Point;
import tp.common.Rectangle;
import tp.main.GameRunner;
import tp.main.Mouse;
import tp.planner.Control;


public abstract class HudPanel {
	public Control control;
	public GameRunner runner;
	public HUD hud;
	protected Point position;
	protected Point size;
	
	public HudPanel(HUD hud) {
		this.hud      = hud;
		this.control  = hud.control;
		this.runner   = control.runner;
		this.position = new Point();
		this.size     = new Point();
	}
	
	public boolean isMouseInArea() {
		return Mouse.inArea(position, size);
	}
	
	public Point getPosition() {
		return position;
	}
	
	public Point getSize() {
		return size;
	}
	
	public int getWidth() {
		return size.x;
	}
	
	public int getHeight() {
		return size.y;
	}
	
	public int getLeftBound() {
		return position.x;
	}
	
	public int getRightBound() {
		return (position.x + size.x);
	}
	
	public int getUpperBound() {
		return position.y;
	}
	
	public int getLowerBound() {
		return (position.y + size.y);
	}
	
	public Rectangle getRect() {
		return new Rectangle(position, size);
	}
	
	public abstract void update();
	public abstract void draw();
	
	public boolean isBusy() {return false;}
}
