package engine.physics;

import java.util.ArrayList;
import engine.math.Circle;
import engine.math.Vector2f;

public class PhysicsEngine {
	private ArrayList<Body> bodies;
	private ArrayList<CollisionInfo> collisions;
	private Vector2f gravity;
	

	
	// ================== CONSTRUCTORS ================== //

	public PhysicsEngine() {
		bodies     = new ArrayList<Body>();
		collisions = new ArrayList<CollisionInfo>();
		gravity    = new Vector2f(0, 4);
	}
	
	

	// =================== ACCESSORS =================== //

	public ArrayList<Body> getBodies() {
		return bodies;
	}
	
	
	
	// ==================== MUTATORS ==================== //

	public void simulate(float delta) {
		collisions.clear();
		
		// Check for collisions.
		for (int i = 0; i < bodies.size(); i++) {
			for (int j = i + 1; j < bodies.size(); j++) {
				Body b1 = bodies.get(i);
				Body b2 = bodies.get(j);
				Circle c1 = (Circle) b1.getShape();
				Circle c2 = (Circle) b2.getShape();
				
				if (b1.isStatic() && b2.isStatic())
					continue;

				if (b1.getPosition().distanceTo(b2.getPosition()) < c1.getRadius() + c2.getRadius()) {
					CollisionInfo collision    = new CollisionInfo();
					collision.body1            = bodies.get(i);
					collision.body2            = bodies.get(j);
					collision.penetration      = collision.body2.getPosition().minus(collision.body1.getPosition());
					collision.penetrationDepth = collision.penetration.length();
					collision.normal           = collision.penetration.normalized();
					collisions.add(collision);
				}
			}
		}
		
		// Resolve collisions.
		for (int i = 0; i < collisions.size(); i++) {
			CollisionInfo c = collisions.get(i);
			float invMass1  = c.body1.getInverseMass();
			float invMass2  = c.body2.getInverseMass();
			
			// Calculate relative velocity.
			Vector2f relativeVelocity = c.body2.getVelocity().minus(c.body1.getVelocity());

			// Calculate relative velocity in terms of the normal direction.
			float velAlongNormal = relativeVelocity.dot(c.normal);

			// Only resolve if velocities aren't separating.
			if (velAlongNormal <= 0) {
    			// Calculate restitution.
    			float e = Math.min(c.body1.getRestitution(), c.body2.getRestitution());
    
    			// Calculate impulse scalar.
    			float impulseScalar = -(1.0f + e) * velAlongNormal;
    			impulseScalar /= invMass1 + invMass2;
    
    			// Apply impulse.
    			Vector2f impulse = c.normal.times(impulseScalar);
    			c.body1.getVelocity().sub(impulse.times(invMass1));
    			c.body2.getVelocity().add(impulse.times(invMass2));
    
    			// Positional correction. 
    			float percent = 0.05f;
    			float slop    = 0.01f;
    			Vector2f correction = c.normal.times(Math.max(c.penetrationDepth - slop, 0.0f) / (invMass1 + invMass2) * percent);
    			c.body1.getVelocity().sub(correction.times(invMass1));
    			c.body2.getVelocity().add(correction.times(invMass2));
			}
		}
		
		
		// Move objects.
		for (int i = 0; i < bodies.size(); i++) {
			Body body = bodies.get(i);
			body.getPosition().add(body.getVelocity().times(delta));
			
			if (body.isDynamic())
				body.getVelocity().add(gravity);
		}
	}
	
	public void setGravity(Vector2f gravity) {
		this.gravity.set(gravity);
	}
	
	public void addBody(Body b) {
		bodies.add(b);
	}
}
