import java.awt.Color;
import java.awt.Graphics;


public class Tile {
	public int x = 0;
	public int y = 0;
	public int cIndex = 0;
	public Color color;
	public boolean solid = false;
	
	public Tile(int x, int y) {
		this.x = x;
		this.y = y;
		this.color = Color.white;
		this.solid = false;
	}

	public Tile(int x, int y, Color color) {
		this.x = x;
		this.y = y;
		this.color = color;
		this.solid = true;
	}
	
	public void connect() {
		cIndex = Connection.getGridConnectIndex(x, y);
	}
	
	public static Color colorAlpha(Color c, int alpha) {
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
	}
	
	public static void drawTile(Graphics g, double x, double y, int connectIndex) {
		g.fillRect((int)x, (int)y, Grid.SCALE, Grid.SCALE);
		g.drawImage(GameData.TILE_OVERLAY_IMAGES[connectIndex], (int)x, (int)y, null);
	}
	
	public static void drawTile(Graphics g, double x, double y, Color color, int connectIndex) {
		g.setColor(color);
		drawTile(g, x, y, connectIndex);
	}
	
	public static void drawTileClearing(Graphics g, double x, double y, Color color, int connectIndex) {
		g.setColor(color);
		drawTile(g, x, y, connectIndex);
		g.setColor(new Color(255, 255, 255, 170));
		drawTile(g, x, y, connectIndex);
	}
	
	public static void drawTileGhost(Graphics g, double x, double y, Color color, int connectIndex) {
		g.setColor(colorAlpha(color, 128));
		drawTile(g, x, y, connectIndex);
	}
	
	public void drawClearing(Graphics g) {
		if (solid) {
			drawTileClearing(g, (double) (x * Grid.SCALE), (double) (y * Grid.SCALE), color, cIndex);
		}
	}
	public void draw(Graphics g) {
		if (solid) {
			drawTile(g, (double) (x * Grid.SCALE), (double) (y * Grid.SCALE), color, cIndex);
		}
	}
	
}
