package com.base.engine.common;


public class Quaternion {
	private float x;
	private float y;
	private float z;
	private float w;
	
	
	
	// ================== CONSTRUCTORS ================== //

	public Quaternion() {
		this(0, 0, 0, 1);
	}
	
	public Quaternion(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Quaternion(Quaternion copy) {
		this.x = copy.x;
		this.y = copy.y;
		this.z = copy.z;
		this.w = copy.w;
	}

	public Quaternion(Vector3f axis, float angle) {
		float sinHalfAngle = (float) Math.sin(angle / 2);
		float cosHalfAngle = (float) Math.cos(angle / 2);

		this.x = axis.x * sinHalfAngle;
		this.y = axis.y * sinHalfAngle;
		this.z = axis.z * sinHalfAngle;
		this.w = cosHalfAngle;
	}

	public Quaternion(Matrix4f rot) {
		set(rot);
	}
	
	

	// =================== ACCESSORS =================== //

	public float length() {
		return (float) Math.sqrt((x * x) + (y * y) + (z * z) + (w * w));
	}
	
	public float distanceTo(Quaternion q) {
		return (float) Math.sqrt(
			(x - q.x) * (x - q.x) +
			(y - q.y) * (y - q.y) + 
			(z - q.z) * (z - q.z) + 
			(w - q.w) * (w - q.w)
		);
	}
	
	public float angleTo(Quaternion q) {
		return smallestAngle(this, q);
	}
	
	public Quaternion normalized() {
		float length = length();
		
		return new Quaternion(x / length, y / length, z / length, w / length);
	}
	
	public Quaternion getConjugate() {
		return new Quaternion(-x, -y, -z, w);
	}
	
	public Quaternion times(Quaternion q) {
		float w_ = w * q.getW() - x * q.getX() - y * q.getY() - z * q.getZ();
		float x_ = x * q.getW() + w * q.getX() + y * q.getZ() - z * q.getY();
		float y_ = y * q.getW() + w * q.getY() + z * q.getX() - x * q.getZ();
		float z_ = z * q.getW() + w * q.getZ() + x * q.getY() - y * q.getX();
		
		return new Quaternion(x_, y_, z_, w_);
	}
	
	public Quaternion times(Vector3f v) {
		float w_ = -x * v.getX() - y * v.getY() - z * v.getZ();
		float x_ =  w * v.getX() + y * v.getZ() - z * v.getY();
		float y_ =  w * v.getY() + z * v.getX() - x * v.getZ();
		float z_ =  w * v.getZ() + x * v.getY() - y * v.getX();
		
		return new Quaternion(x_, y_, z_, w_);
	}

	public Quaternion times(float scalar) {
		return new Quaternion(x * scalar, y * scalar, z * scalar, w * scalar);
	}

	public Matrix4f toRotationMatrix() {
		Vector3f forward = new Vector3f(2.0f * (x*z - w*y), 2.0f * (y*z + w*x), 1.0f - 2.0f * (x*x + y*y));
		Vector3f up      = new Vector3f(2.0f * (x*y + w*z), 1.0f - 2.0f * (x*x + z*z), 2.0f * (y*z - w*x));
		Vector3f right   = new Vector3f(1.0f - 2.0f * (y*y + z*z), 2.0f * (x*y - w*z), 2.0f * (x*z + w*y));
		
		return Matrix4f.createRotation(forward, up, right);
	}

	public Quaternion plus(Quaternion r) {
		return new Quaternion(x + r.x, y + r.y, z + r.z, w + r.w);
	}

	public Quaternion minus(Quaternion r) {
		return new Quaternion(x - r.x, y - r.y, z - r.z, w - r.w);
	}
	
	public float dot(Quaternion r) {
		return ((x * r.x) + (y * r.y) + (z * r.z) + (w * r.w));
	}
	
	public Quaternion nlerp(Quaternion dest, float lerpFactor, boolean shortest) {
		Quaternion correctedDest = dest;

		if(shortest && this.dot(dest) < 0)
			correctedDest = new Quaternion(-dest.getX(), -dest.getY(), -dest.getZ(), -dest.getW());

		return correctedDest.minus(this).multiply(lerpFactor).add(this).normalize();
	}
	
	public Vector3f getForward() {
		return new Vector3f(0, 0, 1).rotate(this);
	}

	public Vector3f getBack() {
		return new Vector3f(0, 0, -1).rotate(this);
	}

	public Vector3f getUp() {
		return new Vector3f(0, 1, 0).rotate(this);
	}

	public Vector3f getDown() {
		return new Vector3f(0, -1, 0).rotate(this);
	}

	public Vector3f getRight() {
		return new Vector3f(1, 0, 0).rotate(this);
	}

	public Vector3f getLeft() {
		return new Vector3f(-1, 0, 0).rotate(this);
	}
	
	public Vector3f getXYZ() {
		return new Vector3f(x, y, z);
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public float getW() {
		return w;
	}
	
	public boolean equals(Quaternion q) {
		return (x == q.x && y == q.y && z == q.z && w == q.w);
	}
	
	

	// ==================== MUTATORS ==================== //
	
	public Quaternion normalize() {
		float length = length();
		x /= length;
		y /= length;
		z /= length;
		w /= length;
		return this;
	}
	
	public Quaternion set(Quaternion q) {
		this.x = q.x;
		this.y = q.y;
		this.z = q.z;
		this.w = q.w;
		return this;
	}
	
	public Quaternion set(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		return this;
	}

	public Quaternion set(Matrix4f rot) {
		float trace = rot.get(0, 0) + rot.get(1, 1) + rot.get(2, 2);

		if (trace > 0) {
			float s = 0.5f / (float) Math.sqrt(trace + 1.0f);
			w = 0.25f / s;
			x = (rot.get(1, 2) - rot.get(2, 1)) * s;
			y = (rot.get(2, 0) - rot.get(0, 2)) * s;
			z = (rot.get(0, 1) - rot.get(1, 0)) * s;
		}
		else {
			if (rot.get(0, 0) > rot.get(1, 1) && rot.get(0, 0) > rot.get(2, 2)) {
				float s = 2.0f * (float) Math.sqrt(1.0f + rot.get(0, 0) - rot.get(1, 1) - rot.get(2, 2));
				w = (rot.get(1, 2) - rot.get(2, 1)) / s;
				x = 0.25f * s;
				y = (rot.get(1, 0) + rot.get(0, 1)) / s;
				z = (rot.get(2, 0) + rot.get(0, 2)) / s;
			}
			else if (rot.get(1, 1) > rot.get(2, 2)) {
				float s = 2.0f * (float) Math.sqrt(1.0f + rot.get(1, 1) - rot.get(0, 0) - rot.get(2, 2));
				w = (rot.get(2, 0) - rot.get(0, 2)) / s;
				x = (rot.get(1, 0) + rot.get(0, 1)) / s;
				y = 0.25f * s;
				z = (rot.get(2, 1) + rot.get(1, 2)) / s;
			}
			else {
				float s = 2.0f * (float) Math.sqrt(1.0f + rot.get(2, 2) - rot.get(0, 0) - rot.get(1, 1));
				w = (rot.get(0, 1) - rot.get(1, 0)) / s;
				x = (rot.get(2, 0) + rot.get(0, 2)) / s;
				y = (rot.get(1, 2) + rot.get(2, 1)) / s;
				z = 0.25f * s;
			}
		}
		
		normalize();
		return this;
	}
	
	public Quaternion add(Quaternion q) {
		this.x += q.x;
		this.y += q.y;
		this.z += q.z;
		this.w += q.w;
		return this;
	}
	
	public Quaternion sub(Quaternion q) {
		this.x -= q.x;
		this.y -= q.y;
		this.z -= q.z;
		this.w -= q.w;
		return this;
	}
	
	public Quaternion multiply(Quaternion q) {
		w = w * q.getW() - x * q.getX() - y * q.getY() - z * q.getZ();
		x = x * q.getW() + w * q.getX() + y * q.getZ() - z * q.getY();
		y = y * q.getW() + w * q.getY() + z * q.getX() - x * q.getZ();
		z = z * q.getW() + w * q.getZ() + x * q.getY() - y * q.getX();
		return this;
	}
	
	public Quaternion multiply(float scalar) {
		this.x *= scalar;
		this.y *= scalar;
		this.z *= scalar;
		this.w *= scalar;
		return this;
	}
	
	public Quaternion setDirection(Vector3f forward, Vector3f up) {
		return set(Matrix4f.createRotation(forward, up));
	}
	
	/** Set the rotation based on a direction vector (Euler angle).  **/
	public Quaternion setDirection(Vector3f dir) {
		Vector2f top = dir.getXZ();
		float yaw    = top.getDirection();
		float pitch  = new Vector2f(top.length(), dir.y).getDirection();
		
		set(new Quaternion(new Vector3f(0, 1, 0), GMath.HALF_PI - yaw));
		multiply(new Quaternion(new Vector3f(1, 0, 0), -pitch));
		
		return this;
	}
	
	public Quaternion rotate(Vector3f axis, float angle) {
		set(new Quaternion(axis, angle).times(this).normalized());
		return this;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public void setW(float w) {
		this.w = w;
	}
	
	
	
	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ", " + w + ")";
	}
	
	
	
	// ================ STATIC METHODS ================ //
	
	/** Return the smallest angle between two quaternions. **/
	public static float smallestAngle(Quaternion q1, Quaternion q2) {
		float dot = q1.dot(q2);
		return (float) Math.acos((2 * dot * dot) - 1);
		
	}
}
