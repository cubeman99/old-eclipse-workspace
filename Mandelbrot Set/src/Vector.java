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
	
	public void setX(float newX) {
		x = newX;
	}
	
	public void setY(float newY) {
		y = newY;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void set(Vector v) {
		this.x = v.getX();
		this.y = v.getY();
	}
	
	public void negate() {
		x *= -1;
		y *= -1;
	}
	
	public float getDir() {
		if( getX() != 0 )
			return (float) Math.tanh((double) (getY() / getX()));
		return 0;
	}
	
	public void add(Vector v) {
		x += v.getX();
		y += v.getY();
	}
	
	public void add(float x, float y) {
		this.x += x;
		this.y += y;
	}
	
	public void sub(Vector v) {
		x -= v.getX();
		y -= v.getY();
	}
	
	public void sub(float x, float y) {
		this.x -= x;
		this.y -= y;
	}
	
	public void scale(float scaler) {
		x *= scaler;
		y *= scaler;
	}
	
	public float lengthSquared() {
		return ((x * x) + (y * y));
	}
	
	public float length() {
		return (float) Math.sqrt(lengthSquared());
	}
	
	public void normalise() {
		float l = length();
		
		if ( l == 0 )
			return;
		
		x /= l;
		y /= l;
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
	
	public void draw(Graphics g, float r) {
		g.drawOval((int)(x - r), (int)(y - r), (int)(r * 2), (int)(r * 2));
	}
}
