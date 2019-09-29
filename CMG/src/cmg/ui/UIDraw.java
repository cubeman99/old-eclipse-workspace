package cmg.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import cmg.graphics.Draw;
import cmg.graphics.SpriteSheet;
import cmg.graphics.View;
import cmg.math.GMath;
import cmg.math.geometry.Circle;
import cmg.math.geometry.Line;
import cmg.math.geometry.Path;
import cmg.math.geometry.Point;
import cmg.math.geometry.Polygon;
import cmg.math.geometry.Rect;
import cmg.math.geometry.Rectangle;
import cmg.math.geometry.Vector;

public class UIDraw {
	public static final Color TRANSPARENT = new Color(0, 0, 0, 0);
	
	public static final int TOP    = 0;
	public static final int MIDDLE = 1;
	public static final int BOTTOM = 2;
	public static final int LEFT   = 0;
	public static final int CENTER = 1;
	public static final int RIGHT  = 2;
	
	public static final BasicStroke STROKE_DASHED = new BasicStroke(
			1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, new float[] {8}, 0);
	
	private static Graphics2D g;
	
	
	
	// =================== ACCESSORS =================== //
	
	public static Graphics2D getGraphics() {
		return g;
	}
	
	public static Color getColor() {
		return g.getColor();
	}
	
	

	// ==================== MUTATORS ==================== //
	
	
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
	
	/** Points **/
	
	public static void drawPoint(Point p) {
		g.drawLine(p.x, p.y, p.x, p.y);
	}
	
	public static void drawPoint(int x, int y) {
		g.drawLine(x, y, x, y);
	}
	
	
	
	/** Lines **/
	
	public static void drawLine(int x1, int y1, int x2, int y2) {
		g.drawLine(x1, y1, x2, y2);
	}

	public static void drawLine(Point end1, Point end2) {
		drawLine(end1.x, end1.y, end2.x, end2.y);
	}

	
	
	/** Rectangles **/
	
	public static void drawRect(int x, int y, int width, int height) {
		g.drawRect(x, y, width, height);
	}

	public static void drawRect(Point pos, Point size) {
		drawRect(pos.x, pos.y, size.x, size.y);
	}

	public static void drawRect(Rect r) {
		drawRect(r.getMinX(), r.getMinY(), Math.abs(r.getWidth()), Math.abs(r.getHeight()));
	}
	
	public static void fillRect(int x, int y, int width, int height) {
		g.fillRect(x, y, width, height);
	}

	public static void fillRect(Point pos, Point size) {
		fillRect(pos.x, pos.y, size.x, size.y);
	}

	public static void fillRect(Rect r) {
		fillRect(r.getMinX(), r.getMinY(), Math.abs(r.getWidth()), Math.abs(r.getHeight()));
	}
	
	
	
	/** Circles **/
	public static void drawCircle(int x, int y, int radius) {
		g.drawOval(x - radius, y - radius, radius * 2, radius * 2);
	}
	
	public static void drawCircle(Point center, int radius) {
		drawCircle(center.x, center.y, radius);
	}
	
	public static void fillCircle(int x, int y, int radius) {
		g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
	}
	
	public static void fillCircle(Point center, int radius) {
		fillCircle(center.x, center.y, radius);
	}
	
	
	
	/** Boxes **/
	
	public static void drawUIBox(Rect box, String text, boolean pressed, Color fillColor, Color outlineColor, Color textColor) {
		g.setColor(fillColor);
		g.fillRect(box.getX1() + 1, box.getY1() + 2, box.getWidth() - 2, box.getHeight() - 4);
		g.fillRect(box.getX1() + 2, box.getY1() + 1, box.getWidth() - 4, box.getHeight() - 2);
		
		g.setColor(outlineColor);
		g.drawLine(box.getX1(), box.getY1() + 1, box.getX1(), box.getY2() - 2);
		g.drawLine(box.getX1() + 1, box.getY1(), box.getX2() - 2, box.getY1());
		g.drawLine(box.getX2() - 1, box.getY1() + 1, box.getX2() - 1, box.getY2() - 2);
		g.drawLine(box.getX1() + 1, box.getY2() - 1, box.getX2() - 2, box.getY2() - 1);
		drawPoint(box.getX1() + 1, box.getY1() + 1);
		drawPoint(box.getX1() + 1, box.getY2() - 2);
		drawPoint(box.getX2() - 2, box.getY1() + 1);
		drawPoint(box.getX2() - 2, box.getY2() - 2);
		
		if (pressed) {
			g.setColor(new Color(0, 0, 0, 160));
			g.drawLine(box.getX1() + 1, box.getY1() + 2, box.getX1() + 1, box.getY2() - 3);
			g.drawLine(box.getX1() + 2, box.getY1() + 1, box.getX2() - 3, box.getY1() + 1);
			drawPoint(box.getX1() + 2, box.getY1() + 2);
			g.setColor(new Color(0, 0, 0, 80));
			g.drawLine(box.getX1() + 2, box.getY1() + 3, box.getX1() + 2, box.getY2() - 2);
			g.drawLine(box.getX1() + 3, box.getY1() + 2, box.getX2() - 2, box.getY1() + 2);
			drawPoint(box.getX1() + 3, box.getY1() + 3);
		}
		
		Draw.setColor(textColor);
		Point p = new Point(box.getCenter());
		if (pressed)
			p.add(1, 1);
		Draw.drawString(text, p.x, p.y - 2, Draw.CENTER, Draw.MIDDLE);
	}
	
	public static void drawCheck(int x, int y, Color color) {
		g.setColor(color);
		g.drawLine(x - 1, y + 2, x + 4, y - 3);
		g.drawLine(x - 1, y + 1, x + 4, y - 4);
		g.drawLine(x - 1, y + 0, x + 3, y - 4);
		g.drawLine(x - 4, y - 1, x - 2, y + 1);
		g.drawLine(x - 4, y - 2, x - 2, y + 0);
		g.drawLine(x - 3, y - 2, x - 2, y - 1);
	}
	
	
	
	/** Text **/
	
	public static void drawString(String str, int x, int y, int halign, int valign) {
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
		g.drawString(str, x + offsetx, y + offsety);
	}
	
	public static void drawString(String str, int x, int y) {
		g.drawString(str, x, y);
	}
	
	public static void drawString(String str, Point pos) {
		Draw.drawString(str, pos.x, pos.y);
	}
	
	public static void drawString(String str, int x, int y, int halign) {
		Draw.drawString(str, x, y, halign, BOTTOM);
	}
	
	public static void drawString(String str, Point pos, int halign) {
		Draw.drawString(str, pos.x, pos.y, halign, BOTTOM);
	}
	
	public static void drawString(String str, Point pos, int halign, int valign) {
		Draw.drawString(str, pos.x, pos.y, halign, valign);
	}
	
	
	
	/** Images **/
	
	public static void drawImage(SpriteSheet sheet, int sx, int sy, int dx, int dy) {
		g.drawImage(sheet.getBuffer(), dx, dy,
				dx + sheet.getImageSize().x, dy + sheet.getImageSize().y,
				sx * (sheet.getImageSize().x + sheet.getSeperation().x),
				sy * (sheet.getImageSize().y + sheet.getSeperation().y),
				(sx + 1) * (sheet.getImageSize().x + sheet.getSeperation().x) - 1,
				(sy + 1) * (sheet.getImageSize().y + sheet.getSeperation().y) - 1,
				null);
	}
}
