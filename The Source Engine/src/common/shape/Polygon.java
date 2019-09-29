package common.shape;

import java.util.ArrayList;
import common.Draw;
import common.GMath;
import common.Vector;
import common.transform.Transformation;


/**
 * Extension of Shape:
 * A class to represent a polygon
 * composed of multiple vertices.
 * 
 * @author David Jordan
 */
public class Polygon extends Shape {
	private ArrayList<Vector> vertices;
	private boolean closed;

	// ================= CONSTRUCTORS ================= //
	
	public Polygon(Line l) {
		this.vertices = new ArrayList<Vector>();
		this.closed   = true;
		addVertex(l.end1);
		addVertex(l.end2);
	}
	
	public Polygon(Rectangle r) {
		this.vertices = new ArrayList<Vector>();
		this.closed   = true;
		addVertex(r.end1);
		addVertex(r.end2.x, r.end1.y);
		addVertex(r.end2);
		addVertex(r.end1.x, r.end2.y);
	}
	
	public Polygon(Polygon p) {
		this.vertices = new ArrayList<Vector>();
		this.closed   = true;
		for (int i = 0; i < p.vertexCount(); i++)
			addVertex(p.getVertex(i));
	}
	
	public Polygon(Vector...vertices) {
		this.vertices = new ArrayList<Vector>();
		this.closed   = true;
		for (int i = 0; i < vertices.length; i++)
			addVertex(vertices[i]);
	}
	
	public Polygon(ArrayList<Vector> vertices) {
		this.vertices = new ArrayList<Vector>();
		this.closed   = true;
		for (int i = 0; i < vertices.size(); i++)
			addVertex(vertices.get(i));
	}
	

	
	// ================== ACCESSORS ================== //
	
	/** Get the vertex at a given index. **/
	public Vector getVertex(int index) {
		while (index < 0)
			index += vertexCount();
		return vertices.get(index % vertexCount());
	}
	
	/** Get the number of vertices. **/
	public int vertexCount() {
		return vertices.size();
	}
	
	/** Get the number of edges. **/
	public int edgeCount() {
		return (closed ? vertices.size() : Math.max(0, vertices.size() - 1));
	}
	
	/** Get a line representing the edge of two vertices. (one at index, one after index) **/
	public Line getEdge(int index) {
		return new Line(
			vertices.get(GMath.getWrappedValue(index, vertexCount())),
			vertices.get(GMath.getWrappedValue(index + 1, vertexCount()))
		);
	}
	
	/** Return whether the polygon is a closed polygon. **/
	public boolean isClosed() {
		return closed;
	}
	
	
	// ================== MUTATORS ================== //
	
	/** Add a vertex (x and y) to this polygon. **/
	public void addVertex(double x, double y) {
		addVertex(new Vector(x, y));
	}

	/** Add a vertex (vector) to this polygon. **/
	public void addVertex(Vector vertex) {
		vertices.add(new Vector(vertex));
	}

	/** Insert a vertex at the given index, pushing the vertex in that index up. **/
	public void addVertex(int index, Vector vertex) {
		vertices.add(index, new Vector(vertex));
	}
	
	/** Remove a vertex at the given index from this polygon. **/
	public void removeVertex(int index) {
		vertices.remove(index);
	}
	
	/** Remove a given vertex from the polygon. **/
	public void removeVertex(Vector vertex) {
		vertices.remove(vertex);
	}
	
	/** Set whether this polygon is closed or not. **/
	public void setClosed(boolean closed) {
		this.closed = closed;
	}
	

	// ================ INHERITED METHODS ================ //
	
	@Override
	/** Get the area of the polygon. **/
	public double computeArea() {
		// TODO: write this function
		return 0;
	}
	
	@Override
	/** Compute and return the center of the polygon. **/
	public Vector computeCenter() {
		center = new Vector();
		for (int i = 0; i < vertexCount(); i++)
			center.add(getVertex(i));
		center.scale(1.0 / (double) vertexCount());
		return center;
	}

	@Override
	/** Get a rectangular bounding box for the polygon. **/
	public Rectangle getBounds() {
		if (vertexCount() == 0)
			return null;
		
		Vector min = new Vector(getVertex(0));
		Vector max = new Vector(getVertex(0));
		
		for (int i = 1; i < vertexCount(); i++) {
			Vector v = getVertex(i);
			if (v.x < min.x)
				min.x = v.x;
			if (v.y < min.y)
				min.y = v.y;
			if (v.x > max.x)
				max.x = v.x;
			if (v.y > max.y)
				max.y = v.y;
		}
		
		return new Rectangle(min, max);
	}
	
	@Override
	/** Return a copy of this polygon. **/
	public Polygon getCopy() {
		return new Polygon(this);
	}

	@Override
	/** Apply a transformation to this polygon. **/
	public Polygon transform(Transformation t) {
		for (int i = 0; i < vertexCount(); i++)
			t.apply(getVertex(i));
		return this;
	}
	
	@Override
	/** Draw the polygon. **/
	public void draw() {
		Draw.drawPolygon(this);
	}
	
	@Override
	/** Draw the filled polygon. **/
	public void drawFill() {
		Draw.fillPolygon(this);
	}
	
	

