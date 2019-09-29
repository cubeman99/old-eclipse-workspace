package main;

import java.awt.Graphics;
import java.awt.Image;

public class ImageDrawer {
	public static int tileSize = 16;
	
	public static void drawTile(Graphics g, Image img, int dx, int dy, int sx, int sy) {
		g.drawImage(img, dx, dy, dx + tileSize, dy + tileSize, sx * tileSize,
				sy * tileSize, (sx + 1) * tileSize, (sy + 1) * tileSize, null);
	}
	
}
