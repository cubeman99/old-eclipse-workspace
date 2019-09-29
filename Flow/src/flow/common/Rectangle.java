package flow.common;


public class Rectangle {
	public Vector corner;
	public Vector size;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Rectangle() {
		this.corner = new Vector();
		this.size   = new Vector();
	}
	
	public Rectangle(double x, double y, double width, double height) {
		this.corner = new Vector(x, y);
		this.size   = new Vector(width, height);
	}
	
	public Rectangle(double x, int y, double size) {
		this.corner = new Vector(x, y);
		this.size   = new Vector(size, size);
	}
	
	public Rectangle(Vector corner, Vector size) {
		this.corner = new Vector(corner);
		this.size   = new Vector(size);
	}
	
	public Rectangle(Rectangle r) {
		this.corner = new Vector(r.corner);
		this.size   = new Vector(r.size);
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
	
	public Vector getCenter() {
		return corner.plus(size.scaledBy(0.5));
	}
	
	public double getArea() {
		return (size.x * size.y);
	}
	
	/** Return whether the given point is contained inside this rectangle.**/
	public boolean contains(Vector point) {
		return (point.x >= getX1() && point.y >= getY1() && point.x < getX2() && point.y < getY2());
	}
	
	/** Return whether the given rectangle is inside this rectangle.**/
	public boolean contains(Rectangle r) {
		return (r.getX1() >= getX1() && r.getY1() >= getY1() && r.getX2() < getX2() && r.getY2() < getY2());
	}
	
	/** Return whether this rectangle touches another. **/
	public boolean touches(Rectangle r) {
		if (r.getX1() - getX2() >= 0.0 || r.getY1() - getY2() >= 0.0)
			return false;
		if (getX1() - r.getX2() >= 0.0 || getY1() - r.getY2() >= 0.0)
			return false;
		return true;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public Rectangle set(double x, double y, double width, double height) {
		this.corner.set(x, y);
		this.size.set(width, height);
		return this;
	}
	
	public Rectangle set(Vector corner, Vector size) {
		this.corner.set(corner);
		this.size.set(size);
		return this;
	}
	
	public Rectangle set(Rectangle r) {
		this.corner = new Vector(r.corner);
		this.size   = new Vector(r.size);
		return this;
	}
	
	public Rectangle setCorner(Vector corner) {
		this.corner.set(corner);
		return this;
	}
	
	public Rectangle setSize(Vector size) {
		this.size.set(size);
		return this;
	}
	
	public Rectangle centerAt(Vector center) {
		corner.set(center.minus(size.scaledBy(0.5)));
		return this;
	}
}
