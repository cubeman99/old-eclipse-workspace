package common.transform;

import common.GMath;
import common.Vector;


/**
 * Rotation.
 * 
 * @author David Jordan
 */
public class Rotation extends Transformation {

	/** Construct a dummy rotation transformation. **/
	public Rotation() {super();}
	
	/** Define a rotation transformation. **/
	public Rotation(Vector fromPoint, Vector toPoint, Vector anchorPoint) {
		super(fromPoint, toPoint, anchorPoint);
	}
	
	
	@Override
	/** Rotate the given vector around an anchor point. **/
	public Vector apply(Vector v) {
		Vector v2     = v.minus(anchorPoint);
		double newDir = v2.direction() + getTheta();
		return v.set(anchorPoint.plus(Vector.polarVector(v2.length(), newDir)));
	}
	
	/** Return the angle difference of this rotation. **/
	public double getTheta() {
		double dir1 = fromPoint.minus(anchorPoint).direction();
		double dir2 = toPoint.minus(anchorPoint).direction();
		while (dir2 < dir1)
			dir2 += GMath.TWO_PI;
		return (dir2 - dir1);
	}
	
	/** Snap the angle of this rotation by a given interval. **/
	public void snapAngle(double snapInterval) {
		double dir1   = fromPoint.minus(anchorPoint).direction();
		double theta  = getTheta();
		double newDir = dir1 + ((int) ((theta / snapInterval) + 0.5) * snapInterval);
		toPoint.set(anchorPoint.plus(Vector.polarVector(10, newDir)));
	}
}
