package collision;

import java.awt.Color;
import java.awt.Graphics;
import main.Keyboard;
import common.GMath;
import common.IntersectionSolutions;
import common.Line;
import common.Ray;
import common.Vector;
import dynamics.Body;


public class Circle extends Shape {
	public double radius;
	
	public Circle(Vector center, double radius) {
		super(center);
		this.radius = radius;
		computeAreaAndCentroid();
	}
	
	public double x() {
		return position.x;
	}
	
	public double y() {
		return position.y;
	}
	
	public double x1() {
		return (position.x - radius);
	}
	
	public double y1() {
		return (position.y - radius);
	}
	
	public double x2() {
		return (position.x + radius);
	}
	
	public double y2() {
		return (position.y + radius);
	}
	
	/** Return if this and another circle are touching **/
	public boolean touching(Circle shape) {
		return (GMath.distance(position, shape.position) < radius + shape.radius);
	}
	
	/** Return if this and another line are touching **/
	public boolean touching(Line line) {
		return (position.distanceTo(line.getClosestPoint(position)) < radius);
	}
	
	/** Return/calculate the circumference. (2 * pi * r) **/
	public double circumference() {
		return (2 * GMath.PI * radius);
	}
	
	/** Return the diameter. (2 * r) **/
	public double diameter() {
		return (2 * radius);
	}

	/**
	 * Compute the area and centroid of this circle. The area is equal
	 * to pi*r^2 and the centroid is always the position (relatively zero).
	 */
	public void computeAreaAndCentroid() {
		area     = GMath.PI * radius * radius;
		centroid.zero();
	}
	
	public AABB getAABB() {
		return new AABB(x1(), y1(), x2(), y2());
	}
	
	public Vector setInterceptionPoint(Vector newPositionOut, Line l, Line motion) {
		//System.out.println(position);
		double theta = GMath.angleBetween(motion.direction(), l.direction());
		if (theta < GMath.EPSILON) {
			// Parallel Lines:
			System.out.println("Parallel!");
			return null;
		}
		Vector intersection   = Line.intersectionEndless(motion, l);
		double minDist        = Line.shortestDistance(motion, l);
		Vector snapPos        = null;
		Vector collisionPoint = null;
		
		
		if (minDist < radius - GMath.EPSILON) {
			boolean method2 = true;

			if (intersection != null) {
				System.out.println("Intersection: " + intersection);
				// SNAP TO THE LINE:
        		double dd = radius / GMath.sin(theta);
        		snapPos = intersection.minus(motion.getVector().setLength(dd));
        		
        		double xx = GMath.sqrt((dd * dd) - (radius * radius));
        		Vector displacement = l.getVector().setLength(xx);
        		if (theta > GMath.HALF_PI)
        			displacement.negate();
        		
        		collisionPoint = intersection.minus(displacement);
        		method2        = !l.aabbContains(collisionPoint);
			}

			if (method2) {
				// SNAP TO AN ENDPOINT:
				Vector anchor;
				if (snapPos == null)
					anchor = l.getClosestPoint(motion.end1);
				else
					anchor = l.getClosestPoint(snapPos);
				
				collisionPoint = new Vector(anchor);
				
				
				double dist   = anchor.distanceToLine(motion);
				
				if (dist * dist <= radius * radius) {
					double dd     = GMath.sqrt((radius * radius) - (dist * dist));
    				snapPos       = anchor.projectionOn(motion).minus(motion.getVector().setLength(dd));
    				System.out.println(dd);
				}
			}
		}
		if (snapPos == null)
			snapPos = motion.end2;
		Vector newSet = snapPos.minus(motion.end1);
		if (newSet.length() > motion.length()) {
			System.out.println("New motion out of bounds: " + newSet.length());
			body.world.debugPoint = collisionPoint;
			return null;
		}
		newPositionOut.set(newSet);
		
		// Return the collision point:
		return collisionPoint;
	}

