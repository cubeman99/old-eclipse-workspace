package com.base.engine.common;





/**
 * A class to represent a Point that has
 * two components: x and y.
 * 
 * @author David Jordan
 */
public class Point {
	public static final Point ORIGIN = new Point();
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

	public Point(java.awt.Point p) {
		this.x = p.x;
		this.y = p.y;
	}

	public Point(Point copy) {
		this.x = copy.x;
		this.y = copy.y;
	}

	public Point(Vector2f v) {
		this.x = (int) v.x;
		this.y = (int) v.y;
	}



	// =================== ACCESSORS =================== //

	public Point plus(Point p) {
		return new Point(x + p.x, y + p.y);
	}

	public Point plus(int x, int y) {
		return new Point(this.x + x, this.y + y);
	}

	public Point plus(int amount) {
		return new Point(x + amount, y + amount);
	}

	public Point minus(Point p) {
		return new Point(x - p.x, y - p.y);
	}

	public Point minus(int x, int y) {
		return new Point(this.x - x, this.y - y);
	}

	public Point minus(int amount) {
		return new Point(x - amount, y - amount);
	}

	public Point times(Point p) {
		return new Point(x * p.x, y * p.y);
	}

	public Point times(int x, int y) {
		return new Point(this.x * x, this.y * y);
	}

	public Point times(int amount) {
		return new Point(x * amount, y * amount);
	}

	public Point dividedBy(Point p) {
		return new Point(x / p.x, y / p.y);
	}

	public Point dividedBy(int x, int y) {
		return new Point(this.x / x, this.y / y);
	}

	public Point dividedBy(int amount) {
		return new Point(x / amount, y / amount);
	}

	public Point wrapped(int x, int y, int xWrap, int yWrap) {
		return new Point(GMath.mod(x, xWrap), GMath.mod(y, yWrap));
	}

	public Point mod(int x, int y) {
		return new Point(this.x % x, this.y % y);
	}

	public Point mod(Point p) {
		return new Point(x % p.x, y % p.y);
	}

	public Point inverse() {
		return new Point(-x, -y);
	}
	
	// TODO
//	public Point getAdjacent(int dir) {
//		return new Point(this).add(Direction.getDirPoint(dir));
//	}
//	
//	public boolean isAdjacentTo(Point p) {
//		return ((x == p.x || y == p.y) && Math.abs(x - p.x)
//				+ Math.abs(y - p.y) == 1);
//	}
	
	public double distanceTo(Point p) {
		return GMath.distance(p.x - x, p.y - y);
	}
	
	public boolean equals(Point p) {
		return (this.x == p.x && this.y == p.y);
	}
	
	public boolean equals(int x, int y) {
		return (this.x == x && this.y == y);
	}



	// ==================== MUTATORS ==================== //

	public Point set(Point pos) {
		this.x = pos.x;
		this.y = pos.y;
		return this;
	}
	
	public Point set(Vector2f v) {
		this.x = (int) v.x;
		this.y = (int) v.y;
		return this;
	}

	public Point set(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public Point add(Point p) {
		this.x += p.x;
		this.y += p.y;
		return this;
	}

	public Point add(int x, int y) {
		this.x += x;
		this.y += y;
		return this;
	}

	public Point sub(Point p) {
		this.x -= p.x;
		this.y -= p.y;
		return this;
	}

	public Point sub(int x, int y) {
		this.x -= x;
		this.y -= y;
		return this;
	}

	public Point multiply(Point p) {
		this.x *= p.x;
		this.y *= p.y;
		return this;
	}

	public Point multiply(int x, int y) {
		this.x *= x;
		this.y *= y;
		return this;
	}

	public Point multiply(int amount) {
		x *= amount;
		y *= amount;
		return this;
	}

	public Point negate() {
		x = -x;
		y = -y;
		return this;
	}
	
	
	
	// ================ INHERITED METHODS ================ //
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj instanceof Point)
			return equals((Point) obj);
		return false;
	}
	
	
	@Override
	/** Return a string containing the x and y components of this vector. **/
	public String toString() {
		return ("(" + x + ", " + y + ")");
	}
}
