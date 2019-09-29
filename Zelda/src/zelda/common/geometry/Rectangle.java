package zelda.common.geometry;


public class Rectangle {
	public Point corner;
	public Point size;



	// ================== CONSTRUCTORS ================== //

	public Rectangle() {
		this.corner = new Point();
		this.size = new Point();
	}

	public Rectangle(int x, int y, int width, int height) {
		this.corner = new Point(x, y);
		this.size = new Point(width, height);
	}

	public Rectangle(int x, int y, int size) {
		this.corner = new Point(x, y);
		this.size = new Point(size, size);
	}

	public Rectangle(Point corner, Point size) {
		this.corner = new Point(corner);
		this.size = new Point(size);
	}

	public Rectangle(Rectangle r) {
		this.corner = new Point(r.corner);
		this.size = new Point(r.size);
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

	public Point getCenter() {
		return new Point(corner.x + (size.x / 2), corner.y + (size.y / 2));
	}

	public int getArea() {
		return (size.x * size.y);
	}

	/** Return whether the given point is contained inside this rectangle. **/
	public boolean contains(int x, int y) {
		return (x >= getX1() && y >= getY1() && x < getX2() && y < getY2());
	}

	/** Return whether the given point is contained inside this rectangle. **/
	public boolean contains(Point point) {
		return (point.x >= getX1() && point.y >= getY1() && point.x < getX2() && point.y < getY2());
	}

	/** Return whether the given rectangle is inside this rectangle. **/
	public boolean contains(Rectangle r) {
		return (r.getX1() >= getX1() && r.getY1() >= getY1()
				&& r.getX2() < getX2() && r.getY2() < getY2());
	}

	/** Return whether this rectangle touches another. **/
	public boolean touches(Rectangle r) {
		if (r.getX1() - getX2() >= 0 || r.getY1() - getY2() >= 0)
			return false;
		if (getX1() - r.getX2() >= 0 || getY1() - r.getY2() >= 0)
			return false;
		return true;
	}

	public Rectangle plus(Point p) {
		return new Rectangle(corner.plus(p), size);
	}

	public Rectangle getSorted() {
		return new Rectangle(this).sort();
	}

	public Rectangle scaledBy(int amount) {
		return new Rectangle(this).scale(amount);
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
		this.size = new Point(r.size);
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

	public Rectangle setX1(int x1) {
		int delta = x1 - corner.x;
		corner.x += delta;
		size.x -= delta;
		return this;
	}

	public Rectangle setY1(int y1) {
		int delta = y1 - corner.y;
		corner.y += delta;
		size.y -= delta;
		return this;
	}

	public Rectangle setX2(int x2) {
		size.x = x2 - corner.x;
		return this;
	}

	public Rectangle setY2(int y2) {
		size.y = y2 - corner.y;
		return this;
	}

	public Rectangle sort() {
		if (size.x < 0) {
			corner.x += size.x;
			size.x = -size.x;
		}
		if (size.y < 0) {
			corner.y += size.y;
			size.y = -size.y;
		}
		return this;
	}

	public Rectangle scale(int amount) {
		corner.scale(amount);
		size.scale(amount);
		return this;
	}

	public Rectangle crop(Rectangle bounds) {
		setX1(Math.max(getX1(), bounds.getX1()));
		setY1(Math.max(getY1(), bounds.getY1()));
		setX2(Math.min(getX2(), bounds.getX2()));
		setY2(Math.min(getY2(), bounds.getY2()));
		return this;
	}

	public Rectangle grow(int xAmount, int yAmount) {
		corner.sub(xAmount, yAmount);
		size.add(new Point(xAmount, yAmount).scale(2));
		return this;
	}



	// ================ IMPLEMENTATIONS ================ //

	@Override
	public String toString() {
		return ("rect(" + corner.x + ", " + corner.y + ", " + size.x + ", "
				+ size.y + ")");
	}
}
