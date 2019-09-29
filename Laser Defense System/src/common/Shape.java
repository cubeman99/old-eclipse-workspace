package common;

import common.Vector;

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
	
}
