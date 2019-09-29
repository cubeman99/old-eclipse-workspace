package com.base.game.jets;

import com.base.engine.common.GMath;
import com.base.engine.common.Vector3f;
import com.base.engine.core.Vertex;
import com.base.engine.entity.Model;
import com.base.engine.entity.SceneObject;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Mesh;
import com.base.engine.rendering.Primitives;
import com.base.engine.rendering.Texture;
import com.base.engine.rendering.resourceManagement.OBJModel;

public class Track extends SceneObject {
	public static final float WIDTH  = 4;
	public static final float HEIGHT = 4;
	public static final float LENGTH = 10;
	
	private static Mesh trackMeshStraight;
	private static Mesh trackMeshCurve;
	
	private float length;
	private Model model;
	
	
	
	public Track(boolean curved) {
		length = 10;
		float len = length / 2;
		

		if (trackMeshCurve == null) {
			Vector3f center = new Vector3f(-LENGTH / 2, 0, -LENGTH / 2);
			OBJModel torus = new OBJModel();
			int detail = 10;
			
			for (int i = 0; i <= detail; i++) {
				float angle = (i / (float) detail) * GMath.HALF_PI;
				float r1    = (LENGTH / 2) - (WIDTH / 2);
				float r2    = (LENGTH / 2) + (WIDTH / 2);

				torus.addTexCoord(i / (float) detail, 0);
				torus.addTexCoord(i / (float) detail, 1);
				torus.addTexCoord(i / (float) detail, 0);
				torus.addTexCoord(i / (float) detail, 1);
				
				torus.addVertex(center.x + GMath.cos(angle) * r1, -(HEIGHT / 2),
    							center.z + GMath.sin(angle) * r1);
				torus.addVertex(center.x + GMath.cos(angle) * r1, (HEIGHT / 2),
    							center.z + GMath.sin(angle) * r1);
				torus.addVertex(center.x + GMath.cos(angle) * r2, -(HEIGHT / 2),
								center.z + GMath.sin(angle) * r2);
        		torus.addVertex(center.x + GMath.cos(angle) * r2, (HEIGHT / 2),
        						center.z + GMath.sin(angle) * r2);
				
        		if (i < detail) {
            		int index1 = i * 4;
            		int index2 = (i + 1) * 4;
            		
    				torus.addFace(
    					index1,
    					index1 + 1,
    					index2,
    					
    					index1,
    					index1 + 1,
    					index2
    				);
    				torus.addFace(
    					index1 + 1,
    					index2 + 1,
    					index2,
    					
    					index1 + 1,
    					index2 + 1,
    					index2
    				);
    				
    				
    				torus.addFace(
    					index1 + 3,
    					index1 + 2,
    					index2 + 2,
    					
    					index1 + 3,
    					index1 + 2,
    					index2 + 2
    				);
    				torus.addFace(
    					index1 + 3,
    					index2 + 2,
    					index2 + 3,
    					
    					index1 + 3,
    					index2 + 2,
    					index2 + 3
    				);

    				
    				// Floor
    				torus.addFace(
    					index1 + 2,
    					index1,
    					index2,
    					
    					index1 + 2,
    					index1 + 1,
    					index2 + 1
    				);
    				torus.addFace(
    					index1 + 2,
    					index2,
    					index2 + 2,
    					
    					index1 + 2,
    					index2 + 1,
    					index2 + 2
    				);

    				
    				// Ceiling
    				torus.addFace(
    					index1 + 3,
    					index2 + 1,
    					index1 + 1,

    					index1 + 3,
    					index2 + 2,
    					index1 + 2
    				);
    				torus.addFace(
    					index1 + 3,
    					index2 + 3,
    					index2 + 1,

    					index1 + 3,
    					index2 + 3,
    					index2 + 2
    				);
        		}
			}
			
			torus.addDummyMaterial();
			torus.calcNormals();
			trackMeshCurve = torus.createMeshes()[0];
		}
		
		if (trackMeshStraight == null) {
			float w = WIDTH / 2;
			float h = HEIGHT / 2;
			
			OBJModel obj = new OBJModel();
			
			obj.addVertex(-w, -h, -LENGTH / 2);
			obj.addVertex( w, -h, -LENGTH / 2);
			obj.addVertex( w,  h, -LENGTH / 2);
			obj.addVertex(-w,  h, -LENGTH / 2);
			obj.addVertex(-w, -h, LENGTH / 2);
			obj.addVertex( w, -h, LENGTH / 2);
			obj.addVertex( w,  h, LENGTH / 2);
			obj.addVertex(-w,  h, LENGTH / 2);
			
			obj.addTexCoord(0, 0);
			obj.addTexCoord(1, 0);
			obj.addTexCoord(1, 1);
			obj.addTexCoord(0, 1);
			
			obj.addFace(0, 5, 1, 1, 3, 2); // floor
			obj.addFace(0, 4, 5, 1, 0, 3);
			obj.addFace(2, 7, 3, 1, 3, 2); // ceiling
			obj.addFace(2, 6, 7, 1, 0, 3);
			obj.addFace(0, 3, 7, 0, 3, 2); // left wall
			obj.addFace(0, 7, 4, 0, 2, 1);
			obj.addFace(1, 5, 6, 1, 0, 3); // right wall
			obj.addFace(1, 6, 2, 1, 3, 2);

			obj.addDummyMaterial();
			obj.calcNormals();
			trackMeshStraight = obj.createMeshes()[0];
		}
		
		Material mat = new Material().setTexture(new Texture("bricks.jpg")).setNormalMap(new Texture("bricks_normal.jpg"));
		model = new Model(curved ? trackMeshCurve : trackMeshStraight, mat);
		
//		Mesh[] meshes = new Mesh[] {
//    		Primitives.createZYPlane(-len, len, -1, 1, -1, false),
//    		Primitives.createZYPlane(-len, len, -1, 1, 1, true),
//    		Primitives.createXZPlane(-1, 1, -len, len, -1, false),
//    		Primitives.createXZPlane(-1, 1, -len, len, -21, true),
//		};
//		Material[] mats = new Material[] {mat, mat, mat, mat};
//		Model model = new Model(meshes, mats, 4);
		
		addAttatchment(model);
		
	}
}
