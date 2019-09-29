package com.base.engine.rendering;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import java.util.ArrayList;
import com.base.engine.common.Vector2f;
import com.base.engine.common.Vector3f;
import com.base.engine.core.Util;
import com.base.engine.core.Vertex;
import com.base.engine.rendering.resourceManagement.MeshResource;

public class DynamicMesh extends Mesh {
	private ArrayList<Vertex> vertices;
	private ArrayList<Integer> indices;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public DynamicMesh() {
		vertices = new ArrayList<Vertex>();
		indices  = new ArrayList<Integer>();
	}
	
	public DynamicMesh(Vertex[] vertices, int[] indices) {
		this();
		for (int i = 0; i < vertices.length; i++)
			this.vertices.add(vertices[i]);
		for (int i = 0; i < indices.length; i++)
			this.indices.add(indices[i]);
		this.resource = new MeshResource(indices.length);
		recalculate();
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public Vertex getVertex(int index) {
		return vertices.get(index);
	}
	
	
	// ==================== MUTATORS ==================== //

	public void setVertexPosition(int index, Vector3f pos) {
		vertices.get(index).setPos(pos);
		recalculate();
	}
	
	private void recalculate() {
		Vertex[] verticesData = new Vertex[vertices.size()];
		int[] indicesData = new int[indices.size()];
		vertices.toArray(verticesData);
		for (int i = 0; i < indices.size(); i++)
			indicesData[i] = indices.get(i);
		
		calcNormals(verticesData, indicesData);
		calcTangents(verticesData, indicesData);

		resource.setSize(indicesData.length);
		
		glBindBuffer(GL_ARRAY_BUFFER, resource.getVbo());
		glBufferData(GL_ARRAY_BUFFER, Util.createFlippedBuffer(verticesData), GL_STATIC_DRAW);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, resource.getIbo());
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, Util.createFlippedBuffer(indicesData), GL_STATIC_DRAW);
	}
}
