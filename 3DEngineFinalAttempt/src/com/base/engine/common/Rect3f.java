package com.base.engine.common;


public class Rect3f {
	public static final int NUM_VERTICES = 8;
	private Vector3f position;
	private Vector3f size;

	
	
	// ================== CONSTRUCTORS ================== //
	
	public Rect3f() {
		this(0, 0, 0, 0, 0, 0);
	}
	
	public Rect3f(Rect3f copy) {
		this.position = new Vector3f(copy.position);
		this.size     = new Vector3f(copy.size);
	}
	
	public Rect3f(Vector3f position, Vector3f size) {
		this.position = new Vector3f(position);
		this.size     = new Vector3f(size);
	}
	
	public Rect3f(float x, float y, float z, float width, float height, float length) {
		position = new Vector3f(x, y, z);
		size     = new Vector3f(width, height, length);
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public boolean contains(Vector3f v) {
		return (v.x >= getMinX() && v.x < getMaxX() &&
				v.y >= getMinY() && v.y < getMaxY() &&
				v.z >= getMinZ() && v.z < getMaxZ());
	}
	
	public Rect3f getRotationBoundingBox(Quaternion rotation) {
		Vector3f min = new Vector3f(getVertex(0).rotate(rotation));
		Vector3f max = new Vector3f(min);
		
		for (int i = 1; i < NUM_VERTICES; i++) {
			Vector3f vertex = getVertex(i).rotate(rotation);
			
			if (vertex.x < min.x)
				min.x = vertex.x;
			if (vertex.y < min.y)
				min.y = vertex.y;
			if (vertex.z < min.z)
				min.z = vertex.z;
			
			if (vertex.x > max.x)
				max.x = vertex.x;
			if (vertex.y > max.y)
				max.y = vertex.y;
			if (vertex.z > max.z)
				max.z = vertex.z;
		}
		return new Rect3f(min, max.minus(min));
	}
	
	public Vector3f getVertex(int index) {
		Vector3f vertex = new Vector3f(position);
		if ((index & 1) == 1)
			vertex.x += size.x;
		if (((index >> 1) & 1) == 1)
			vertex.y += size.y;
		if (((index >> 2) & 1) == 1)
			vertex.z += size.z;
		return vertex;
	}
	
	public float getComponent(int component) {
		switch (component) {
		case Axis.NEGATIVE_X_AXIS:
			return getMinX();
		case Axis.NEGATIVE_Y_AXIS:
			return getMinY();
		case Axis.NEGATIVE_Z_AXIS:
			return getMinZ();
		case Axis.POSITIVE_X_AXIS:
			return getMaxX();
		case Axis.POSITIVE_Y_AXIS:
			return getMaxY();
		case Axis.POSITIVE_Z_AXIS:
			return getMaxZ();
		}
		return 0;
	}
	
	public Rect2f swizzle(int axisX, int axisY) {
		return new Rect2f(position.swizzle(axisX, axisY), size.swizzle(axisX, axisY));
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public Vector3f getSize() {
		return size;
	}
	
	public Vector3f getCenter() {
		return position.plus(size.times(0.5f));
	}
	
	public float getMinX() {
		return Math.min(position.x, position.x + size.x);
	}
	
	public float getMinY() {
		return Math.min(position.y, position.y + size.y);
	}
	
	public float getMinZ() {
		return Math.min(position.z, position.z + size.z);
	}

	public float getMaxX() {
		return Math.max(position.x, position.x + size.x);
	}

	public float getMaxY() {
		return Math.max(position.y, position.y + size.y);
	}

	public float getMaxZ() {
		return Math.max(position.z, position.z + size.z);
	}
	
	public float getX() {
		return position.x;
	}
	
	public float getY() {
		return position.y;
	}
	
	public float getZ() {
		return position.z;
	}
	
	public float getWidth() {
		return size.x;
	}
	
	public float getHeight() {
		return size.y;
	}
	
	public float getLength() {
		return size.z;
	}
	
	
	// ==================== MUTATORS ==================== //

	public Rect3f include(Rect3f r) {
		if (r.getMinX() < getMinX())
			setComponent(Axis.NEGATIVE_X_AXIS, r.getMinX());
		if (r.getMinY() < getMinY())
			setComponent(Axis.NEGATIVE_Y_AXIS, r.getMinY());
		if (r.getMinZ() < getMinZ())
			setComponent(Axis.NEGATIVE_Z_AXIS, r.getMinZ());
		if (r.getMaxX() > getMaxX())
			setComponent(Axis.POSITIVE_X_AXIS, r.getMaxX());
		if (r.getMaxY() > getMaxY())
			setComponent(Axis.POSITIVE_Y_AXIS, r.getMaxY());
		if (r.getMaxZ() > getMaxZ())
			setComponent(Axis.POSITIVE_Z_AXIS, r.getMaxZ());
		return this;
	}
	
	public Rect3f set(Rect3f r) {
		this.position.set(r.getPosition());
		this.size.set(r.getSize());
		return this;
	}
	
	public Rect3f set(Vector3f position, Vector3f size) {
		this.position.set(position);
		this.size.set(size);
		return this;
	}
	
	public Rect3f set(float x, float y, float z, float width, float height, float length) {
		this.position.set(x, y, z);
		this.size.set(width, height, length);
		return this;
	}
	
	public void setComponent(int component, float value) {
		if (component == Axis.NEGATIVE_X_AXIS) {
			size.x += position.x - value;
			position.x = value;
		}
		else if (component == Axis.POSITIVE_X_AXIS) {
			size.x = value - position.x;
		}
		else if (component == Axis.NEGATIVE_Y_AXIS) {
			size.y += position.y - value;
			position.y = value;
		}
		else if (component == Axis.POSITIVE_Y_AXIS) {
			size.y = value - position.y;
		}
		else if (component == Axis.NEGATIVE_Z_AXIS) {
			size.z += position.z - value;
			position.z = value;
		}
		else if (component == Axis.POSITIVE_Z_AXIS) {
			size.z = value - position.z;
		}
	}
	
	public Rect3f sort() {
		if (size.x < 0) {
			position.x += size.x;
			size.x = -size.x;
		}
		if (size.y < 0) {
			position.y += size.y;
			size.y = -size.y;
		}
		if (size.z < 0) {
			position.z += size.z;
			size.z = -size.z;
		}
		return this;
	}
	
	public void setPosition(Vector3f position) {
		this.position.set(position);
	}
	
	public void setSize(Vector3f size) {
		this.size.set(size);
	}
	
	public void setWidth(float width) {
		size.x = width;
	}

	public void setHeight(float height) {
		size.y = height;
	}

	public void setLength(float length) {
		size.z = length;
	}
	
	public void setX(float x) {
		position.x = x;
	}
	
	public void setY(float y) {
		position.y = y;
	}
	
	public void setZ(float z) {
		position.z = z;
	}
	
	
	
	// ================ STATIC METHODS ================ //
	
	public static Rect3f createBoundingBox(Vector3f[] vertices) {
		if (vertices.length == 0)
			return null;
		
		Vector3f min = new Vector3f(vertices[0]);
		Vector3f max = new Vector3f(min);
		
		for (int i = 1; i < vertices.length; i++) {
			Vector3f vertex = vertices[i];
			
			if (vertex.x < min.x)
				min.x = vertex.x;
			if (vertex.y < min.y)
				min.y = vertex.y;
			if (vertex.z < min.z)
				min.z = vertex.z;
			
			if (vertex.x > max.x)
				max.x = vertex.x;
			if (vertex.y > max.y)
				max.y = vertex.y;
			if (vertex.z > max.z)
				max.z = vertex.z;
		}
		return new Rect3f(min, max.minus(min));
	}
}
