package com.base.engine.rendering.resourceManagement;


public class OBJFace {
	public static final int SIZE = 3; // triangles
	private int[] vertices;
	private int[] textureCoords;
	private int[] normals;
	private String material;
	private OBJModel model;
	
	
	// ================== CONSTRUCTORS ================== //

	public OBJFace(OBJModel model, String material, String part1, String part2, String part3) {
		this.model         = model;
		this.material      = material;
		this.vertices      = new int[SIZE];
		this.textureCoords = new int[SIZE];
		this.normals       = new int[SIZE];
		
		for (int i = 0; i < SIZE; i++) {
			textureCoords[i] = -1;
			normals[i] = -1;
		}
		
		setIndices(0, part1);
		setIndices(1, part2);
		setIndices(2, part3);
	}
	
	public OBJFace(OBJModel model) {
		this.model         = model;
		this.material      = "";
		this.vertices      = new int[SIZE];
		this.textureCoords = new int[SIZE];
		this.normals       = new int[] {-1, -1, -1};
		
	}
	
	

	// =================== ACCESSORS =================== //
	
	public int getVertex(int index) {
		return vertices[index];
	}
	
	public int getTextureCoord(int index) {
		return textureCoords[index];
	}
	
	public int getNormal(int index) {
		return normals[index];
	}
	
	public int[] getVertices() {
		return vertices;
	}
	
	public int[] getTextureCoords() {
		return textureCoords;
	}
	
	public int[] getNormals() {
		return normals;
	}
	
	public String getMaterial() {
		return material;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public OBJFace setVertices(int index1, int index2, int index3) {
		vertices[0] = index1;
		vertices[1] = index2;
		vertices[2] = index3;
		return this;
	}
	
	public void setVertex(int index, int vertexIndex) {
		vertices[index] = vertexIndex;
	}

	public void setTextureCoord(int index, int textureCoordIndex) {
		textureCoords[index] = textureCoordIndex;
	}
	
	public OBJFace setTextureCoords(int index1, int index2, int index3) {
		textureCoords[0] = index1;
		textureCoords[1] = index2;
		textureCoords[2] = index3;
		return this;
	}
	
	public void setNormal(int index, int normalIndex) {
		normals[index] = normalIndex;
	}
	
	private void setIndices(int index, String data) {
		String[] seperatedData = data.split("/");
		// "vertex/texCoord/normal"
		
		// Vertex index
		vertices[index] = Integer.parseInt(seperatedData[0]) - 1;
		if (vertices[index] < 0)
			vertices[index] += model.getVertexPositions().size() + 1;
		
		if (seperatedData.length > 1) {
    		// Texture Coord index
    		textureCoords[index] = -1;
    		if (seperatedData[1].length() > 0) {
    			textureCoords[index] = Integer.parseInt(seperatedData[1]) - 1;
        		if (textureCoords[index] < 0)
        			textureCoords[index] += model.getTextureCoords().size() + 1;
    		}
		}
		
		if (seperatedData.length > 2) {
    		// Normal index
    		normals[index] = -1;
    		if (seperatedData.length >= 3) {
    			normals[index] = Integer.parseInt(seperatedData[2]) - 1;
        		if (normals[index] < 0)
        			normals[index] += model.getNormals().size() + 1;
    		}
		}
	}
}
