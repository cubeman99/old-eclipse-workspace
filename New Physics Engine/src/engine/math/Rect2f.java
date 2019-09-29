package engine.math;


public class Rect2f {
	private Vector2f position;
	private Vector2f size;

	
	
	// ================== CONSTRUCTORS ================== //
	
	public Rect2f() {
		this(0, 0, 0, 0);
	}
	
	public Rect2f(Vector2f position, Vector2f size) {
		this.position = new Vector2f(position);
		this.size     = new Vector2f(size);
	}
	
	public Rect2f(float x, float y, float width, float height) {
		position = new Vector2f(x, y);
		size     = new Vector2f(width, height);
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public boolean touches(Rect2f r) {
		return (r.getMinX() -   getMaxX() < 0 && r.getMinY() -   getMaxY() < 0 &&
				  getMinX() - r.getMaxX() < 0 &&   getMinY() - r.getMaxY() < 0);
	}
	
	public boolean contains(Vector2f v) {
		return (v.x >= getMinX() && v.x < getMaxX() &&
				v.y >= getMinY() && v.y < getMaxY());
	}
	
	public boolean contains(Rect2f r) {
		return (r.getMinX() >= getMinX() && r.getMaxX() <= getMaxX() &&
				r.getMinY() >= getMinY() && r.getMaxY() <= getMaxY());
	}
	
	public Vector2f getPosition() {
		return position;
	}
	
	public Vector2f getSize() {
		return size;
	}
	
	public Vector2f getCenter() {
		return position.plus(size.times(0.5f));
	}
	
	public float getMinX() {
		return Math.min(position.x, position.x + size.x);
	}
	
	public float getMinY() {
		return Math.min(position.y, position.y + size.y);
	}

	public float getMaxX() {
		return Math.max(position.x, position.x + size.x);
	}

	public float getMaxY() {
		return Math.max(position.y, position.y + size.y);
	}
	
	public float getX() {
		return position.x;
	}
	
	public float getY() {
		return position.y;
	}
	
	public float getWidth() {
		return size.x;
	}
	
	public float getHeight() {
		return size.y;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public Rect2f set(Vector2f position, Vector2f size) {
		this.position.set(position);
		this.size.set(size);
		return this;
	}
	
	public Rect2f set(float x, float y, float width, float height) {
		this.position.set(x, y);
		this.size.set(width, height);
		return this;
	}
	
	public Rect2f sort() {
		if (size.x < 0) {
			position.x += size.x;
			size.x = -size.x;
		}
		if (size.y < 0) {
			position.x += size.y;
			size.y = -size.y;
		}
		return this;
	}
	
	public void setPosition(Vector2f position) {
		this.position.set(position);
	}
	
	public void setSize(Vector2f size) {
		this.size.set(size);
	}
	
	public void setWidth(float width) {
		size.x = width;
	}

	public void setHeight(float height) {
		size.y = height;
	}
	
	public void setX(float x) {
		position.x = x;
	}
	
	public void setY(float y) {
		position.y = y;
	}
}
