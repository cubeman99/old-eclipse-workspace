package cmg.math.geometry;

import java.util.ArrayList;
import cmg.math.GMath;


/**
 * Extension of Shape:
 * A class to represent a polygon
 * composed of multiple vertices.
 * 
 * @author David Jordan
 */
public class Polygon {
	private ArrayList<Vector> vertices;
	private boolean closed;
	
	

	// ================= CONSTRUCTORS ================= //
	
	public Polygon(Line l) {
		this.vertices = new ArrayList<Vector>();
		this.closed   = true;
		addVertex(l.end1);
		addVertex(l.end2);
	}
	
	public Polygon(Rectangle r) {
		this.vertices = new ArrayList<Vector>();
		this.closed   = true;
	}
	
	public Polygon(Polygon p) {
		this.vertices = new ArrayList<Vector>();
		this.closed   = true;
		for (int i = 0; i < p.vertexCount(); i++)
			addVertex(p.getVertex(i));
	}
	
	public Polygon(Vector...vertices) {
		this.vertices = new ArrayList<Vector>();
		this.closed   = true;
		for (int i = 0; i < vertices.length; i++)
			addVertex(vertices[i]);
	}
	
	public Polygon(ArrayList<Vector> vertices) {
		this.vertices = new ArrayList<Vector>();
		this.closed   = true;
		for (int i = 0; i < vertices.size(); i++)
			addVertex(vertices.get(i));
	}
	

	
	// ================== ACCESSORS ================== //
	
	/** Get the vertex at a given index. **/
	public Vector getVertex(int index) {
		while (index < 0)
			index += vertexCount();
		return vertices.get(index % vertexCount());
	}
	
	/** Get the number of vertices. **/
	public int vertexCount() {
		return vertices.size();
	}
	
	/** Get the number of edges. **/
	public int edgeCount() {
		return (closed ? vertices.size() : Math.max(0, vertices.size() - 1));
	}
	
	/** Get a line representing the edge of two vertices. (one at index, one after index) **/
	public Line getEdge(int index) {
		return new Line(
			vertices.get(GMath.getWrappedValue(index, vertexCount())),
			vertices.get(GMath.getWrappedValue(index + 1, vertexCount()))
		);
	}
	
	/** Return whether the polygon is a closed polygon. **/
	public boolean isClosed() {
		return closed;
	}
	
	/** Return an AWT Polygon object from this polygon. **/
	public java.awt.Polygon getAWT() {
		java.awt.Polygon awtPoly = new java.awt.Polygon();
		for (int i = 0; i < vertexCount(); i++) {
			Vector v = getVertex(i);
			awtPoly.addPoint((int) v.x, (int) v.y);
		}
		return awtPoly;
	}
	
	
	// ================== MUTATORS ================== //
	
	/** Add a vertex (x and y) to this polygon. **/
	public void addVertex(double x, double y) {
		addVertex(new Vector(x, y));
	}

	/** Add a vertex (vector) to this polygon. **/
	public void addVertex(Vector vertex) {
		vertices.add(new Vector(vertex));
	}

	/** Insert a vertex at the given index, pushing the vertex in that index up. **/
	public void addVertex(int index, Vector vertex) {
		vertices.add(index, new Vector(vertex));
	}
	
	/** Remove a vertex at the given index from this polygon. **/
	public void removeVertex(int index) {
		vertices.remove(index);
	}
	
	/** Remove a given vertex from the polygon. **/
	public void removeVertex(Vector vertex) {
		vertices.remove(vertex);
	}
	
	/** Remove all vertices. **/
	public void clear() {
		vertices.clear();
	}
	
	/** Set whether this polygon is closed or not. **/
	public void setClosed(boolean closed) {
		this.closed = closed;
	}
}
