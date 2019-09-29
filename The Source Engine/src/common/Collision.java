package common;

import common.shape.Circle;
import common.shape.Line;
import common.shape.Polygon;
import common.shape.Shape;
import map.Map;


/**
 * This static class contains key computations for
 * collisions between shapes.
 * 
 * @author David Jordan
 */
public class Collision {
	public static final int MAX_IMPULSE_ITERATIONS   = 4;      // The max amount of time impulse iterations per step.
	public static final double RESTITUTION_THRESHOLD = 0.0001; // The minimum amount of restitution.
	
	
	
	/**
	 * Get the minimum translation vector of a dynamic shape with a static shape.
	 * This will also return information on the collision axis for determining
	 * future velocity values.
	 * 
	 * @param shape1 - The shape of the dynamic body.
	 * @param shape2 - The shape of the static body.
	 * @param velocity - The velocity of the dynamic body.
	 * @return The final Collision Result
	 */
	public static CollisionResult getMinimumTranslationVector(Shape shape1, Shape shape2, Vector velocity) {

		if (shape1 instanceof Circle) {
			// (N) Circle to Line:
			if (shape2 instanceof Line)
				return solveMinimumTranslation((Circle) shape1, (Line) shape2, velocity);
			
			// (N) Circle to Polygon:
			if (shape2 instanceof Polygon)
				return solveMinimumTranslation((Circle) shape1, (Polygon) shape2, velocity);
			
			// (N) Circle to Circle:
			if (shape2 instanceof Circle)
				return solveMinimumTranslation((Circle) shape1, (Circle) shape2, velocity);
		}
		if (shape1 instanceof Polygon) {
			// (N) Polygon to Line:
			if (shape2 instanceof Line)
				return solveMinimumTranslation((Polygon) shape1, (Line) shape2, velocity);
			
			// (N) Polygon to Polygon:
			if (shape2 instanceof Polygon)
				return solveMinimumTranslation((Polygon) shape1, (Polygon) shape2, velocity);
			
			// (I) Polygon to Circle:
			if (shape2 instanceof Circle)
				return solveMinimumTranslation((Circle) shape1, (Polygon) shape2, velocity).invertTranslation();
		}
		if (shape1 instanceof Line) {
			// (N) Line to Line:
			if (shape2 instanceof Line)
				return solveMinimumTranslation((Line) shape1, (Line) shape2, velocity);
			
			// (I) Line to Polygon:
			if (shape2 instanceof Polygon)
				return solveMinimumTranslation((Polygon) shape1, (Line) shape2, velocity).invertTranslation();
			
			// (I) Line to Circle:
			if (shape2 instanceof Circle)
				return solveMinimumTranslation((Circle) shape1, (Line) shape2, velocity).invertTranslation();
		}
		
		return new CollisionResult(velocity);
	}
	
	
	/** Minimum Translation Vector: Circle to Line **/
	private static CollisionResult solveMinimumTranslation(Circle c, Line l, Vector velocity) {
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
	

	/** Minimum Translation Vector: Circle to Polygon **/
	private static CollisionResult solveMinimumTranslation(Circle c, Polygon p, Vector velocity) {
		CollisionResult result = new CollisionResult(velocity);
		
		for (int i = 0; i < p.edgeCount(); i++) {
			Line l = p.getEdge(i);
			minResultToOut(result, solveMinimumTranslation(c, l, velocity));
		}
		
		return result;
	}


	/** Minimum Translation Vector: Circle to Circle **/
	private static CollisionResult solveMinimumTranslation(Circle c1, Circle c2, Vector velocity) {
		CollisionResult result = new CollisionResult(velocity);
		
		double radiusSum = c1.radius + c2.radius;
		
		Line motion = new Line(c1.position, c1.position.plus(velocity));
		if (c2.position.distanceToSegment(motion) >= radiusSum)
			return result;
		
		Vector projection    = c2.position.projectionOn(motion);
		double distance      = GMath.distance(projection, c2.position);
		double displacement  = GMath.sqrt((radiusSum * radiusSum) - (distance * distance));
		Vector snapPosition  = projection.minus(velocity.lengthVector(displacement));
		
		result.collisionAxis = new Vector(snapPosition, c2.position).getPerpendicular();
		result.willCollide   = true;
		result.minTranslationVector.set(snapPosition.minus(c1.position));
		
		return result;
	}
	

	/** Minimum Translation Vector: Polygon to Line **/
	public static CollisionResult solveMinimumTranslation(Polygon p, Line l, Vector velocity) {
		CollisionResult result = new CollisionResult(velocity);
		
		// Test edges of polygon:
		for (int i = 0; i < p.vertexCount(); i++) {
			Vector v = p.getVertex(i);
			Line motion = new Line(v, v.plus(result.minTranslationVector));
			Vector intersect = Line.intersection(motion, l);
			
			if (intersect != null) {
				result.minTranslationVector.set(v, intersect);
				result.setWillCollide(true);
				result.setCollisionAxis(l.getVector());
			}
		}
		
		// Test Edges of line:
		Vector end = new Vector(l.end1);
		
		for (int x = 0; x < 2; x++) {
			for (int i = 0; i < p.edgeCount(); i++) {
				Line edge = p.getEdge(i);
				Line motion = new Line(end, end.minus(result.minTranslationVector));
				Vector intersect = Line.intersection(motion, edge);
				
				if (intersect != null) {
					result.minTranslationVector.set(new Vector(end, intersect).negate());
					result.setWillCollide(true);
					result.setCollisionAxis(edge.getVector());
				}
			}
			end = l.end2;
		}
		
		if (result.minTranslationVector.length() > 0)
			result.minTranslationVector.setLength(GMath.max(0, result.minTranslationVector.length() - GMath.EPSILON));
		
		return result;
	}
	

	/** Minimum Translation Vector: Polygon to Polygon **/
	private static CollisionResult solveMinimumTranslation(Polygon p1, Polygon p2, Vector velocity) {
		CollisionResult result = new CollisionResult(velocity);
		
		for (int i = 0; i < p2.edgeCount(); i++) {
			Line l = p2.getEdge(i);
			minResultToOut(result, solveMinimumTranslation(p1, l, velocity));
		}
		
		return result;
	}


	/** Minimum Translation Vector: Line to Line **/
	private static CollisionResult solveMinimumTranslation(Line l1, Line l2, Vector velocity) {
		CollisionResult result = new CollisionResult(velocity);
		
		// TODO: write this.
		
		return result;
	}
	
	
	/** Set a collision result to another one only if its minimum translation vector is shorter than the original's.**/
	public static void minResultToOut(CollisionResult result1, CollisionResult result2) {
		if (result2.minTranslationVector.length() < result1.minTranslationVector.length())
			result1.set(result2);
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
	
	
	
	
	
	
	
	
	
	
	/**
	 * 
	 * 
	 * @param map - The map for the body to check collisions in.
	 * @param c - The circular shape of the dynamic body.
	 * @param velocity - The velocity of the dynamic body.
	 * 
	 * @return Whether the body had any collisions.
	 */
	public static boolean applyCollisions(Map map, Circle shape, Vector velocity) {
		double friction    = 0.0; // Right now set to zero
		double restitution = 0.0; // Right now set to zero
		boolean colliding  = false;
//		double elapsedTime = 0.0;
//		double stepTime    = Main.STEP_TIME;
		
		// Cycle through the collision process on equally spaced time impulses:
		for (int j = 0; j < MAX_IMPULSE_ITERATIONS; j++) {
			Vector nextVelocity = new Vector(velocity);
    		Vector snapVelocity = new Vector(velocity.scaledBy(1.0 / (double) MAX_IMPULSE_ITERATIONS));
//    		Vector snapVelocity = new Vector(velocity);
    		
    		// Snap the velocity with all the walls in the map.
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
//    		elapsedTime += (snapVelocity.length() / velocity.length()) * stepTime;
    		
    		// Apply the velocity and set velocity to the next velocity:
    		shape.position.add(snapVelocity);
    		velocity.set(nextVelocity);
    		
    		if (velocity.length() < GMath.EPSILON) {
    			break;
    		}
		}
		
		//System.out.println((elapsedTime / stepTime) * 100);
		
		return colliding;
	}
	
	
	public static boolean applyCollisions(Map map, Polygon shape, Vector velocity) {
		double friction     = 0.0; // Right now set to zero
		double restitution  = 0.0; // Right now set to zero
		boolean colliding   = false;
		
		// Cycle through the collision process on equally spaced time impulses:
		for (int j = 0; j < MAX_IMPULSE_ITERATIONS; j++) {
			Vector nextVelocity = new Vector(velocity);
			Vector snapVelocity = new Vector(velocity.scaledBy(1.0 / (double) MAX_IMPULSE_ITERATIONS));

			for (Polygon p : map.walls) {
				for (int i = 0; i < p.edgeCount(); i++) {
					Line edge = p.getEdge(i);

					Vector alignAxis   = new Vector();
					Vector translation = Collision.getMinTranslationVector(shape, edge, snapVelocity, alignAxis);
					translation.setLength(GMath.max(0, translation.length() - GMath.EPSILON));
					snapVelocity.set(translation);
					
					if (alignAxis.length() > GMath.EPSILON) {
						// Apply friction and restitution:
						Vector vecFriction    = velocity.rejectionOn(alignAxis).scaledBy(1.0 - friction);
						Vector vecRestitution = velocity.projectionOn(alignAxis).scaledBy(restitution);
						if (vecRestitution.length() < RESTITUTION_THRESHOLD)
							vecRestitution.zero();
						nextVelocity.set(vecFriction.minus(vecRestitution));
					}
				}
			}
			
			shape.translate(snapVelocity);
			velocity.set(nextVelocity);
		}
		
		return colliding;
	}
	
	public static Vector getMinTranslationVector(Polygon p, Line l, Vector velocity, Vector alignAxisOut) {
		Vector minTranslation = new Vector(velocity);
		
		// Test edges of polygon:
		for (int i = 0; i < p.vertexCount(); i++) {
			Vector v = p.getVertex(i);
			Line motion = new Line(v, v.plus(minTranslation));
			Vector intersect = Line.intersection(motion, l);
			
			if (intersect != null) {
				minTranslation.set(v, intersect);
				alignAxisOut.set(l.getVector().getPerpendicular());
			}
		}
		
		// Test Edges of line:
		Vector end = new Vector(l.end1);
		
		for (int x = 0; x < 2; x++) {
			for (int i = 0; i < p.edgeCount(); i++) {
				Line edge = p.getEdge(i);
				Line motion = new Line(end, end.minus(minTranslation));
				Vector intersect = Line.intersection(motion, edge);
				
				if (intersect != null) {
					minTranslation.set(new Vector(end, intersect).negate());
					alignAxisOut.set(edge.getVector().getPerpendicular());
				}
			}
			end = l.end2;
		}
		
		return minTranslation;
	}
	
	
	/**
	 * Project a dynamic circular body onto a line (move it to the edge of the line)
	 * thus changing the velocity of the body to conform to that projection.
	 * 
	 * @param l - The static line for the circle to be projected onto.
	 * @param c - The circular shape of the dynamic body.
	 * @param velocity The velocity of the dynamic body.
	 * 
	 * @return The point of contact with the line (if the body will collide).
	 */
	private static Vector projectCircleOnto(Line l, Circle c, Vector velocity) {
		Line motion  = new Line(c.position, c.position.plus(velocity));
		double theta = GMath.angleBetween(velocity.direction(), l.direction());
		
		// Check if the lines are parallel:
		if (theta < GMath.EPSILON)
			return null;
		
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
        		// then move on to snap to an endpoint of the line:
        		snapToEndpoint = !l.boundsContains(contactPoint);
			}

			// Snap to one of the line's endpoints:
//			snapToEndpoint = false;
			if (snapToEndpoint) {
				// Determine which endpoint is the anchor point:
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
			return null;
		
		// Find and set the velocity based on the snap position:
		Vector snapVelocity = snapPosition.minus(c.position);
		if (snapVelocity.length() > velocity.length())
			return null;
//		snapVelocity.setLength(GMath.max(0, snapVelocity.length() - GMath.EPSILON));
		velocity.set(snapVelocity);
		
		// Return the point of contact:
		return contactPoint;
	}
}
