package common.transform;

import common.GMath;
import common.Vector;


/**
 * Scalation.
 * 
 * @author David Jordan
 */
public class Scalation extends Transformation {

	/** Construct a dummy scale transformation. **/
	public Scalation() {super();}
	
	/** Define a scalation. **/
	public Scalation(Vector fromPoint, Vector toPoint, Vector anchorPoint) {
		super(fromPoint, toPoint, anchorPoint);
	}
	
	
	@Override
	/** Scale the given vector from an anchor point. **/
	public Vector apply(Vector v) {
		Vector to    = toPoint.minus(anchorPoint);
		Vector from  = fromPoint.minus(anchorPoint);
		Vector scale = new Vector(1, 1);
		
		if (GMath.abs(from.x) > GMath.EPSILON)
			scale.x = to.x / from.x;
		if (GMath.abs(from.y) > GMath.EPSILON)
			scale.y = to.y / from.y;
		
		return v.scale(anchorPoint, scale);
	}
}