	// ================= UNUSED METHODS ================= //
	
	/** Structure that stores the results of the PolygonCollision function **/
	public static class PolygonCollisionResult {
		public boolean willIntersect; // Are the polygons going to intersect forward in time?
		public boolean intersect; // Are the polygons currently intersecting
		public Vector minimumTranslationVector; // The translation to apply to polygon A to push the polygons appart.
		public Vector newVelocity;
		public Line edge;
		
		public PolygonCollisionResult() {
			willIntersect = false;
			intersect     = false;
			newVelocity   = new Vector();
			minimumTranslationVector = new Vector();
			edge = new Line();
		}
		
		public String toString() {
			return ("[" + willIntersect + ", " + intersect + ", " + minimumTranslationVector + "]");
		}
	}

	/** Check if polygon A is going to collide with polygon B for the given velocity **/
	public static PolygonCollisionResult polygonCollision(Polygon polygonA, Polygon polygonB, Vector velocity) {
		PolygonCollisionResult result = new PolygonCollisionResult();
		result.intersect     = true;
		result.willIntersect = true;
		result.newVelocity.set(velocity);
		
		int edgeCountA = polygonA.edgeCount();
		int edgeCountB = polygonB.edgeCount();
		double minIntervalDistance = Double.MAX_VALUE;
		Vector translationAxis = new Vector();
		Vector edge;

		// Loop through all the edges of both polygons
		for (int edgeIndex = 0; edgeIndex < edgeCountA + edgeCountB; edgeIndex++) {
			if (edgeIndex < edgeCountA) {
				edge = polygonA.getEdge(edgeIndex).getVector();
			}
			else {
				edge = polygonB.getEdge(edgeIndex - edgeCountA).getVector();
			}

			// ===== 1. Find if the polygons are currently intersecting =====

			// Find the axis perpendicular to the current edge
			Vector axis = edge.getPerpendicular().normalize();

			// Find the projection of the polygon on the current axis
			double minA = 0;
			double minB = 0;
			double maxA = 0;
			double maxB = 0;
			Vector p1   = projectPolygon(axis, polygonA, minA, maxA);
			minA        = p1.x;
			maxA        = p1.y;
			Vector p2   = projectPolygon(axis, polygonB, minB, maxB);
			minB        = p2.x;
			maxB        = p2.y;
			
			// Check if the polygon projections are currently intersecting
			if (intervalDistance(minA, maxA, minB, maxB) > 0)
				result.intersect = false;

			// ===== 2. Now find if the polygons *will* intersect =====

			// Project the velocity on the current axis
			double velocityProjection = axis.dot(velocity);

			// Get the projection of polygon A during the movement
			if (velocityProjection < 0) {
				minA += velocityProjection;
			}
			else {
				maxA += velocityProjection;
			}

			// Do the same test as above for the new projection
			double intervalDistance = intervalDistance(minA, maxA, minB, maxB);
			if (intervalDistance > 0)
				result.willIntersect = false;

			// If the polygons are not intersecting and won't intersect, exit
			// the loop
			if (!result.intersect && !result.willIntersect)
				break;

			// Check if the current interval distance is the minimum one. If so
			// store
			// the interval distance and the current distance.
			// This will be used to calculate the minimum translation vector
			if (edgeIndex < edgeCountA) {
				result.edge = polygonA.getEdge(edgeIndex);
			}
			else {
				result.edge = polygonB.getEdge(edgeIndex - edgeCountA);
			}
			System.out.print("E");
			result.newVelocity.set(result.newVelocity.rejectionOn(edge.getPerpendicular()));
			
			intervalDistance = GMath.abs(intervalDistance);
			if (intervalDistance < minIntervalDistance) {
				minIntervalDistance = intervalDistance;
				translationAxis = axis;

				Vector d = polygonA.center.minus(polygonB.center);
				if (d.dot(translationAxis) < 0)
					translationAxis.negate();
			}
		}

		// The minimum translation vector can be used to push the polygons
		// apart.
		// First moves the polygons by their velocity
		// then move polygonA by MinimumTranslationVector.
		if (result.willIntersect)
			result.minimumTranslationVector = translationAxis.scaledBy(minIntervalDistance);

		return result;
	}

	/** Calculate the distance between [minA, maxA] and [minB, maxB]
	 * The distance will be negative if the intervals overlap
	 */
	public static double intervalDistance(double minA, double maxA, double minB, double maxB) {
		if (minA < minB)
			return minB - maxA;
		return minA - maxB;
	}
	
	/** Calculate the projection of a polygon on an axis and returns it as a [min, max] interval **/
	public static Vector projectPolygon(Vector axis, Polygon polygon, double min, double max) {
		// To project a point on an axis use the dot product
		double d = axis.dot(polygon.getVertex(0));
		min = d;
		max = d;
		for (int i = 0; i < polygon.vertexCount(); i++) {
			d = polygon.getVertex(i).dot(axis);
			if (d < min) {
				min = d;
			}
			else {
				if (d > max) {
					max = d;
				}
			}
		}
		return new Vector(min, max);
	}
}
