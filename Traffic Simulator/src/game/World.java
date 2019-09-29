package game;

import java.util.ArrayList;
import common.GMath;
import common.Point;

public class World {
	public Point size;
	public Road[][] roads;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	/** Create a new world with the given dimensions. **/
	public World(int width, int height) {
		size     = new Point(width, height);
		roads    = new Road[size.x][size.y];
		
		clearTiles();
	}

	
	
	// =================== ACCESSORS =================== //

	public Road get(int x, int y) {
		return roads[x][y];
	}
	
	public Road get(Point p) {
		return get(p.x, p.y);
	}
	
	public boolean isValid(int x, int y) {
		return (x >= 0 && y >= 0 && x < size.x && y < size.y);
	}
	
	public boolean isValid(Point p) {
		return isValid(p.x, p.y);
	}
	
	public Road getNeighbor(int x, int y, int direction) {
		Point p = new Point(x, y).plus(GMath.DIRECTION_POINTS[direction]);
		if (isValid(p))
			return get(p);
		return null;
	}
	
	public Road getNeighbor(Point p, int direction) {
		return getNeighbor(p.x, p.y, direction);
	}
	
	public ArrayList<Road> getAdjacentRoads(Point p) {
		ArrayList<Road> list = new ArrayList<Road>();
		
		for (int x = p.x - 1; x <= p.x + 1; x++) {
			for (int y = p.y - 1; y <= p.y + 1; y++) {
				if (x != p.x || y != p.y && isValid(x, y)) {
					if (get(x, y) != null)
						list.add(get(x, y));
				}
			}
		}
		return list;
	}
	
	public ArrayList<Road> getAreaRoads(Point p) {
		ArrayList<Road> list = new ArrayList<Road>();
		
		for (int x = p.x - 1; x <= p.x + 1; x++) {
			for (int y = p.y - 1; y <= p.y + 1; y++) {
				if (isValid(x, y)) {
					if (get(x, y) != null)
						list.add(get(x, y));
				}
			}
		}
		return list;
	}
	
	
	// ==================== MUTATORS ==================== //
	
	/** Clear the world of all tiles. **/
	public void clearTiles() {
		for (int x = 0; x < size.x; x++) {
			for (int y = 0; y < size.y; y++) {
				roads[x][y] = null;
			}
		}
	}
	
	/** Place a road. **/
	public void place(int x, int y) {
		roads[x][y] = new Road(this, x, y);
	}
	
	/** Remove a tile. **/
	public void remove(int x, int y) {
		roads[x][y] = null;
	}
	
	public void connectArea(int x, int y) {
		ArrayList<Road> list = getAreaRoads(new Point(x, y));
		for (Road r : list)
			r.connect();
	}
	
	/** Update the world. **/
	public void update() {
		for (int x = 0; x < size.x; x++) {
			for (int y = 0; y < size.y; y++) {
				if (roads[x][y] != null)
					roads[x][y].update();
			}
		}
	}
	
	/** Draw the world. **/
	public void draw() {
		for (int x = 0; x < size.x; x++) {
			for (int y = 0; y < size.y; y++) {
				if (roads[x][y] != null)
					roads[x][y].draw();
			}
		}
	}
}
