package common.transform;

import common.Vector;


/**
 * Translation.
 * 
 * @author David Jordan
 */
public class Translation extends Transformation {

	/** Construct a dummy translation transformation. **/
	public Translation() {super();}

	/** Define a translation as a displacement. **/
	public Translation(Vector displacement) {
		super(new Vector(), displacement, new Vector());
	}
	
	/** Define a translation from one point to another. **/
	public Translation(Vector fromPoint, Vector toPoint) {
		super(new Vector(fromPoint), new Vector(toPoint), new Vector(fromPoint));
	}
	
	
	/** Return a vector representing the displacement of the translation. **/
	public Vector getDisplacement() {
		return new Vector(fromPoint, toPoint);
	}
	
	@Override
	/** Translate the given vector by the displacement of 'from' and 'to'. **/
	public Vector apply(Vector v) {
		return v.add(getDisplacement());
	}
}
