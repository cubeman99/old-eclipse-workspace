import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.IOException;


public class TileSheet {
	public int tileWidth;
	public int tileHeight;
	public int rowWidth;
	public int imageCount;
	public Image image;
	
	public static TileSheet tsWires;
	
	
	public static void initialize() {
		tsWires = new TileSheet("wires.png", 16, 16, 5, 10);
	}
	
	public TileSheet(String filename, int tileWidth, int tileHeight, int rowWidth, int imageCount) {
		this.tileWidth	= tileWidth;
		this.tileHeight	= tileHeight;
		this.rowWidth	= rowWidth;
		this.imageCount	= imageCount;
		if (rowWidth <= 0)
			this.rowWidth = imageCount;
		
		try {
			image = ImageLoader.loadImage(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getImageIndex(int x, int y) {
		int ind = (rowWidth * y) + x;
		if (ind < imageCount)
			return ind;
		return -1;
	}
	
	public Point getIndexPosition(int index) {
		Point p = new Point();
		p.y = (int) Math.floor(index / rowWidth);
		p.x = index % rowWidth;
		
		return p;
	}
	
	public void draw(Graphics g, int dx, int dy, int sx, int sy) {
		if (getImageIndex(sx, sy) >= 0) {
			g.drawImage(image, dx, dy, dx + tileWidth, dy + tileHeight, sx * tileWidth, sy * tileHeight, (sx + 1) * tileWidth, (sy + 1) * tileHeight, null);
		}
	}
	
	public void draw(Graphics g, int dx, int dy, int index) {
		if (index >= 0) {
			Point p = getIndexPosition(index);
			g.drawImage(image, dx, dy, dx + tileWidth, dy + tileHeight, p.x * tileWidth, p.y * tileHeight, (p.x + 1) * tileWidth, (p.y + 1) * tileHeight, null);
		}
	}
}
