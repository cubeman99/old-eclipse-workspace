package cmg.math.geometry;

import java.io.Serializable;
import cmg.math.GMath;



/**
 * A class to represent a Vector that has
 * two components: x and y.
 * 
 * @author David Jordan
 */
public class Vec3 implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final Vec3 ORIGIN = new Vec3();
	public double x, y, z;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Vec3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vec3() {
		this(0, 0, 0);
	}
	
	public Vec3(Vec3 v) {
		this(v.x, v.y, v.z);
	}
	
	public Vec3(Vec3 v1, Vec3 v2) {
		this(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	/** Return this vector plus another. **/
	public Vec3 plus(Vec3 v) {
		return new Vec3(x + v.x, y + v.y, z + v.z);
	}
	
	/** Return this vector plus x and y components. **/
	public Vec3 plus(double x, double y, double z) {
		return new Vec3(this.x + x, this.y + y, this.z + z);
	}
	
	/** Return this vector minus another. **/
	public Vec3 minus(Vec3 v) {
		return new Vec3(x - v.x, y - v.y, z - v.z);
	}

	/** Return this vector minus x and y components. **/
	public Vec3 minus(double x, double y, double z) {
		return new Vec3(this.x - x, this.y - y, this.z - z);
	}
	
	/** Return this vector multiplied by another vector. **/
	public Vec3 times(Vec3 v) {
		x *= v.x;
		y *= v.y;
		z *= v.z;
		return this;
	}

	/** Return this vector divided by another vector. **/
	public Vec3 dividedBy(Vec3 v) {
		x /= v.x;
		y /= v.y;
		z /= v.z;
		return this;
	}

	/** Return this vector scaled by a magnitude. **/
	public Vec3 scaledBy(double a) {
		return new Vec3(x * a, y * a, z * a);
	}

	/** Return this vector divided by a magnitude. **/
	public Vec3 scaledByInv(double a) {
		return new Vec3(x / a, y / a, z / a);
	}

	/** Return the inverse of this vector. **/
	public Vec3 inverse() {
		return new Vec3(-x, -y, -z);
	}
	
	/** Return the length of the vector. **/
	public double length() {
		return Vec3.distance(x, y, z);
	}
	
	/** Return the squared length of the vector. **/
	public double lengthSquared() {
		return ((x * x) + (y * y));
	}

	/** Return the distance between this vector and another. **/
	public double distanceTo(Vec3 v) {
		return Vec3.distance(v.x - x, v.y - y, v.z - z);
	}
	
	/** Return the dot product of this vector and another. **/
	public double dot(Vec3 b) {
		return ((x * b.x) + (y * b.y) + (z * b.z));
	}
	
	/** return the scaler projection to another vector. { s = |a| cos(theta) } **/
	public double scalarProjection(Vec3 b) {
		return dot(b.normalized()); // TODO
	}
	
	/** Return the normalized vector. **/
	public Vec3 normalized() {
		double len = length();
		if (len > 0)
    		return new Vec3(x / len, y / len, z / len);
		return new Vec3();
	}
	
	/** Return a vector with the same direction but a set length. **/
	public Vec3 lengthVector(double length) {
		return this.normalized().scaledBy(length);
	}

	/** Return the projection of this vector on another vector. **/
	public Vec3 projectionOn(Vec3 b) {
		return b.lengthVector(scalarProjection(b));
	}

	/** Return the rejection of this vector on another vector. **/
	public Vec3 rejectionOn(Vec3 b) {
		return new Vec3(projectionOn(b), this);
	}
	
	
	
	// ==================== MUTATORS ==================== //

	/** Set the vector to zero **/
	public Vec3 zero() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
		return this;
	}
	
	/** Set the x and y components. **/
	public Vec3 set(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	/** Set this vector to a copy of another vector. **/
	public Vec3 set(Vec3 v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
		return this;
	}
	
	/** Set this vector from the displacement between two vectors. **/
	public Vec3 set(Vec3 v1, Vec3 v2) {
		this.x = v2.x - v1.x;
		this.y = v2.y - v1.y;
		this.z = v2.z - v1.z;
		return this;
	}
	
	/** Add another vector. **/
	public Vec3 add(Vec3 v) {
		set(x + v.x, y + v.y, z + v.z);
		return this;
	}
	
	/** Add x and y components. **/
	public Vec3 add(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}
	
	/** Subtract another vector. **/
	public Vec3 sub(Vec3 v) {
		set(x - v.x, y - v.y, z - v.z);
		return this;
	}
	
	/** Subtract x and y components. **/
	public Vec3 sub(double x, double y, double z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}

	/** Scale this vector by a magnitude. **/
	public Vec3 scale(double a) {
		this.x *= a;
		this.y *= a;
		this.z *= a;
		return this;
	}
	
	/** Scale this vector by the inverse of a magnitude (divide by it). **/
	public Vec3 scaleInv(double a) {
		this.x /= a;
		this.y /= a;
		this.z /= a;
		return this;
	}
	
	/** Multiply by another vector. **/
	public Vec3 multiply(Vec3 v) {
		x *= v.x;
		y *= v.y;
		z *= v.z;
		return this;
	}

	/** Divide by another vector. **/
	public Vec3 divide(Vec3 v) {
		x /= v.x;
		y /= v.y;
		z /= v.z;
		return this;
	}
	
	/** Negate the vector components. **/
	public Vec3 negate() {
		x = -x;
		y = -y;
		z = -z;
		return this;
	}
	
	/** Normalize the vector; make it in terms of the unit circle. **/
	public Vec3 normalize() {
		double len = length();
		if (len > 0) {
    		x /= len;
    		y /= len;
    		z /= len;
		}
		return this;
	}

	/** Set the length of the vector. **/
	public Vec3 setLength(double length) {
		return normalize().scale(length);
	}
	
	
	
	// ================ INHERITED METHODS ================ //

	@Override
	/** Return a string containing the x and y components of this vector. **/
	public String toString() {
		return ("(" + x + ", " + y + ", " + z + ")");
	}
	
	
	
	//================ STATIC METHODS ================ //
	
	public static double distance(double x, double y, double z) {
		return GMath.sqrt((x * x) + (y * y) + (z * z));
	}
}
