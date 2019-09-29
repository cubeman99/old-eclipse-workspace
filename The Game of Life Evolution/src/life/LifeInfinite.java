package life;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

/**
 * An infinite grid of Conway's game of life.
 * 
 * @author David Jordan
 */
public class LifeInfinite {
	private ArrayList<Point> aliveCells;
	private ArrayList<Point> newAliveCells;
	private Dimension gridScale;
	
	
	// ================== CONSTRUCTORS ================== //
	
	public LifeInfinite() {
		aliveCells    = new ArrayList<Point>();
		newAliveCells = new ArrayList<Point>();
		gridScale     = new Dimension(8, 8);
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	/** Return whether the given cell is alive. **/
	public boolean isCellAlive(int x, int y) {
		Point p = new Point(x, y);
		for (int i = 0; i < aliveCells.size(); i++) {
			if (aliveCells.get(i).equals(p))
				return true;
		}
		return false;
	}
	
	/** Return the grid cell position that the given point is hovering over. **/
	public Point getGridPoint(Point viewPoint) {
		return new Point(
			viewPoint.x / gridScale.width,
			viewPoint.y / gridScale.height
		);
	}
	
	/** Return the population; the total number of alive cells. **/
	public int getPopulation() {
		return aliveCells.size();
	}
	
	/** Get the integer value for a cell (1 = alive, 0 = dead). **/
	private int getCellValue(int x, int y) {
		return (isCellAlive(x, y) ? 1 : 0);
	}
	
	/** Return the number of neighboring alive cells around the given cell. **/
	private int getCellNeighborCount(int x, int y) {
		return (
			getCellValue(x + 1, y + 0) +
			getCellValue(x + 1, y - 1) +
			getCellValue(x + 0, y - 1) +
			getCellValue(x - 1, y - 1) +
			getCellValue(x - 1, y + 0) +
			getCellValue(x - 1, y + 1) +
			getCellValue(x + 0, y + 1) +
			getCellValue(x + 1, y + 1)
		);
	}
	
	/** Check if each neighbor of the given cell position should be born. **/
	private void tickNeighbors(Point p) {
		checkBirth(p.x + 0, p.y - 1);
		checkBirth(p.x + 1, p.y - 1);
		checkBirth(p.x + 1, p.y + 0);
		checkBirth(p.x + 1, p.y + 1);
		checkBirth(p.x + 0, p.y + 1);
		checkBirth(p.x - 1, p.y + 1);
		checkBirth(p.x - 1, p.y + 0);
		checkBirth(p.x - 1, p.y - 1);
	}

	/** Check if the given cell should be born. **/
	private void checkBirth(int x, int y) {
		int n = getCellNeighborCount(x, y);
		if (n == 3)
			setNewCell(x, y, true);
	}
	
	
	
	// ==================== MUTATORS ==================== //

	/** Clear the grid of all cells. **/
	public void clear() {
		aliveCells.clear();
	}
	
	/** Tick all cells. **/
	public void tick() {
		newAliveCells = new ArrayList<Point>();
		
		for (int i = 0; i < aliveCells.size(); i++) {
			Point cell    = aliveCells.get(i);
			int neighbors = getCellNeighborCount(cell.x, cell.y);
			
			if (neighbors == 2 || neighbors == 3)
				setNewCell(cell.x, cell.y, true);
			
			tickNeighbors(cell);
		}
		
		aliveCells = newAliveCells;
	}
	
	/** Set the state of the cell at the given location. **/
	public void setCell(int x, int y, boolean state) {
		for (int i = 0; i < aliveCells.size(); i++) {
			Point c = aliveCells.get(i);
			if (c.x == x && c.y == y) {
				if (!state)
					aliveCells.remove(i);
				return;
			}
		}
		
		if (state)
			aliveCells.add(new Point(x, y));
	}
	
	/** Set the state of the cell at the given location. **/
	private void setNewCell(int x, int y, boolean state) {
		for (int i = 0; i < newAliveCells.size(); i++) {
			Point c = newAliveCells.get(i);
			if (c.x == x && c.y == y) {
				if (!state)
					newAliveCells.remove(i);
				return;
			}
		}
		if (state)
			newAliveCells.add(new Point(x, y));
	}

	/** Draw all alive cells in the infinite grid. **/
	public void draw(Graphics g) {
		
		
		g.setColor(Color.BLACK);
		for (int i = 0; i < aliveCells.size(); i++) {
			Point c = aliveCells.get(i);
			g.fillRect(c.x * gridScale.width, c.y * gridScale.height, gridScale.width, gridScale.height);
		}
	}
}
