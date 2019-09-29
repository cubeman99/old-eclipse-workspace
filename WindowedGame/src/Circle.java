import java.awt.Graphics;


public class Circle {
	public Vector center;
	public float radius;
	
	public Circle(float x, float y, float r) {
		this.center = new Vector(x, y);
		this.radius = r;
	}
	
	public Circle(Vector v, float r) {
		this.center = v;
		this.radius = r;
	}
	
	public float getX() {
		return center.getX();
	}
	
	public float getY() {
		return center.getY();
	}
	
	public void setX(float x) {
		center.setX(x);
	}
	
	public void setY(float y) {
		center.setY(y);
	}
	
	public void set(float x, float y) {
		center.set(x, y);
	}
	
	public void set(Vector v) {
		center.set(v);
	}
	
	public Vector getCenter() {
		return center;
	}
	
	public float getRadius() {
		return radius;
	}
	
	public float diameter() {
		return getRadius() * 2;
	}
	
	public float circumference() {
		return (float) (getRadius() * 2 * Math.PI);
	}
	
	public float getX1() {
		return getX() - getRadius();
	}

	public float getY1() {
		return getY() - getRadius();
	}

	public float getX2() {
		return getX() + getRadius();
	}

	public float getY2() {
		return getY() + getRadius();
	}
	
	public void draw(Graphics g) {
		g.drawOval((int)getX1(), (int)getY1(), (int)diameter(), (int)diameter());
	}
	
	public boolean collisionLine(Line l) {
		Vector vecNearest = new Vector(l.getClosestPoint(getCenter()));
		
		return (vecNearest.distance(getCenter()) < getRadius());
	}
}
