package game;

import java.awt.Color;
import java.util.ArrayList;
import common.GMath;
import common.Point;
import common.Vector;
import common.graphics.Draw;

public class Road {
	public Point position;
	public World world;
	private Road[] neighbors;
	
	// ================== CONSTRUCTORS ================== //
	
	public Road(World world, int x, int y) {
		this.world    = world;
		this.position = new Point(x, y);
		this.neighbors = new Road[4];
		
		for (int i = 0; i < 4; i++)
			neighbors[i] = null;
	}
	
	
	
	// =================== ACCESSORS =================== //

	public Point getPosition() {
		return position;
	}
	
	public Road getNeighbor(int direction) {
		return neighbors[direction];
	}
	
	public boolean hasNeighbor(int direction) {
		return (neighbors[direction] != null);
	}
	
	public int getNeighborCount() {
		int count = 0;
		for (int i = 0; i < 4; i++) {
			if (neighbors[i] != null)
				count++;
		}
		return count;
	}
	
	public ArrayList<Road> getPossibleNeighbors() {
		ArrayList<Road> list = new ArrayList<Road>();
		for (int i = 0; i < 4; i++) {
			if (neighbors[i] != null)
				list.add(neighbors[i]);
		}
		return list;
	}
	
	public boolean isIntersection() {
		return (getNeighborCount() > 2);
	}
	
	public Vector getCenter() {
		return new Vector(getPosition()).plus(0.5, 0.5);
	}
	
	public double getDistanceTo(Road r) {
		if (getNeighborCount() == 1)
			return (GMath.PI * 0.5);
		
		int dirTo   = 0;
		int dirFrom = 0;
		for (int i = 0; i < 4; i++) {
			if (neighbors[i] == r)
				dirTo = i;
			if (neighbors[i] != null && neighbors[i] != r)
				dirFrom = (i + 2) % 4;
		}
		if (dirTo == dirFrom)
			return 1;
		if (dirTo - dirFrom == -1 || dirTo - dirFrom == 3)
			return (GMath.HALF_PI * 1);
		return (GMath.HALF_PI * 0.25);
	}
	
	
	// ==================== MUTATORS ==================== //
	
	public void connect() {
		for (int dir = 0; dir < 4; dir++)
			neighbors[dir] = world.getNeighbor(position, dir);
	}
	
	public void update() {
		
	}
	
	public void draw() {
		Color colEdge = Color.WHITE;
		Color colLine = Color.GRAY;
		Color colBack = new Color(30, 30, 30);
		int n   = getNeighborCount();
		int dx  = position.x;
		int dy  = position.y;
		double eps = 1.0 / Draw.getZoom();
		Draw.setColor(colBack);
		Draw.fillRect(dx, dy, 1, 1);
		
		if (n == 0) {
			
		}
		else if (n == 1) {
			Draw.setColor(colEdge);
			if (hasNeighbor(0))
				Draw.drawArc(dx + 1, dy + 0.5, 0.5, GMath.HALF_PI, GMath.PI);
			else if (hasNeighbor(1))
				Draw.drawArc(dx + 0.5, dy, 0.5, GMath.PI, GMath.PI);
			else if (hasNeighbor(2))
				Draw.drawArc(dx, dy + 0.5, 0.5, GMath.THREE_HALVES_PI, GMath.PI);
			else if (hasNeighbor(3))
				Draw.drawArc(dx + 0.5, dy + 1, 0.5, 0, GMath.PI);
		}
		else if (n == 2) {
			if (hasNeighbor(0) && hasNeighbor(2)) {
				Draw.setColor(colLine);
				Draw.drawLine(dx, dy + 0.5, dx + 1, dy + 0.5);
				Draw.setColor(colEdge);
				Draw.drawLine(dx, dy, dx + 1, dy);
				Draw.drawLine(dx, dy + 1, dx + 1, dy + 1);
			}
			else if (hasNeighbor(1) && hasNeighbor(3)) {
				Draw.setColor(colLine);
				Draw.drawLine(dx + 0.5, dy, dx + 0.5, dy + 1);
				Draw.setColor(colEdge);
				Draw.drawLine(dx, dy, dx, dy + 1);
				Draw.drawLine(dx + 1, dy, dx + 1, dy + 1);
			}
			else {
				if (!hasNeighbor(0) && !hasNeighbor(1)) {
					Draw.setColor(colEdge);
					Draw.drawArc(dx, dy + 1, 1, 0, GMath.HALF_PI);
					Draw.setColor(colLine);
					Draw.drawArc(dx, dy + 1, 0.5, 0, GMath.HALF_PI);
				}
				else if (!hasNeighbor(1) && !hasNeighbor(2)) {
					Draw.setColor(colEdge);
					Draw.drawArc(dx + 1, dy + 1, 1, GMath.HALF_PI, GMath.HALF_PI);
					Draw.setColor(colLine);
					Draw.drawArc(dx + 1, dy + 1, 0.5, GMath.HALF_PI, GMath.HALF_PI);
				}
				else if (!hasNeighbor(2) && !hasNeighbor(3)) {
					Draw.setColor(colEdge);
					Draw.drawArc(dx + 1, dy, 1, GMath.PI, GMath.HALF_PI);
					Draw.setColor(colLine);
					Draw.drawArc(dx + 1, dy, 0.5, GMath.PI, GMath.HALF_PI);
				}
				else if (!hasNeighbor(3) && !hasNeighbor(0)) {
					Draw.setColor(colEdge);
					Draw.drawArc(dx, dy, 1, GMath.THREE_HALVES_PI, GMath.HALF_PI);
					Draw.setColor(colLine);
					Draw.drawArc(dx, dy, 0.5, GMath.THREE_HALVES_PI, GMath.HALF_PI);
				}
			}
		}
		else {
			Draw.setColor(colEdge);
			if (!hasNeighbor(0))
				Draw.drawLine(dx + 1, dy, dx + 1, dy + 1);
			else if (!hasNeighbor(1))
				Draw.drawLine(dx, dy, dx + 1, dy);
			else if (!hasNeighbor(2))
				Draw.drawLine(dx, dy, dx, dy + 1);
			else if (!hasNeighbor(3))
				Draw.drawLine(dx, dy + 1, dx + 1, dy + 1);

			Draw.setColor(colLine);
			if (hasNeighbor(0))
				Draw.drawLine(dx + 1 - eps, dy, dx + 1 - eps, dy + 0.5);
			if (hasNeighbor(1))
				Draw.drawLine(dx, dy, dx + 0.5, dy);
			if (hasNeighbor(2))
				Draw.drawLine(dx, dy + 0.5, dx, dy + 1);
			if (hasNeighbor(3))
				Draw.drawLine(dx + 0.5, dy + 1 - eps, dx + 1, dy + 1 - eps);
		}
	}
}
