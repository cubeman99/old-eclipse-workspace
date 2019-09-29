package map;

import common.Vector;
import common.shape.Polygon;
import common.shape.Rectangle;
import common.shape.Shape;


public abstract class MapObject {
	public static final int TYPE_POLYGON = 0;
	public static final int TYPE_POINT   = 1;
	
	public Shape shape;
	public double angle;
	public int type;
	protected boolean finalVertices;
	protected boolean fixedVertices;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public MapObject() {
		this(null);
	}
	
	public MapObject(Shape s) {
		this.shape         = s;
		this.angle         = 0;
		this.type          = TYPE_POINT;
		this.finalVertices = false;
		this.fixedVertices = false;
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	/** Get the polygon shape of this object (if this is a polygon). **/
	public Polygon getPolygon() {
		if (shape instanceof Polygon)
			return (Polygon) shape;
		return null;
	}

	/** Get the vector position of this object (if this is a point). **/
	public Vector getPosition() {
		if (shape instanceof Vector)
			return (Vector) shape;
		return null;
	}
	
	/** Return whether the vertices on the shape can be  **/
	public boolean isFinalVertices() {
		return finalVertices;
	}
	
	/** Return whether the vertices on the shape can be  **/
	public boolean isFixedVertices() {
		return fixedVertices;
	}
}
