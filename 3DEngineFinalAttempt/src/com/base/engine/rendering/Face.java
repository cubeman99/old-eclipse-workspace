package com.base.engine.rendering;

import com.base.engine.rendering.resourceManagement.OBJModel;

public class Face {
	public static final int SIZE = 4; // quadrangles
	private int[] vertices;
	private int[] textureCoords;
	
	
	// ================== CONSTRUCTORS ================== //

	public Face(int vertex1, int vertex2, int vertex3, int vertex4) {
		this.vertices      = new int[SIZE];
		this.textureCoords = new int[SIZE];
		vertices[0] = vertex1;
		vertices[1] = vertex2;
		vertices[2] = vertex3;
		vertices[3] = vertex4;
	}
	
	

	// =================== ACCESSORS =================== //
	
	public int getVertex(int index) {
		return vertices[index];
	}
	
	public int getTextureCoord(int index) {
		return textureCoords[index];
	}
	
	public int[] getVertices() {
		return vertices;
	}
	
	public int[] getTextureCoords() {
		return textureCoords;
	}
	
	
	
	// ==================== MUTATORS ==================== //

	public void setVertex(int index, int vertexIndex) {
		vertices[index] = vertexIndex;
	}

	public void setTextureCoord(int index, int textureCoordIndex) {
		textureCoords[index] = textureCoordIndex;
	}
}
