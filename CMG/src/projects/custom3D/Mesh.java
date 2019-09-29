package projects.custom3D;

import java.util.ArrayList;

public class Mesh {
	private ArrayList<Vector3> vertices;
	private ArrayList<Integer> edges;
	private ArrayList<Integer> faces;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Mesh() {
		vertices = new ArrayList<Vector3>();
		edges    = new ArrayList<Integer>();
		faces    = new ArrayList<Integer>();
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public int getEdgeVertexIndex(int edgeIndex) {
		return edges.get(edgeIndex);
	}
	
	public int getFaceVertexIndex(int faceIndex) {
		return faces.get(faceIndex);
	}
	
	public ArrayList<Vector3> getVertices() {
		return vertices;
	}
	
	public ArrayList<Integer> getEdges() {
		return edges;
	}
	
	public ArrayList<Integer> getFaces() {
		return faces;
	}
	
	

	// ==================== MUTATORS ==================== //
	
	public int addVertex(double x, double y, double z) {
		for (int i = 0; i < vertices.size(); i++) {
			if (vertices.get(i).equals(x, y, z))
				return i;
		}
		vertices.add(new Vector3(x, y, z));
		return (vertices.size() - 1);
	}
	
	public void addEdge(double x1, double y1, double z1, double x2, double y2, double z2) {
		edges.add(addVertex(x1, y1, z1));
		edges.add(addVertex(x2, y2, z2));
	}

	public void addFace(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3) {
		faces.add(addVertex(x1, y1, z1));
		faces.add(addVertex(x2, y2, z2));
		faces.add(addVertex(x3, y3, z3));
	}
}
