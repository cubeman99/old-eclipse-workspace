

import java.awt.Color;
import java.awt.Graphics;


public class Circle {
	public double radius;
	public double x;
	public double y;
	
	public Circle(double x, double y, double radius) {
		this.x = x;
		this.y = y;
		this.radius = radius;
	}
	
	public double getCircumference() {
		return (2 * Math.PI * radius);
	}

	public double getArea() {
		return (Math.PI * radius * radius);
	}
	
	public Vector getCenter() {
		return new Vector(x, y);
	}
	
	public boolean colliding(Circle c) {
		return Collisions.circleCircle(this, c);
	}
	
	public void draw(Graphics g, Color col) {
		g.setColor(col);
		g.drawOval((int)(x - radius), (int)(y - radius), (int)(radius * 2), (int)(radius * 2));
	}
}

