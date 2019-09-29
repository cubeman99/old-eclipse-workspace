package common;

public class Vector {
	public static final Vector ORIGIN = new Vector();
	
	public double x, y;
	
	
	// CONSTRUCTORS:
	
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
	
	public Vector(Vector v1, Vector v2) {
		this(v2.x - v1.x, v2.y - v1.y);
	}
	
	
	// ACCESSORS / CALCULATIONS:
	
	/** Return this vector plus another. **/
	public Vector plus(Vector v) {
		return new Vector(x + v.x, y + v.y);
	}
	
	/** Return this vector minus another. **/
	public Vector minus(Vector v) {
		return new Vector(x - v.x, y - v.y);
	}

	/** Return this vector times a scaler. **/
	public Vector scaledBy(double a) {
		return new Vector(x * a, y * a);
	}

	/** Return the inverse of this vector. **/
	public Vector inverse() {
		return new Vector(-x, -y);
	}
	
	/** Return the length of the vector. **/
	public double length() {
		return GMath.distance(x, y);
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
		return ((x + b.y) + (y * b.y));
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

	/** Return the projection of this vector on b **/
	public Vector projectionOn(Vector b) {
		return b.lengthVector(scalarProjection(b));
	}

	/** Return the rejection of this vector on b **/
	public Vector rejectionOn(Vector b) {
		return new Vector(projectionOn(b), this);
	}

	/** Return the perpendicular vector of this (Used for normalized vectors). **/
	public Vector getPerpendicular() {
		return new Vector(-y, x);
		//return new Vector(this).setDirection(direction() + GMath.HALF_PI);
	}
	
	
	// MUTATORS:

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
	
	/** Set the vector with an angle and length **/
	public Vector setPolar(double length, double theta) {
		this.x =  GMath.cos(theta) * length;
		this.y = -GMath.sin(theta) * length;
		return this;
	}
	
	/** Set this vector to a copy of another vector. **/
	public Vector set(Vector v) {
		this.x = v.x;
		this.y = v.y;
		return this;
	}
	
	/** Add another vector. **/
	public Vector add(Vector v) {
		set(x + v.x, y + v.y);
		return this;
	}
	
	/** Subtract another vector. **/
	public Vector sub(Vector v) {
		set(x - v.x, y - v.y);
		return this;
	}

	/** Scale this vector by a magnitude. **/
	public Vector scale(double a) {
		set(x * a, y * a);
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
	
	public Vector setLength(double length) {
		return this.normalize().scale(length);
	}
	
	public Vector setDirection(double direction) {
		double length = length();
		set(GMath.cos(direction) * length, -GMath.sin(direction) * length);
		return this;
	}
	
	public Vector reflectOverDirection(double direction, double friction, double restitution) {
		double diff = direction() - direction;
		setDirection(diff);
		x = -x;
		y = -y;
		x *= (1.0 - friction);
		y *= restitution;
		setDirection(-diff);
		return this;
	}
	
	public Vector reflectOverDirection(double direction) {
		Vector reflectionAxis = new Vector(1, 0).setDirection(direction + GMath.HALF_PI);
		Vector projection     = rejectionOn(reflectionAxis);
		sub(projection.scale(2.0));
		return this;
	}
	
	public Vector clip(Vector lower, Vector upper) {
		x = GMath.max(lower.x, GMath.min(upper.x, x));
		y = GMath.max(lower.y, GMath.min(upper.y, y));
		return this;
	}
	
	public boolean equals(Vector v) {
		return (this.x == v.x && this.y == v.y);
	}

	@Override
	public String toString() {
		return ("(" + x + ", " + y + ")");
	}
	
	
	// STATIC METHODS:
	
	public static Vector polarVector(double length, double direction) {
		return new Vector(0, length).setDirection(direction);
	}
}
