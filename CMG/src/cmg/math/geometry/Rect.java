package cmg.math.geometry;



public class Rect {
	public Point corner;
	public Point size;



	// ================== CONSTRUCTORS ================== //

	public Rect() {
		this.corner = new Point();
		this.size = new Point();
	}

	public Rect(int x, int y, int width, int height) {
		this.corner = new Point(x, y);
		this.size = new Point(width, height);
	}

	public Rect(int x, int y, int size) {
		this.corner = new Point(x, y);
		this.size = new Point(size, size);
	}

	public Rect(Point corner, Point size) {
		this.corner = new Point(corner);
		this.size = new Point(size);
	}

	public Rect(Rect r) {
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
	public boolean contains(Rect r) {
		return (r.getX1() >= getX1() && r.getY1() >= getY1()
				&& r.getX2() < getX2() && r.getY2() < getY2());
	}

	/** Return whether this rectangle touches another. **/
	public boolean touches(Rect r) {
		if (r.getX1() - getX2() >= 0 || r.getY1() - getY2() >= 0)
			return false;
		if (getX1() - r.getX2() >= 0 || getY1() - r.getY2() >= 0)
			return false;
		return true;
	}

	public Rect plus(Point p) {
		return new Rect(corner.plus(p), size);
	}

	public Rect getSorted() {
		return new Rect(this).sort();
	}

	public Rect scaledBy(int amount) {
		return new Rect(this).scale(amount);
	}



	// ==================== MUTATORS ==================== //

	public Rect set(int x, int y, int width, int height) {
		this.corner.set(x, y);
		this.size.set(width, height);
		return this;
	}

	public Rect set(Point corner, Point size) {
		this.corner.set(corner);
		this.size.set(size);
		return this;
	}

	public Rect set(Rect r) {
		this.corner = new Point(r.corner);
		this.size = new Point(r.size);
		return this;
	}

	public Rect setCorner(Point corner) {
		this.corner.set(corner);
		return this;
	}

	public Rect setSize(Point size) {
		this.size.set(size);
		return this;
	}

	public Rect setX1(int x1) {
		int delta = x1 - corner.x;
		corner.x += delta;
		size.x -= delta;
		return this;
	}

	public Rect setY1(int y1) {
		int delta = y1 - corner.y;
		corner.y += delta;
		size.y -= delta;
		return this;
	}

	public Rect setX2(int x2) {
		size.x = x2 - corner.x;
		return this;
	}

	public Rect setY2(int y2) {
		size.y = y2 - corner.y;
		return this;
	}

	public Rect sort() {
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

	public Rect scale(int amount) {
		corner.scale(amount);
		size.scale(amount);
		return this;
	}

	public Rect crop(Rect bounds) {
		setX1(Math.max(getX1(), bounds.getX1()));
		setY1(Math.max(getY1(), bounds.getY1()));
		setX2(Math.min(getX2(), bounds.getX2()));
		setY2(Math.min(getY2(), bounds.getY2()));
		return this;
	}

	public Rect grow(int xAmount, int yAmount) {
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
