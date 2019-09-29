package common;

public class Point {
	public int x;
	public int y;
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Point() {
		this.x = 0;
		this.y = 0;
	}
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Point(Point p) {
		this.x = p.x;
		this.y = p.y;
	}
	
	public Point(java.awt.Point p) {
		this.x = p.x;
		this.y = p.y;
	}
	
	
	
	// =================== ACCESSORS =================== //
	
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
	
	
	
	// ==================== MUTATORS ==================== //
	
	public Point set(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Point set(Point p) {
		this.x = p.x;
		this.y = p.y;
		return this;
	}
	
	public Point zero() {
		this.x = 0;
		this.y = 0;
		return this;
	}
	
	public Point add(int x, int y) {
		this.x += x;
		this.y += y;
		return this;
	}
	
	public Point add(Point p) {
		this.x += p.x;
		this.y += p.y;
		return this;
	}
	
	public Point sub(int x, int y) {
		this.x -= x;
		this.y -= y;
		return this;
	}
	
	public Point sub(Point p) {
		this.x -= p.x;
		this.y -= p.y;
		return this;
	}
	
	@Override
	public String toString() {
		return ("(" + x + ", " + y + ")");
	}
}
