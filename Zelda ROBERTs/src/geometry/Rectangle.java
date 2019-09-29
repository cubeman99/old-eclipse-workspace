package geometry;

/**
 * A class to encapsulate a rectangle with a position and size.
 * This is mainly used for collision detection and bounding boxes.
 * @author	Robert Jordan
 */
public class Rectangle {

	// ======================= Members ========================

	/** The position of the rectangle. */
	public Vector point;
	/** The size of the rectangle. */
	public Vector size;
	
	// ===================== Constructors =====================
	
	/** Constructs the default rectangle. */
	public Rectangle() {
		this.point	= new Vector();
		this.size	= new Vector();
	}
	/** Constructs the rectangle with the specified dimensions and location. */
	public Rectangle(double x, double y, double w, double h) {
		this.point	= new Vector(x, y);
		this.size	= new Vector(w, h);
	}
	/** Constructs the rectangle with the specified dimensions and location. */
	public Rectangle(Vector point, Vector size) {
		this.point	= new Vector(point);
		this.size	= new Vector(size);
	}
	/** Constructs the rectangle from the specified rectangle. */
	public Rectangle(Rectangle rect) {
		this.point	= new Vector(rect.point);
		this.size	= new Vector(rect.size);
	}

	// ====================== Transform =======================
	
	/** Translates the rectangle. */
	public Rectangle translate(double x, double y) {
		point.add(x, y);
		return this;
	}
	/** Translates the rectangle. */
	public Rectangle translate(Vector distance) {
		point.add(distance);
		return this;
	}
	/** Returns a translated rectangle. */
	public Rectangle translatedBy(double x, double y) {
		return new Rectangle(this).translate(x, y);
	}
	/** Returns a translated rectangle. */
	public Rectangle translatedBy(Vector distance) {
		return new Rectangle(this).translate(distance);
	}

	// ====================== Collision =======================
	
	/** Tests whether the rectangle contains the point. */
	public boolean contains(double x, double y) {
		return	(x >= point.x && x < point.x + size.x) &&
				(y >= point.y && y < point.y + size.y);
	}
	/** Tests whether the rectangle contains the point. */
	public boolean contains(Vector point) {
		return	(point.x >= this.point.x && point.x < this.point.x + this.size.x) &&
				(point.y >= this.point.y && point.y < this.point.y + this.size.y);
	}
	/** Tests whether the rectangle contains the rectangle. */
	public boolean contains(double x, double y, double w, double h) {
		return	(x >= point.x && x + w <= point.x + size.x) &&
				(y >= point.y && y + h <= point.y + size.y);
	}
	/** Tests whether the rectangle contains the rectangle. */
	public boolean contains(Vector point, Vector size) {
		return	(point.x >= point.x && point.x + size.x <= point.x + size.x) &&
				(point.y >= point.y && point.y + size.y <= point.y + size.y);
	}
	/** Tests whether the rectangle contains the rectangle. */
	public boolean contains(Rectangle rect) {
		return	rect.point.x >= point.x &&
				rect.point.x + rect.size.x <= point.x + size.x &&
				rect.point.y >= point.y &&
				rect.point.y + rect.size.y <= point.y + size.y;
	}
	/** Tests whether the rectangle is colliding with the rectangle. */
	public boolean colliding(double x, double y, double w, double h) {
		return	(x + w > point.x && x < point.x + size.x) &&
				(y + h > point.y && y < point.y + size.y);
		/*return	(x >= point.x || x + w > point.x) &&
				(x <= point.x || x < point.x + size.x) &&
				(y >= point.y || y + h > point.y) &&
				(y <= point.y || y < point.y + size.y);*/
	}
	/** Tests whether the rectangle is colliding with the rectangle. */
	public boolean colliding(Vector point, Vector size) {
		return	(point.x + size.x > this.point.x && point.x < this.point.x + this.size.x) &&
				(point.y + size.y > this.point.y && point.y < this.point.y + this.size.y);
		/*return	(point.x >= this.point.x || point.x + size.x > this.point.x) &&
				(point.x <= this.point.x || point.x < this.point.x + this.size.x) &&
				(point.y >= this.point.y || point.y + size.y > this.point.y) &&
				(point.y <= this.point.y || point.y < this.point.y + this.size.y);*/
	}
	/** Tests whether the rectangle is colliding with the rectangle. */
	public boolean colliding(Rectangle rect) {
		return	(rect.point.x + rect.size.x > point.x) &&
				(rect.point.x < point.x + size.x) &&
				(rect.point.y + rect.size.y > point.y) &&
				(rect.point.y < point.y + size.y);
		/*return	(rect.point.x >= point.x || rect.point.x + rect.size.x > point.x) &&
				(rect.point.x <= point.x || rect.point.x < point.x + size.x) &&
				(rect.point.y >= point.y || rect.point.y + rect.size.y > point.y) &&
				(rect.point.y <= point.y || rect.point.y < point.y + size.y);*/
	}
	/** Tests whether the rectangle is inside the rectangle. */
	public boolean inside(double x, double y, double w, double h) {
		return	(x <= point.x && x + w >= point.x + size.x) &&
				(y <= point.y && y + h >= point.y + size.y);
	}
	/** Tests whether the rectangle is inside the rectangle. */
	public boolean inside(Vector point, Vector size) {
		return	(point.x <= this.point.x && point.x + size.x >= this.point.x + this.size.x) &&
				(point.y <= this.point.y && point.y + size.y >= this.point.y + this.size.y);
	}
	/** Tests whether the rectangle is inside the rectangle. */
	public boolean inside(Rectangle rect) {
		return	rect.point.x <= point.x &&
				rect.point.x + rect.size.x >= point.x + size.x &&
				rect.point.y <= point.y &&
				rect.point.y + rect.size.y >= point.y + size.y;
	}
}
