

import java.awt.Color;
import java.awt.Graphics;

public class Rect {
	public double x;
	public double y;
	public double width;
	public double height;
	
	public Rect() {
		x 		= 0;
		y 		= 0;
		width 	= 0;
		height	= 0;
	}
	
	public Rect(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	public Rect(Rect r) {
		this.x = r.x;
		this.y = r.y;
		this.width = r.width;
		this.height = r.height;
	}
	
	public double getX1() {
		return x;
	}

	public double getY1() {
		return y;
	}

	public double getX2() {
		return (x + width);
	}

	public double getY2() {
		return (y + height);
	}
	
	public boolean containsPoint(Vector v) {
		return (v.x >= x && v.y >= y && v.x < x + width && v.y < y + height);
	}
	
	public void draw(Graphics g, Color col) {
		g.setColor(col);
		g.drawRect((int)x, (int)y, (int)width, (int)height);
	}
}
