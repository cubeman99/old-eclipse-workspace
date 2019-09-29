package life;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Random;

public class Life {
	private boolean[][] cells;
	private Dimension size;
	private Random random;
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Life(int width, int height) {
		cells  = new boolean[width][height];
		size   = new Dimension(width, height);
		random = new Random();
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	/** Return the width of the grid. **/
	public int getWidth() {
		return size.width;
	}
	
	/** Return the height of the grid. **/
	public int getHeight() {
		return size.height;
	}
	
	/** Return whether a point is inside the grid.**/
	public boolean inBounds(int x, int y) {
		return (x >= 0 && y >= 0 && x < getWidth() && y < getHeight());
	}
	
	/** Return whether the given cell is alive. **/
	public boolean isCellAlive(int x, int y) {
		if (!inBounds(x, y))
			return false;
		return cells[x][y];
	}
	
	/** Get the integer value for a cell (1 = alive, 0 = dead). **/
	public int getCellValue(int x, int y) {
		if (!inBounds(x, y))
			return 0;
		return (cells[x][y] ? 1 : 0);
	}
	
	/** Return the number of neighboring alive cells around the given cell. **/
	public int getCellNeighborCount(int x, int y) {
		int count = 0;
		count += getCellValue(x + 1, y + 0);
		count += getCellValue(x + 1, y - 1);
		count += getCellValue(x + 0, y - 1);
		count += getCellValue(x - 1, y - 1);
		count += getCellValue(x - 1, y + 0);
		count += getCellValue(x - 1, y + 1);
		count += getCellValue(x + 0, y + 1);
		count += getCellValue(x + 1, y + 1);
		return count;
	}
	
	
	// ==================== MUTATORS ==================== //
	
	/** Clear the grid of all cells. **/
	public void clear() {
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++)
				cells[x][y] = false;
		}
	}
	
	/** Tick the life grid, changing cells according to the algorithm. **/
	public void tick() {
		boolean[][] newCells = new boolean[getWidth()][getHeight()];
		
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				int n = getCellNeighborCount(x, y);
				
				if (n < 2 || n > 3)
					newCells[x][y] = false;
				else if (n == 3)
					newCells[x][y] = true;
				else
					newCells[x][y] = cells[x][y];
			}
		}
		
		cells = newCells;
	}
	
	/** Set the state of the given cell. **/
	public void setCell(int x, int y, boolean state) {
		if (inBounds(x, y))
			cells[x][y] = state;
	}
	
	/** Randomize the entire grid. **/
	public void randomize() {
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++)
				cells[x][y] = random.nextBoolean();
		}
	}

	/** Draw the grid. **/
	public void draw(Graphics g) {
		int scale = 8;
		
		g.setColor(Color.BLACK);
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				if (isCellAlive(x, y))
					g.fillRect(x * scale, y * scale, scale, scale);
			}
		}
	}
}
