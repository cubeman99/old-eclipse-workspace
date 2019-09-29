package common;


/**
 * A class to represent a Point on the
 * coordinate plane that has two integer
 * components: x and y.
 * 
 * @author David Jordan
 */
public class Point {
	public static final Point ORIGIN = new Point(0, 0);
	public int x, y;


	// ================== CONSTRUCTORS ================== //

	/** Construct a point with the given x and y components. **/
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/** Construct a point at the origin (0, 0). **/
	public Point() {
		this(0, 0);
	}

	/** Construct a point as a copy of another point. **/
	public Point(Point p) {
		this(p.x, p.y);
	}
	
	

	// =================== ACCESSORS =================== //

	/** Return this vector plus another. **/
	public Point plus(Point p) {
		return new Point(x + p.x, y + p.y);
	}

	/** Return this vector plus x and y components. **/
	public Point plus(int x, int y) {
		return new Point(this.x + x, this.y + y);
	}

	/** Return this vector minus another. **/
	public Point minus(Point p) {
		return new Point(x - p.x, y - p.y);
	}

	/** Return this vector minus x and y components. **/
	public Point minus(int x, int y) {
		return new Point(this.x - x, this.y - y);
	}

	/** Return the distance between this point and another. **/
	public double distanceTo(Point p) {
		return GMath.distance(p.x - x, p.y - y);
	}



	// ==================== MUTATORS ==================== //

	/** Set the point to the origin. **/
	public Point zero() {
		this.x = 0;
		this.y = 0;
		return this;
	}

	/** Set the x and y components. **/
	public Point set(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}

	/** Set this vector to a copy of another vector. **/
	public Point set(Point p) {
		this.x = p.x;
		this.y = p.y;
		return this;
	}

	/** Add another point. **/
	public Point add(Point p) {
		this.x += p.x;
		this.y += p.y;
		return this;
	}

	/** Add x and y components. **/
	public Point add(int x, int y) {
		this.x += x;
		this.y += y;
		return this;
	}

	/** Subtract another vector. **/
	public Point sub(Point p) {
		this.x -= p.x;
		this.y -= p.y;
		return this;
	}

	/** Subtract x and y components. **/
	public Point sub(int x, int y) {
		this.x -= x;
		this.y -= y;
		return this;
	}

	/** Scale this vector by a magnitude. **/
	public Point scale(int a) {
		set(x * a, y * a);
		return this;
	}

	/** Negate the vector components. **/
	public Point negate() {
		x = -x;
		y = -y;
		return this;
	}



	// =============== INHERITED METHODS =============== //

	@Override
	public String toString() {
		return ("(" + x + ", " + y + ")");
	}



	//================ STATIC METHODS ================ //

	public static Vector polarVector(double length, double direction) {
		return new Vector(0, length).setDirection(direction);
	}
}
