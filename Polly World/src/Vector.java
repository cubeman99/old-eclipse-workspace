import java.awt.Graphics;
import java.awt.Point;


public class Vector {
	public float x;
	public float y;
	
	public Vector() {
		this.x = 0;
		this.y = 0;
	}
	
	public Vector(float x, float y) {
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
	
	public Vector setX(float newX) {
		x = newX;
		return this;
	}
	
	public Vector setY(float newY) {
		y = newY;
		return this;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public Vector set(float x, float y) {
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
	
	public float direction() {
		return Direction.direction(x, y);
	}
	
	public Vector add(Vector v) {
		x += v.getX();
		y += v.getY();
		return this;
	}
	
	public Vector add(float x, float y) {
		this.x += x;
		this.y += y;
		return this;
	}
	
	public Vector sub(Vector v) {
		x -= v.getX();
		y -= v.getY();
		return this;
	}
	
	public Vector sub(float x, float y) {
		this.x -= x;
		this.y -= y;
		return this;
	}
	
	public Vector scale(float scaler) {
		x *= scaler;
		y *= scaler;
		return this;
	}
	
	public float lengthSquared() {
		return ((x * x) + (y * y));
	}
	
	public float length() {
		return (float) Math.sqrt(lengthSquared());
	}
	
	public Vector normalise() {
		float l = length();
		
		if ( l == 0 )
			return this;
		
		x /= l;
		y /= l;
		return this;
	}
	
	public float distance(Vector v) {
		float dx = v.getX() - getX();
		float dy = v.getY() - getY();
		return (float) Math.sqrt((dx * dx) + (dy * dy));
	}
	
	public float dot(Vector a) {
		return (x * a.getX()) + (y * a.getY());
	}
	
	public Vector projectOntoUnit(Vector b) {
		float dp = b.dot(this);
		return new Vector(dp * b.getX(), dp * b.getY());
	}
	
	public Vector nearest(Vector v1, Vector v2) {
		// return the nearest vector form "point"
		if( distance(v1) < distance(v2) )
			return v1;
		
		return v2;
	}
	
	public static Vector nearest(Vector point, Vector v1, Vector v2) {
		// return the nearest vector form "point"
		if( point.distance(v1) < point.distance(v2) )
			return v1;
		
		return v2;
	}
	
	public void draw(Graphics g, float r) {
		g.drawOval((int)(x - r), (int)(y - r), (int)(r * 2), (int)(r * 2));
	}
}
