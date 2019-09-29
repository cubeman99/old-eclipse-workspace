package common.transform;

import common.Vector;
import common.shape.Shape;


/**
 * Transformation.
 * 
 * @author David Jordan
 */
public abstract class Transformation {
	protected Vector fromPoint;
	protected Vector toPoint;
	protected Vector anchorPoint;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	/** Define a translation with filler variables. **/
	public Transformation() {
		this.fromPoint   = new Vector();
		this.toPoint     = new Vector();
		this.anchorPoint = new Vector();
	}
	
	/** Define a translation with 'from' and 'to' points and an anchor point. **/
	public Transformation(Vector fromPoint, Vector toPoint, Vector anchorPoint) {
		this.fromPoint   = fromPoint;
		this.toPoint     = toPoint;
		this.anchorPoint = anchorPoint;
	}
	
	
	
	// ================ ABSTRACT METHODS ================ //
	
	/** Apply this transformation to a vector. **/
	public abstract Vector apply(Vector v);
	
	
	
	// =================== ACCESSORS =================== //
	
	/** Return the anchor point. **/
	public Vector getAnchorPoint() {
		return anchorPoint;
	}

	/** Return the 'from' point. **/
	public Vector getFromPoint() {
		return fromPoint;
	}

	/** Return the 'to' point. **/
	public Vector getToPoint() {
		return toPoint;
	}
	
	/** Return a transformed version of a given shape. **/
	public Shape getApplied(Shape s) {
		return s.getCopy().transform(this);
	}
	
	
	
	// ==================== MUTATORS ==================== //

	/** Set the anchor point. **/
	public void setAnchorPoint(Vector anchorPoint) {
		this.anchorPoint = anchorPoint;
	}

	/** Set the 'from' point. **/
	public void setFromPoint(Vector fromPoint) {
		this.fromPoint = fromPoint;
	}

	/** Set the 'to' point. **/
	public void setToPoint(Vector toPoint) {
		this.toPoint = toPoint;
	}
	
	/** Apply this transformation to a shape. **/
	public Shape apply(Shape s) {
		return s.transform(this);
	}
}
