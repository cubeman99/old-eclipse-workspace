package com.base.game;

import com.base.engine.common.Quaternion;
import com.base.engine.common.Vector2f;
import com.base.engine.common.Vector3f;
import com.base.engine.core.Vertex;
import com.base.engine.rendering.DynamicMesh;
import com.base.engine.rendering.Mesh;


public class TestModels {
	/*
	v -1.000000 0.000000 1.000000
    v 1.000000 0.000000 1.000000
    v -1.000000 0.000000 -1.000000
    v 1.000000 0.000000 -1.000000
    vt 0.000100 0.000100
    vt 0.999900 0.000100
    vt 0.999900 0.999900
    vt 0.000100 0.999900
    vn 0.000000 1.000000 0.000000
    usemtl None
    s off
    f 1/1/1 2/2/1 4/3/1 3/4/1

	
	 */
    public static Mesh createPlaneMesh() {
    	Vertex[] vertices = new Vertex[] {
    		// Bottom
        	new Vertex(-1, 0,  1, 0, 1),
        	new Vertex( 1, 0,  1, 1, 1),
        	new Vertex(-1, 0, -1, 0, 0),
        	new Vertex( 1, 0, -1, 0, 1)
    	};
    	
    	int[] indices = new int[] {
//    		0, 1, 3,
//    		0, 3, 2
    		0, 3, 1,
    		0, 2, 3
    	};
    	
    	return new Mesh(vertices, indices, true, true);
    }
    	
    public static Mesh createBoxMesh() {
    	Vertex[] vertices = new Vertex[] {
    		// Bottom
        	new Vertex(-1, -1, -1, 0, 0),
        	new Vertex(-1,  1, -1, 0, 1),
        	new Vertex( 1, -1, -1, 1, 0),
        	new Vertex( 1,  1, -1, 1, 1),
    		// Top
        	new Vertex(-1, -1,  1, 0, 1),
        	new Vertex(-1,  1,  1, 0, 0),
        	new Vertex( 1, -1,  1, 1, 1),
        	new Vertex( 1,  1,  1, 1, 0),
    		// Right
        	new Vertex( 1, -1, -1, 0, 0),
        	new Vertex( 1,  1, -1, 0, 1),
        	new Vertex( 1, -1,  1, 1, 0),
        	new Vertex( 1,  1,  1, 1, 1),
    		// Left
        	new Vertex(-1, -1, -1, 0, 1),
        	new Vertex(-1,  1, -1, 0, 0),
        	new Vertex(-1, -1,  1, 1, 1),
        	new Vertex(-1,  1,  1, 1, 0),
        	// Front
        	new Vertex(-1, -1, -1, 0, 0),
        	new Vertex( 1, -1, -1, 0, 1),
        	new Vertex(-1, -1,  1, 1, 0),
        	new Vertex( 1, -1,  1, 1, 1),
        	// Back
        	new Vertex(-1,  1, -1, 0, 1),
        	new Vertex( 1,  1, -1, 0, 0),
        	new Vertex(-1,  1,  1, 1, 1),
        	new Vertex( 1,  1,  1, 1, 0)
    	};
    	
    	int[] indices = new int[] {
    		// Bottom
    		0, 1, 3,
    		0, 3, 2,
    		// Top
    		4, 7, 5,
    		4, 6, 7,
    		// Right
    		8, 9, 11,
    		8, 11, 10,
    		// Left
    		12, 15, 13,
    		12, 14, 15,
    		// Front
    		16, 17, 19,
    		16, 19, 18,
    		// Back
    		20, 23, 21,
    		20, 22, 23
    	};
    	
    	return new Mesh(vertices, indices, true, true);
    }
	