	public void collide(Line l) {
		//	return;
		if (touching(l)) {
			collideSnap(l);
			return;
		}
		if (body.motion.x == 0 && body.motion.y == 0)
			return;
		body.collisionPoint = setInterceptionPoint(body.motion, l, new Line(position, position.plus(body.motion)));
		if (body.collisionPoint != null) {
			body.limitDirection(GMath.direction(position.plus(body.motion), body.collisionPoint));
		}
	}
	
	
	public void collideSnap(Line l) {
		if (!touching(l))
			return;
		double lineDir = l.getVector().direction();
		Vector closest = l.getClosestPoint(position);
		Vector moveVec = new Vector(closest, position);
		moveVec.setLength(radius);
		position.set(closest.plus(moveVec));
		
//		body.limitDirection(lineDir + GMath.HALF_PI);
		
		body.motion.set(
				body.motion.rejectionOn(moveVec).scaledBy(1.0 - body.friction).minus(
				body.motion.projectionOn(moveVec).scaledBy(body.restitution)
		));
		
		
		Vector testVec = new Vector(body.motion).setDirection(body.motion.direction() - lineDir);
		Vector testVec2 = position.minus(l.end1);
		testVec2.setDirection(testVec2.direction() - lineDir);
		Vector finVec = new Vector(testVec.x, testVec2.y);
		
		double sign = -GMath.sign(-finVec.x * finVec.y);
		
		body.anglularVelocity = (sign * body.motion.length()) / radius;// * (Math.PI / 180);//( / shape.circumference()) * 2 * Math.PI;*/
	}
	
	public void collideOLD(Line l) {
		Vector nextPos = position;
		
		Vector vecNearest = new Vector(l.getClosestPoint(nextPos));
		
		if (!touching(l))
			return;
		
		Vector vecSnap = new Vector(nextPos);
		vecSnap.sub(vecNearest).normalize().scale(radius);
		
		Vector vecOutline = new Vector(nextPos);
		vecOutline.sub(vecOutline);
		
		vecSnap.set(vecNearest).add(vecOutline);
		Vector vecNewPos = new Vector(vecNearest);
		vecNewPos.add(vecSnap);
		
		vecSnap.set(vecNearest).sub(nextPos).normalize().scale(radius);
		
		vecNewPos.set(vecNearest).sub(vecSnap);
		//position.set(vecNewPos);
		position.set(vecNewPos);
//		body.stepMovement.set(new Vector(position, vecNewPos));
/*
		if (body.motion.length() < 0.5 || body.restitution < GMath.EPSILON)
			body.limitDirection(vecSnap.direction());
		else
			body.motion.reflectOverDirection(vecSnap.direction()).scale(body.restitution);
		*/
//		Vector reflectVector = Vector.vectorFromPolar(1, vecSnap.direction());
//		body.motion.set(
//				body.motion.rejectionOn(reflectVector).scaledBy(1.0 - body.friction).plus(
//				body.motion.projectionOn(reflectVector).scaledBy(body.restitution)
//		));
		
		body.limitDirection(vecSnap.direction());
		//body.motion.reflectOverDirection(vecSnap.direction(), body.friction, body.restitution);
		
		double dirDif = l.getVector().direction();
		Vector testVec = new Vector(body.motion).setDirection(body.motion.direction() - dirDif);
		Vector testVec2 = position.minus(l.end1);
		testVec2.setDirection(testVec2.direction() - dirDif);
		Vector finVec = new Vector(testVec.x, testVec2.y);
		
		double sign = -GMath.sign(-finVec.x * finVec.y);
		
		body.anglularVelocity = (sign * body.motion.length()) / radius;// * (Math.PI / 180);//( / shape.circumference()) * 2 * Math.PI;*/
		
	}
	
	public void collide(Circle c) {
		Vector nextPosition = position.plus(body.motion);
		Line motionLine     = new Line(position, nextPosition);
		
		//IntersectionSolutions intersect = motionLine.intersectCircle(c);
		boolean colliding = touching(c);
		
		if (colliding) {
			Vector v = position.minus(c.position);
			v.normalize().scale(c.radius);
			
			//collisionPoints.add(new Vector(p.x, p.y).add(v));
			
			v.normalize().scale(c.radius + radius);
			
			position.set(c.position.plus(v));
			
			if (body.motion.length() < 0.5 || body.restitution < GMath.EPSILON)
				body.limitDirection(GMath.direction(position, c.position));
			else
				body.motion.reflectOverDirection(GMath.direction(position, c.position)).scale(body.restitution);
		}
	}
	
	public Vector rayCast(Ray r) {
		Vector output = new Vector();
		
		
		
		return output;
	}
	
	
	/** Draw the circle. **/
	public void draw(Graphics g) {
		g.drawOval((int) x1(), (int) y1(), (int) diameter(), (int) diameter());
		if (body != null) {
    		Vector angleVector = position.plus(Vector.vectorFromPolar(radius, body.angle));
    		g.drawLine((int) position.x, (int) position.y, (int) angleVector.x, (int) angleVector.y);
		}
	}
}
