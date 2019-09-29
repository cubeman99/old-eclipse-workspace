package engine.physics;

import engine.math.Shape;
import engine.math.Vector2f;

public class Body {
	private Shape shape;
	private Vector2f velocity;
	private Vector2f position;
	
	private float mass;
	private float restitution;
	private boolean dynamic;

	
	// ================== CONSTRUCTORS ================== //
	
	public Body() {
		shape       = null;
		position    = new Vector2f();
		velocity    = new Vector2f();
		mass        = 1.0f;
		restitution = 0.0f;
		dynamic     = true;
	}
	
	

	// =================== ACCESSORS =================== //

	public boolean isDynamic() {
		return dynamic;
	}
	
	public boolean isStatic() {
		return !dynamic;
	}
	
	public Shape getShape() {
		return shape;
	}
	
	public Vector2f getPosition() {
		return position;
	}
	
	public Vector2f getVelocity() {
		return velocity;
	}
	
	public float getMass() {
		return mass;
	}
	
	public float getInverseMass() {
		if (isStatic() || mass == 0)
			return 0;
		return (1.0f / mass);
	}
	
	public float getRestitution() {
		return restitution;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void setStatic() {
		dynamic = false;
	}
	
	public void setDynamic() {
		dynamic = true;
	}
	
	public void setShape(Shape shape) {
		this.shape = shape;
	}
	
	public void setPosition(Vector2f position) {
		this.position.set(position);
	}
	
	public void setVelocity(Vector2f velocity) {
		this.velocity.set(velocity);
	}
	
	public void setMass(float mass) {
		this.mass = mass;
	}
	
	public void setRestitution(float restitution) {
		this.restitution = restitution;
	}
}
