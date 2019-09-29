package engine.math;






public class Vector2f {
	public float x;
	public float y;
	
	
	
	// ================== CONSTRUCTORS ================== //

	public Vector2f() {
		this(0, 0);
	}
	
	public Vector2f(float a) {
		this.x = a;
		this.y = a;
	}
	
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2f(Vector2f copy) {
		this.x = copy.x;
		this.y = copy.y;
	}
	
	

	// =================== ACCESSORS =================== //
	
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float length() {
		return (float) Math.sqrt((x * x) + (y * y));
	}
	
	public Vector2f inverse() {
		return new Vector2f(-x, -y);
	}
	
	public float dot(Vector2f r) {
		return ((x * r.getX()) + (y * r.getY()));
	}

	public Vector2f normalized() {
		float length = length();
		return new Vector2f(x / length, y / length);
	}
	
	public Vector2f lengthVector(float l) {
		return new Vector2f(this).setLength(l);
	}

	public Vector2f rotate(float angle) {
		double rad = Math.toRadians(angle);
		double cos = Math.cos(rad);
		double sin = Math.sin(rad);
		
		return new Vector2f(
			(float) ((x * cos) - (y * sin)),
			(float) ((x * sin) + (y * cos))
		);
	}

	public Vector2f plus(Vector2f v) {
		return new Vector2f(x + v.getX(), y + v.getY());
	}

	public Vector2f plus(float amount) {
		return new Vector2f(x + amount, y + amount);
	}

	public Vector2f plus(float x, float y) {
		return new Vector2f(this.x + x, this.y + y);
	}

	public Vector2f minus(Vector2f v) {
		return new Vector2f(x - v.getX(), y - v.getY());
	}

	public Vector2f minus(float amount) {
		return new Vector2f(x - amount, y - amount);
	}

	public Vector2f minus(float x, float y) {
		return new Vector2f(this.x - x, this.y - y);
	}

	public Vector2f times(Vector2f v) {
		return new Vector2f(x * v.getX(), y * v.getY());
	}

	public Vector2f times(float amount) {
		return new Vector2f(x * amount, y * amount);
	}

	public Vector2f dividedBy(Vector2f v) {
		return new Vector2f(x / v.getX(), y / v.getY());
	}

	public Vector2f dividedBy(float amount) {
		return new Vector2f(x / amount, y / amount);
	}

	public Vector2f lerp(Vector2f destination, float lerpFactor) {
		return destination.minus(this).multiply(lerpFactor).add(this);
	}
	
	public Vector2f angularLerp(Vector2f destination, float lerpFactor) {
		float dir = getDirection();
		float goalDir = destination.getDirection();
		
		float angle = goalDir - dir;
		if (angle > Math2D.PI)
			angle = Math2D.TWO_PI - angle;
		if (angle < -Math2D.PI)
			angle = -Math2D.TWO_PI - angle;
		
		return Vector2f.polarVector(1, dir + (angle * lerpFactor));
		
	}

	public Vector2f abs() {
		return new Vector2f(Math.abs(x), Math.abs(y));
	}
	
	public float distanceTo(Vector2f v) {
		return (float) Math.sqrt(((v.x - x) * (v.x - x)) + ((v.y - y) * (v.y - y)));
	}
	
	public float directionTo(Vector2f v) {
		return Math2D.getDirection(v.x - x, v.y - y);
	}
	
	public float getDirection() {
		return Math2D.getDirection(x, y);
	}
	
	/** return the scaler projection to another vector. { s = |a| cos(theta) } **/
	public float scalarProjection(Vector2f b) {
		return (length() * (float) Math.cos(angleBetween(b)));
	}
	
	/** return the scaler rejection to another vector. { s = |a| sin(theta) } **/
	public float scalarRejection(Vector2f b) {
		return (length() * (float) Math.sin(angleBetween(b)));
	}

	/** Return the angle between this and another vector. **/
	public float angleBetween(Vector2f b) {
		return (getDirection() - b.getDirection());
	}
	

	
	// ==================== MUTATORS ==================== //
	
	public Vector2f set(Vector2f v) {
		this.x = v.x;
		this.y = v.y;
		return this;
	}

	public Vector2f set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public Vector2f zero() {
		x = 0;
		y = 0;
		return this;
	}
	
	public Vector2f add(Vector2f v) {
		this.x += v.x;
		this.y += v.y;
		return this;
	}
	
	public Vector2f sub(Vector2f v) {
		this.x -= v.x;
		this.y -= v.y;
		return this;
	}
	
	public Vector2f multiply(Vector2f v) {
		this.x *= v.x;
		this.y *= v.y;
		return this;
	}
	
	public Vector2f multiply(float amount) {
		this.x *= amount;
		this.y *= amount;
		return this;
	}
	
	public Vector2f divide(Vector2f v) {
		this.x /= v.x;
		this.y /= v.y;
		return this;
	}

	public Vector2f normalize() {
		float length = length();
		x /= length;
		y /= length;
		return this;
	}
	
	public Vector2f setLength(float l) {
		float length = length();
		x = (x / length) * l;
		y = (y / length) * l;
		return this;
	}
	
	public Vector2f negate() {
		x = -x;
		y = -y;
		return this;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	
	
	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
	
	
	// ================ STATIC METHODS ================ //
	
	/** Return a new vector from the polar components of length and direction. **/
	public static Vector2f polarVector(float length, float direction) {
		return new Vector2f((float) Math.cos(direction), (float) Math.sin(direction));
	}
}
