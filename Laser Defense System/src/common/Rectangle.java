package common;

import common.GMath;
import common.Vector;


/**
 * Extension of Shape:
 * A class to represent a rectangle
 * with two corner Vectors.
 * 
 * @author David Jordan
 */
public class Rectangle extends Shape {
	public Vector end1;
	public Vector end2;
	

	// ================= CONSTRUCTORS ================= //

	public Rectangle() {
		this(0, 0, 0, 0);
	}
	
	public Rectangle(double x1, double y1, double x2, double y2) {
		this.end1 = new Vector(x1, y1);
		this.end2 = new Vector(x2, y2);
	}
	
	public Rectangle(Vector end1, Vector end2) {
		this.end1 = new Vector(end1);
		this.end2 = new Vector(end2);
	}
	
	public Rectangle(Vector end2) {
		this(0, 0, end2.x, end2.y);
	}
	
	public Rectangle(Line l) {
		this(l.end1, l.end2);
	}
	
	public Rectangle(Rectangle copy) {
		this(copy.end1, copy.end2);
	}
	

	
	// ================== ACCESSORS ================== //

	/** Return the x component for the first end point. **/
	public double x1() {
		return end1.x;
	}

	/** Return the y component for the first end point. **/
	public double y1() {
		return end1.y;
	}

	/** Return the x component for the first end point. **/
	public double x2() {
		return end2.x;
	}

	/** Return the y component for the second end point. **/
	public double y2() {
		return end2.y;
	}

	/** Return the minimum x value of the two end points. **/
	public double minX() {
		return GMath.min(x1(), x2());
	}

	/** Return the minimum y value of the two end points. **/
	public double minY() {
		return GMath.min(y1(), y2());
	}

	/** Return the maximum x value of the two end points. **/
	public double maxX() {
		return GMath.max(x1(), x2());
	}

	/** Return the maximum y value of the two end points. **/
	public double maxY() {
		return GMath.max(y1(), y2());
	}

	/** Return width of the rectangle (can be negative!). **/
	public double width() {
		return (end2.x - end1.x);
	}

	/** Return height of the rectangle (can be negative!). **/
	public double height() {
		return (end2.y - end1.y);
	}
	
	/** Return a vector containing the dimensions of this rectangle. **/
	public Vector getSize() {
		return end2.minus(end1);
	}
	
	/** Return if a point is inside this rectangle.**/
	public boolean contains(Vector point) {
		return (point.x >= minX() && point.y >= minY() && point.x < maxX() && point.y < maxY());
	}
	
	/** Return if another rectangle is inside this rectangle.**/
	public boolean contains(Rectangle r) {
		return (r.minX() >= minX() && r.minY() >= minY() && r.maxX() < maxX() && r.maxY() < maxY());
	}

	/** Return true if this and another rectangle are overlapping. **/
	public boolean touching(Rectangle r) {
		return testOverlap(this, r);
		
	}
	
	/** Return a copy of this rectangle but with sorted ends. **/
	public Rectangle getSorted() {
		return new Rectangle(this).sortEnds();
	}
	
	
	// ================== MUTATORS ================== //
	
	/** Set the x and y components for each end point of the rectangle. **/
	public Rectangle set(double x1, double y1, double x2, double y2) {
		end1 = new Vector(x1, y1);
		end2 = new Vector(x2, y2);
		return this;
	}

	/** Set the ends of this rectangle with two vectors. **/
	public Rectangle set(Vector end1, Vector end2) {
		end1 = new Vector(end1);
		end2 = new Vector(end2);
		return this;
	}
	
	/** Set this rectangle as the end points of a line. **/
	public Rectangle set(Line l) {
		end1 = new Vector(l.end1);
		end2 = new Vector(l.end2);
		return this;
	}
	
	/** Set this rectangle as a copy of another rectangle. **/
	public Rectangle set(Rectangle r) {
		end1 = new Vector(r.end1);
		end2 = new Vector(r.end2);
		return this;
	}
	
	/** Swap end1 with end2 and vice versa. **/
	public Rectangle swapEnds() {
		Vector temp = new Vector(end1);
		end1.set(end2);
		end2.set(temp);
		return this;
	}
	
	/** Grow the rectangle by a given amount. **/
	public Rectangle grow(double amount) {
		end1.x -= amount / 2;
		end1.y -= amount / 2;
		end2.x += amount / 2;
		end2.y += amount / 2;
		return this;
	}
	
	/** Put the lower corner as end1 and the upper corner as end2. **/
	public Rectangle sortEnds() {
		Vector e1 = new Vector(minX(), minY());
		Vector e2 = new Vector(maxX(), maxY());
		end1.set(e1);
		end2.set(e2);
		return this;
	}
	
	/** Return a copy of this rectangle. **/
	public Rectangle getCopy() {
		return new Rectangle(this);
	}
	
	
	
	// ================= STATIC METHODS ================= //
	
	/** Return true if two rectangles are overlapping. **/
	public static boolean testOverlap(Rectangle a, Rectangle b) {
		if (b.x1() - a.x2() > 0.0f || b.y1() - a.y2() > 0.0f)
			return false;
		if (a.x1() - b.x2() > 0.0f || a.y1() - b.y2() > 0.0f)
			return false;
		return true;
	}
}
