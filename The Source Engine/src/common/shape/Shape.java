package common.shape;

import common.Vector;
import common.transform.Rotation;
import common.transform.Scalation;
import common.transform.Transformation;
import common.transform.Translation;

/**
 * The abstract shape class is used to represent a shape
 * that has certain boundaries, can be drawn, and can be
 * translated, etc.
 * 
 * @see Line
 * @see Circle
 * @see Polygon
 * @see Rectangle
 * 
 * @author David Jordan
 */
public abstract class Shape {
	public Vector center = null;
	public double area   = -1.0;
	
	
	// ================== ACCESSORS ================== //

	/** Get the area of this shape. */
	public abstract double computeArea();
	
	/** Get the center point of this shape. **/
	public abstract Vector computeCenter();

	/** Return if a point is contained in this shape **/
//	public abstract boolean contains(Vector point);
	
	/** Return a rectangular bounding box for this shape. **/
	public abstract Rectangle getBounds();
	
	/** Return a copy of this shape. **/
	public abstract Shape getCopy();
	
	
	
	// ================== MUTATORS ================== //

	/** Apply a transformation to this shape. **/
	public abstract Shape transform(Transformation t);
	
	/** Draw the shape outline. **/
	public abstract void draw();
	
	/** Draw the shape, filling in the middle. **/
	public abstract void drawFill();
	
	
	
	// ================ FINAL ACCESSORS ================ //

	/** Return a transformed version of this shape. **/
	public final Shape getTransformed(Transformation t) {
		return getCopy().transform(t);
	}
	
	/** Return a translated version of this shape. **/
	public final Shape getTranslated(Vector translation) {
		return getCopy().translate(translation);
	}
	
	/** Return a translated version of this shape. **/
	public final Shape getTranslated(Vector fromPoint, Vector toPoint) {
		return getCopy().translate(fromPoint, toPoint);
	}
	
	/** Return a scaled version of this shape. **/
	public final Shape getScaled(Vector fromPoint, Vector toPoint, Vector anchorPoint) {
		return getCopy().scale(fromPoint, toPoint, anchorPoint);
	}
	
	/** Return a rotated version of this shape. **/
	public final Shape getRotated(Vector fromPoint, Vector toPoint, Vector anchorPoint) {
		return getCopy().rotate(fromPoint, toPoint, anchorPoint);
	}
	
	/** Return if a point is contained within this shape's bounding box. **/
	public boolean boundsContains(Vector v) {
		return getBounds().contains(v);
	}
	
	/** Return if a point is contained in this shape **/
	public final boolean isTouching(Shape shape) {
		// TODO: write this function.
		return false;
	}
	
	/** Get the area of this shape / compute it if needed. */
	public final double getArea() {
		return (area < 0) ? computeArea() : area;
	}
	
	/** Get the center of this shape / compute it if needed. */
	public final Vector getCenter() {
		return (center == null) ? computeCenter() : center;
	}
	
	
	
	// ================ FINAL MUTATORS ================ //
	
	/** Translate this shape by displacement. **/
	public final Shape translate(Vector displacement) {
		return transform(new Translation(displacement));
	}
	
	/** Translate this shape by the displacement of 'from' to 'to'. **/
	public final Shape translate(Vector fromPoint, Vector toPoint) {
		return transform(new Translation(fromPoint, toPoint));
	}
	
	/** Scale this shape from an anchor point. **/
	public final Shape scale(Vector fromPoint, Vector toPoint, Vector anchorPoint) {
		return transform(new Scalation(fromPoint, toPoint, anchorPoint));
	}
	
	/** Rotate this shape around an anchor point. **/
	public final Shape rotate(Vector fromPoint, Vector toPoint, Vector anchorPoint) {
		return transform(new Rotation(fromPoint, toPoint, anchorPoint));
	}
}
