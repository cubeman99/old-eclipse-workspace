package game;

import cmg.math.GMath;
import cmg.math.geometry.Circle;
import cmg.math.geometry.Line;
import cmg.math.geometry.Vector;

public class Collision {

	
	
	/** Minimum Translation Vector: Circle to Line **/
	public static CollisionResult solveMinimumTranslation(Circle c, Line l, Vector velocity) {
		CollisionResult result = new CollisionResult(velocity);
		Line motion            = new Line(c.position, c.position.plus(velocity));
		double theta           = GMath.angleBetween(velocity.direction(), l.direction());
		
		// Check if the lines are parallel:
		if (theta < GMath.EPSILON)
			return result;
		
		Vector intersection   = Line.intersectionEndless(motion, l);
		double minDist        = Line.shortestDistance(motion, l);
		Vector snapPosition   = null; // The position the circle will end up
		Vector contactPoint   = null; // The point of contact
		
		// Only continue if the distance between the motion and
		// static line is less than the circles radius:
		if (minDist < c.radius - GMath.EPSILON) {
			boolean snapToEndpoint = true;

			// Snap to the slope of the line:
			if (intersection != null) {
				// Calculate displacement (from intersection) to the snap position:
        		double d = c.radius / GMath.sin(theta);
        		Vector snapDisplacement = velocity.lengthVector(d);

				// Calculate displacement (from intersection) to the contact point:
        		double x = GMath.sqrt((d * d) - c.radiusSquared());
        		Vector contactDisplacement = l.getVector().setLength(x);
        		if (theta > GMath.HALF_PI)
        			contactDisplacement.negate();

        		// Use the displacements to find the snap position and contact point:
        		snapPosition = intersection.minus(snapDisplacement);
        		contactPoint = intersection.minus(contactDisplacement);
        		
        		// If the contact point is not on the static line,
        		// then move on to snap to an end point of the line:
        		snapToEndpoint = !l.boundsContains(contactPoint);
			}

			// Snap to one of the line's end points:
//			snapToEndpoint = false;
			if (snapToEndpoint) {
				// Determine which end point is the anchor point:
				Vector anchor;
				if (snapPosition == null)
					anchor = l.getClosestPoint(motion.end1);
				else
					anchor = l.getClosestPoint(snapPosition);
				
				// Find the displacement from the projection of the anchor
				// onto the line of motion to the snap position:
				double sqrDist = GMath.sqr(anchor.distanceToLine(motion));
				if (sqrDist <= c.radiusSquared()) {
					double d = GMath.sqrt(c.radiusSquared() - sqrDist);
	        		Vector snapDisplacement = velocity.lengthVector(d);
					snapPosition = anchor.projectionOn(motion).minus(snapDisplacement);
					contactPoint = new Vector(anchor);
				}
			}
		}
		
		// If no snap position, then there are no collisions:
		if (snapPosition == null)
			return result;
		
		// Find and set the velocity based on the snap position:
		Vector snapVelocity = snapPosition.minus(c.position);
		if (snapVelocity.length() > velocity.length())
			return result;
		
//		snapVelocity.setLength(GMath.max(0, snapVelocity.length() - GMath.EPSILON));
//		velocity.set(snapVelocity);
		
		result.minTranslationVector.set(snapVelocity);
		result.willCollide   = true;
		result.collisionAxis = new Vector(snapPosition, contactPoint).getPerpendicular();
		
		return result;
	}
	
	
	

	/**
	 * Inner class to represent a collision
	 * result between two shapes.
	 */
	public static class CollisionResult {
		public boolean willCollide;
		public Vector minTranslationVector;
		public Vector collisionAxis;
		public Vector collisionPoint;
		
		public CollisionResult() {
			willCollide          = false;
			minTranslationVector = new Vector();
			collisionAxis        = null;
			collisionPoint       = null;
		}
		
		public CollisionResult(Vector velocity) {
			willCollide          = false;
			minTranslationVector = new Vector(velocity);
			collisionAxis        = null;
			collisionPoint       = null;
		}
		
		
		/** Set this collision result from another collision result object. **/
		public void set(CollisionResult copy) {
			this.willCollide          = copy.willCollide;
			this.minTranslationVector = copy.minTranslationVector;
			this.collisionAxis        = copy.collisionAxis;
			this.collisionPoint       = copy.collisionPoint;
		}
		
		/** Invert the minimum translation vector and return this. **/
		public CollisionResult invertTranslation() {
			minTranslationVector.negate();
			return this;
		}
		
		public void setCollisionAxis(Vector collisionAxis) {
			if (this.collisionAxis == null)
				this.collisionAxis = collisionAxis;
			else
				this.collisionAxis.set(collisionAxis);
		}
		
		public void setWillCollide(boolean willCollide) {
			this.willCollide = willCollide;
		}
	}
}