    public static Mesh createCube() {
    	Vertex[] vertices = new Vertex[] {
    		// Bottom
        	new Vertex(0, 0, 0, 0, 0),
        	new Vertex(0, 1, 0, 0, 1),
        	new Vertex(1, 0, 0, 1, 0),
        	new Vertex(1, 1, 0, 1, 1),
    		// Top
        	new Vertex(0, 0, 1, 0, 1),
        	new Vertex(0, 1, 1, 0, 0),
        	new Vertex(1, 0, 1, 1, 1),
        	new Vertex(1, 1, 1, 1, 0),
    		// Right
        	new Vertex(1, 0, 0, 0, 0),
        	new Vertex(1, 1, 0, 0, 1),
        	new Vertex(1, 0, 1, 1, 0),
        	new Vertex(1, 1, 1, 1, 1),
    		// Left
        	new Vertex(0, 0, 0, 0, 1),
        	new Vertex(0, 1, 0, 0, 0),
        	new Vertex(0, 0, 1, 1, 1),
        	new Vertex(0, 1, 1, 1, 0),
        	// Front
        	new Vertex(0, 0, 0, 0, 0),
        	new Vertex(1, 0, 0, 0, 1),
        	new Vertex(0, 0, 1, 1, 0),
        	new Vertex(1, 0, 1, 1, 1),
        	// Back
        	new Vertex(0, 1, 0, 0, 1),
        	new Vertex(1, 1, 0, 0, 0),
        	new Vertex(0, 1, 1, 1, 1),
        	new Vertex(1, 1, 1, 1, 0)
    	};
    	
    	int[] indices = new int[] {
    		// Bottom
    		0, 1, 3,
    		0, 3, 2,
    		// Top
    		4, 7, 5,
    		4, 6, 7,
    		// Right
    		8, 9, 11,
    		8, 11, 10,
    		// Left
    		12, 15, 13,
    		12, 14, 15,
    		// Front
    		16, 17, 19,
    		16, 19, 18,
    		// Back
    		20, 23, 21,
    		20, 22, 23
    	};
    	
    	return new Mesh(vertices, indices, true, true);
    }
	
    public static DynamicMesh createDynamicBox() {
    	Vertex[] vertices = new Vertex[] {
    		// Bottom
        	new Vertex(-1, -1, -1, 0, 0),
        	new Vertex(-1,  1, -1, 0, 1),
        	new Vertex( 1, -1, -1, 1, 0),
        	new Vertex( 1,  1, -1, 1, 1),
    		// Top
        	new Vertex(-1, -1,  1, 0, 1),
        	new Vertex(-1,  1,  1, 0, 0),
        	new Vertex( 1, -1,  1, 1, 1),
        	new Vertex( 1,  1,  1, 1, 0),
    		// Right
        	new Vertex( 1, -1, -1, 0, 0),
        	new Vertex( 1,  1, -1, 0, 1),
        	new Vertex( 1, -1,  1, 1, 0),
        	new Vertex( 1,  1,  1, 1, 1),
    		// Left
        	new Vertex(-1, -1, -1, 0, 1),
        	new Vertex(-1,  1, -1, 0, 0),
        	new Vertex(-1, -1,  1, 1, 1),
        	new Vertex(-1,  1,  1, 1, 0),
        	// Front
        	new Vertex(-1, -1, -1, 0, 0),
        	new Vertex( 1, -1, -1, 0, 1),
        	new Vertex(-1, -1,  1, 1, 0),
        	new Vertex( 1, -1,  1, 1, 1),
        	// Back
        	new Vertex(-1,  1, -1, 0, 1),
        	new Vertex( 1,  1, -1, 0, 0),
        	new Vertex(-1,  1,  1, 1, 1),
        	new Vertex( 1,  1,  1, 1, 0)
    	};
    	
    	int[] indices = new int[] {
    		// Bottom
    		0, 1, 3,
    		0, 3, 2,
    		// Top
    		4, 7, 5,
    		4, 6, 7,
    		// Right
    		8, 9, 11,
    		8, 11, 10,
    		// Left
    		12, 15, 13,
    		12, 14, 15,
    		// Front
    		16, 17, 19,
    		16, 19, 18,
    		// Back
    		20, 23, 21,
    		20, 22, 23
    	};
    	
    	return new DynamicMesh(vertices, indices);
    }
    
