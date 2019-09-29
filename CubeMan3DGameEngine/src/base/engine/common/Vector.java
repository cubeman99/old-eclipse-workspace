package base.engine.common;

import java.io.Serializable;


/**
 * A class to represent a Vector that has
 * two components: x and y.
 * 
 * @author David Jordan
 */
public class Vector implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final Vector ORIGIN = new Vector();
	public float x, y;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Vector(float x, float y) {
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
	public Vector plus(float x, float y) {
		return new Vector(this.x + x, this.y + y);
	}
	
	/** Return this vector minus another. **/
	public Vector minus(Vector v) {
		return new Vector(x - v.x, y - v.y);
	}

	/** Return this vector minus x and y components. **/
	public Vector minus(float x, float y) {
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
	public Vector scaledBy(float a) {
		return new Vector(x * a, y * a);
	}

	/** Return this vector divided by a magnitude. **/
	public Vector scaledByInv(float a) {
		return new Vector(x / a, y / a);
	}

	/** Return the inverse of this vector. **/
	public Vector inverse() {
		return new Vector(-x, -y);
	}
	
	/** Return the length of the vector. **/
	public float length() {
		return (float) GMath.distance(x, y);
	}
	
	/** Return the squared length of the vector. **/
	public float lengthSquared() {
		return ((x * x) + (y * y));
	}
	
	/** Return the angle of the vector in radians. **/
	public float direction() {
		return GMath.direction(this);
	}

	/** Return the angle between this and another vector. **/
	public float angleBetween(Vector b) {
		return (direction() - b.direction());
	}

	/** Return the distance between this vector and another. **/
	public float distanceTo(Vector v) {
		return GMath.distance(this, v);
	}
	
	/** Return the dot product of this vector and another. **/
	public float dot(Vector b) {
		return ((x * b.x) + (y * b.y));
	}
	
	/** return the scaler projection to another vector. { s = |a| cos(theta) } **/
	public float scalarProjection(Vector b) {
		return (length() * GMath.cos(angleBetween(b)));
	}
	
	/** return the scaler rejection to another vector. { s = |a| sin(theta) } **/
	public float scalarRejection(Vector b) {
		return (length() * GMath.sin(angleBetween(b)));
	}
	
	/** Return the normalized vector. **/
	public Vector normalized() {
		float len = length();
		if (len > 0) {
    		return new Vector(x / len, y / len);
		}
		return new Vector();
	}
	
	/** Return a vector with the same direction but a set length. **/
	public Vector lengthVector(float length) {
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
	
	/** Return the projection of this point onto a line. **/
//	public Vector projectionOn(Line l) {
//		return l.end1.plus(this.minus(l.end1).projectionOn(l.getVector()));
//	}
	
	/** Return the perpendicular vector of this (Used for normalized vectors). **/
	public Vector getPerpendicular() {
		return new Vector(-y, x);
	}
	
	/** Return the perpendicular distance to a line (endless). **/
//	public float distanceToLine(Line l) {
//		return minus(l.end1).rejectionOn(l.getVector()).length();
//	}

	/** Return the closest distance to a line segment. **/
	/*public float distanceToSegment(Line l) {
		Vector vecL = l.getVector();
		float dist = GMath.min(distanceTo(l.end1), distanceTo(l.end2));
		Vector rej  = this.minus(l.end1).rejectionOn(vecL);
		
		Vector testInside = minus(rej);
//		if (!vecL.getRect().contains(this.minus(l.end1).minus(rej))) 
//			return dist;
		if ((testInside.x < l.minX() || testInside.x > l.maxX()) && !l.isVertical()) 
			return dist;
		if ((testInside.y < l.minY() || testInside.y > l.maxY()) && !l.isHorizontal()) 
			return dist;
		return GMath.min(dist, rej.length());
	}*/
	
	/** Return a rectangle that has corners at the origin and at this vector's position. **/
//	public Rectangle getRect() {
//		return new Rectangle(0, 0, x, y);
//	}
	
	
	
	// ==================== MUTATORS ==================== //

	/** Set the vector to zero **/
	public Vector zero() {
		this.x = 0;
		this.y = 0;
		return this;
	}
	
	/** Set the x and y components. **/
	public Vector set(float x, float y) {
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
	public Vector setPolar(float length, float theta) {
		this.x =  GMath.cos(theta) * length;
		this.y = -GMath.sin(theta) * length;
		return this;
	}
	
	/** Add another vector. **/
	public Vector add(Vector v) {
		set(x + v.x, y + v.y);
		return this;
	}
	
	/** Add x and y components. **/
	public Vector add(float x, float y) {
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
	public Vector sub(float x, float y) {
		this.x -= x;
		this.y -= y;
		return this;
	}

	/** Scale this vector by a magnitude. **/
	public Vector scale(float a) {
		this.x *= a;
		this.y *= a;
		return this;
	}
	
	/** Scale this vector by the inverse of a magnitude (divide by it). **/
	public Vector scaleInv(float a) {
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
		float len = length();
		if (len > 0) {
    		x /= len;
    		y /= len;
		}
		return this;
	}

	/** Set the length of the vector. **/
	public Vector setLength(float length) {
		return this.normalize().scale(length);
	}

	/** Set the direction (angle) of the vector. **/
	public Vector setDirection(float direction) {
		float length = length();
		set(GMath.cos(direction) * length, -GMath.sin(direction) * length);
		return this;
	}
	
	
	
	// ================ INHERITED METHODS ================ //

	@Override
	/** Return a string containing the x and y components of this vector. **/
	public String toString() {
		return ("(" + x + ", " + y + ")");
	}
	
	
	
	//================ STATIC METHODS ================ //
	
	/** Return a new vector from the polar components of length and direction. **/
	public static Vector polarVector(float length, float direction) {
		return new Vector(0, length).setDirection(direction);
	}
}
