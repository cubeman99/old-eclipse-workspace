package dynamics;

import java.util.ArrayList;
import main.Main;
import common.Collision;
import common.GMath;
import common.Vector;
import common.Collision.CollisionResult;


/**
 * This is the physics world that contains all
 * static and dynamic bodies and is the object
 * that solves all collisions between them every
 * time step.
 * 
 * @author David Jordan
 */
public class PhysicsWorld {
	private ArrayList<Body> staticBodies;
	private ArrayList<Body> dynamicBodies;


	// ================== CONSTRUCTORS ================== //

	public PhysicsWorld() {
		staticBodies  = new ArrayList<Body>();
		dynamicBodies = new ArrayList<Body>();
	}



	// =================== ACCESSORS =================== //

	// ~ none... yet ~



	// ==================== MUTATORS ==================== //

	/** Add a physics body to the world. **/
	public void addBody(Body body) {
		if (body.getType() == BodyType.STATIC)
			staticBodies.add(body);
		else
			dynamicBodies.add(body);
	}

	/** Remove a physics body from the world and return if the body was found. **/
	public boolean removeBody(Body body) {
		if (body.getType() == BodyType.STATIC)
			return staticBodies.remove(body);
		return dynamicBodies.remove(body);
	}
	
	/** Remove all physics bodies from the world. **/
	public void clearBodies() {
		staticBodies.clear();
		dynamicBodies.clear();
	}

	/**
	 * Main Physics update call:
	 * Solve all collisions between static and
	 * dynamic bodies for this time step.
	 */
	public void solve() {

		// Cycle through all dynamic bodies:
		for (int i = 0; i < dynamicBodies.size(); i++) {
			Body dynamicBody      = dynamicBodies.get(i);

			// Solve this dynamic body's collisions:
			solveDynamicCollisions(dynamicBody);
		}

	}

	/** Solve the collisions for a dynamic body. **/
	public boolean solveDynamicCollisions(Body dynamicBody) {
		
		double stepTime = Main.STEP_TIME;
		double elapsedTime = 0;
		
		
		// Cycle through the collision process on equally spaced time impulses:
		for (int i = 0; i < Collision.MAX_IMPULSE_ITERATIONS && elapsedTime < stepTime - GMath.EPSILON; i++) {
			Vector nextVelocity = new Vector(dynamicBody.velocity);
//			Vector snapVelocity = new Vector(dynamicBody.velocity.scaledByInv(Collision.MAX_IMPULSE_ITERATIONS));
//			Vector snapVelocity = new Vector(dynamicBody.velocity);
			CollisionResult finalResult = new CollisionResult(dynamicBody.velocity);
			
			// Find the minimum translation vector:
			for (int si = 0; si < staticBodies.size(); si++) {
				Body staticBody      = staticBodies.get(si);
//				Vector checkVelocity = new Vector(snapVelocity.scaledByInv(Collision.MAX_IMPULSE_ITERATIONS));
				
				CollisionResult result = Collision.getMinimumTranslationVector(dynamicBody.getShape(), staticBody.getShape(), finalResult.minTranslationVector);
				Collision.minResultToOut(finalResult, result);
				
				/*if (result.minTranslationVector.length() < snapVelocity.length()) {
					finalResult.set(result);
					snapVelocity.set(result.minTranslationVector);
				}*/
			}
			
//			dynamicBody.shape.translate(snapVelocity);
//			dynamicBody.shape.translate(dynamicBody.velocity);

//			if (finalResult.minTranslationVector.length() > 0)
//				finalResult.minTranslationVector.setLength(GMath.max(0, finalResult.minTranslationVector.length() - GMath.EPSILON));
			
			// Apply friction and restitution:
			if (finalResult != null) {
				dynamicBody.shape.translate(finalResult.minTranslationVector);
				elapsedTime += (finalResult.minTranslationVector.length() / dynamicBody.velocity.length()) * stepTime;
				
    			if (finalResult.collisionAxis != null) {
    				Vector vecFriction    = dynamicBody.velocity.projectionOn(finalResult.collisionAxis).scaledBy(1.0 - dynamicBody.friction);
    				Vector vecRestitution = dynamicBody.velocity.rejectionOn(finalResult.collisionAxis).scaledBy(dynamicBody.restitution);
    				if (vecRestitution.length() < Collision.RESTITUTION_THRESHOLD)
    					vecRestitution.zero();
    				nextVelocity.set(vecFriction.minus(vecRestitution));
    			}
			}
			
//			elapsedTime = 100;
			dynamicBody.velocity.set(nextVelocity);
			
//			if (dynamicBody.velocity.length() < GMath.EPSILON) {
//				dynamicBody.velocity.zero();
//				break;
//			}
		}
		
		return false;
//		return (finalResult == null) ? false : finalResult.willCollide;
		/*
		for (Polygon p : map.walls) {
			for (int i = 0; i < p.vertexCount(); i++) {
				// Set the velocity to the projection on to the static line
				Vector collisionPoint = projectCircleOnto(p.getEdge(i), shape, snapVelocity);

				// Limit the next velocity (if colliding):
				if (collisionPoint != null) {
					Vector lineVec = new Vector(shape.position.plus(snapVelocity), collisionPoint);
					colliding      = true;

					// Apply friction and restitution:
					Vector vecFriction    = velocity.rejectionOn(lineVec).scaledBy(1.0 - friction);
					Vector vecRestitution = velocity.projectionOn(lineVec).scaledBy(restitution);
					if (vecRestitution.length() < RESTITUTION_THRESHOLD)
						vecRestitution.zero();
					nextVelocity.set(vecFriction.minus(vecRestitution));
				}
			}
		}

		// Add to the elapsed time:
		elapsedTime += (snapVelocity.length() / velocity.length()) * stepTime;

		// Apply the velocity and set velocity to the next velocity:
		shape.position.add(snapVelocity);
		velocity.set(nextVelocity);

		if (velocity.length() < GMath.EPSILON) {
			break;
		}
		*/
	}
	
}
