package projects.fractals;


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
	
	public float dot(float x, float y) {
		return ((this.x * x) + (this.y * y));
	}

	public Vector2f normalized() {
		float length = length();
		return new Vector2f(x / length, y / length);
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

	public Vector2f minus(Vector2f v) {
		return new Vector2f(x - v.getX(), y - v.getY());
	}

	public Vector2f minus(float amount) {
		return new Vector2f(x - amount, y - amount);
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

	public Vector2f abs() {
		return new Vector2f(Math.abs(x), Math.abs(y));
	}
	
	public float distanceTo(Vector2f v) {
		return (float) Math.sqrt(((v.x - x) * (v.x - x)) + ((v.y - y) * (v.y - y)));
	}
	
	public float getDirection() {
		float dir = 0.0f;
		if (x != 0.0f) {
			dir = ((float) Math.atan(y / x));
			if (x < 0.0f)
				dir += (float) Math.PI;
		}
		else if (y < 0)
			dir = (3.0f / 2.0f) * (float) Math.PI;
		else if (y > 0)
			dir = (float) Math.PI * 0.5f;
		if (dir < 0)
			dir =  ((float) Math.PI * 2.0f) + dir;
		return dir;
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
		return "(" + x + " " + y + ")";
	}
}
