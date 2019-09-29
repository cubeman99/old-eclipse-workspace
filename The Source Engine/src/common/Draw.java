package common;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import common.shape.Circle;
import common.shape.Line;
import common.shape.Polygon;
import common.shape.Rectangle;
import common.shape.Shape;
import map.Path;



public class Draw {
	public static final BasicStroke STROKE_DASHED = new BasicStroke(
			1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, new float[] {8}, 0);
	
	private static Graphics2D g;
	private static Color color	     = Color.BLACK;
	private static Vector pan	     = new Vector();
	private static double zoom	     = 1.0;
	private static double imageScale = 32.0; // pixels per meter
	
	
	// =================== ACCESSORS =================== //
	
	public static Color getColor() {
		return color;
	}
	
	public static Graphics2D getGraphics() {
		return g;
	}
	
	/** Get the x position on the screen. **/
	private static int dx(double x) {
		return (int) ((x - pan.x) * zoom);
	}
	
	/** Get the y position on the screen. **/
	private static int dy(double y) {
		return (int) ((y - pan.y) * zoom);
	}
	
	/** Get a magnitude scaled by the zoom. **/
	private static int z(double a) {
		return (int) (a * zoom);
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	/** Set the graphics object that the draw functions will work on. **/
	public static void setGraphics(Graphics g) {
		Draw.g = (Graphics2D) g;
		g.setColor(color);
	}
	
	/** Set the color to draw with. **/
	public static void setColor(Color color) {
		Draw.color = color;
		g.setColor(color);
	}
	
	/** Set the pan and zoom of the view. **/
	public static void setView(Vector pan, double zoom) {
		Draw.pan  = new Vector(pan);
		Draw.zoom = zoom;
	}

	/** Set the pan and zoom of the view to that of a View Control object. **/
	public static void setView(ViewControl view) {
		Draw.pan  = view.pan;
		Draw.zoom = view.zoom;
	}
	
	/** Set the stroke for drawing. **/
	public static void setStroke(Stroke s) {
		g.setStroke(s);
	}
	
	/** Reset the stroke to a solid line. **/
	public static void resetStroke() {
		g.setStroke(new BasicStroke());
	}
	
	

	// ================ DRAWING FUNCTIONS ================ //
	
	/** Lines **/
	public static void drawLine(double x1, double y1, double x2, double y2) {
		g.drawLine(dx(x1), dy(y1), dx(x2), dy(y2));
	}

	public static void drawLine(Vector end1, Vector end2) {
		drawLine(end1.x, end1.y, end2.x, end2.y);
	}
	
	public static void drawLine(Line l) {
		drawLine(l.x1(), l.y1(), l.x2(), l.y2());
	}
	
	
	/** Rectangles **/
	public static void drawRect(double x, double y, double width, double height) {
		g.drawRect(dx(x), dy(y), z(width), z(height));
	}

	public static void drawRect(Vector pos, Vector size) {
		drawRect(pos.x, pos.y, size.x, size.y);
	}

	public static void drawRect(Rectangle r) {
		drawRect(r.minX(), r.minY(), GMath.abs(r.width()), GMath.abs(r.height()));
	}
	
	public static void fillRect(double x, double y, double width, double height) {
		g.fillRect(dx(x), dy(y), z(width), z(height));
	}

	public static void fillRect(Vector pos, Vector size) {
		fillRect(pos.x, pos.y, size.x, size.y);
	}

	public static void fillRect(Rectangle r) {
		fillRect(r.minX(), r.minY(), GMath.abs(r.width()), GMath.abs(r.height()));
	}

	
	/** Circles **/
	public static void drawCircle(double x, double y, double radius) {
		g.drawOval(dx(x) - z(radius), dy(y) - z(radius), z(radius * 2.0), z(radius * 2.0));
	}
	
	public static void drawCircle(Vector center, double radius) {
		drawCircle(center.x, center.y, radius);
	}
	
	public static void drawCircle(Circle c) {
		drawCircle(c.position.x, c.position.y, c.radius);
	}
	
	public static void fillCircle(double x, double y, double radius) {
		g.fillOval(dx(x - radius), dy(y - radius), z(radius * 2.0), z(radius * 2.0));
	}
	
	public static void fillCircle(Vector center, double radius) {
		fillCircle(center.x, center.y, radius);
	}
	
	public static void fillCircle(Circle c) {
		fillCircle(c.position.x, c.position.y, c.radius);
	}
	
	
	/** Arcs **/
	public static void drawArc(double x, double y, double radius, double startAngle, double arcAngle) {
		g.drawArc(dx(x - radius), dy(y - radius), z(radius * 2.0), z(radius * 2.0), (int) GMath.toDegrees(startAngle), (int) GMath.toDegrees(arcAngle));
	}
	public static void fillArc(double x, double y, double radius, double startAngle, double arcAngle) {
		g.fillArc(dx(x - radius), dy(y - radius), z(radius * 2.0), z(radius * 2.0), (int) GMath.toDegrees(startAngle), (int) GMath.toDegrees(arcAngle));
	}
	public static void drawArc(Vector pos, double radius, double startAngle, double arcAngle) {
		Draw.drawArc(pos.x, pos.y, radius, startAngle, arcAngle);
	}
	public static void fillArc(Vector pos, double radius, double startAngle, double arcAngle) {
		Draw.fillArc(pos.x, pos.y, radius, startAngle, arcAngle);
	}
	
	
	/** Polygons **/
	public static void drawPolygon(Polygon p) {
		for (int i = 0; i < p.edgeCount(); i++) {
			Line l = p.getEdge(i);
			Draw.drawLine(l);
		}
	}
	
	public static void fillPolygon(Polygon p) {
		java.awt.Polygon awtPoly = new java.awt.Polygon();
		for (int i = 0; i < p.vertexCount(); i++) {
			Vector v = p.getVertex(i);
			awtPoly.addPoint(dx(v.x), dy(v.y));
		}
		g.fillPolygon(awtPoly);
	}
	
	
	/** Any Shape **/
	public static void drawShape(Shape shape) {
		if (shape instanceof Circle)
			drawCircle((Circle) shape);
		else if (shape instanceof Line)
			drawLine((Line) shape);
		else if (shape instanceof Rectangle)
			drawRect((Rectangle) shape);
		else if (shape instanceof Polygon)
			drawPolygon((Polygon) shape);
	}
	
	public static void fillShape(Shape shape) {
		if (shape instanceof Circle)
			fillCircle((Circle) shape);
		else if (shape instanceof Line)
			drawLine((Line) shape);
		else if (shape instanceof Rectangle)
			fillRect((Rectangle) shape);
		else if (shape instanceof Polygon)
			fillPolygon((Polygon) shape);
	}
	
	/** Paths **/
	public static void drawPath(Path p) {
		for (int i = 0; i < p.getLength(); i++) {
	    	Draw.fillCircle(p.getPoint(i), 0.11);
	    	if (i < p.getLength() - 1)
	    		Draw.drawLine(p.getPoint(i), p.getPoint(i + 1));
		}
	}
	
	/** Images **/
	public static void drawImage(Image img, double x, double y, double scale) {
		double w = img.getWidth(null);
		double h = img.getHeight(null);
		g.drawImage(img, dx(x), dy(y), z(w / scale), z(h / scale), null);
	}

	public static void drawImage(Image img, Vector position, double scale) {
		Draw.drawImage(img, position.x, position.y, scale);
	}
	
	public static void drawImage(Image img, double x, double y) {
		Draw.drawImage(img, x, y, imageScale);
	}
	
	public static void drawImage(Image img, Vector position) {
		Draw.drawImage(img, position.x, position.y, imageScale);
	}
	
	/** Text **/
	public static void drawStringCentered(String str, double x, double y) {
		FontMetrics fm   = g.getFontMetrics();
		java.awt.geom.Rectangle2D rect = fm.getStringBounds(str, g);

		int textHeight = (int)(rect.getHeight()); 
		int textWidth  = (int)(rect.getWidth());
		
		g.drawString(str, dx(x) - (textWidth / 2), dy(y) + (textHeight / 2));
	}
	
	public static void drawStringCentered(String str, Vector pos) {
		Draw.drawStringCentered(str, pos.x, pos.y);
	}
}
