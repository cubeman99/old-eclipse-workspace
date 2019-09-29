import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.IOException;


public class TileSheet {
	public Image image = null;
	public int width;
	public int height;
	public int originX;
	public int originY;
	public int columnCount;
	public int imageCount;
	
	public static TileSheet LargeShip1;
	
	public static void loadAllTileSheets() {
		//LargeShip1 = new TileSheet("LargeShip1.png", 64, 64, 6, 6);
	}
	
	public TileSheet(Image image, int width, int height, int originX, int originY, int columns, int images) {
		this.image			= image;
		this.width			= width;
		this.height			= height;
		this.originX		= originX;
		this.originY		= originY;
		this.columnCount	= columns;
		this.imageCount		= images;
	}
	
	public TileSheet(String path, int width, int height, int originX, int originY, int columns, int images) {
		this((Image) null, width, height, originX, originY, columns, images);
		try {
			this.image = ImageLoader.loadImage(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public TileSheet(Image image, int width, int height, int columns, int images) {
		this(image, width, height, 0, 0, columns, images);
	}
	
	public TileSheet(String path, int width, int height, int columns, int images) {
		this(path, width, height, 0, 0, columns, images);
	}
	
	
	public Image getSubimg(int index) {
		return null;
	}
	
	public Point getSubimgPoint(int index) {
		Point p = new Point();
		p.x = index % columnCount;
		p.y = (int) (index / columnCount);
		return p;
	}
	
	public void drawSubimg(Graphics g, int index, int x, int y) {
		Point p = getSubimgPoint(index);
		g.drawImage(image, x, y, x + width, y + height, p.x * width, p.y * height, (p.x * width) + width, (p.y * height) + height, null);
	}
	
	
}
