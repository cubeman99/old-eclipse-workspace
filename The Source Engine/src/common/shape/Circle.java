package common.shape;

import common.Draw;
import common.GMath;
import common.Vector;
import common.transform.Transformation;


/**
 * Extension of Shape:
 * A class to represent a circle with
 * a position and radius.
 * 
 * @author David Jordan
 */
public class Circle extends Shape {
	public Vector position;
	public double radius;
	
	
	// ================= CONSTRUCTORS ================= //
	
	public Circle(double radius) {
		this(new Vector(), radius);
	}
	
	public Circle(Vector position, double radius) {
		this.position = new Vector(position);
		this.radius   = radius;
	}
	
	public Circle(double x, double y, double radius) {
		this.position = new Vector(x, y);
		this.radius   = radius;
	}
	
	public Circle(Circle copy) {
		this.position = new Vector(copy.position);
		this.radius   = copy.radius;
	}
	
	

	// ================== ACCESSORS ================== //
	
	/** Return if this and another circle are touching **/
	public boolean touching(Circle c) {
		return (GMath.distance(position, c.position) < radius + c.radius);
	}
	
	/** Return if this and a line segment are touching **/
	public boolean touching(Line l) {
		return (position.distanceTo(l.getClosestPoint(position)) < radius);
	}
	
	/** Return the circumference. (2 * pi * r) **/
	public double getCircumference() {
		return (2 * GMath.PI * radius);
	}
	
	/** Return the diameter. (2 * r) **/
	public double getDiameter() {
		return (2 * radius);
	}
	
	/** Return the squared radius. **/
	public double radiusSquared() {
		return (radius * radius);
	}
	
	
	
	// ================== MUTATORS ================== //

	/** Set the circle's position and radius. **/
	public Circle set(Vector position, double radius) {
		this.position = new Vector(position);
		this.radius   = radius;
		return this;
	}
	
	
	
	// ================ INHERITED METHODS ================ //

	@Override
	/** Compute and return the area of the circle. **/
	public double computeArea() {
		area = GMath.PI * radius * radius;
		return area;
	}

	@Override
	@Deprecated
	public Vector computeCenter() {
		center = new Vector(position);
		return center;
	}


	/** Return the rectangular bounding box for this circle. **/
	@Override
	public Rectangle getBounds() {
		return new Rectangle(position.minus(radius, radius), position.plus(radius, radius));
	}

	@Override
	/** Return a copy of this circle. **/
	public Circle getCopy() {
		return new Circle(this);
	}

	@Override
	/** Apply a transformation to this circle. **/
	public Circle transform(Transformation t) {
		t.apply(position);
		// TODO change the radius somehow
		return this;
	}
	
	@Override
	/** Draw the circle. **/
	public void draw() {
		Draw.drawCircle(this);
	}
	
	@Override
	/** Draw the filled circle. **/
	public void drawFill() {
		Draw.fillCircle(this);
	}
}
