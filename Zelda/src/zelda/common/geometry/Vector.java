package zelda.common.geometry;

import zelda.common.util.GMath;



/**
 * A class to represent a Vector that has two components: x and y.
 * 
 * @author David
 */
public class Vector {
	public static final int NUM_COMPONENTS = 2;
	public static final Vector ORIGIN = new Vector();

	public double x, y;



	// ================== CONSTRUCTORS ================== //

	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector() {
		this(0, 0);
	}

	public Vector(Vector v) {
		this(v.x, v.y);
	}

	public Vector(Point p) {
		this(p.x, p.y);
	}

	public Vector(Vector v1, Vector v2) {
		this(v2.x - v1.x, v2.y - v1.y);
	}



	// =================== ACCESSORS =================== //

	/** Return this vector plus another. **/
	public Vector plus(Vector v) {
		return new Vector(x + v.x, y + v.y);
	}

	/** Return this vector plus x and y components. **/
	public Vector plus(double x, double y) {
		return new Vector(this.x + x, this.y + y);
	}

	/** Return this vector minus another. **/
	public Vector minus(Vector v) {
		return new Vector(x - v.x, y - v.y);
	}

	/** Return this vector minus x and y components. **/
	public Vector minus(double x, double y) {
		return new Vector(this.x - x, this.y - y);
	}

	/** Return this vector multiplied by another vector. **/
	public Vector times(Vector v) {
		x *= v.x;
		y *= v.y;
		return this;
	}

	/** Return this vector divided by another vector. **/
	public Vector dividedBy(Vector v) {
		x /= v.x;
		y /= v.y;
		return this;
	}

	/** Return this vector scaled by a magnitude. **/
	public Vector scaledBy(double a) {
		return new Vector(x * a, y * a);
	}

	/** Return this vector divided by a magnitude. **/
	public Vector scaledByInv(double a) {
		return new Vector(x / a, y / a);
	}

	/** Return the inverse of this vector. **/
	public Vector inverse() {
		return new Vector(-x, -y);
	}

	/** Return the length of the vector. **/
	public double length() {
		return GMath.distance(x, y);
	}

	/** Return the squared length of the vector. **/
	public double lengthSquared() {
		return ((x * x) + (y * y));
	}

	/** Return the angle of the vector in radians. **/
	public double direction() {
		return GMath.direction(this);
	}

	/** Return the angle between this and another vector. **/
	public double angleBetween(Vector b) {
		return (direction() - b.direction());
	}

	/** Return the distance between this vector and another. **/
	public double distanceTo(Vector v) {
		return GMath.distance(this, v);
	}

	/** Return the dot product of this vector and another. **/
	public double dot(Vector b) {
		return ((x * b.x) + (y * b.y));
	}

	/** return the scaler projection to another vector. { s = |a| cos(theta) } **/
	public double scalarProjection(Vector b) {
		return (length() * GMath.cos(angleBetween(b)));
	}

	/** Return the normalized vector. **/
	public Vector normalized() {
		double len = length();
		if (len > 0) {
			return new Vector(x / len, y / len);
		}
		return new Vector();
	}

	/** Return a vector with the same direction but a set length. **/
	public Vector lengthVector(double length) {
		return this.normalized().scaledBy(length);
	}

	/** Return the projection of this vector on another vector. **/
	public Vector projectionOn(Vector b) {
		return b.lengthVector(scalarProjection(b));
	}

	/** Return the rejection of this vector on another vector. **/
	public Vector rejectionOn(Vector b) {
		return new Vector(projectionOn(b), this);
	}

	/** Return the perpendicular vector of this (Used for normalized vectors). **/
	public Vector getPerpendicular() {
		return new Vector(-y, x);
	}

	/** Get the component of the given index, 0 = x, 1 = y. **/
	public double comp(int index) {
		return (index == 0 ? x : y);
	}
	
	public Vector wrapped(Vectangle bounds) {
		return new Vector(
    		bounds.getX1() + GMath.getWrappedValue(x - bounds.getX1(), bounds.getWidth()),
    		bounds.getY1() + GMath.getWrappedValue(y - bounds.getY1(), bounds.getHeight())
		);
	}

	//
	// /** Return the projection of this point onto a line. **/
	// public Vector projectionOn(Line l) {
	// return l.end1.plus(this.minus(l.end1).projectionOn(l.getVector()));
	// }
	//
	// /** Return the perpendicular distance to a line (endless). **/
	// public double distanceToLine(Line l) {
	// return minus(l.end1).rejectionOn(l.getVector()).length();
	// }
	//
	// /** Return the closest distance to a line segment. **/
	// public double distanceToSegment(Line l) {
	// Vector vecL = l.getVector();
	// double dist = GMath.min(distanceTo(l.end1), distanceTo(l.end2));
	// Vector rej = this.minus(l.end1).rejectionOn(vecL);
	//
	// Vector testInside = minus(rej);
	// // if (!vecL.getRect().contains(this.minus(l.end1).minus(rej)))
	// // return dist;
	// if ((testInside.x < l.minX() || testInside.x > l.maxX()) &&
	// !l.isVertical())
	// return dist;
	// if ((testInside.y < l.minY() || testInside.y > l.maxY()) &&
	// !l.isHorizontal())
	// return dist;
	// return GMath.min(dist, rej.length());
	// }
	//
	// /** Return a rectangle that has corners at the origin and at this
	// vector's position. **/
	// public Rectangle getRect() {
	// return new Rectangle(0, 0, x, y);
	// }

