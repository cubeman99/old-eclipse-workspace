package com.base.game;

import com.base.engine.core.Vector3f;
import com.base.engine.rendering.Mesh;
import com.base.engine.rendering.Vertex;


public class TestModels {
	
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
        	new Vertex( 1,  1,  1, 1, 0),

        	new Vertex(1, 1, 1, 0, 0) // dummy vertex
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

    public static Mesh createTestMesh1() {
    	Vertex[] vertices = new Vertex[] {
        	new Vertex(-1,  1, -1, 0, 0),
        	new Vertex( 1,  1, -1, 0, 1),
        	new Vertex(-1,  1,  1, 1, 0),
        	new Vertex( 1,  1,  1, 1, 1),
    	};
    	
    	
    	
    	int[] indices = new int[] {
    		0, 1, 3,
    		0, 3, 2
    	};
    	
    	vertices[0].setNormal(new Vector3f(1, -1, 0));
    	vertices[1].setNormal(new Vector3f(1, -1, 0));
    	vertices[2].setNormal(new Vector3f(1, -1, 0));
    	vertices[3].setNormal(new Vector3f(1, -1, 0));

    	Mesh m = new Mesh(vertices, indices);
    	return m;
    }
    
    public static Mesh createTestMesh2() {
    	Vertex[] vertices = new Vertex[] {
    		new Vertex(-1, -1, -1, 0, 0),
    		new Vertex(-1,  1, -1, 1, 0),
    		new Vertex( 1, -1, -1, 0, 1),
    		new Vertex( 1,  1, -1, 1, 1),
    		new Vertex(-1, -1,  1, 0, 0),
    		new Vertex(-1,  1,  1, 1, 0),
    		new Vertex( 1, -1,  1, 0, 1),
    		new Vertex( 1,  1,  1, 1, 1)
    	};

    	int[] indices = new int[] {
    		0, 2, 1,
    		1, 2, 3,
    		
    		4, 5, 6,
    		5, 7, 6,
    		
    		0, 4, 2,
    		2, 4, 6,
    		
    		1, 3, 7,
//    		5, 1, 7,
    	};
    	
    	return new Mesh(vertices, indices, true, true);
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
        	new Vertex( 1,  1,  1, 1, 1),

        	new Vertex(1, 1, 1, 0, 0) // dummy vertex
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
    	/*
    	vertices[0].setNormal(new Vector3f(0, 0, 1));
    	vertices[1].setNormal(new Vector3f(0, 0, 1));
    	vertices[2].setNormal(new Vector3f(0, 0, 1));
    	vertices[3].setNormal(new Vector3f(0, 0, 1));

    	vertices[4].setNormal(new Vector3f(0, 0, -1));
    	vertices[5].setNormal(new Vector3f(0, 0, -1));
    	vertices[6].setNormal(new Vector3f(0, 0, -1));
    	vertices[7].setNormal(new Vector3f(0, 0, -1));

    	vertices[8].setNormal(new Vector3f(-1, 0, 0));
    	vertices[9].setNormal(new Vector3f(-1, 0, 0));
    	vertices[10].setNormal(new Vector3f(-1, 0, 0));
    	vertices[11].setNormal(new Vector3f(-1, 0, 0));

    	vertices[12].setNormal(new Vector3f(1, 0, 0));
    	vertices[13].setNormal(new Vector3f(1, 0, 0));
    	vertices[14].setNormal(new Vector3f(1, 0, 0));
    	vertices[15].setNormal(new Vector3f(1, 0, 0));

    	vertices[16].setNormal(new Vector3f(0, 1, 0));
    	vertices[17].setNormal(new Vector3f(0, 1, 0));
    	vertices[18].setNormal(new Vector3f(0, 1, 0));
    	vertices[19].setNormal(new Vector3f(0, 1, 0));

    	vertices[20].setNormal(new Vector3f(0, 1, 0));
    	vertices[21].setNormal(new Vector3f(0, 1, 0));
    	vertices[22].setNormal(new Vector3f(0, 1, 0));
    	vertices[23].setNormal(new Vector3f(0, 1, 0));
    	
    	Mesh m = new Mesh(vertices, indices);
    	*/
    	
    	return new Mesh(vertices, indices, true, true);
    }
}
