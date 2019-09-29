package com.base.engine.common;




public class Vector3f {
	public static final Vector3f ORIGIN = new Vector3f(0, 0, 0);
	public static final Vector3f X_AXIS = new Vector3f(1, 0, 0);
	public static final Vector3f Y_AXIS = new Vector3f(0, 1, 0);
	public static final Vector3f Z_AXIS = new Vector3f(0, 0, 1);
	
	public static final int XPOS  = 1;
	public static final int XNEG  = 2;
	public static final int YPOS  = 4;
	public static final int YNEG  = 8;
	public static final int ZPOS  = 16;
	public static final int ZNEG  = 32;
	public static final int XY = 0;
	public static final int XZ = 1;
	public static final int YX = 2;
	public static final int YZ = 3;
	public static final int ZX = 4;
	public static final int ZY = 5;
	
	public float x;
	public float y;
	public float z;
	
	
	
	// ================== CONSTRUCTORS ================== //

	public Vector3f() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	
	public Vector3f(float a) {
		this.x = a;
		this.y = a;
		this.z = a;
	}
	
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3f(Vector3f copy) {
		this.x = copy.x;
		this.y = copy.y;
		this.z = copy.z;
	}

	
	
	// =================== ACCESSORS =================== //

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}
	
	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}
	
	public float distanceTo(Vector3f v) {
		return (float) Math.sqrt(Vector3f.squaredDistance(v.x - x, v.y - y, v.z - z));
	}

	public float max() {
		return Math.max(x, Math.max(y, z));
	}
	
	public float dot(Vector3f r) {
		return x * r.getX() + y * r.getY() + z * r.getZ();
	}
	
	public Vector3f cross(Vector3f r) {
		float x_ = y * r.getZ() - z * r.getY();
		float y_ = z * r.getX() - x * r.getZ();
		float z_ = x * r.getY() - y * r.getX();
		
		return new Vector3f(x_, y_, z_);
	}
	
	public Vector3f inverse() {
		return new Vector3f(-x, -y, -z);
	}
	
	public Vector3f normalized() {
		float length = length();
		if (length == 0)
			return new Vector3f(0, 0, 1);
		return new Vector3f(x / length, y / length, z / length);
	}
	
	public Vector3f getReflected(Vector3f normal) {
		return normal.times(2 * dot(normal)).sub(this);
	}

	public Vector3f rotate(float angle, Vector3f axis) {
		float sinAngle = (float) Math.sin(-angle);
		float cosAngle = (float) Math.cos(-angle);
		
		return  cross(axis.times(sinAngle)).plus(            // Rotation on local X
				times(cosAngle).plus(                        // Rotation on local Z
				axis.times(dot(axis.times(1 - cosAngle))))); // Rotation on local Y
	}
	
	public Vector3f rotate(Quaternion rotation) {
		Quaternion conjugate = rotation.getConjugate();
		Quaternion w = rotation.times(this).times(conjugate);
		return new Vector3f(w.getX(), w.getY(), w.getZ());
	}
	
	public Vector3f plus(Vector3f r) {
		return new Vector3f(x + r.getX(), y + r.getY(), z + r.getZ());
	}
	
	public Vector3f plus(float r) {
		return new Vector3f(x + r, y + r, z + r);
	}
	
	public Vector3f plus(float dx, float dy, float dz) {
		return new Vector3f(x + dx, y + dy, z + dz);
	}

	public Vector3f minus(Vector3f r) {
		return new Vector3f(x - r.getX(), y - r.getY(), z - r.getZ());
	}
	
	public Vector3f minus(float r) {
		return new Vector3f(x - r, y - r, z - r);
	}
	
	public Vector3f times(Vector3f r) {
		return new Vector3f(x * r.getX(), y * r.getY(), z * r.getZ());
	}
	
	public Vector3f times(float r) {
		return new Vector3f(x * r, y * r, z * r);
	}
	
	public Vector3f dividedBy(Vector3f r) {
		return new Vector3f(x / r.getX(), y / r.getY(), z / r.getZ());
	}
	
	public Vector3f dividedBy(float r) {
		return new Vector3f(x / r, y / r, z / r);
	}
	
	public Vector3f abs() {
		return new Vector3f(Math.abs(x), Math.abs(y), Math.abs(z));
	}
	
	public boolean equals(Vector3f v) {
		return (x == v.x && y == v.y && z == v.z);
	}
	
	// Swizzling!
	public Vector2f getXY() { return new Vector2f(x, y); }
	public Vector2f getYX() { return new Vector2f(y, x); }
	public Vector2f getYZ() { return new Vector2f(y, z); }
	public Vector2f getZY() { return new Vector2f(z, y); }
	public Vector2f getXZ() { return new Vector2f(x, z); }
	public Vector2f getZX() { return new Vector2f(z, x); }

	public float getAxisComponent(int axis) {
		if (axis == Axis.X_AXIS)
			return x;
		if (axis == Axis.Y_AXIS)
			return y;
		if (axis == Axis.Z_AXIS)
			return z;
		return 0;
	}
	
	public Vector2f swizzle(int axis1, int axis2) {
		return new Vector2f(getAxisComponent(axis1),
							getAxisComponent(axis2));
	}
	