    public static Mesh createInvertedBoxMesh() {
    	Vertex[] vertices = new Vertex[] {
    		// Bottom
        	new Vertex(-1, -1, -1, 0, 1),
        	new Vertex(-1,  1, -1, 0, 0),
        	new Vertex( 1, -1, -1, 1, 1),
        	new Vertex( 1,  1, -1, 1, 0),
    		// Top
        	new Vertex(-1, -1,  1, 0, 0),
        	new Vertex(-1,  1,  1, 0, 1),
        	new Vertex( 1, -1,  1, 1, 0),
        	new Vertex( 1,  1,  1, 1, 1),
    		// Right
        	new Vertex( 1, -1, -1, 0, 1),
        	new Vertex( 1,  1, -1, 0, 0),
        	new Vertex( 1, -1,  1, 1, 1),
        	new Vertex( 1,  1,  1, 1, 0),
    		// Left
        	new Vertex(-1, -1, -1, 0, 0),
        	new Vertex(-1,  1, -1, 0, 1),
        	new Vertex(-1, -1,  1, 1, 0),
        	new Vertex(-1,  1,  1, 1, 1),
        	// Front
        	new Vertex(-1, -1, -1, 0, 1),
        	new Vertex( 1, -1, -1, 0, 0),
        	new Vertex(-1, -1,  1, 1, 1),
        	new Vertex( 1, -1,  1, 1, 0),
        	// Back
        	new Vertex(-1,  1, -1, 0, 0),
        	new Vertex( 1,  1, -1, 0, 1),
        	new Vertex(-1,  1,  1, 1, 0),
        	new Vertex( 1,  1,  1, 1, 1)
    	};
    	
    	int[] indices = new int[] {
    		// Bottom
    		0, 3, 1,
    		0, 2, 3,
    		// Top
    		4, 5, 7,
    		4, 7, 6,
    		// Right
    		8, 11, 9,
    		8, 10, 11,
    		// Left
    		12, 13, 15,
    		12, 15, 14,
    		// Front
    		16, 19, 17,
    		16, 18, 19,
    		// Back
    		20, 21, 23,
    		20, 23, 22,
    	};
    	
    	return new Mesh(vertices, indices, true, true);
    }
	
