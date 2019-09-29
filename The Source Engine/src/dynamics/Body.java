package dynamics;

import common.Vector;
import common.shape.Shape;



/**
 * This is a class to represent a rigid physics
 * body that has certain physical attributes and
 * its collisions are solved within its physics
 * world.
 * 
 * @author David Jordan
 */
public class Body {
	public Shape shape;            // The shape of the physics body.
	public BodyType type;          // The type of body, Static or Dynamic.
	public double friction;        // The ratio of friction for the body (must be from zero to one).
	public double restitution;     // The ratio of "bounce" for the body (must be from zero to one).
	
	public double angle;           // The rotation of the shape.
	public double angularVelocity; // The rate of change for the angle of the shape.
	public Vector velocity;        // The velocity of the body.
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Body(Shape shape, BodyType type) {
		this.shape           = shape;
		this.type            = type;
		this.friction        = 0.0;
		this.restitution     = 0.0;
		
		this.angle           = 0.0;
		this.angularVelocity = 0.0;
		this.velocity        = new Vector();
	}
	
	
	
	// =================== ACCESSORS =================== //

	/** Return the shape of the physics body. **/
	public Shape getShape() {
		return shape;
	}
	

	/** Return the body type of the physics body (Static or Dynamic). **/
	public BodyType getType() {
		return type;
	}
	
	/** Return the friction of the physics body. **/
	public double getFriction() {
		return friction;
	}
	
	/** Return the restitution of the physics body. **/
	public double getRestitution() {
		return restitution;
	}

	/** Return the velocity of the physics body. **/
	public Vector getVelocity() {
		return velocity;
	}
	
	
	// ==================== MUTATORS ==================== //
	
	/** Set the friction of the physics body. **/
	public void setFriction(double friction) {
		this.friction = friction;
	}
	
	/** Set the restitution of the physics body. **/
	public void setRestitution(double restitution) {
		this.restitution = restitution;
	}

	/** Add velocity to the physics body. **/
	public void addVelocity(Vector amount) {
		velocity.add(amount);
	}
	
	
	
	// ================ STATIC METHODS ================ //

	/** Construct and return a new dynamic body. **/
	public static Body newDynamicBody(Shape shape) {
		return new Body(shape, BodyType.DYNAMIC);
	}
	
	/** Construct and return a new static body. **/
	public static Body newStaticBody(Shape shape) {
		return new Body(shape, BodyType.STATIC);
	}
}
