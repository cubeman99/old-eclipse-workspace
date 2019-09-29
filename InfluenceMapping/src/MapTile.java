


import java.awt.Graphics;

public class MapTile {
	public boolean isSolid = false;
	public int x = 0;
	public int y = 0;
	public int size;

	public MapTile(int x, int y) {
		this.x = x;
		this.y = y;
		this.size = 32;
	}
	
	public void onDestroy() {
		
	}
	
	public void drawTileSquare(Graphics g) {
		g.fillRect(x * Game.world.tileSize, y * Game.world.tileSize, Game.world.tileSize, Game.world.tileSize);
	}
	
	public void draw(Graphics g) {
		
	}
}
