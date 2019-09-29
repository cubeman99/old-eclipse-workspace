

import java.awt.Color;
import java.awt.Graphics;

public class WallTile extends MapTile {
	
	public WallTile(int xx, int yy) {
		super(xx, yy);
		isSolid = true;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.gray);
		drawTileSquare(g);
	}
}
