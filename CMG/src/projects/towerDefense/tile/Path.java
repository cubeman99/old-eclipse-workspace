package projects.towerDefense.tile;

import java.awt.Color;
import projects.towerDefense.Resources;
import projects.towerDefense.entity.Creep;
import cmg.graphics.Draw;
import cmg.main.Keyboard;
import cmg.math.Direction;
import cmg.math.geometry.Point;
import cmg.math.geometry.Vector;

public class Path extends Tile {
	public static final int TYPE_NONE  = 0;
	public static final int TYPE_PATH  = 1;
	public static final int TYPE_START = 2;
	public static final int TYPE_END   = 3;
	private Path[] connections;
	private int type;
	

	
	// ================== CONSTRUCTORS ================== //
	
	public Path() {
		super();
		connections = new Path[Direction.NUM_DIRS];
		type = TYPE_NONE;
	}
	
	

	// =================== ACCESSORS =================== //
	
	public int getType() {
		return type;
	}
	
	public int numConnections() {
		int count = 0;
		for (int dir = 0; dir < Direction.NUM_DIRS; dir++) {
			if (connections[dir] != null)
				count++;
		}
		return count;
	}
	
	public Path getConnection(int dir) {
		return connections[dir];
	}
	
	public void removeInvalidConnections() {
		for (int dir = 0; dir < Direction.NUM_DIRS; dir++) {
			Path p = connections[dir];
			if (p != null && level.getTile(p.getLocation()) != p)
				connections[dir] = null;
		}
	}
	
	public void checkConnections() {
		for (int dir = 0; dir < Direction.NUM_DIRS; dir++) {
			Point p = location.getAdjacent(dir);
			connections[dir] = null;
			if (level.contains(p) && level.getTile(p) instanceof Path) {
				connections[dir] = (Path) level.getTile(p);
				connections[dir].connections[(dir + 2) % 4] = this;
			}
		}
	}
	
	

	// ==================== MUTATORS ==================== //
	
	public void setConnection(int dir, Path p) {
		connections[dir] = p;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	
	
	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	public void initialize() {
		
	}
	
	@Override
	public void update() {
		super.update();
		removeInvalidConnections();
		
		if (level.getCursor().equals(location) && Keyboard.home.pressed()) {
			if (type == TYPE_START)
				type = TYPE_NONE;
			else
				type = TYPE_START;
		}
		
		if (type == TYPE_START && Keyboard.enter.pressed()) {
			level.addEntity(new Creep());
		}
	}
	
	@Override
	public void draw() {
		super.draw();

		
		Vector center = new Vector(location).scale(SIZE).plus(SIZE / 2, SIZE / 2);
		
		int index = 0;
		int bit   = 1;
		for (int dir = 0; dir < Direction.NUM_DIRS; dir++) {
			if (connections[dir] != null)
				index = index | bit;
			bit *= 2;
		}
		Draw.drawImage(Resources.SHEET_TILES, index % 4, index / 4, location.x * SIZE, location.y * SIZE);
		
		if (type != TYPE_NONE) {
			Draw.setColor(Color.WHITE);
    		for (int dir = 0; dir < Direction.NUM_DIRS; dir++) {
    			if (connections[dir] != null) {
    				Vector v = center.plus(Direction.lengthVector(SIZE / 2, dir));
    				Draw.drawLine(center, v);
    			}
    		}
		}
		
		if (type == TYPE_START) {
			Draw.setColor(Color.GREEN);
			Draw.fillCircle(center, SIZE / 8);
		}
		else if (type == TYPE_END) {
			Draw.setColor(Color.RED);
			Draw.fillCircle(center, SIZE / 8);
		}
	}
}
