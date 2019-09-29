package com.base.engine.rendering;

import com.base.engine.common.GMath;
import com.base.engine.common.Vector2f;
import com.base.engine.common.Vector3f;
import com.base.engine.core.Vertex;
import com.base.engine.rendering.resourceManagement.OBJModel;

public class Primitives {
	public static Mesh createXZPlane(float left, float right, float near, float far,
			float y, float texX1, float texX2, float texY1, float texY2, boolean flipped)
	{
		Vertex[] vertices = new Vertex[4];
		vertices[0] = new Vertex(left,  y,  far, texX1, texY2);
		vertices[1] = new Vertex(right, y,  far, texX2, texY2);
		vertices[2] = new Vertex(right, y, near, texX2, texY1);
		vertices[3] = new Vertex(left,  y, near, texX1, texY1);
		int[] indices = new int[] {0, 1, 2, 0, 2, 3};
		if (flipped)
			indices = new int[] {0, 2, 1, 0, 3, 2};
		return new Mesh(vertices, indices, true, true);
	}
	
	public static Mesh createXYPlane(float left, float right, float bottom, float top,
			float z, float texX1, float texX2, float texY1, float texY2, boolean flipped)
	{
		Vertex[] vertices = new Vertex[4];
		vertices[0] = new Vertex(left, top, z, texX1, texY2);
		vertices[1] = new Vertex(right, top, z, texX2, texY2);
		vertices[2] = new Vertex(right, bottom, z, texX2, texY1);
		vertices[3] = new Vertex(left, bottom, z, texX1, texY1);
		int[] indices = new int[] {0, 1, 2, 0, 2, 3};
		if (flipped)
			indices = new int[] {0, 2, 1, 0, 3, 2};
		return new Mesh(vertices, indices, true, true);
	}
	
	public static Mesh createZYPlane(float left, float right, float bottom, float top,
			float x, float texX1, float texX2, float texY1, float texY2, boolean flipped)
	{
		Vertex[] vertices = new Vertex[4];
		vertices[0] = new Vertex(x, top, left, texX1, texY2);
		vertices[1] = new Vertex(x, top, right, texX2, texY2);
		vertices[2] = new Vertex(x, bottom, right, texX2, texY1);
		vertices[3] = new Vertex(x, bottom, left, texX1, texY1);
		int[] indices = new int[] {0, 1, 2, 0, 2, 3};
		if (flipped)
			indices = new int[] {0, 2, 1, 0, 3, 2};
		return new Mesh(vertices, indices, true, true);
	}
	
	public static Mesh createZYPlane(float left, float right, float bottom, float top, float x, boolean flipped) {
		return createZYPlane(left, right, bottom, top, x, 0, 1, 0, 1, flipped);
	}
	
	public static Mesh createXYPlane(float left, float right, float bottom, float top, float z, boolean flipped) {
		return createXYPlane(left, right, bottom, top, z, 0, 1, 0, 1, flipped);
	}
	
	public static Mesh createXZPlane(float left, float right, float near, float far, float y, boolean flipped) {
		return createXZPlane(left, right, near, far, y, 0, 1, 0, 1, flipped);
	}
	
	public static Mesh createCircle(float x, float z, float radius, int detail, boolean flipped) {
		Vertex[] vertices = new Vertex[detail + 1];
		int[] indices = new int[detail * 3];
		vertices[0] = new Vertex(x, 0, z, 0.5f, 0.5f);
		
		for (int i = 0; i < detail; i++) {
			float angle = (i / (float) detail) * GMath.TWO_PI;
			vertices[i + 1] = new Vertex(x + GMath.cos(angle) * radius, 0, z + GMath.sin(angle) * radius, 
					GMath.cos(angle) * 0.5f + 0.5f, GMath.sin(angle) * 0.5f + 0.5f);
			
			int index = i * 3;
			indices[index] = 0;
			indices[index + (flipped ? 1 : 2)] = i + 1;
			indices[index + (flipped ? 2 : 1)] = ((i + 1) % detail) + 1;
		}
		return new Mesh(vertices, indices, true, true);
	}
	
	public static Mesh createBox(float x, float y, float z, float width, float height, float length) {
		OBJModel obj = new OBJModel();
		float w = width / 2;
		float h = height / 2;
		float l = length / 2;
		
		obj.addVertex(x - w, y - h, z - l);
		obj.addVertex(x + w, y - h, z - l);
		obj.addVertex(x + w, y - h, z + l);
		obj.addVertex(x - w, y - h, z + l);
		obj.addVertex(x - w, y + h, z - l);
		obj.addVertex(x + w, y + h, z - l);
		obj.addVertex(x + w, y + h, z + l);
		obj.addVertex(x - w, y + h, z + l);
		
		obj.addTexCoord(0, 0);
		obj.addTexCoord(0, 1);
		obj.addTexCoord(1, 1);
		obj.addTexCoord(1, 0);
		
		obj.addFace(0, 5, 1, 0, 2, 1); // Front
		obj.addFace(0, 4, 5, 0, 3, 2);
		obj.addFace(2, 7, 3, 0, 2, 1); // Back
		obj.addFace(2, 6, 7, 0, 3, 2);
		obj.addFace(1, 6, 2, 0, 2, 1); // Right
		obj.addFace(1, 5, 6, 0, 3, 2);
		obj.addFace(3, 4, 0, 0, 2, 1); // Left
		obj.addFace(3, 7, 4, 0, 3, 2);
		obj.addFace(1, 3, 0, 0, 2, 1); // Bottom
		obj.addFace(1, 2, 3, 0, 3, 2);
		obj.addFace(4, 6, 5, 0, 2, 1); // Top
		obj.addFace(4, 7, 6, 0, 3, 2);
		
		obj.addDummyMaterial();
		obj.calcNormals(false);
		return obj.createMeshes()[0];
	}
	
