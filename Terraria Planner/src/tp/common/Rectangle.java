package tp.common;


public class Rectangle {
	public Point corner;
	public Point size;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Rectangle() {
		this.corner = new Point();
		this.size   = new Point();
	}
	
	public Rectangle(int x, int y, int width, int height) {
		this.corner = new Point(x, y);
		this.size   = new Point(width, height);
	}
	
	public Rectangle(int x, int y, int size) {
		this.corner = new Point(x, y);
		this.size   = new Point(size, size);
	}
	
	public Rectangle(Point corner, Point size) {
		this.corner = new Point(corner);
		this.size   = new Point(size);
	}
	
	public Rectangle(Rectangle r) {
		this.corner = new Point(r.corner);
		this.size   = new Point(r.size);
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public Point getCorner() {
		return corner;
	}
	
	public Point getSize() {
		return size;
	}
	
	public int getWidth() {
		return size.x;
	}
	
	public int getHeight() {
		return size.y;
	}
	
	public int getMinX() {
		return Math.min(corner.x, corner.x + size.x);
	}
	
	public int getMinY() {
		return Math.min(corner.y, corner.y + size.y);
	}
	
	public int getMaxX() {
		return Math.max(corner.x, corner.x + size.x);
	}
	
	public int getMaxY() {
		return Math.max(corner.y, corner.y + size.y);
	}
	
	public int getX1() {
		return corner.x;
	}
	
	public int getY1() {
		return corner.y;
	}
	
	public int getX2() {
		return (corner.x + size.x);
	}
	
	public int getY2() {
		return (corner.y + size.y);
	}
	
	public int getArea() {
		return (size.x * size.y);
	}
	
	/** Return whether the given point is contained inside this rectangle.**/
	public boolean contains(int x, int y) {
		return (x >= getX1() && y >= getY1() && x < getX2() && y < getY2());
	}
	
	/** Return whether the given point is contained inside this rectangle.**/
	public boolean contains(Point point) {
		return (point.x >= getX1() && point.y >= getY1() && point.x < getX2() && point.y < getY2());
	}
	
	/** Return whether the given rectangle is inside this rectangle.**/
	public boolean contains(Rectangle r) {
		return (r.getX1() >= getX1() && r.getY1() >= getY1() && r.getX2() < getX2() && r.getY2() < getY2());
	}
	
	/** Return whether this rectangle touches another. **/
	public boolean touches(Rectangle r) {
		if (r.getX1() - getX2() >= 0 || r.getY1() - getY2() >= 0)
			return false;
		if (getX1() - r.getX2() >= 0 || getY1() - r.getY2() >= 0)
			return false;
		return true;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public Rectangle set(int x, int y, int width, int height) {
		this.corner.set(x, y);
		this.size.set(width, height);
		return this;
	}
	
	public Rectangle set(Point corner, Point size) {
		this.corner.set(corner);
		this.size.set(size);
		return this;
	}
	
	public Rectangle set(Rectangle r) {
		this.corner = new Point(r.corner);
		this.size   = new Point(r.size);
		return this;
	}
	
	public Rectangle setCorner(Point corner) {
		this.corner.set(corner);
		return this;
	}
	
	public Rectangle setSize(Point size) {
		this.size.set(size);
		return this;
	}
	
	public Rectangle scale(int amount) {
		this.corner.scale(amount);
		this.size.scale(amount);
		return this;
	}
}
