package com.base.engine.rendering;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;
import com.base.engine.common.Rect2f;
import com.base.engine.common.Rect3f;
import com.base.engine.common.Vector2f;
import com.base.engine.common.Vector3f;
import com.base.engine.core.Util;
import com.base.engine.core.Vertex;
import com.base.engine.entity.SceneObject;
import com.base.engine.rendering.resourceManagement.MeshResource;

public class Brush extends SceneObject {
	private Vector3f[] vertexPositions;
	private Face[] faces;
	private int numVertices;
	private Mesh mesh;
	
	

	// ================== CONSTRUCTORS ================== //

	public Brush() {
		super();
		mesh = new Mesh();
		initCube();
	}

	
	
	// =================== ACCESSORS =================== //
	
	@Override
	public Rect3f getBoundingBox() {
		return Rect3f.createBoundingBox(vertexPositions);
	}
	
	public Mesh getMesh() {
		return mesh;
	}
	
	public Vector3f getVertexPosition(int index) {
		return vertexPositions[index];
	}
	
	public int getNumVertices() {
		return numVertices;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	private void drawVertex(int index, int axis1, int axis2) {
		Vector3f v = vertexPositions[index];
		glVertex2f(v.swizzle(axis1, axis2).x,
				   v.swizzle(axis1, axis2).y);
	}
	
	public void drawWireframe(int axis1, int axis2) {
		glBegin(GL_LINE_LOOP);
		drawVertex(0, axis1, axis2);
		drawVertex(1, axis1, axis2);
		drawVertex(3, axis1, axis2);
		drawVertex(2, axis1, axis2);
		glEnd();
		
		glBegin(GL_LINE_LOOP);
		drawVertex(4, axis1, axis2);
		drawVertex(5, axis1, axis2);
		drawVertex(7, axis1, axis2);
		drawVertex(6, axis1, axis2);
		glEnd();

		glBegin(GL_LINES);
		drawVertex(0, axis1, axis2);
		drawVertex(4, axis1, axis2);
		drawVertex(1, axis1, axis2);
		drawVertex(5, axis1, axis2);
		drawVertex(2, axis1, axis2);
		drawVertex(6, axis1, axis2);
		drawVertex(3, axis1, axis2);
		drawVertex(7, axis1, axis2);
		glEnd();
	}
	
	public void initCube() {
		vertexPositions = new Vector3f[8];
		faces = new Face[6];
		numVertices = 8;
		
		vertexPositions[0] = new Vector3f(-1, -1, -1);
		vertexPositions[1] = new Vector3f( 1, -1, -1);
		vertexPositions[2] = new Vector3f(-1, -1,  1);
		vertexPositions[3] = new Vector3f( 1, -1,  1);
		vertexPositions[4] = new Vector3f(-1,  1, -1);
		vertexPositions[5] = new Vector3f( 1,  1, -1);
		vertexPositions[6] = new Vector3f(-1,  1,  1);
		vertexPositions[7] = new Vector3f( 1,  1,  1);
		
		
		faces[0] = new Face(1, 3, 2, 0); // Bottom
		faces[1] = new Face(4, 6, 7, 5); // Top
		faces[2] = new Face(3, 7, 6, 2); // Front
		faces[3] = new Face(0, 4, 5, 1); // Back
		faces[4] = new Face(2, 6, 4, 0); // Left
		faces[5] = new Face(1, 5, 7, 3); // Right

		mesh.setResource(new MeshResource(1));
		recalculate();
	}
	
	public void recalculate() {
		Vertex[] vertices = new  Vertex[vertexPositions.length * 3];
		int[] indices     = new int[faces.length * 6];
		
		int index = 0;
		for (int i = 0; i < faces.length; i++) {
			int[] faceIndices = new int[Face.SIZE];
			
			for (int j = 0; j < Face.SIZE; j++) {
				int vertexIndex = faces[i].getVertex(j) * 3;
				Vector3f pos = vertexPositions[faces[i].getVertex(j)];
				
				while (vertices[vertexIndex] != null) {
					vertexIndex++;
					if (vertexIndex >= (faces[i].getVertex(j) * 3) + 3) {
						// Error!
						new Exception().printStackTrace();
						System.exit(1);
					}
				}
				
				Vertex v = new Vertex(pos, new Vector2f(0, 0));
				v.setTexCoord(new Vector2f(
						j == 0 || j == 1 ? 0 : 1,
						j == 0 || j == 3 ? 0 : 1));
				
				vertices[vertexIndex] = v;
				faceIndices[j] = vertexIndex;
			}
			
			indices[index++] = faceIndices[0];
			indices[index++] = faceIndices[1];
			indices[index++] = faceIndices[2];
			
			indices[index++] = faceIndices[0];
			indices[index++] = faceIndices[2];
			indices[index++] = faceIndices[3];
		}
		
		mesh.setVertices(vertices, indices, true, true);
	}
}
