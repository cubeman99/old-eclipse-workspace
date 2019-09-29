package engine.physics;

import engine.math.Shape;
import engine.math.Vector2f;


public class AABB extends Shape {
	private Vector2f min;
	private Vector2f max;

	
	
	// ================== CONSTRUCTORS ================== //
	
	public AABB() {
		this(0, 0, 0, 0);
	}
	
	public AABB(Vector2f min, Vector2f max) {
		this.min = new Vector2f(min);
		this.max = new Vector2f(max);
	}
	
	public AABB(float x1, float y1, float x2, float y2) {
		min = new Vector2f(x1, y1);
		max = new Vector2f(x2, y2);
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public boolean touches(AABB r) {
		return (r.min.x - max.x < 0 && r.min.y - max.y < 0 &&
				  min.x - r.max.x < 0 && min.y - r.max.y < 0);
	}
	
	public boolean contains(Vector2f v) {
		return (v.x >= getMinX() && v.x < getMaxX() &&
				v.y >= getMinY() && v.y < getMaxY());
	}
	
	public boolean contains(AABB r) {
		return (r.getMinX() >= getMinX() && r.getMaxX() <= getMaxX() &&
				r.getMinY() >= getMinY() && r.getMaxY() <= getMaxY());
	}
	
	public Vector2f getMin() {
		return min;
	}
	
	public Vector2f getMax() {
		return max;
	}
	
	public Vector2f getSize() {
		return new Vector2f(max.x - min.x, max.y - min.y);
	}
	
	public Vector2f getCenter() {
		return min.plus(max).times(0.5f);
	}
	
	public float getMinX() {
		return min.x;
	}
	
	public float getMinY() {
		return min.y;
	}

	public float getMaxX() {
		return max.x;
	}

	public float getMaxY() {
		return max.y;
	}
	
	public float getWidth() {
		return (max.x - min.x);
	}
	
	public float getHeight() {
		return (max.y - min.y);
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public AABB set(Vector2f min, Vector2f max) {
		this.min.set(min);
		this.max.set(max);
		return this;
	}
	
	public AABB set(float x1, float y1, float x2, float y2) {
		min.set(x1, y1);
		max.set(x2, y2);
		return this;
	}
	
	public void setMin(Vector2f min) {
		this.min.set(min);
	}
	
	public void setMax(Vector2f max) {
		this.max.set(max);
	}
	
	public void setMinX(float minX) {
		min.x = minX;
	}
	
	public void setMinY(float minY) {
		min.y = minY;
	}
	
	public void setMaxX(float maxX) {
		max.x = maxX;
	}
	
	public void setMaxY(float maxY) {
		max.y = maxY;
	}
}
