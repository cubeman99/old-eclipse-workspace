import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;


public class WaterSimulator {
	public static final int WIDTH  = 20;
	public static final int HEIGHT = 20;
	public static final int SCALE  = 16;
	public Tile[][] waterGrid;
	
	
	public WaterSimulator() {
		waterGrid = new Tile[WIDTH][HEIGHT];
		clearWaterGrid();
	}
	
	public void clearWaterGrid() {
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				waterGrid[x][y] = new Tile(x, y);
			}
		}
	}
	
	public Point getMouseCell() {
		Point p = new Point();
		p.x = (int) Math.floor(Game.mouseX / SCALE);
		p.y = (int) Math.floor(Game.mouseY / SCALE);
		return p;
	}
	
	public void setCell(Point p, double amount) {
		setCell(p.x, p.y, amount);
	}
	
	public void setCell(int x, int y, double amount) {
		if (inBounds(x, y))
			waterGrid[x][y].amount = Math.min(100, amount);
	}
	
	public boolean inBounds(int x, int y) {
		return (x >= 0 && y >= 0 && x < WIDTH && y < HEIGHT);
	}
	
	public boolean inBounds(Point p) {
		return inBounds(p.x, p.y);
	}

	public double getCell(int x, int y) {
		if (inBounds(x, y))
			return waterGrid[x][y].amount;
		return 100;
	}
	
	public boolean cellSolid(int x, int y) {
		if (inBounds(x, y))
			return waterGrid[x][y].solid;
		return true;
	}
	
	public double getCellDown(int x, int y) {
		return getCell(x, y + 1);
	}
	
	public double getCellLeft(int x, int y) {
		return getCell(x - 1, y);
	}
	
	public double getCellRight(int x, int y) {
		return getCell(x + 1, y);
	}
	
	public void updateWaterGrid() {

		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				waterGrid[x][y].updated = false;
			}
		}
		
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				if (!waterGrid[x][y].updated && !cellSolid(x, y)) {
					waterGrid[x][y].updated = true;
					double amount = getCell(x, y);
					double temp = amount;
					if (amount < 1) {
						setCell(x, y, 0);
						continue;
					}
					if (amount > 0 && getCell(x, y + 1) < 100 && !cellSolid(x, y + 1)) {
						// MOVE WATER DOWN ONE
						temp = amount;
						amount -= 100 - getCell(x, y + 1);
						setCell(x, y + 1, getCell(x, y + 1) + temp);
						waterGrid[x][y + 1].updated = true;
					}
					else if (amount > 0) {
						boolean LL = (getCell(x - 1, y) < amount && !cellSolid(x - 1, y));
						boolean RR = (getCell(x + 1, y) < amount && !cellSolid(x + 1, y));
						
						if (LL && RR) {
							// AVERAGE BETWEEN LEFT AND RIGHT
							double avg = (amount + getCell(x - 1, y) + getCell(x + 1, y)) / 3;
		                    amount = Math.ceil(avg);
		                    setCell(x - 1, y, Math.floor(avg));
		                    setCell(x + 1, y, Math.floor(avg));
							waterGrid[x - 1][y].updated = true;
							waterGrid[x + 1][y].updated = true;
						}
						else if (LL) {
							// AVERAGE WITH LEFT
							double avg = (amount + getCell(x - 1, y)) / 2;
		                    amount = Math.ceil(avg);
		                    setCell(x - 1, y, Math.floor(avg));
//							waterGrid[x - 1][y].updated = true;
						}
						else if (RR) {
							// AVERAGE WITH RIGHT
							double avg = (amount + getCell(x + 1, y)) / 2;
		                    amount = Math.ceil(avg);
		                    setCell(x + 1, y, Math.floor(avg));
//							waterGrid[x + 1][y].updated = true;
						}
					}
					setCell(x, y, amount);
				}
			}
		}
	}
	
	public void update() {
		Point ms = getMouseCell();
		if (Game.mbLeft && inBounds(ms)) {
			if (Game.keyDown[KeyEvent.VK_CONTROL]) {
				waterGrid[ms.x][ms.y].solid  = true;
				waterGrid[ms.x][ms.y].amount = 100;
			}
			else
				setCell(ms, 100);
		}
		if (Game.mbRight && inBounds(ms)) {
			if (Game.keyDown[KeyEvent.VK_CONTROL]) {
				waterGrid[ms.x][ms.y].solid  = false;
				waterGrid[ms.x][ms.y].amount = 0;
			}
			else
				setCell(ms, 0);
		}
		
		if (Game.keyPressed[KeyEvent.VK_R])
			clearWaterGrid();
		
		// UPDATE WATER GRID:
		updateWaterGrid();
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
		g.setColor(Color.blue);
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				waterGrid[x][y].draw(g);
			}
		}
	}
}