	/** Return this point snapped to a grid. **/
	public Vector getSnappedToGrid(Vector gridOffset, Vector gridScale) {
		return GMath.getSnappedToGrid(this, gridOffset, gridScale);
	}

	/** Return this point snapped to a grid. **/
	public Vector getSnappedToGrid(Vector gridOffset, double gridScale) {
		return GMath.getSnappedToGrid(this, gridOffset, gridScale);
	}

	// ==================== MUTATORS ==================== //

	/** Set the vector to zero **/
	public Vector zero() {
		this.x = 0;
		this.y = 0;
		return this;
	}

	/** Set the x and y components. **/
	public Vector set(double x, double y) {
		this.x = x;
		this.y = y;
		return this;
	}

	/** Set this vector to a copy of another vector. **/
	public Vector set(Vector v) {
		this.x = v.x;
		this.y = v.y;
		return this;
	}

	/** Set this vector from a point. **/
	public Vector set(Point p) {
		this.x = p.x;
		this.y = p.y;
		return this;
	}

	/** Set this vector from the displacement between two vectors. **/
	public Vector set(Vector v1, Vector v2) {
		this.x = v2.x - v1.x;
		this.y = v2.y - v1.y;
		return this;
	}

	/** Set the vector with an angle and length. **/
	public Vector setPolar(double length, double theta) {
		this.x = GMath.cos(theta) * length;
		this.y = -GMath.sin(theta) * length;
		return this;
	}

	/** Add another vector. **/
	public Vector add(Vector v) {
		set(x + v.x, y + v.y);
		return this;
	}

	/** Add x and y components. **/
	public Vector add(double x, double y) {
		this.x += x;
		this.y += y;
		return this;
	}

	/** Subtract another vector. **/
	public Vector sub(Vector v) {
		set(x - v.x, y - v.y);
		return this;
	}

	/** Subtract x and y components. **/
	public Vector sub(double x, double y) {
		this.x -= x;
		this.y -= y;
		return this;
	}

	/** Scale this vector by a magnitude. **/
	public Vector scale(double a) {
		this.x *= a;
		this.y *= a;
		return this;
	}

	/** Scale this vector by the inverse of a magnitude (divide by it). **/
	public Vector scaleInv(double a) {
		this.x /= a;
		this.y /= a;
		return this;
	}

	/** Scale this vector's x and y components from an anchor point. **/
	public Vector scale(Vector anchor, Vector scale) {
		x = anchor.x + ((x - anchor.x) * scale.x);
		y = anchor.y + ((y - anchor.y) * scale.y);
		return this;
	}

	/** Multiply by another vector. **/
	public Vector multiply(Vector v) {
		x *= v.x;
		y *= v.y;
		return this;
	}

	/** Divide by another vector. **/
	public Vector divide(Vector v) {
		x /= v.x;
		y /= v.y;
		return this;
	}

	/** Negate the vector components. **/
	public Vector negate() {
		x = -x;
		y = -y;
		return this;
	}

	/** Normalize the vector; make it in terms of the unit circle. **/
	public Vector normalize() {
		double len = length();
		if (len > 0) {
			x /= len;
			y /= len;
		}
		return this;
	}

	/** Set the length of the vector. **/
	public Vector setLength(double length) {
		return this.normalize().scale(length);
	}

	/** Set the direction (angle) of the vector. **/
	public Vector setDirection(double direction) {
		double length = length();
		set(GMath.cos(direction) * length, -GMath.sin(direction) * length);
		return this;
	}

	/** Set the component of the given index (0 = x, 1 = y). **/
	public Vector setComp(int index, double value) {
		if (index == 0)
			x = value;
		else
			y = value;
		return this;
	}
	
	/** Rotate the vector around an anchor-point the given number of degrees. **/
//	public Vector rotate(Vector anchor, double degrees) {
//		double dir  = GMath.direction(this, anchor);
//		double dist = distanceTo(anchor);
//		setPolar(dist, dir + degrees);
//		return this;
//	}

	/** Snap this point onto to a grid. **/
	public Vector snapToGrid(Vector gridOffset, Vector gridScale) {
		return GMath.snapToGrid(this, gridOffset, gridScale);
	}

	/** Snap this point onto to a grid. **/
	public Vector snapToGrid(Vector gridOffset, double gridScale) {
		return GMath.snapToGrid(this, gridOffset, gridScale);
	}

	public boolean advanceToward(Vector v, double speed) {
		if (Math.abs(v.x - x) > speed)
			x += Math.signum(v.x - x) * speed;
		else
			x = v.x;
		if (Math.abs(v.y - y) > speed)
			y += Math.signum(v.y - y) * speed;
		else
			y = v.y;
		return (distanceTo(v) < speed);
	}


	// ================ INHERITED METHODS ================ //

	@Override
	/** Return a string containing the x and y components of this vector. **/
	public String toString() {
		return ("(" + x + ", " + y + ")");
	}



	// ================ STATIC METHODS ================ //

	/** Return a new vector from the polar components of length and direction. **/
	public static Vector polarVector(double length, double direction) {
		return new Vector(0, length).setDirection(direction);
	}
}
