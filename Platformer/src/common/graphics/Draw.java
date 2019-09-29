package common.graphics;

import game.World;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import common.Point;
import common.Rectangle;
import common.Settings;
import common.Vector;

public class Draw {
	private static Graphics2D g;
	private static ViewControl viewControl;
	


	public static void setGraphics(Graphics g) {
		Draw.g = (Graphics2D) g;
	}
	
	public static void setView(ViewControl viewControl) {
		Draw.viewControl = viewControl;
	}
	
	private static int dx(double x) {
		return (int) ((x - viewControl.pan.x) * Settings.TILE_SIZE);
	}
	
	private static int dy(double y) {
		return (int) ((y - viewControl.pan.y) * Settings.TILE_SIZE);
	}
	
	private static int z(double a) {
		return (int) (a * Settings.TILE_SIZE);
	}
	
	public static void setColor(Color c) {
		g.setColor(c);
	}
	
	
	
	public static void drawSprite(Sprite spr, double x, double y) {
		g.drawImage(spr.getImage(), dx(x), dy(y), null);
	}
	
	public static void drawSprite(Sprite spr, Vector pos) {
		drawSprite(spr, pos.x, pos.y);
	}

	public static void drawSprite(Sprite spr, double x, double y, double xScale, double yScale) {
		Point size = spr.getSize();
		int dx1    = dx(x) - (int) (xScale * spr.getOrigin().x);
		int dy1    = dy(y) - (int) (yScale * spr.getOrigin().y);
		int dx2    = dx(x) + (int) (xScale * (spr.getSize().x - spr.getOrigin().x));
		int dy2    = dy(y) + (int) (yScale * (spr.getSize().y - spr.getOrigin().y));
		
		g.drawImage(spr.getImage(),
				Math.min(dx1, dx2), Math.min(dy1, dy2),
				Math.max(dx1, dx2), Math.max(dy1, dy2),
				(xScale < 0 ? size.x : 0), (yScale < 0 ? size.y : 0), 
				(xScale < 0 ? 0 : size.x), (yScale < 0 ? 0 : size.y), null);
		
		/*
		Draw.setColor(Color.RED);
		g.drawRect(Math.min(dx1, dx2), Math.min(dy1, dy2),
				Math.abs(dx2 - dx1), Math.abs(dy2 - dy1));
		Draw.setColor(Color.GREEN);
		g.drawOval(dx1 - 2, dy1 - 2, 4, 4);
		Draw.setColor(Color.BLUE);
		g.drawOval(dx2 - 2, dy2 - 2, 4, 4);
		*/
	}
	
	public static void drawSprite(Sprite spr, Point pos) {
		drawSprite(spr, pos.x, pos.y);
	}
	
	public static void drawRect(Rectangle r) {
		g.drawRect(dx(r.getX1()), dy(r.getY1()), z(r.getWidth()), z(r.getHeight()));
	}
}
