

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;


public class World extends Entity {
	public MapTile tiles[][];
	public int width;
	public int height;
	public int tileSize = 32;
	public boolean drawGrid = false;
	public NodeMap nodeMap = new NodeMap();
	
	public ArrayList<Pickup> pickups = new ArrayList<Pickup>();
	
	public World(int width, int height) {
		this.width = width;
		this.height = height;
		tiles = new MapTile[width][height];
		
		initiateTiles();
		
	}

	public void loadWorldFromFile(String path) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(Game.class.getResource(path));
        int w = bufferedImage.getWidth();
        int h = bufferedImage.getHeight();
        
        width = w;
        height = h;
		tiles = new MapTile[w][h];
		initiateTiles();
		
        int[] rgbs = new int[w * h];
        Arrays.fill(rgbs, 0xffA8A800);
        
        bufferedImage.getRGB(0, 0, w, h, rgbs, 0, w);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int col = rgbs[x + y * w]; //  & 0xffffff
                
                if (col == Color.white.getRGB())
                    tiles[x][y] = new FloorTile(x, y);
                else if (col == Color.black.getRGB())
                    tiles[x][y] = new WallTile(x, y);
                else if (col == new Color(0, 128, 0).getRGB()) {
                    tiles[x][y] = new FloorTile(x, y);
                    pickups.add(new Pickup(tileSize * (x + 0.5), tileSize * (y + 0.5)));
                }
                else {
	                for (int i = 1; i <= 8; i++) {
	                	TeamControl tc = TeamControl.teams.get(i);
	                	if (col == tc.color.getRGB()) {
	                        tiles[x][y] = new SpawnTile(x, y, i);
	                	}
	                }
                }
                
            }
        }
        Game.GAME_WIDTH  = w * tileSize;
        Game.GAME_HEIGHT = h * tileSize;
		Game.buffer			  = new BufferedImage(Game.GAME_WIDTH, Game.GAME_HEIGHT, BufferedImage.TYPE_INT_RGB);
		Game.backgroundBuffer = new BufferedImage(Game.GAME_WIDTH, Game.GAME_HEIGHT, BufferedImage.TYPE_INT_RGB);
		renderWorld(Game.backgroundBuffer.getGraphics());
		
		for (Unit u : Game.unitControl.units) {
			if (u.teamControl.spawnTiles.size() > 0)
				u.setPosition(u.teamControl.pickSpawnPosition());
		}
    }
	
	public void initialize() {
		

		try {
			loadWorldFromFile("images/maps/map1.png");
        }
		catch (IOException ex) {
            throw new RuntimeException("Unable to load map file", ex);
        }
        
		
		Game.nodeMap.reconstructNodeGrid();
	}
	
	public boolean pointInsideWorld(Vector v) {
		Rect r = getWorldRect();
		return (r.containsPoint(v));
	}

	public boolean pointInsideWorld(double x, double y) {
		Rect r = getWorldRect();
		return (r.containsPoint(new Vector(x, y)));
	}
	
	public Rect getWorldRect() {
		return new Rect(0, 0, width * tileSize, height * tileSize);
	}

	public void update() {
		if (Game.keyPressed[KeyEvent.VK_G]) {
			drawGrid = !drawGrid;
		}
		if (Game.keyPressed[KeyEvent.VK_P]) {
			Game.addEntity(new Pickup(tileSize * (Game.getMouseTileX() + 0.5), tileSize * (Game.getMouseTileY() + 0.5)));
		}
		
		if (Game.keyDown[KeyEvent.VK_CONTROL] && (Game.mbLeft || Game.mbRight)) {
			int mx = Game.mouseX / tileSize;
			int my = Game.mouseY / tileSize;
			if (mx >= 0 && my >= 0 && mx < width && my < height) {
				tiles[mx][my].onDestroy();
				if (Game.mbLeft) {
					if (Game.keyDown[KeyEvent.VK_SPACE])
						tiles[mx][my] = new SpawnTile(mx, my, Game.unitControl.player.team);
					else
						tiles[mx][my] = new WallTile(mx, my);
				}
				else
					tiles[mx][my] = new FloorTile(mx, my);
			}
		}

		// Update all Pickups:
		for (Pickup p : pickups)
			p.update();
	}
	
	public void initiateTiles() {
		// Initiate All Tiles to default
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				tiles[x][y] = new FloorTile(x, y);
			}
		}
	}
	
	public Rect getTileRect(int x, int y) {
		return new Rect(x * tileSize, y * tileSize, tileSize, tileSize);
	}
	
	public boolean realTile(int x, int y) {
		return (x >= 0 && y >= 0 && x < width && y < height);
	}
	
	public void renderWorld(Graphics g) {
		// Draw Black Background
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
		
		// Draw Tiles
		g.setColor(Color.gray);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				tiles[x][y].draw(g);
			}
		}
	}
	
	public void draw(Graphics g) {
		// Draw Grid
		if (drawGrid) {
			g.setColor(Color.darkGray);
			for (int i = 0; i < Math.max(width + 1, height + 1); i++) {
				g.drawLine(i * tileSize, 0, i * tileSize, height * tileSize);
				g.drawLine(0, i * tileSize, width * tileSize, i * tileSize);
			}
		}
		
		// Draw Node Map
		nodeMap.draw(g);

		// Draw all Pickups:
		for (Pickup p : pickups)
			p.draw(g);
	}
}
