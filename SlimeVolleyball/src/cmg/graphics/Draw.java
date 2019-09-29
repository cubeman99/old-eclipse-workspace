package cmg.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import cmg.math.geometry.Polygon;
import cmg.math.GMath;
import cmg.math.geometry.Circle;
import cmg.math.geometry.Line;
import cmg.math.geometry.Path;
import cmg.math.geometry.Rectangle;
import cmg.math.geometry.Vector;

public class Draw {
	public static final int TOP    = 0;
	public static final int MIDDLE = 1;
	public static final int BOTTOM = 2;
	public static final int LEFT   = 0;
	public static final int CENTER = 1;
	public static final int RIGHT  = 2;
	
	public static final BasicStroke STROKE_DASHED = new BasicStroke(
			1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, new float[] {8}, 0);
	
	private static View view = new View();
	private static Graphics2D g;
	
	
	
	// =================== ACCESSORS =================== //
	
	public static View getView() {
		return view;
	}
	
	public static Graphics2D getGraphics() {
		return g;
	}
	
	public static Color getColor() {
		return g.getColor();
	}
	
	/** Get the x position on the screen. **/
	private static int dx(double x) {
		return view.dx(x);
	}
	
	/** Get the y position on the screen. **/
	private static int dy(double y) {
		return view.dy(y);
	}
	
	/** Get a magnitude scaled by the zoom. **/
	private static int z(double a) {
		return view.z(a);
	}
	
	

	// ==================== MUTATORS ==================== //
	
	public static void setView(View view) {
		Draw.view.set(view);
	}
	
	public static void setGraphics(Graphics newGraphics) {
		g = (Graphics2D) newGraphics;
	}
	
	public static void setColor(Color c) {
		g.setColor(c);
	}
	
	/** Set the stroke for drawing. **/
	public static void setStroke(Stroke s) {
		g.setStroke(s);
	}
	
	/** Reset the stroke to a solid line. **/
	public static void resetStroke() {
		g.setStroke(new BasicStroke());
	}
	
	public static void setAntiAliasing(boolean enabled) {
		Draw.getGraphics().setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
				enabled ? RenderingHints.VALUE_ANTIALIAS_ON
						: RenderingHints.VALUE_ANTIALIAS_OFF);
	}
	
	

	// =============== DRAWING FUNCTIONS =============== //

	/** Lines **/
	public static void drawLine(double x1, double y1, double x2, double y2) {
		g.drawLine(dx(x1), dy(y1), dx(x2), dy(y2));
	}

	public static void drawLine(Vector end1, Vector end2) {
		drawLine(end1.x, end1.y, end2.x, end2.y);
	}
	
	public static void draw(Line l) {
		drawLine(l.x1(), l.y1(), l.x2(), l.y2());
	}

	
	
	/** Rectangles **/
	public static void drawRect(double x, double y, double width, double height) {
		g.drawRect(dx(x), dy(y), z(width) - 1, z(height) - 1);
	}

	public static void drawRect(Vector pos, Vector size) {
		drawRect(pos.x, pos.y, size.x, size.y);
	}

	public static void draw(Rectangle r) {
		drawRect(r.getMinX(), r.getMinY(), GMath.abs(r.getWidth()), GMath.abs(r.getHeight()));
	}
	
	public static void fillRect(double x, double y, double width, double height) {
		g.fillRect(dx(x), dy(y), z(width) - 1, z(height) - 1);
	}

	public static void fillRect(Vector pos, Vector size) {
		fillRect(pos.x, pos.y, size.x, size.y);
	}

	public static void fill(Rectangle r) {
		fillRect(r.getMinX(), r.getMinY(), GMath.abs(r.getWidth()), GMath.abs(r.getHeight()));
	}
	
	
	
	/** Circles **/
	public static void drawCircle(double x, double y, double radius) {
		g.drawOval(dx(x) - z(radius), dy(y) - z(radius), z(radius * 2.0), z(radius * 2.0));
	}
	
	public static void drawCircle(Vector center, double radius) {
		drawCircle(center.x, center.y, radius);
	}
	
	public static void draw(Circle c) {
		drawCircle(c.position.x, c.position.y, c.radius);
	}
	
	public static void fillCircle(double x, double y, double radius) {
		g.fillOval(dx(x - radius), dy(y - radius), z(radius * 2.0), z(radius * 2.0));
	}
	
	public static void fillCircle(Vector center, double radius) {
		fillCircle(center.x, center.y, radius);
	}
	
	public static void fill(Circle c) {
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
	
	
	
	/** Paths **/
	
	public static void draw(Path p) {
		for (int i = 0; i < p.numEdges(); i++)
			draw(p.getEdge(i));
	}
	
	
	
	/** Polygons **/
	
	public static void draw(Polygon p) {
		for (int i = 0; i < p.edgeCount(); i++) {
			Line l = p.getEdge(i);
			Draw.draw(l);
		}
	}
	
	public static void fill(Polygon p) {
		java.awt.Polygon awtPoly = new java.awt.Polygon();
		for (int i = 0; i < p.vertexCount(); i++) {
			Vector v = p.getVertex(i);
			awtPoly.addPoint(dx(v.x), dy(v.y));
		}
		g.fillPolygon(awtPoly);
	}
	
	
	
	/** Text **/
	
	public static void drawString(String str, double x, double y, int halign, int valign) {
		FontMetrics fm   = g.getFontMetrics();
		java.awt.geom.Rectangle2D rect = fm.getStringBounds(str, g);
		int offsetx    = 0;
		int offsety    = 0;
		int textHeight = (int) (rect.getHeight()); 
		int textWidth  = (int) (rect.getWidth());
		if (halign == RIGHT)
			offsetx = -textWidth;
		else if (halign == CENTER)
			offsetx = -(textWidth / 2);
		if (valign == TOP)
			offsety = textHeight;
		else if (valign == MIDDLE)
			offsety = (textHeight / 2);
		g.drawString(str, dx(x) + offsetx, dy(y) + offsety);
	}
	
	public static void drawString(String str, double x, double y) {
		g.drawString(str, dx(x), dy(y));
	}
	
	public static void drawString(String str, Vector pos) {
		Draw.drawString(str, pos.x, pos.y);
	}
	
	public static void drawString(String str, double x, double y, int halign) {
		Draw.drawString(str, x, y, halign, BOTTOM);
	}
	
	public static void drawString(String str, Vector pos, int halign) {
		Draw.drawString(str, pos.x, pos.y, halign, BOTTOM);
	}
	
	public static void drawString(String str, Vector pos, int halign, int valign) {
		Draw.drawString(str, pos.x, pos.y, halign, valign);
	}
	
	
	
	/** Images **/
	
	public static void drawImage(SpriteSheet sheet, int sx, int sy, double dx, double dy) {
		g.drawImage(sheet.getBuffer(),
				dx(dx), dy(dy),
				dx(dx + sheet.getImageSize().x),
				dy(dy + sheet.getImageSize().y),
				sx * (sheet.getImageSize().x + sheet.getSeperation().x),
				sy * (sheet.getImageSize().y + sheet.getSeperation().y),
				(sx + 1) * (sheet.getImageSize().x + sheet.getSeperation().x) - 1,
				(sy + 1) * (sheet.getImageSize().y + sheet.getSeperation().y) - 1,
				null);
	}
}
