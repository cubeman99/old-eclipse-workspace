package cmg.math.geometry;

import java.util.ArrayList;


public class Path {
	private ArrayList<Vector> vertices;
	
	
	// ================= CONSTRUCTORS ================= //

	public Path() {
		this.vertices = new ArrayList<Vector>();
	}
	
	public Path(Line l) {
		this.vertices = new ArrayList<Vector>();
		addVertex(l.end1);
		addVertex(l.end2);
	}
	
	public Path(Path p) {
		this.vertices = new ArrayList<Vector>();
		for (int i = 0; i < p.numVertices(); i++)
			addVertex(p.getVertex(i));
	}
	
	public Path(Vector...vertices) {
		this.vertices = new ArrayList<Vector>();
		for (int i = 0; i < vertices.length; i++)
			addVertex(vertices[i]);
	}
	
	public Path(ArrayList<Vector> vertices) {
		this.vertices = new ArrayList<Vector>();
		for (int i = 0; i < vertices.size(); i++)
			addVertex(vertices.get(i));
	}
	

	
	// ================== ACCESSORS ================== //
	
	/** Get the vertex at a given index. **/
	public Vector getVertex(int index) {
		if (index < 0 || index >= numVertices())
			return null;
		return vertices.get(index);
	}
	
	/** Return the first vertex in the path. **/
	public Vector getStart() {
		if (numVertices() > 0)
			return vertices.get(0);
		return null;
	}
	/** Return the last vertex in the path. **/
	
	public Vector getEnd() {
		if (numVertices() > 0)
			return vertices.get(numVertices() - 1);
		return null;
	}
	
	/** Get the number of vertices. **/
	public int numVertices() {
		return vertices.size();
	}
	
	/** Get the number of edges. **/
	public int numEdges() {
		if (numVertices() == 0)
			return 0;
		return (numVertices() - 1);
	}
	
	/** Get the edge (line) at the given index. **/
	public Line getEdge(int index) {
		if (index < 0 || index >= numVertices() - 1)
			return null;
		return new Line(
			vertices.get(index % numVertices()),
			vertices.get((index + 1) % numVertices())
		);
	}
	
	/** Return the total length of the path. **/
	public double length() {
		double dist = 0;
		for (int i = 0; i < vertices.size() - 1; i++)
			dist += vertices.get(i).distanceTo(vertices.get(i));
		return dist;
	}
	
	/** Return a reversed version of this path. **/
	public Path getReversed() {
		return new Path(this).reverse();
	}

	/** Get the closest point on the path to a given point. **/
	public Vector getClosestPoint(Vector point) {
		Vector closestPoint = null;
		double closestDist  = 0;
		
		for (int i = 0; i < numEdges(); i++) {
			Vector v = getEdge(i).getClosestPoint(point);
			double dist = point.distanceTo(v);
			if (dist < closestDist || closestPoint == null) {
				closestPoint = v;
				closestDist = dist;
			}
		}
		return closestPoint;
	}
	
	
	
	// ================== MUTATORS ================== //
	
	/** Add a vertex to the path. **/
	public void addVertex(double x, double y) {
		addVertex(new Vector(x, y));
	}

	/** Add a vertex to the path. **/
	public void addVertex(Vector vertex) {
		vertices.add(new Vector(vertex));
	}

	/** Insert a vertex at the given index. **/
	public void addVertex(int index, Vector vertex) {
		vertices.add(index, new Vector(vertex));
	}
	
	/** Remove the vertex at the given index. **/
	public void removeVertex(int index) {
		vertices.remove(index);
	}
	
	/** Remove a given vertex reference from the polygon. **/
	public void removeVertex(Vector vertex) {
		vertices.remove(vertex);
	}
	
	/** Remove all vertices. **/
	public void clear() {
		vertices.clear();
	}
	
	public Path reverse() {
		int n  = numVertices();
		int n2 = n / 2;
		for (int i = 0; i < n2; i++) {
			Vector temp = vertices.get(i);
			vertices.set(i, vertices.get(n - i - 1));
			vertices.set(n - i - 1, temp);
		}
		return this;
	}
	
	public Path expand(double length) {
		if (numEdges() == 0)
			return this;
		
		Line[] edges = new Line[numEdges()];
		Line prev    = null;
		
		for (int i = 0; i < numEdges(); i++) {
			Line edge = new Line(getEdge(i));
			edges[i] = edge;
			edge.add(edge.getVector().getPerpendicular().setLength(length));
			
			if (prev != null) {
				Vector v = Line.intersectionEndless(edge, prev);
				if (v != null) {
					edge.end1 = new Vector(v);
					prev.end2 = new Vector(v);
				}
				else
					edge.end1 = new Vector(prev.end2);
			}
			
			prev = edge;
		}
		
		for (int i = 0; i < edges.length; i++) {
			vertices.get(i).set(edges[i].end1);
			if (i == edges.length - 1)
				vertices.get(i + 1).set(edges[i].end2);
		}
		
		return this;
	}
}
