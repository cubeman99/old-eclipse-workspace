package common.transform;

import common.Vector;
import common.shape.Line;


/**
 * Mirror Transformation.
 * 
 * @author David Jordan
 */
public class MirrorTransformation extends Transformation {
	private Line axis;
	
	/** Construct a dummy rotation transformation. **/
	public MirrorTransformation() {super();}
	
	/** Define a mirror transformation with a line as the axis. **/
	public MirrorTransformation(Line axis) {
		super();
		this.axis = new Line(axis);
	}
	
	/** Define a mirror transformation with two points to define the axis. **/
	public MirrorTransformation(Vector axisPoint1, Vector axisPoint2) {
		super();
		this.axis = new Line(axisPoint1, axisPoint2);
	}
	
	/** Define a mirror transformation with two points to define the axis. **/
	public MirrorTransformation(double axisPointX1, double axisPointY1, double axisPointX2, double axisPointY2) {
		super();
		this.axis = new Line(axisPointX1, axisPointY1, axisPointX2, axisPointY2);
	}
	
	
	@Override
	/** Flip the given vector over the axis. **/
	public Vector apply(Vector v) {
		Vector v2 = v.minus(axis.end1);
		Vector flip = v2.rejectionOn(axis.getVector()).scale(2);
		return v.set(v.minus(flip));
	}
}
