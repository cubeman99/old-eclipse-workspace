

import java.awt.Graphics;
import java.awt.Point;


public class Vector {
	public double x;
	public double y;
	
	public Vector() {
		this.x = 0;
		this.y = 0;
	}
	
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector(Vector v) {
		this.x = v.getX();
		this.y = v.getY();
	}
	
	public Vector(Point p) {
		this.x = p.x;
		this.y = p.y;
	}
	
	public Vector setX(double newX) {
		x = newX;
		return this;
	}
	
	public Vector setY(double newY) {
		y = newY;
		return this;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public Vector set(double x, double y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Vector set(Vector v) {
		this.x = v.getX();
		this.y = v.getY();
		return this;
	}
	
	public Vector negate() {
		x *= -1;
		y *= -1;
		return this;
	}
	
	public double direction() {
		return GMath.direction(x, y);
	}
	
	public Vector add(Vector v) {
		x += v.getX();
		y += v.getY();
		return this;
	}
	
	public Vector add(double x, double y) {
		this.x += x;
		this.y += y;
		return this;
	}
	
	public Vector subtract(Vector v) {
		x -= v.getX();
		y -= v.getY();
		return this;
	}
	
	public Vector subtract(double x, double y) {
		this.x -= x;
		this.y -= y;
		return this;
	}
	
	public Vector scale(double scaler) {
		x *= scaler;
		y *= scaler;
		return this;
	}
	
	public double lengthSquared() {
		return ((x * x) + (y * y));
	}
	
	public double length() {
		return Math.sqrt(lengthSquared());
	}
	
	public Vector normalise() {
		double l = length();
		
		if (l == 0)
			return this;
		
		x /= l;
		y /= l;
		return this;
	}
	
	public double distance(Vector v) {
		return GMath.distance(this, v);
	}
	
	public double dot(Vector a) {
		return (x * a.getX()) + (y * a.getY());
	}
	
	public double dotProduct(Vector a) {
		return (length() * GMath.cos(Math.abs(a.direction() - direction())));
	}
	
	public Vector projectOntoUnit(Vector b) {
		double dp = b.dot(this);
		return new Vector(dp * b.getX(), dp * b.getY());
	}
	
	public Vector nearest(Vector v1, Vector v2) {
		// return the nearest Vector (v1 or v2) form this Vector
		if (distance(v1) < distance(v2))
			return v1;
		
		return v2;
	}
	
	public static Vector nearest(Vector point, Vector v1, Vector v2) {
		// return the nearest Vector (v1 or v2) form "point"
		if (point.distance(v1) < point.distance(v2))
			return v1;
		
		return v2;
	}
	
	public void draw(Graphics g, double r) {
		g.drawOval((int)(x - r), (int)(y - r), (int)(r * 2), (int)(r * 2));
	}
}
