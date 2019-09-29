package engine.math;


/**
 * Extension of Shape:
 * A class to represent a circle with
 * a position and radius.
 * 
 * @author David Jordan
 */
public class Circle extends Shape {
	private Vector2f position;
	private float radius;
	
	
	
	// ================= CONSTRUCTORS ================= //
	
	public Circle(float radius) {
		this(new Vector2f(), radius);
	}
	
	public Circle(Vector2f position, float radius) {
		this.position = new Vector2f(position);
		this.radius   = radius;
	}
	
	public Circle(float x, float y, float radius) {
		this.position = new Vector2f(x, y);
		this.radius   = radius;
	}
	
	public Circle(Circle copy) {
		this.position = new Vector2f(copy.position);
		this.radius   = copy.radius;
	}
	
	

	// ================== ACCESSORS ================== //
	
	public Vector2f getPosition() {
		return position;
	}
	
	public float getRadius() {
		return radius;
	}
	
	/** Return the circumference. (2 * pi * r) **/
	public float getCircumference() {
		return (2.0f * Math2D.PI * radius);
	}
	
	/** Return the diameter. (2 * r) **/
	public float getDiameter() {
		return (2 * radius);
	}
	
	/** Return the squared radius. **/
	public float radiusSquared() {
		return (radius * radius);
	}
	
	
	
	// ================== MUTATORS ================== //
	
	public void setPosition(Vector2f position) {
		this.position.set(position);
	}
	
	public void setRadius(float radius) {
		this.radius = radius;
	}
}
