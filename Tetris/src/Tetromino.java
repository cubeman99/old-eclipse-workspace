import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;


public class Tetromino {
	public int x = 0;
	public double y = 0;
	public int type = 0;
	public Color color = Color.white;
	public String name = "";
	public int dir = 0;
	
	public boolean[][][] grid;
	public int[][][] gridCIndex;
	public int[] width;
	public int[] height;
	
	public Timer finishTimer = new Timer();
	public static final double FINISH_DELAY_TIME = 0.5;
	
	public Tetromino() {
		grid   		= new boolean[4][4][4];
		gridCIndex 	= new int[4][4][4];
		width  		= new int[4];
		height 		= new int[4];
		
		clearGrids();
		loadTetromino();
		
		this.x = (int) Math.floor(((double) Grid.WIDTH - (double) width[0]) / 2.0d);
	}
	
	public Tetromino(String name, int width, int height, Color color) {
		// FOR MAKING A TEMPLATE ONLY!
		this.grid   		= new boolean[4][4][4];
		this.gridCIndex 	= new int[4][4][4];
		this.width  		= new int[4];
		this.height 		= new int[4];

		clearGrids();
		
		this.name		= name;
		this.width[0]	= width;
		this.height[0]	= height;
		this.color		= color;
	}
	
	public void clearGrids() {
		for (int i = 0; i < 4; i++) {
			for (int x = 0; x < 4; x++) {
				for (int y = 0; y < 4; y++) {
					grid[i][x][y] = false;
					gridCIndex[i][x][y] = 0;
				}
			}
			width[i]  = 0;
			height[i] = 0;
		}
	}
	
	public void loadTetromino() {
		Tetromino template = GameData.loadNextTetromino();
		this.name  = template.name;
		this.color = template.color;
		for (int i = 0; i < 4; i++) {
			for (int x = 0; x < 4; x++) {
				for (int y = 0; y < 4; y++) {
					this.grid[i][x][y] = template.grid[i][x][y];
					this.gridCIndex[i][x][y] = template.gridCIndex[i][x][y];
				}
			}
			this.width[i]  = template.width[i];
			this.height[i] = template.height[i];
		}
	}
	