    public static Mesh createPrismMesh() {
    	Vertex[] vertices = new Vertex[] {
    		// Bottom
        	new Vertex(-1, -1, -1, 0, 0),
        	new Vertex( 1, -1, -1, 1, 0),
        	new Vertex(-1, -1,  1, 0, 1),
        	new Vertex( 1, -1,  1, 1, 1),
        	// Top
        	new Vertex(0, 1, 0, 0.5f, 0.5f)
    	};
    	
    	int[] indices = new int[] {
    		// Bottom
    		0, 1, 2,
    		1, 3, 2,
    		// Sides
    		1, 0, 4,
    		0, 2, 4,
    		2, 3, 4,
    		3, 1, 4
    	};
    	
    	return new Mesh(vertices, indices, true, true);
    }


    
    public static Mesh createSphere() {
    	int detail = 30;
    	
    	float angle = 360 / detail;
    	int i       = 0;
    	int x       = 0;
    	int y       = 0;
    	int width   = detail + 1;
    	int height  = (detail / 2) + 1;
    	
    	Vertex[] vertices = new Vertex[width * height];
    	int[] indices = new int[width * height * 6];
    	
		for (float pitch = -90; pitch <= 90; pitch += angle) {
			for (float yaw = 0; yaw <= 360; yaw += angle) {
    			Vector3f pos = new Vector3f(0, 0, 1);
    			
    			if (pitch != 360 && pitch != 0)
    				pos = pos.rotate((float) Math.toRadians(pitch), new Vector3f(1, 0, 0)).normalize();
    			pos = pos.rotate((float) Math.toRadians(yaw), new Vector3f(0, 1, 0)).normalize();
//    			pos.x = x / (float) width;
//    			pos.y = y / (float) height;
//    			pos.y = ;
//    			pos.set(x / (float) width, y / (float) height, 0);
    			
    			vertices[i] = new Vertex(pos, new Vector2f(x / (float) width, y / (float) height));
    			
    			if (x < width - 1 && y < height - 1) {
    				int index = (y * width * 6) + (x * 6);
    				int i2 = i + 1;
    				if (x == width - 2)
    					i2 = i - width + 2;
    				if (y > 0) {
						indices[index + 0] = i;
						indices[index + 1] = i + width;
						indices[index + 2] = i2;
					}
					if (y < height - 2) {
						indices[index + 3] = i2;
						indices[index + 4] = i + width;
						indices[index + 5] = i2 + width;
    				}
    			}
				
				i++;
				x++;
        	}
			
			x = 0;
			y++;
    	}
		
    	return new Mesh(vertices, indices, true, true);
    }

    
    public static Mesh createCubeMap() {
    	float e = 0.001f;
    	
    	Vertex[] vertices = new Vertex[] {
    		// Bottom
        	new Vertex(-1, -1, -1, 0.25f + e, 0.50f),
        	new Vertex(-1, -1,  1, 0.25f + e, 0.75f - e),
        	new Vertex( 1, -1, -1, 0.50f - e, 0.50f),
        	new Vertex( 1, -1,  1, 0.50f - e, 0.75f - e),
    		// Top
        	new Vertex(-1,  1, -1, 0.25f + e, 0.25f),
        	new Vertex(-1,  1,  1, 0.25f + e, 0.00f + e),
        	new Vertex( 1,  1, -1, 0.50f - e, 0.25f),
        	new Vertex( 1,  1,  1, 0.50f - e, 0.00f + e),
    		// Right
        	new Vertex( 1, -1, -1, 0.50f, 0.50f - e),
        	new Vertex( 1, -1,  1, 0.75f, 0.50f - e),
        	new Vertex( 1,  1, -1, 0.50f, 0.25f + e),
        	new Vertex( 1,  1,  1, 0.75f, 0.25f + e),
    		// Left
        	new Vertex(-1, -1, -1, 0.25f,     0.50f - e),
        	new Vertex(-1, -1,  1, 0.00f + e, 0.50f - e),
        	new Vertex(-1,  1, -1, 0.25f,     0.25f + e),
        	new Vertex(-1,  1,  1, 0.00f + e, 0.25f + e),
        	// Front
        	new Vertex(-1,  1, -1, 0.25f, 0.50f - e),
        	new Vertex( 1,  1, -1, 0.50f, 0.50f - e),
        	new Vertex(-1, -1, -1, 0.25f, 0.25f + e),
        	new Vertex( 1, -1, -1, 0.50f, 0.25f + e),
        	// Back
        	new Vertex(-1,  1,  1, 1.00f - e, 0.50f - e),
        	new Vertex( 1,  1,  1, 0.75f,     0.50f - e),
        	new Vertex(-1, -1,  1, 1.00f - e, 0.25f + e),
        	new Vertex( 1, -1,  1, 0.75f,     0.25f + e)
    	};
    	
    	int[] indices = new int[] {
    		// Bottom
    		0, 1, 3,
    		0, 3, 2,
    		// Top
    		4, 7, 5,
    		4, 6, 7,
    		// Right
    		8, 9, 11,
    		8, 11, 10,
    		// Left
    		12, 15, 13,
    		12, 14, 15,
    		// Front
    		16, 19, 17,
    		16, 18, 19,
    		// Back
    		20, 21, 23,
    		20, 23, 22,
    	};
    	
    	return new Mesh(vertices, indices, true, true);
    }
    	
    public static Mesh createDisplacementMesh(int width, int height, float gridSize) {
    	int size = (width + 1) * (height + 1);
    	Vertex[] vertices = new Vertex[size];
    	
    	int index = 0;
    	for (int y = 0; y < height + 1; y++) {
    		for (int x = 0; x < width + 1; x++) {
        		vertices[index++] = new Vertex(x * gridSize, 0, y * gridSize, 0, 0);
        		
        	}
    	}
    	
    	int numIndices = (width * height) * 6;
    	int[] indices = new int[numIndices];
    	
    	index = 0;
    	for (int y = 0; y < height; y++) {
    		for (int x = 0; x < width; x++) {
    			int v1 = y * (width + 1) + x;
    			int v2 = v1 + 1;
    			int v3 = v1 + (width + 1);
    			int v4 = v3 + 1;

    			indices[index++] = v1;
    			indices[index++] = v3;
    			indices[index++] = v2;

    			indices[index++] = v3;
    			indices[index++] = v4;
    			indices[index++] = v2;
        	}
    	}
    	
    	return new Mesh(vertices, indices, true, true);
    }
}
