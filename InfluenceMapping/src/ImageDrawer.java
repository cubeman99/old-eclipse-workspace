

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

public class ImageDrawer {
	
	public static void drawImage(Graphics g, Image image, double x, double y) {
		g.drawImage(image, (int) x, (int) y, null);
	}
	
	public static void drawImage(Graphics g, Image image, double x, double y, int originX, int originY, double scale, double dir, double alpha) {
		AffineTransform tx = new AffineTransform();
		
		tx.translate(x - ((double)originX * scale), y - ((double)originY * scale));
		if (scale != 1.0)
			tx.scale(scale, scale);
		if (dir != 0.0)
			tx.rotate(Math.toRadians(dir), originX, originY);
		
		Graphics2D g2d = (Graphics2D) g;
		
		AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) Math.max(0.0f, Math.min(1.0f, alpha)));
		
		g2d.setComposite(composite);
		g2d.setTransform(tx);
		g2d.drawImage(image, 0, 0, null);
		g2d.setTransform(new AffineTransform());
	}
}
