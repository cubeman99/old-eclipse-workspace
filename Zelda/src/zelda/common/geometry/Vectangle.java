package zelda.common.geometry;

import zelda.common.collision.Collidable;
import zelda.common.util.Direction;
import zelda.common.util.GMath;
import zelda.game.entity.CollisionBox;


public class Vectangle {
	public Vector corner;
	public Vector size;



	// ================== CONSTRUCTORS ================== //

	public Vectangle() {
		this.corner = new Vector();
		this.size = new Vector();
	}

	public Vectangle(double x, double y, double width, double height) {
		this.corner = new Vector(x, y);
		this.size = new Vector(width, height);
	}

	public Vectangle(double x, double y, double size) {
		this.corner = new Vector(x, y);
		this.size = new Vector(size, size);
	}

	public Vectangle(Vector corner, Vector size) {
		this.corner = new Vector(corner);
		this.size = new Vector(size);
	}

	public Vectangle(Vectangle r) {
		this.corner = new Vector(r.corner);
		this.size = new Vector(r.size);
	}

	public Vectangle(Rectangle r) {
		this.corner = new Vector(r.corner);
		this.size = new Vector(r.size);
	}

	public Vectangle(Collidable c) {
		this(c, c.getPosition());
	}

	public Vectangle(Collidable c, Vector position) {
		this(c.getCollisionBox().getVect(position));
	}

	public Vectangle(CollisionBox box, Vector position) {
		this(box.getVect(position));
	}



	// =================== ACCESSORS =================== //

	public Vector getCorner() {
		return corner;
	}

	public Vector getSize() {
		return size;
	}

	public double getWidth() {
		return size.x;
	}

	public double getHeight() {
		return size.y;
	}

	public double getMinX() {
		return Math.min(corner.x, corner.x + size.x);
	}

	public double getMinY() {
		return Math.min(corner.y, corner.y + size.y);
	}

	public double getMaxX() {
		return Math.max(corner.x, corner.x + size.x);
	}

	public double getMaxY() {
		return Math.max(corner.y, corner.y + size.y);
	}

	public double getX1() {
		return corner.x;
	}

	public double getY1() {
		return corner.y;
	}

	public double getX2() {
		return (corner.x + size.x);
	}

	public double getY2() {
		return (corner.y + size.y);
	}

	public double getEdge(int dir) {
		if (dir == Direction.RIGHT)
			return getX2();
		if (dir == Direction.UP)
			return getY1();
		if (dir == Direction.LEFT)
			return getX1();
		if (dir == Direction.DOWN)
			return getY2();
		return 0;
	}

	public double getArea() {
		return (size.x * size.y);
	}

	public Vector getCenter() {
		return new Vector(corner.x + (size.x * 0.5), corner.y + (size.y * 0.5));
	}

	/** Return whether the given point is contained inside this rectangle. **/
	public boolean contains(int x, int y) {
		return (x >= getX1() && y >= getY1() && x < getX2() && y < getY2());
	}

	/** Return whether the given point is contained inside this rectangle. **/
	public boolean contains(Point point) {
		return (point.x >= getX1() && point.y >= getY1() && point.x < getX2() && point.y < getY2());
	}

	/** Return whether the given vector is contained inside this rectangle. **/
	public boolean contains(Vector v) {
		return (v.x >= getX1() && v.y >= getY1() && v.x < getX2() && v.y < getY2());
	}

	/** Return whether the given rectangle is inside this rectangle. **/
	public boolean contains(Vectangle r) {
		return (r.getX1() >= getX1() && r.getY1() >= getY1()
				&& r.getX2() < getX2() && r.getY2() < getY2());
	}

	/** Return whether this rectangle touches another. **/
	public boolean touches(Vectangle r) {
		if (r.getX1() - getX2() >= 0 || r.getY1() - getY2() >= 0)
			return false;
		if (getX1() - r.getX2() >= 0 || getY1() - r.getY2() >= 0)
			return false;
		return true;
	}

	public Vectangle plus(Vector v) {
		return new Vectangle(corner.plus(v), size);
	}


	// ==================== MUTATORS ==================== //

	public Vectangle set(double x, double y, double width, double height) {
		this.corner.set(x, y);
		this.size.set(width, height);
		return this;
	}

	public Vectangle set(Vector corner, Vector size) {
		this.corner.set(corner);
		this.size.set(size);
		return this;
	}

	public Vectangle set(Vectangle r) {
		this.corner = new Vector(r.corner);
		this.size = new Vector(r.size);
		return this;
	}

	public Vectangle setCorner(Vector corner) {
		this.corner.set(corner);
		return this;
	}

	public Vectangle setSize(Vector size) {
		this.size.set(size);
		return this;
	}

	public Vectangle floor() {
		corner.x = (int) corner.x;
		corner.y = (int) corner.y;
		return this;
	}
	
	public Vectangle extend(double length, int dir) {
		if (dir % 2 == 0)
			size.x += length;
		else 
			size.y += length;
		if (dir == 1)
			corner.y -= length;
		else if (dir == 2)
			corner.x -= length;
		return this;
	}

	// public Rectangle scale(int amount) {
	// this.corner.scale(amount);
	// this.size.scale(amount);
	// return this;
	// }



	// ================ IMPLEMENTATIONS ================ //

	@Override
	public String toString() {
		return ("rect(" + corner.x + ", " + corner.y + ", " + size.x + ", "
				+ size.y + ")");
	}
}