	public static Mesh createPrism(float x, float y, float z, float radius, float height, int numSides) {
		Vertex[] vertices = new Vertex[(numSides + 1) * 2];
		int[] indices = new int[numSides * 12];
		vertices[0] = new Vertex(x, y, z, 0.5f, 0.5f);
		vertices[numSides + 1] = new Vertex(x, y + height, z, 0.5f, 0.5f);
		
		for (int i = 0; i < numSides; i++) {
			float angle = (i / (float) numSides) * GMath.TWO_PI;
			vertices[i + 1] = new Vertex(x + GMath.cos(angle) * radius, y, z + GMath.sin(angle) * radius, 0, 0);
			vertices[i + numSides + 2] = new Vertex(x + GMath.cos(angle) * radius, y + height, z + GMath.sin(angle) * radius, 0, 0);
			
			int index = (i * 3);
			indices[index + 0] = 0;
			indices[index + 1] = i + 1;
			indices[index + 2] = ((i + 1) % numSides) + 1;
			
			index = ((i + numSides) * 3);
			indices[index + 0] = numSides + 1;
			indices[index + 2] = i + numSides + 2;
			indices[index + 1] = ((i + 1) % numSides) + numSides + 2;
			
			index = (((i * 2) + numSides + numSides) * 3);
			indices[index + 0] = ((i + 1) % numSides) + numSides + 2;
			indices[index + 1] = ((i + 1) % numSides) + 1;
			indices[index + 2] = i + numSides + 2;

			indices[index + 3] = ((i + 1) % numSides) + 1;
			indices[index + 4] = i + 1;
			indices[index + 5] = i + numSides + 2;
		}
		return new Mesh(vertices, indices, true, true);
	}
	
	public static Mesh createTorus(float x, float y, float z, float holeRadius, float ringRadius, int detail1, int detail2) {
		OBJModel torus = new OBJModel();
		
		for (int i = 0; i <= detail1; i++) {
			for (int j = 0; j <= detail2; j++) {
				float angle1 = (i / (float) detail1) * GMath.TWO_PI;
				float angle2 = (j / (float) detail2) * GMath.TWO_PI;
				float r      = holeRadius + GMath.sin(angle2) * ringRadius;

				torus.addTexCoord(i / (float) detail1, 1 - (j / (float) detail2));
				
    			if (i < detail1 && j < detail2) {
    				torus.addVertex(
        					x + GMath.cos(angle1) * r,
        					y + GMath.cos(angle2) * ringRadius,
        					z + GMath.sin(angle1) * r);
    				
    				torus.addFace(
    					(i * detail2) + j,
    					(((i + 1) % detail1) * detail2) + ((j + 1) % detail2),
    					(i * detail2) + ((j + 1) % detail2),
    					
    					(i * (detail2 + 1)) + j,
    					((i + 1) * (detail2 + 1)) + (j + 1),
    					(i * (detail2 + 1)) + (j + 1)
    				);

    				torus.addFace(
    					(i * detail2) + j,
    					(((i + 1) % detail1) * detail2) + j,
    					(((i + 1) % detail1) * detail2) + ((j + 1) % detail2),
    					
    					(i * (detail2 + 1)) + j,
    					((i + 1) * (detail2 + 1)) + j,
    					((i + 1) * (detail2 + 1)) + (j + 1)
    				);
    			}
			}
		}
		
		torus.addDummyMaterial();
		torus.calcNormals();
		return torus.createMeshes()[0];
	}
	
	public static Mesh createSphere(float x, float y, float z, float radius, int detailX, int detailY) {
		OBJModel obj = new OBJModel();
		
		for (int yy = 0; yy <= detailY; yy++) {
    		for (int xx = 0; xx <= detailX; xx++) {
    			float angle = GMath.TWO_PI * (xx / (float) detailX);
    			float r = GMath.sin(GMath.PI * (yy / (float) detailY)) * radius;
    			float h = -GMath.cos(GMath.PI * (yy / (float) detailY)) * radius;
    			Vector3f v = new Vector3f(x + GMath.cos(angle) * r, y + h, z + GMath.sin(angle) * r);
    			
    			Vector2f tex = new Vector2f(xx / (float) detailX, yy / (float) detailY);
    			if (yy == 0 || yy == detailY)
    				tex.x = (xx + 0.5f) / (float) detailX;
    			
    			obj.addVertex(v);
    			obj.addTexCoord(tex);
    			
    			if (xx < detailX && yy < detailY) {
        			int index1  = (yy * (detailX + 1)) + xx;
        			int index1b = (yy * (detailX + 1)) + ((xx + 1) % detailX);
        			int index2  = ((yy + 1) * (detailX + 1)) + xx;
        			int index2b = ((yy + 1) * (detailX + 1)) + ((xx + 1) % detailX);
        			
        			if (yy == 0)
            			obj.addFace(index1, index2, index2b, index1, index2, index2 + 1);
        			else if (yy == detailY - 1)
            			obj.addFace(index1b, index1, index2, index1 + 1, index1, index2);
        			else {
            			obj.addFace(index1b, index1, index2, index1 + 1, index1, index2);
            			obj.addFace(index1b, index2, index2b, index1 + 1, index2, index2 + 1);
        			}
    			}
    		}
		}
		
		obj.addDummyMaterial();
		obj.calcNormals();
		return obj.createMeshes()[0];
	}
}
