package geometry;

/**
 * An integer-based point class with an x and y coordinate. This can be
 * used for positions and sizes.
 * @author	Robert Jordan
 */
public class Point {

	// ======================= Members ========================
	
	/** The x coordinate of the point. */
	public int x;
	/** The y coordinate of the point. */
	public int y;

	// ===================== Constructors =====================
	
	/**
	 * Constructs the default point at (0, 0).
	 * @return	Returns a point at (0, 0).
	 */
	public Point() {
		this.x = 0;
		this.y = 0;
	}
	/**
	 * Constructs a point at the specified coordinates.
	 * @param	x - The x coordinate.
	 * @param	y - The y coordinate.
	 * @return	Returns a point at (x, y).
	 */
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	/**
	 * Constructs a point at the specified coordinates.
	 * @param	x - The x coordinate.
	 * @param	y - The y coordinate.
	 * @return	Returns a point at (x, y).
	 */
	public Point(double x, double y) {
		this.x = (int)x;
		this.y = (int)y;
	}
	/**
	 * Constructs a point from the specified point.
	 * @param	p - The point to copy the coordinates of.
	 * @return	Returns a point at the same coordinates as the given point.
	 */
	public Point(Point p) {
		this.x = p.x;
		this.y = p.y;
	}
	/**
	 * Constructs a point from the specified vector.
	 * @param	v - The vector to copy the coordinates of.
	 * @return	Returns a point at the same integer coordinates as the
	 * given vector.
	 */
	public Point(Vector v) {
		this.x = (int)v.x;
		this.y = (int)v.y;
	}
	
	// ======================= General ========================
	
	/**
	 * Returns the point in the form of a string as "(x, y)".
	 * @return	Returns a string representing the point's x and y values.
	 */
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	/**
	 * Tests whether the point is equal to the specified point or vector.
	 * Returns false if the specified object is null or not a point or vector.
	 * @param	obj - The point or vector to compare to.
	 * @return	Returns true if the points have the same x and y values.
	 */
	public boolean equals(Object obj) {
		if (obj != null) {
			if (obj instanceof Point) {
				return (this.x == ((Point)obj).x) && (this.y == ((Point)obj).y);
			}
			else if (obj instanceof Point) {
				return (this.x == ((Vector)obj).x) && (this.y == ((Vector)obj).y);
			}
		}
		return false;
	}
	/**
	 * Converts the point into a vector and returns that.
	 * @return	Returns a vector representing the point.
	 */
	public Vector getVector() {
		return new Vector(this);
	}
	
	// ====================== Arithmetic ======================
	
	/**
	 * Returns the point plus the x and y distance.
	 * @param	x - The x distance to add.
	 * @param	y - The y distance to add.
	 * @return	Returns a point with the added distance.
	 */
	public Point plus(int x, int y) {
		return new Point(this.x + x, this.y + y);
	}
	/**
	 * Returns the point plus the point distance.
	 * @param	p - The point of the distance to add.
	 * @return	Returns a point with the added distance.
	 */
	public Point plus(Point p) {
		return new Point(this.x + p.x, this.y + p.y);
	}
	/**
	 * Returns the point minus the x and y distance.
	 * @param	x - The x distance to subtract.
	 * @param	y - The y distance to subtract.
	 * @return	Returns a point with the subtracted distance.
	 */
	public Point minus(int x, int y) {
		return new Point(this.x - x, this.y - y);
	}
	/**
	 * Returns the point minus the vector distance.
	 * @param	v - The point of the distance to subtract.
	 * @return	Returns a point with the subtracted distance.
	 */
	public Point minus(Point p) {
		return new Point(this.x - p.x, this.y - p.y);
	}
	/**
	 * Returns the point multiplied by the scale.
	 * @param	scale - The scale to multiply the point by.
	 * @return	Returns a point with the values multiplied by the scale.
	 */
	public Point scaledBy(double scale) {
		return new Point((int)(this.x * scale), (int)(this.y * scale));
	}
	/**
	 * Returns the inverse of the point.
	 * @return	Returns a point with the inverse values.
	 */
	public Point inverse() {
		return new Point(-this.x, -this.y);
	}
	
	// ================= Modified Arithmetic ==================
	
	/**
	 * Adds the x and y distance to the point.
	 * @param	x - The x distance to add.
	 * @param	y - The y distance to add.
	 * @return	Returns the point with the added distance.
	 */
	public Point add(int x, int y) {
		this.x += x;
		this.y += y;
		return this;
	}
	/**
	 * Adds the point distance to the vector.
	 * @param	p - The point of the distance to add.
	 * @return	Returns the point with the added distance.
	 */
	public Point add(Point p) {
		this.x += p.x;
		this.y += p.y;
		return this;
	}
	/**
	 * Subtracts the x and y distance from the point.
	 * @param	x - The x distance to subtract.
	 * @param	y - The y distance to subtract.
	 * @return	Returns the point with the subtracted distance.
	 */
	public Point sub(int x, int y) {
		this.x -= x;
		this.y -= y;
		return this;
	}
	/**
	 * Subtracts the point distance from the vector.
	 * @param	v - The point of the distance to subtract.
	 * @return	Returns the point with the subtracted distance.
	 */
	public Point sub(Point p) {
		this.x -= p.x;
		this.y -= p.y;
		return this;
	}
	/**
	 * Multiplies the point by the scale.
	 * @param	scale - The scale to multiply the point by.
	 * @return	Returns a new scaled point.
	 */
	public Point scale(double scale) {
		this.x *= scale;
		this.y *= scale;
		return this;
	}
	/**
	 * Negates the point.
	 * @return	Returns the new negated point.
	 */
	public Point negate() {
		this.x = -x;
		this.y = -y;
		return this;
	}
	/**
	 * Sets the point to (0, 0).
	 * @return	Returns the new point at the position (0, 0).
	 */
	public Point zero() {
		this.x = 0;
		this.y = 0;
		return this;
	}
	/**
	 * Sets the point to the new position.
	 * @param	x - The new x coordinate.
	 * @param	y - The new y coordinate.
	 * @return	Returns the new point with the modified position.
	 */
	public Point set(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}
	/**
	 * Sets the point to the new position.
	 * @param	p - The new point value.
	 * @return	Returns the new point with the modified position.
	 */
	public Point set(Point p) {
		this.x = p.x;
		this.y = p.y;
		return this;
	}
	/**
	 * Sets the point to the new position.
	 * @param	x - The new x coordinate.
	 * @param	y - The new y coordinate.
	 * @return	Returns the new point with the modified position.
	 */
	public Point set(double x, double y) {
		this.x = (int)x;
		this.y = (int)y;
		return this;
	}
	/**
	 * Sets the point to the new position.
	 * @param	v - The new point value.
	 * @return	Returns the new point with the modified position.
	 */
	public Point set(Vector v) {
		this.x = (int)v.x;
		this.y = (int)v.y;
		return this;
	}
}
