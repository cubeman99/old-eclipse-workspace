import java.awt.Graphics;
import java.awt.Image;


public class Icon {
	public Image image;
	public int subx;
	public int suby;
	public int width;
	public int height;
	public boolean drawWhole;
	
	public Icon(Image img, int sx, int sy, int w, int h) {
		image = img;
		subx = sx;
		suby = sy;
		width = w;
		height = h;
		drawWhole = false;
	}
	
	public Icon(Image img, int sx, int sy) {
		this(img, sx, sy, 16, 16);
	}
	
	public Icon(Image img) {
		image = img;
		drawWhole = true;
	}
	
	public void draw(Graphics g, int x, int y) {
		if( drawWhole )
			g.drawImage(image, x, y, null);
		else
			g.drawImage(image, x, y, x + 16, y + 16, subx * 16, suby * 16, (subx * 16) + 16, (suby * 16) + 16, null);
	}
}
