package com.base.engine.common;


public class Axis {
	public static final int X_AXIS          = 0x001;
	public static final int POSITIVE_X_AXIS = 0x002;
	public static final int NEGATIVE_X_AXIS = 0x004;
	
	public static final int Y_AXIS          = 0x008;
	public static final int POSITIVE_Y_AXIS = 0x010;
	public static final int NEGATIVE_Y_AXIS = 0x020;
	
	public static final int Z_AXIS          = 0x040;
	public static final int POSITIVE_Z_AXIS = 0x080;
	public static final int NEGATIVE_Z_AXIS = 0x100;

	public static final int XZ_PLANE        = X_AXIS | Z_AXIS;
	public static final int XY_PLANE        = X_AXIS | Y_AXIS;
	public static final int ZY_PLANE        = Z_AXIS | Y_AXIS;

	public static final int XZY_SPACE       = X_AXIS | Y_AXIS | Z_AXIS;
	
	public static Vector3f getAxisVector(int axis) {
		if (axis == X_AXIS || axis == POSITIVE_X_AXIS)
			return new Vector3f(1, 0, 0);
		if (axis == NEGATIVE_X_AXIS)
			return new Vector3f(-1, 0, 0);
		if (axis == Y_AXIS || axis == POSITIVE_Y_AXIS)
			return new Vector3f(0, 1, 0);
		if (axis == NEGATIVE_Y_AXIS)
			return new Vector3f(0, -1, 0);
		if (axis == Z_AXIS || axis == POSITIVE_Z_AXIS)
			return new Vector3f(0, 0, 1);
		if (axis == NEGATIVE_Z_AXIS)
			return new Vector3f(0, 0, -1);
		return new Vector3f(0, 0, 0);
	}
}
