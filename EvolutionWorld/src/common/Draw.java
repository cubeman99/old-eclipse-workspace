package common;

import java.awt.Color;
import java.awt.Graphics;

public class Draw {
	private static Graphics g;
	private static Color color	= Color.BLACK;
	private static Vector pan	= new Vector();
	private static double zoom	= 1.0;
	
	
	public static Color getColor() {
		return color;
	}
	
	public static Graphics getGraphics() {
		return g;
	}

	public static void setGraphics(Graphics g) {
		Draw.g = g;
		g.setColor(color);
	}
	
	public static void setColor(Color color) {
		Draw.color = color;
		g.setColor(color);
	}
	
	public static void setView(Vector pan, double zoom) {
		Draw.pan  = new Vector(pan);
		Draw.zoom = zoom;
	}
	
	private static int dx(double x) {
		return (int) ((x - pan.x) * zoom);
	}
	
	private static int dy(double y) {
		return (int) ((y - pan.y) * zoom);
	}
	
	private static int z(double a) {
		return (int) (a * zoom);
	}

	/** Lines **/
	public static void drawLine(double x1, double y1, double x2, double y2) {
		g.drawLine(dx(x1), dy(y1), dx(x2), dy(y2));
	}

	public static void drawLine(Vector end1, Vector end2) {
		drawLine(end1.x, end1.y, end2.x, end2.y);
	}
	
	
	/** Rectangles **/
	public static void drawRect(double x, double y, double width, double height) {
		g.drawRect(dx(x), dy(y), z(width), z(height));
	}

	public static void drawRect(Vector pos, Vector size) {
		drawRect(pos.x, pos.y, size.x, size.y);
	}
	
	public static void fillRect(double x, double y, double width, double height) {
		g.fillRect(dx(x), dy(y), z(width), z(height));
	}

	public static void fillRect(Vector pos, Vector size) {
		fillRect(pos.x, pos.y, size.x, size.y);
	}

	
	/** Circles **/
	public static void drawCircle(double x, double y, double radius) {
		g.drawOval(dx(x - radius), dy(y - radius), z(radius * 2.0), z(radius * 2.0));
	}
	
	public static void drawCircle(Vector center, double radius) {
		drawCircle(center.x, center.y, radius);
	}
	
	public static void fillCircle(double x, double y, double radius) {
		g.fillOval(dx(x - radius), dy(y - radius), z(radius * 2.0), z(radius * 2.0));
	}
	
	public static void fillCircle(Vector center, double radius) {
		fillCircle(center.x, center.y, radius);
	}
	
	
	/** Circle Arc **/
	public static void drawArc(double x, double y, double radius, double startAngle, double arcAngle) {
		g.drawArc(dx(x - radius), dy(y - radius), z(radius * 2.0), z(radius * 2.0), (int) GMath.toDegrees(startAngle), (int) GMath.toDegrees(arcAngle));
	}
	public static void drawArc(Vector pos, double radius, double startAngle, double arcAngle) {
		Draw.drawArc(pos.x, pos.y, radius, startAngle, arcAngle);
	}
}
