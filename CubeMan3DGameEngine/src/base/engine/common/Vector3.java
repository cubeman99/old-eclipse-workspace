package base.engine.common;




/**
 * A class to represent a Vector that has
 * two components: x and y.
 * 
 * @author David Jordan
 */
public class Vector3 {
	public static final Vector3 ORIGIN = new Vector3();
	public float x, y, z;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Vector3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3() {
		this(0, 0, 0);
	}
	
	public Vector3(Vector3 v) {
		this(v.x, v.y, v.z);
	}
	
	public Vector3(Vector3 v1, Vector3 v2) {
		this(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	/** Return this vector plus another. **/
	public Vector3 plus(Vector3 v) {
		return new Vector3(x + v.x, y + v.y, z + v.z);
	}
	
	/** Return this vector plus x and y components. **/
	public Vector3 plus(float x, float y, float z) {
		return new Vector3(this.x + x, this.y + y, this.z + z);
	}
	
	/** Return this vector minus another. **/
	public Vector3 minus(Vector3 v) {
		return new Vector3(x - v.x, y - v.y, z - v.z);
	}

	/** Return this vector minus x and y components. **/
	public Vector3 minus(float x, float y, float z) {
		return new Vector3(this.x - x, this.y - y, this.z - z);
	}
	
	/** Return this vector multiplied by another vector. **/
	public Vector3 times(Vector3 v) {
		x *= v.x;
		y *= v.y;
		z *= v.z;
		return this;
	}

	/** Return this vector divided by another vector. **/
	public Vector3 dividedBy(Vector3 v) {
		x /= v.x;
		y /= v.y;
		z /= v.z;
		return this;
	}
	

	public Vector3 cross(Vector3 r) {
		float x_ = y * r.z - z * r.y;
		float y_ = z * r.x - x * r.z;
		float z_ = x * r.y - y * r.x;
		
		return new Vector3(x_, y_, z_);
	}
	
	/** Return this vector scaled by a magnitude. **/
	public Vector3 scaledBy(float a) {
		return new Vector3(x * a, y * a, z * a);
	}

	/** Return this vector divided by a magnitude. **/
	public Vector3 scaledByInv(float a) {
		return new Vector3(x / a, y / a, z / a);
	}

	/** Return the inverse of this vector. **/
	public Vector3 inverse() {
		return new Vector3(-x, -y, -z);
	}
	
	/** Return the length of the vector. **/
	public float length() {
		return Vector3.distance(x, y, z);
	}
	
	/** Return the squared length of the vector. **/
	public float lengthSquared() {
		return ((x * x) + (y * y));
	}

	/** Return the distance between this vector and another. **/
	public float distanceTo(Vector3 v) {
		return Vector3.distance(v.x - x, v.y - y, v.z - z);
	}
	
	/** Return the dot product of this vector and another. **/
	public float dot(Vector3 b) {
		return ((x * b.x) + (y * b.y) + (z * b.z));
	}
	
	/** return the scaler projection to another vector. { s = |a| cos(theta) } **/
	public float scalarProjection(Vector3 b) {
		return dot(b.normalized()); // TODO
	}
	
	/** Return the normalized vector. **/
	public Vector3 normalized() {
		float len = length();
		if (len > 0)
    		return new Vector3(x / len, y / len, z / len);
		return new Vector3();
	}
	
	/** Return a vector with the same direction but a set length. **/
	public Vector3 lengthVector(float length) {
		return this.normalized().scaledBy(length);
	}

	/** Return the projection of this vector on another vector. **/
	public Vector3 projectionOn(Vector3 b) {
		return b.lengthVector(scalarProjection(b));
	}

	/** Return the rejection of this vector on another vector. **/
	public Vector3 rejectionOn(Vector3 b) {
		return new Vector3(projectionOn(b), this);
	}
	
	public boolean equals(float x, float y, float z) {
		return (this.x == x && this.y == y && this.z == z);
	}
	
	
	
	// ==================== MUTATORS ==================== //

	/** Set the vector to zero **/
	public Vector3 zero() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
		return this;
	}
	
	/** Set the x and y components. **/
	public Vector3 set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	/** Set this vector to a copy of another vector. **/
	public Vector3 set(Vector3 v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
		return this;
	}
	
	/** Set this vector from the displacement between two vectors. **/
	public Vector3 set(Vector3 v1, Vector3 v2) {
		this.x = v2.x - v1.x;
		this.y = v2.y - v1.y;
		this.z = v2.z - v1.z;
		return this;
	}
	
	/** Add another vector. **/
	public Vector3 add(Vector3 v) {
		set(x + v.x, y + v.y, z + v.z);
		return this;
	}
	
	/** Add x and y components. **/
	public Vector3 add(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}
	
	/** Subtract another vector. **/
	public Vector3 sub(Vector3 v) {
		set(x - v.x, y - v.y, z - v.z);
		return this;
	}
	
	/** Subtract x and y components. **/
	public Vector3 sub(float x, float y, float z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}

	/** Scale this vector by a magnitude. **/
	public Vector3 scale(float a) {
		this.x *= a;
		this.y *= a;
		this.z *= a;
		return this;
	}
	
	/** Scale this vector by the inverse of a magnitude (divide by it). **/
	public Vector3 scaleInv(float a) {
		this.x /= a;
		this.y /= a;
		this.z /= a;
		return this;
	}
	
	/** Multiply by another vector. **/
	public Vector3 multiply(Vector3 v) {
		x *= v.x;
		y *= v.y;
		z *= v.z;
		return this;
	}

	/** Divide by another vector. **/
	public Vector3 divide(Vector3 v) {
		x /= v.x;
		y /= v.y;
		z /= v.z;
		return this;
	}
	
	/** Negate the vector components. **/
	public Vector3 negate() {
		x = -x;
		y = -y;
		z = -z;
		return this;
	}
	
	/** Normalize the vector; make it in terms of the unit circle. **/
	public Vector3 normalize() {
		float len = length();
		if (len > 0) {
    		x /= len;
    		y /= len;
    		z /= len;
		}
		return this;
	}

	/** Set the length of the vector. **/
	public Vector3 setLength(float length) {
		return normalize().scale(length);
	}
	
	/** Rotate the vector around an axis. **/
	public Vector3 rotate(float angle, Vector3 axis) {
		float sinAngle = (float) Math.sin(-angle);
		float cosAngle = (float) Math.cos(-angle);

		return this.cross(axis.scale(sinAngle)).add(              // Rotation on local X
				(this.scale(cosAngle)).add(                       // Rotation on local Z
				axis.scale(this.dot(axis.scale(1 - cosAngle))))); // Rotation on local Y
	}
	
	/** Apply a quaternion rotation**/
	public Vector3 rotate(Quaternion rotation)
	{
		Quaternion conjugate = rotation.conjugate();

		Quaternion w = rotation.times(this).times(conjugate);

		return new Vector3(w.getX(), w.getY(), w.getZ());
	}
	
	
	
	// ================ INHERITED METHODS ================ //
	
	@Override
	/** Return a string containing the x and y components of this vector. **/
	public String toString() {
		return ("(" + x + ", " + y + ", " + z + ")");
	}
	
	
	
	//================ STATIC METHODS ================ //
	
	public static float distance(float x, float y, float z) {
		return (float) GMath.sqrt((x * x) + (y * y) + (z * z));
	}
}
