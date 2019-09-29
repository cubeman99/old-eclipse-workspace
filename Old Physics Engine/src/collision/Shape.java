package collision;

import java.awt.Graphics;
import common.Line;
import common.Ray;
import common.Vector;
import dynamics.Body;

public abstract class Shape {
	public Body body;
	public Vector position;
	public Vector positionLast;
	public double area;
	public Vector centroid;
	
	public Shape(Vector position) {
		this.position     = new Vector(position);
		this.positionLast = new Vector(position);
		this.centroid     = new Vector();
		this.area         = 0.0;
	}
	
	public void setBody(Body body) {
		this.body = body;
	}
	
	public void collide(Shape shape) {
		if (shape instanceof Circle)
			collide((Circle) shape);
		else if (shape instanceof Polygon)
			;//collide((Circle) shape);
	}

	/** Return if this shape and another circle are touching **/
	public abstract boolean touching(Circle shape);
	
	/** Return if this shape and another line are touching **/
	public abstract boolean touching(Line line);
	
	/** Collide with a line **/
	public abstract void collide(Line l);
	
	public abstract void collideSnap(Line l);
	
	/** Collide with a circle **/
	public abstract void collide(Circle c);

	/** Get the AABB bounding box of this shape. **/
	public abstract AABB getAABB();
	
	/** Get the closest point on a polygon from a ray **/
	public abstract Vector rayCast(Ray r);

	/** Compute the centroid (center of mass) for this shape.  **/
	public abstract void computeAreaAndCentroid();
	
	/** Draw the shape **/
	public abstract void draw(Graphics g);
}