	public void initializeGrids() {
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				grid[1][y][3 - x]	  = grid[0][x][y];
				grid[2][3 - x][3 - y] = grid[0][x][y];
				grid[3][3 - y][x]	  = grid[0][x][y];
			}
		}
		width[1]  = height[0];
		height[1] = width[0];
		width[3]  = height[0];
		height[3] = width[0];
		width[2]  = width[0];
		height[2] = height[0];
		
		for (int i = 0; i < 4; i++) {
			while (!gridXFlush(i)) {
				for (int x = 0; x < 3; x++) {
					for (int y = 0; y < 4; y++) {
						grid[i][x][y] = grid[i][x + 1][y];
						grid[i][x + 1][y] = false;
					}
				}
			}
			while (!gridYFlush(i)) {
				for (int y = 0; y < 3; y++) {
					for (int x = 0; x < 4; x++) {
						grid[i][x][y] = grid[i][x][y + 1];
						grid[i][x][y + 1] = false;
					}
				}
			}
		}
		
		connectGrids();
	}
	
	public void connectGrids() {
		for (int i = 0; i < 4; i++) {
			for (int x = 0; x < 4; x++) {
				for (int y = 0; y < 4; y++) {
					gridCIndex[i][x][y] = Connection.getConnectIndex(grid[i], x, y, 4, 4);
				}
			}
		}
	}
	
	public boolean gridYFlush(int rotIndex) {
		for (int x = 0; x < 4; x++) {
			if (grid[rotIndex][x][0])
				return true;
		}
		return false;
	}

	public boolean gridXFlush(int rotIndex) {
		for (int y = 0; y < 4; y++) {
			if (grid[rotIndex][0][y])
				return true;
		}
		return false;
	}
	
	public boolean colliding(int cx, int cy) {
		if (cx < 0 || cx + width[dir] - 1 >= Grid.WIDTH || cy + height[dir] - 1 >= Grid.HEIGHT)
			return true;
		else {
			for (int x = 0; x < width[dir]; x++) {
				for (int y = 0; y < height[dir]; y++) {
					if (Grid.isSolid(cx + x, cy + y) && grid[dir][x][y]) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void moveHorizontally(int xAmount) {
		int cx = x + xAmount;
		int cy = (int) Math.round(y);
		if (!colliding(cx, cy)) {
			x = cx;
			if (colliding(cx, (int) Math.floor(y)) || colliding(cx, (int) Math.floor(y) + 1))
				y = cy;
			return;
		}
	}
	
	public void moveRight() {
		moveHorizontally(1);
	}
	
	public void moveLeft() {
		moveHorizontally(-1);
	}
	
	public Vector getFallPosition() {
		for (int y = (int) Math.round(this.y); y + height[dir] - 1 < Grid.HEIGHT; y++) {
			if (colliding(x, y)) {
				return new Vector(x, y - 1);
			}
		}
		return new Vector(x, Grid.HEIGHT - height[dir]);
	}
	
	public void instantFall() {
		setPosition(getFallPosition());
	}
	
	public void setPosition(Vector v) {
		this.x = (int) v.x;
		this.y = v.y;
	}
	
	public boolean checkPlaced() {
		int cx = x;
		int cy = (int) Math.floor(y);
		if (y + height[dir] < Grid.HEIGHT) {
			for (int y = 1; y < height[dir] + 1; y++) {
				for (int x = 0; x < width[dir]; x++) {
					if (Grid.isSolid(cx + x, cy + y) && grid[dir][x][y - 1]) {
						return true;
					}
				}
			}
			return false;
		}
		return true;
	}

	public void place(Vector v) {
		place((int) v.x, (int) v.y);
	}
	
	public void place(int x, int y) {
		this.x = x;
		this.y = y;
		deconstruct();
		Game.tetromino = new Tetromino();
		Grid.scanGrid();
		return;
	}
	
	public void rotate(boolean counterClockWise) {
		int dirPrev = dir;
		if (counterClockWise) {
			dir += 1;
			if (dir > 3)
				dir = 0;
		}
		else {
			dir -= 1;
			if (dir < 0)
				dir = 3;
		}
		ArrayList<Vector> spaces = new ArrayList<Vector>();
		int cx = this.x;
		int cy = (int) Math.round(y);
		double shortest = 100000;
		for (int x1 = 0; x1 < 4; x1++) {
			for (int y1 = 0; y1 < 4; y1++) {
				if (grid[dirPrev][x1][y1]) {
					for (int x2 = 0; x2 < 4; x2++) {
						for (int y2 = 0; y2 < 4; y2++) {
							if (grid[dir][x2][y2] && Math.floor(y + y1 - y2) >= 0) {
								Vector v = new Vector(cx + x1 - x2, cy + y1 - y2);
								if (!colliding((int) v.x, (int) v.y)) {
									// Possible Rotation Position!
									double W2 = (double) width[dirPrev] / 2.0d;
									double H2 = (double) height[dirPrev] / 2.0d;
									double dist = GMath.distance(cx + W2, cy + H2, v.x + H2, v.y + W2);
									if (dist <= shortest) {
										if (dist < shortest)
											spaces.clear();
										shortest = dist;
										if (!colliding((int) v.x, (int) Math.floor(y + y1 - y2)) && !colliding((int) v.x, (int) Math.floor(y + y1 - y2) + 1))
											v.y = this.y + y1 - y2;
										spaces.add(v);
									}
								}
							}
						}
					}
				}
			}
		}
		if (spaces.size() > 0) {
			int index = Game.random.nextInt(spaces.size());
			this.x = (int) spaces.get(index).x;
			this.y = spaces.get(index).y;
		}
		else
			dir = dirPrev;
	}
	
	public void deconstruct() {
		System.out.println(GameData.fallSpeed);
		int dX = x;
		int dY = (int) Math.round(y);
		
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				if (grid[dir][x][y])
					Grid.addTile(dX + x, dY + y, color);
			}
		}
	}
	
	public void update() {
		
		if (colliding(x, (int) Math.floor(y + 1))) {
			finishTimer.start();
			//place(x, (int) Math.floor(y));
		}
		else {
			y += (GameData.fallSpeed + (GMath.bool(Game.keys.down.isDown) * 2)) / (double) Grid.SCALE;
			finishTimer.stop();
		}
		
		if (finishTimer.running && finishTimer.getSeconds() >= FINISH_DELAY_TIME) {
			place(x, (int) Math.floor(y));
			return;
		}
		
		if (Game.keys.right.isPressed)
			moveRight();
		if (Game.keys.left.isPressed)
			moveLeft();
		
		if (Game.keys.flipL.isPressed)
			rotate(true);
		if (Game.keys.flipR.isPressed)
			rotate(false);
		
		if (Game.keys.instant.isPressed)
			instantFall();
	}
	
	public void drawTetrominoGhost(Graphics g, Vector dv) {
		drawTetromino(g, (int) dv.x, dv.y, true);
	}
	
	public void drawTetromino(Graphics g, int dx, double dy) {
		drawTetromino(g, dx, dy, false);
	}
	
	public void drawTetromino(Graphics g, int dx, double dy, boolean ghost) {
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				if (grid[dir][x][y]) {
					if (ghost)
						Tile.drawTileGhost(g, (dx + x) * Grid.SCALE, (dy + y) * Grid.SCALE, color, gridCIndex[dir][x][y]);
					else
						Tile.drawTile(g, (dx + x) * Grid.SCALE, (dy + y) * Grid.SCALE, color, gridCIndex[dir][x][y]);
//					g.setColor(Color.black);
//					g.drawString("" + gridCIndex[dir][x][y], (dx + x) * Grid.SCALE + 1, (int) ((dy + y) * Grid.SCALE) + 1);
//					g.setColor(Color.white);
//					g.drawString("" + gridCIndex[dir][x][y], (dx + x) * Grid.SCALE, (int) ((dy + y) * Grid.SCALE));
				}
			}
		}
	}
	
	public void draw(Graphics g) {
		if (GameData.showGhosts)
			drawTetrominoGhost(g, getFallPosition());
		drawTetromino(g, this.x, this.y);
	}
}
