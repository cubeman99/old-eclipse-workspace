package collision;

import java.awt.Graphics;
import common.GMath;
import common.Line;
import common.Ray;
import common.Settings;
import common.Vector;

public class Polygon extends Shape {
	
	public Vector[] vertices;
	public int vertexCount;
	
	
	public Polygon(Vector position) {
		super(position);
		
		vertices    = new Vector[Settings.MAX_POLYGON_VERTICES];
		vertexCount = 0;
		for (Vector v : vertices)
			v = new Vector();
	}

	/** Add a vertex (x and y) to this polygon. **/
	public void addVertex(double x, double y) {
		addVertex(new Vector(x, y));
		computeAreaAndCentroid();
	}

	/** Add a vertex (vector) to this polygon. **/
	public void addVertex(Vector vertex) {
		if (vertexCount < vertices.length) {
			vertices[vertexCount] = new Vector(vertex);
			vertexCount++;
		}
	}

	/** Check if touching a circle. **/
	public boolean touching(Circle shape) {
		return false;
	}

	/** Check if touching a line. **/
	public boolean touching(Line line) {
		return false;
	}

	/** Return the absolute position of a vertex. **/
	public Vector absoluteVertex(int index) {
		if (index >= vertexCount)
			return null;
		return vertices[index].plus(position);
	}
	
	/** Return the absolute line of an edge **/
	public Line absoluteEdge(int index) {
		if (index >= vertexCount)
			return null;
		Vector endVertex = (index == vertexCount - 1) ? vertices[0] : vertices[index + 1];
		return new Line(vertices[index].plus(position), endVertex.plus(position));
	}

	/** 
	 * Return if this polygon is legal. That is, the area must be
	 * greater than zero and it must have at least three sides.
	 */
	public boolean isValid() {
		return (area > 0.0 && vertexCount > 2);
	}

	/** Compute the centroid of this polygon **/
	public void computeAreaAndCentroid() {
		centroid.zero();
		area = 0.0;
		
		// Must have three vertices to define a polygon.
		if (vertexCount < 3)
			return;
		
		for (int i = 0; i < vertexCount; i++) {
			Vector nextVertex = (i < vertexCount - 1) ? vertices[i + 1] : vertices[0];
			double a    = (vertices[i].x * nextVertex.y) - (nextVertex.x * vertices[i].y);
			area       += a;
			centroid.x += (vertices[i].x + nextVertex.x) * a;
			centroid.y += (vertices[i].y + nextVertex.y) * a;
		}
		
		area *= 0.5;
		centroid.scale(1.0 / (6.0 * area));
	}

	public void collide(Line l) {
		if (!isValid())
			return;
		
		
	}
	
	public void collideSnap(Line l) {
		
	}
	
	public void collide2(Line l) {
		
		if (!isValid())
			return;

		Vector closestVertex    = null;
		double closestDistance  = 0;
		Vector farthestVertex   = null;
		double farthestDistance = 0;
		for (int i = 0; i < vertexCount; i++) {
			Vector v = vertices[i].plus(position);
			double dist = v.distanceTo(l.getClosestPoint(v));
			if (dist < closestDistance || i == 0) {
				closestVertex   = v;
				closestDistance = dist;
			}
			if (dist > farthestDistance || i == 0) {
				farthestVertex   = v;
				farthestDistance = dist;
			}
		}
		double endDistance = closestVertex.distanceTo(farthestVertex);
		double lineDir  = l.getVector().direction();
		
		Vector closest  = l.getClosestPoint(closestVertex);
		Vector moveVec  = new Vector(closestVertex, closest);
		
		double testDist = GMath.distance(closest, closestVertex) + GMath.distance(closest, farthestVertex);

		
		if (closest.distanceTo(farthestVertex) < endDistance) {
			body.world.debugPoint = closest;
			position.set(position.plus(moveVec));
			
			body.motion.set(
					body.motion.rejectionOn(moveVec).scaledBy(1.0 - body.friction).minus(
					body.motion.projectionOn(moveVec).scaledBy(body.restitution)
			));
			
//			body.limitDirection(lineDir + GMath.HALF_PI);
//			body.limitDirection(moveVec.direction());
		}
	}
	
	public void collideOLD(Line l) {
		if (!isValid())
			return;
		
		Vector closestVertex   = null;
		double closestDistance = 0;
		for (int i = 0; i < vertexCount; i++) {
			Vector v = vertices[i].plus(positionLast);
			double dist = v.distanceTo(l.getClosestPoint(v));
			if (dist < closestDistance || i == 0) {
				closestVertex   = v;
				closestDistance = dist;
			}
		}
		
		Vector movement = new Vector(positionLast, position);
		
		Line displacement = new Line(closestVertex, closestVertex.plus(movement));
		Vector intersect  = l.intersection(displacement);
		
		if (intersect != null) {
			body.world.debugPoint = intersect;
			position = positionLast.plus(intersect.minus(closestVertex));
			body.limitDirection(l.getVector().getPerpendicular().direction());
			//body.motion.zero();
		}
	}
	
	public void collide(Circle c) {
		
	}
	
	public Vector rayCast(Ray r) {
		Vector output = null;
		Line rayLine = new Line(r.position, r.position.plus(Vector.vectorFromPolar(1000, r.direction)));
		double closestDistance = 0.0;

		for (int i = 0; i < vertexCount; i++) {
			//Vector v = rayLine.intersection(absoluteEdge(i));
			Vector v = absoluteEdge(i).intersection(rayLine);
			if (v != null) {
				if (GMath.abs(GMath.direction(r.position, v) - r.direction) < 0.1) {
        			double dist = v.distanceTo(r.position);
        			if (dist < closestDistance || output == null) {
        				output  = new Vector(v);
        				closestDistance = dist;
        			}
				}
			}
		}
		return output;
	}
	
	public AABB getAABB() {
		if (vertexCount < 2)
			return new AABB();

		Vector min = new Vector(vertices[0]);
		Vector max = new Vector(vertices[0]);
		for (int i = 1; i < vertexCount; i++) {
			min.x = GMath.min(min.x, vertices[i].x);
			min.y = GMath.min(min.y, vertices[i].y);
			max.x = GMath.max(max.x, vertices[i].x);
			max.y = GMath.max(max.y, vertices[i].y);
		}
		
		return new AABB(min.plus(position), max.plus(position));
	}
	
	public void draw(Graphics g) {
		for (int i = 0; i < vertexCount; i++) {
			Vector nextVertex = vertices[0];
			if (i < vertexCount - 1)
				nextVertex = vertices[i + 1];
			g.drawLine((int) (position.x + vertices[i].x),
					   (int) (position.y + vertices[i].y),
					   (int) (position.x + nextVertex.x),
					   (int) (position.y + nextVertex.y));
		}
	}
}
