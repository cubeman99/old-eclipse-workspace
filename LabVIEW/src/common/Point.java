package common;

public class Point {
	public int x;
	public int y;
	
	
	public Point() {
		this(0, 0);
	}
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Point(Point p) {
		this(p.x, p.y);
	}
	
	public Point(java.awt.Point p) {
		this(p.x, p.y);
	}
	
	
	public Point set(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Point set(Point p) {
		return set(p.x, p.y);
	}
	
	public Point set(java.awt.Point p) {
		return set(p.x, p.y);
	}
	
	public Point zero() {
		return set(0, 0);
	}
	
	public Point add(int x, int y) {
		this.x += x;
		this.y += y;
		return this;
	}
	
	public Point add(Point p) {
		return add(p.x, p.y);
	}
	
	public Point sub(int x, int y) {
		this.x -= x;
		this.y -= y;
		return this;
	}
	
	public Point sub(Point p) {
		return sub(p.x, p.y);
	}
	
	public Point plus(int x, int y) {
		return new Point(this.x + x, this.y + y);
	}
	
	public Point plus(Point p) {
		return new Point(x + p.x, y + p.y);
	}
	
	public Point minus(int x, int y) {
		return new Point(this.x - x, this.y - y);
	}
	
	public Point minus(Point p) {
		return new Point(x - p.x, y - p.y);
	}
	
	public boolean equals(Point p) {
		return (x == p.x && y == p.y);
	}
	
	@Override
	public String toString() {
		return ("(" + x + ", " + y + ")");
	}
	
}
