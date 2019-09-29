package projects.towerDefense.tile;

import projects.towerDefense.Level;
import cmg.math.geometry.Point;
import cmg.math.geometry.Vector;

public abstract class Tile {
	public static final double SIZE = 48;
	protected Level level;
	protected Point location;
	
	

	// ================== CONSTRUCTORS ================== //
	
	public Tile() {
		location = new Point();
		level    = null;
	}

	

	// =================== ACCESSORS =================== //
	
	public boolean isHovered() {
		return level.getCursor().equals(location);
	}
	
	public boolean isSelected() {
		return false; // TODO
	}
	
	public Point getLocation() {
		return location;
	}
	
	public Vector getPosition() {
		return new Vector(location);
	}
	
	public Vector getCenter() {
		return new Vector(location).plus(0.5, 0.5);
	}
	
	public Level getLevel() {
		return level;
	}
	
	@Override
	public Tile clone() {
		return null;
	}
	
	
	
	// ==================== MUTATORS ==================== //

	public void initialize(Level level, Point loc) {
		this.level    = level;
		this.location = new Point(loc);
		initialize();
	}
	
	public void initialize() {
		
	}
	
	public void update() {
		
	}

	public void preDraw()  {}
	public void draw()     {}
	public void postDraw() {}
}
