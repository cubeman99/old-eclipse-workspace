import java.awt.Graphics;
import java.awt.Image;


public class Tileset {
	public Image image;
	public int tileWidth;
	public int tileHeight;
	public int tileSepx;
	public int tileSepy;
	public int tileOffsetx;
	public int tileOffsety;
	
	public Tileset(Image img, int width, int height, int sepx, int sepy, int offx, int offy) {
		image = img;
		tileWidth = width;
		tileHeight = height;
		tileSepx = sepx;
		tileSepy = sepy;
		tileOffsetx = offx;
		tileOffsety = offy;
	}
	
	public Tileset(Image img, int width, int height, int sepx, int sepy) {
		this(img, width, height, sepx, sepy, 0, 0);
	}
	
	public Tileset(Image img, int width, int height) {
		this(img, width, height, 0, 0, 0, 0);
	}
	public Tileset(Image img) {
		this(img, 16, 16, 0, 0, 0, 0);
	}
	
	
	public void draw(Graphics g, int x, int y, int subx, int suby) {
		g.drawImage(image,
				x,
				y,
				x + tileWidth,
				y + tileHeight,
				(subx * tileWidth) + tileSepx,
				(suby * tileHeight) + tileSepy,
				(subx * (tileWidth + tileSepx)) + tileWidth + tileSepx,
				(suby * (tileHeight + tileSepy)) + tileHeight + tileSepy,
				null);
	}
	
	public void getIcon(Graphics g, int subx, int suby) {
		//g.drawImage(image, x, y, x + 16, y + 16, subx * 16, suby * 16, (subx * 16) + 16, (suby * 16) + 16, null);
	}
	
	
	public static Tileset terrain 	= new Tileset(ImageLoader.imageTerrain, 8, 8);
	public static Tileset vines 	= new Tileset(ImageLoader.imageVines);
	public static Tileset tree 		= new Tileset(ImageLoader.imageTree);
	public static Tileset cracks	= new Tileset(ImageLoader.imageCracks);
}
