import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;


public class Grid {
	public static Tile[][] tiles;
	public static final int SCALE  = 24;
	public static final int WIDTH  = 10;
	public static final int HEIGHT = 23;
	
	public static final double CLEAR_DELAY_TIME = 0.2;
	public static Timer clearDelayTimer = new Timer();
	public static ArrayList<Integer> clearedLines = new ArrayList<Integer>();
	
	public static void initialize() {
		tiles = new Tile[WIDTH][HEIGHT];
		clearTiles();
	}
	
	public static void clearTiles() {
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				tiles[x][y] = new Tile(x, y);
			}
		}
	}
	
	public static int scanGrid() {
		// Checks for lines that can be cleared and
		// returns the amount of lines cleared
		boolean nothing = false;
		
		while (!nothing) {
			nothing = true;
			loop: for (int y = 0; y < HEIGHT; y++) {
				for (int x = 0; x < WIDTH; x++) {
					if (!isSolid(x, y))
						break;
					if (x == WIDTH - 1 && !clearedLines.contains(y)) {
						nothing = false;
						clearedLines.add(y);
						break loop;
					}
				}
			}
		}
		if (clearedLines.size() > 0)
			clearDelayTimer.start();
		return clearedLines.size();
	}
	
	public static void shiftGridDown(int shiftY) {
		for (int y = shiftY; y > 0; y--) {
			for (int x = 0; x < WIDTH; x++) {
				tiles[x][y] = tiles[x][y - 1];
				tiles[x][y].x = x;
				tiles[x][y].y = y;
				tiles[x][y - 1] = new Tile(x, y - 1);
			}
		}
	}
	
	public static void connectSpace(int x, int y) {
		connectArea(x - 1, y - 1, x + 2, y + 2);
	}
	
	public static void connectArea(int x1, int y1, int x2, int y2) {
		for (int cx = Math.max(0, x1); cx < Math.min(WIDTH, x2); cx++) {
			for (int cy = Math.max(0, y1); cy < Math.min(HEIGHT, y2); cy++) {
				tiles[cx][cy].connect();
			}
		}
	}
	
	public static void addTile(int x, int y, Color color) {
		if (inBounds(x, y)) {
			tiles[x][y].solid = true;
			tiles[x][y].color = color;
			connectSpace(x, y);
		}
	}
	
	public static boolean inBounds(int x, int y) {
		return (x >= 0 && y >= 0 && x < WIDTH && y < HEIGHT);
	}
	
	public static Tile getTile(int x, int y) {
		return tiles[x][y];
	}
	
	public static boolean isSolid(int x, int y) {
		return getTile(x, y).solid;
	}

	public static Color getColor(int x, int y) {
		return getTile(x, y).color;
	}
	
	public static boolean[][] getBooleanMap(Color colorFilter) {
		boolean[][] map = new boolean[WIDTH][HEIGHT];
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				map[x][y] = tiles[x][y].solid && (tiles[x][y].color == colorFilter);
			}
		}
		return map;
	}
	
	public static void update() {
		if (clearDelayTimer.running && clearDelayTimer.getSeconds() >= CLEAR_DELAY_TIME) {
			for (int i = 0; i < clearedLines.size(); i++) {
				shiftGridDown(clearedLines.get(i));
				connectArea(0, clearedLines.get(i), WIDTH, clearedLines.get(i) + 2);
			}
			GameData.addClearScore(clearedLines.size());
			clearedLines.clear();
			clearDelayTimer.stop();
		}
	}
	
	public static void draw(Graphics g) {
		// Draw a black background
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
		
		// Draw a grid
		if (GameData.showGrid){
			g.setColor(Color.darkGray);
			for (int x = 0; x < WIDTH; x++)
				g.drawLine(x * SCALE, 0, x * SCALE, HEIGHT * SCALE);
			for (int y = 0; y < HEIGHT; y++)
				g.drawLine(0, y * SCALE, WIDTH * SCALE, y * SCALE);
		}
		
		// Draw all Tiles
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				if (clearedLines.contains(y))
					tiles[x][y].drawClearing(g);
				else
					tiles[x][y].draw(g);
			}
		}
		
		g.setColor(Color.white);
		g.drawString("Score: " + GameData.score, 32, (HEIGHT * SCALE) + 32);
		
		GameData.tetrominoQueue.get(0).drawTetromino(g, (WIDTH + 2), SCALE);
	}
}
