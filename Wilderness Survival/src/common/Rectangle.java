package common;


/**
 * 
 * 
 * 
 * @author David Jordan
 */
public class Rectangle {
	public Vector position;
	public Vector size;
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Rectangle() {
		this.position = new Vector();
		this.size     = new Vector();
	}

	public Rectangle(Vector position, Vector size) {
		this.position = position;
		this.size     = size;
	}

	public Rectangle(Rectangle copy) {
		set(copy);
	}
	
	
	
	// =================== ACCESSORS =================== //

	/** Return the top left corner of the rectangle. **/
	public Vector getPosition() {
		return position;
	}

	/** Return the dimensions of the rectangle. **/
	public Vector getSize() {
		return size;
	}
	
	/** Return the center point of the rectangle. **/
	public Vector getCenter() {
		return position.plus(size.scaledBy(0.5));
	}

	/** Return if this rectangles components are the same as another's (be careful when using this). **/
	public boolean equals(Rectangle r) {
		return (position.equals(r.position) && size.equals(r.size));
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	/** Set the rectangle's components to another rectangles components. **/
	public void set(Rectangle copy) {
		this.position = new Vector(copy.position);
		this.size     = new Vector(copy.size);
	}

	/** Set the rectangle's position and size components. **/
	public void set(Vector position, Vector size) {
		this.position = position;
		this.size     = size;
	}

	/** Set the rectangle's top left corner's position. **/
	public void setPosition(Vector position) {
		this.position = position;
	}

	/** Set the rectangle's dimensions. **/
	public void setSize(Vector size) {
		this.size = size;
	}
	
	
	
	// =============== INHERITED METHODS =============== //
	
	@Override
	/** Return a string to represent this rectangle. **/
	public String toString() {
		return ("Rectangle[" + position + ", " + size + "]");
	}
}