//	public Vector2f swizzle(int components) {
//		switch (components) {
//		case XY:
//			return getXY();
//		case XZ:
//			return getXZ();
//		case YX:
//			return getYX();
//		case YZ:
//			return getYZ();
//		case ZX:
//			return getZX();
//		case ZY:
//			return getZY();
//		}
//		return null;
//	}
	
	

	// ==================== MUTATORS ==================== //

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setZ(float z) {
		this.z = z;
	}
	
	public void setXY(Vector2f v) {this.x = v.x; this.y = v.y;}
	public void setXZ(Vector2f v) {this.x = v.x; this.z = v.y;}
	public void setYX(Vector2f v) {this.y = v.x; this.x = v.y;}
	public void setYZ(Vector2f v) {this.y = v.x; this.z = v.y;}
	public void setZX(Vector2f v) {this.z = v.x; this.x = v.y;}
	public void setZY(Vector2f v) {this.z = v.x; this.y = v.y;}
	
	public void setXY(float x, float y) {this.x = x; this.y = y;}
	public void setXZ(float x, float z) {this.x = x; this.z = z;}
	public void setYX(float y, float x) {this.y = y; this.x = x;}
	public void setYZ(float y, float z) {this.y = y; this.z = z;}
	public void setZX(float z, float x) {this.z = z; this.x = x;}
	public void setZY(float z, float y) {this.z = z; this.y = y;}
	
	/** Set the vector to the origin **/
	public Vector3f zero() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
		return this;
	}
	
	/** Set the x, y, and z components. **/
	public Vector3f set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	/** Set this vector to a copy of another vector. **/
	public Vector3f set(Vector3f v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
		return this;
	}
	
	public Vector3f setComponent(int axis, float value) {
		if (axis == Axis.X_AXIS)
			x = value;
		else if (axis == Axis.Y_AXIS)
			y = value;
		else if (axis == Axis.Z_AXIS)
			z = value;
		return this;
	}
	
	public Vector3f negate() {
		x = -x;
		y = -y;
		z = -z;
		return this;
	}
	
	public Vector3f add(Vector3f v) {
		x += v.x;
		y += v.y;
		z += v.z;
		return this;
	}
	
	public Vector3f sub(Vector3f v) {
		x -= v.x;
		y -= v.y;
		z -= v.z;
		return this;
	}
	
	public Vector3f normalize() {
		float length = length();
		x /= length;
		y /= length;
		z /= length;
		return this;
	}
	
	public Vector3f setLength(float length) {
		float oldLength = length();
		x = (x / oldLength) * length;
		y = (y / oldLength) * length;
		z = (z / oldLength) * length;
		return this;
	}
	
	
	
	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}
	
	
	
	// ================ STATIC METHODS ================ //
	
	public static float squaredDistance(Vector3f v1, Vector3f v2) {
		return squaredDistance(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
	}
	
	public static float squaredDistance(float x, float y, float z) {
		return (x * x) + (y * y) + (z * z);
	}
}
