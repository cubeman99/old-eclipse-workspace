package base.engine.rendering;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import base.engine.common.Util;
import base.engine.common.Vector3;

public class Mesh {
	private int vbo; // vertex buffer object
	private int ibo; // index buffer object
	private int size;
	
	public Mesh() {
		vbo  = glGenBuffers();
		ibo  = glGenBuffers();
		size = 0;
	}

	public void addVertices(Vertex[] vertices, int[] indices) {
		addVertices(vertices, indices, false, false);
	}
	
	public void addVertices(Vertex[] vertices, int[] indices, boolean calcNormals, boolean calcTangents) {
		if (calcNormals)
			calculateNormals(vertices, indices);
		if (calcTangents)
			calculateTangents(vertices, indices);
		
		size = indices.length;
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, Util.createFlippedBuffer(vertices), GL_STATIC_DRAW);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, Util.createFlippedBuffer(indices), GL_STATIC_DRAW);
	}
	
	private void calculateNormals(Vertex[] vertices, int[] indices) {
		for (int i = 0; i < indices.length; i += 3) {
			int i0 = indices[i];
			int i1 = indices[i + 1];
			int i2 = indices[i + 2];
			
			Vector3 v1 = vertices[i1].getPosition().minus(vertices[i0].getPosition());
			Vector3 v2 = vertices[i2].getPosition().minus(vertices[i0].getPosition());
			Vector3 normal = v1.cross(v2).normalize();
			
			vertices[i0].getNormal().add(normal);
			vertices[i1].getNormal().add(normal);
			vertices[i2].getNormal().add(normal);
		}

		for (int i = 0; i < vertices.length; i ++) {
			vertices[i].getNormal().normalize();
		}
	}
	
	private void calculateTangents(Vertex[] vertices, int[] indices) {
		for (int i = 0; i < indices.length; i += 3) {
			int i0 = indices[i];
			int i1 = indices[i + 1];
			int i2 = indices[i + 2];
			
			Vector3 edge1 = vertices[i1].getPosition().minus(vertices[i0].getPosition());
			Vector3 edge2 = vertices[i2].getPosition().minus(vertices[i0].getPosition());
			
			float deltaU1 = vertices[i1].getTexCoord().x - vertices[i0].getTexCoord().x;
			float deltaV1 = vertices[i1].getTexCoord().y - vertices[i0].getTexCoord().y;
			float deltaU2 = vertices[i2].getTexCoord().x - vertices[i0].getTexCoord().x;
			float deltaV2 = vertices[i2].getTexCoord().y - vertices[i0].getTexCoord().y;
			
			float f = 1.0f / ((deltaU1 * deltaV2) - (deltaU2 * deltaV1));
			
			Vector3 tangent = new Vector3(0, 0, 0);
			tangent.x = f * ((deltaV2 * edge1.x) - (deltaV1 * edge2.x));
			tangent.y = f * ((deltaV2 * edge1.y) - (deltaV1 * edge2.y));
			tangent.z = f * ((deltaV2 * edge1.z) - (deltaV1 * edge2.z));
			
			vertices[i0].getTangent().add(tangent);
			vertices[i1].getTangent().add(tangent);
			vertices[i2].getTangent().add(tangent);
		}

		for (int i = 0; i < vertices.length; i ++) {
			vertices[i].getTangent().normalize();
		}
	}
	
	public void draw() {
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0);  // Vertex Positions
		glVertexAttribPointer(1, 2, GL_FLOAT, false, Vertex.SIZE * 4, 12); // Texture Coords
		glVertexAttribPointer(2, 3, GL_FLOAT, false, Vertex.SIZE * 4, 20); // Normals
		glVertexAttribPointer(3, 3, GL_FLOAT, false, Vertex.SIZE * 4, 32); // Tangents
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glDrawElements(GL_TRIANGLES, size, GL_UNSIGNED_INT, 0);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
	}
}
