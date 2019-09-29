package common.graphics;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import common.Vector;
import main.ImageLoader;

public class Draw {
	private static Vector center;
	private static double angle;
	private static Graphics2D g;
	
	private static Vector pos(double r, double a) {
		return center.plus(Vector.polarVector(r, a - angle));
	}
	
	private static double ang(double a) {
		return (a - angle);
	}
	
	public static void setGraphics(Graphics g) {
		Draw.g = (Graphics2D) g;
	}
	
	public static void setCenter(Vector center) {
		Draw.center = new Vector(center);
	}
	
	public static void setAngle(double angle) {
		Draw.angle = angle;
	}
	
	public static Vector getCenter() {
		return center;
	}
	
	public static Graphics2D getGraphics() {
		return g;
	}
	
	public static void drawRing(double radius, int thickness) {
		if (thickness != 1)
			g.setStroke(new BasicStroke(thickness));
		
		g.drawOval((int) (center.x - radius), (int) (center.y - radius), (int) radius * 2, (int) radius* 2);
		
		g.setStroke(new BasicStroke());
	}
	
	public static void draw(Sprite spr, double radius, double angle) {
		drawImage(spr.getImage(), radius, angle, angle, spr.getOrigin().x, spr.getOrigin().y, 1);
	}
	
	public static void draw(Sprite spr, double radius, double angle, double rotation) {
		drawImage(spr.getImage(), radius, angle, rotation, spr.getOrigin().x, spr.getOrigin().y, 1);
	}
	
	public static void draw(Sprite spr, double radius, double angle, double rotation, double alpha) {
		drawImage(spr.getImage(), radius, angle, rotation, spr.getOrigin().x, spr.getOrigin().y, alpha);
	}
	
	public static void drawImage(Image image, double radius, double angle, double rotation, double originx, double originy, double alpha) {
		AffineTransform tx = new AffineTransform();
		Vector pos   = pos(radius, angle);
		double dir   = ang(angle);
		double scale = 1.0;

		tx.translate(pos.x - (originx * scale), pos.y - (originy * scale));
		tx.rotate(-ang(rotation), originx, originy);
		
		AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0.0f, (float) Math.min(1.0f, alpha)));
		
		g.setComposite(composite);
		g.setTransform(tx);
		g.drawImage(image, 0, 0, null);
		g.setTransform(new AffineTransform());	
	}
}
