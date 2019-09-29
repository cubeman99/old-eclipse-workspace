package com.base.game.rubiksCube;

import com.base.engine.common.Quaternion;
import com.base.engine.common.Vector3f;
import com.base.engine.entity.Model;
import com.base.engine.entity.SceneObject;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Mesh;
import com.base.engine.rendering.Primitives;

public class Cubie extends SceneObject {
	public int[] originalPosition;
	public int[] positions;
	public int[] faces;
	public Quaternion resultRotation;
	public Vector3f resultPosition;
	
	

	// ================== CONSTRUCTORS ================== //

	public Cubie(RubiksCube cube, int x, int y, int z) {
		positions        = new int[] {x * 2, y * 2, z * 2};
		originalPosition = new int[] {x * 2, y * 2, z * 2};
		faces            = new int[6];
		resultRotation   = new Quaternion();
		resultPosition   = new Vector3f();
		
		for (int i = 0; i < faces.length; i++) {
			int axis = i / 2;
			int layer = (i % 2 == 0 ? 1 : cube.getSize(axis));
			if (positions[axis] / 2 == layer)
				faces[i] = i;
			else
				faces[i] = -1;
		}
		
		float r = 0.5f;
		float w = 7;
		float tx = 1 / 126.0f;
		float ty = 1 / 18.0f;
		float texX1a = (x == 1  ? 4 : 6) / w + tx;
		float texX1b = (x == 1  ? 5 : 7) / w - tx;
		float texY1a = (y == 1  ? 2 : 6) / w + tx;
		float texY1b = (y == 1  ? 3 : 7) / w - tx;
		float texZ1a = (z == 1 ? 0 : 6)  / w + tx;
		float texZ1b = (z == 1 ? 1 : 7)  / w - tx;
		
		float texX2a = (x == cube.getSize(0) ? 5 : 6) / w + tx;
		float texX2b = (x == cube.getSize(0) ? 6 : 7) / w - tx;
		float texY2a = (y == cube.getSize(1) ? 3 : 6) / w + tx;
		float texY2b = (y == cube.getSize(1) ? 4 : 7) / w - tx;
		float texZ2a = (z == cube.getSize(2) ? 1 : 6) / w + tx;
		float texZ2b = (z == cube.getSize(2) ? 2 : 7) / w - tx;
		
		Mesh[] meshes = new Mesh[] {
			Primitives.createXYPlane(-r, r, -r, r, -r, texZ1b, texZ1a, ty, 1 - ty, false),
			Primitives.createXYPlane(-r, r, -r, r,  r, texZ2a, texZ2b, ty, 1 - ty, true),
			Primitives.createZYPlane(-r, r, -r, r, -r, texX1a, texX1b, ty, 1 - ty, true),
			Primitives.createZYPlane(-r, r, -r, r,  r, texX2a, texX2b, 1 - ty, ty, false),
			Primitives.createXZPlane(-r, r, -r, r, -r, texY1a, texY1b, ty, 1 - ty, true),
			Primitives.createXZPlane(-r, r, -r, r,  r, texY2b, texY2a, ty, 1 - ty, false),
		};
		Material[] mats = new Material[6];
		for (int i = 0; i < mats.length; i++)
			mats[i] = cube.getMaterial();
		Model model = new Model(meshes, mats, 6);
		addChild(model);
	}
	
	

	// =================== ACCESSORS =================== //

	public boolean isAtPosition(int x, int y, int z) {
		return (getX() == x && getY() == y && getZ() == z);
	}
	
	public int getPosition(int axis) {
		return positions[axis] / 2;
	}
	
	public int getX() {
		return positions[0] / 2;
	}
	
	public int getY() {
		return positions[1] / 2;
	}
	
	public int getZ() {
		return positions[2] / 2;
	}
	
	public int getFace(int... dir) {
		return getFace(faces, dir);
	}
	
	public static int getFace(int[] faces, int... dir) {
		for (int i = 0; i < dir.length; i++) {
			if (dir[i] != 0)
				return faces[((dir[i] + 1) / 2) + (i * 2)];
		}
		return -1;
	}
	
	

	// ==================== MUTATORS ==================== //

	public void applyResultTransform() {
		getTransform().setPosition(resultPosition);
		getTransform().setRotation(resultRotation);
	}
	
	public void setResultPosition(Vector3f resultPosition) {
		this.resultPosition = resultPosition;
	}
	
	public void setResultRotation(Quaternion resultRotation) {
		this.resultRotation = resultRotation;
	}
	
	public void setFace(int value, int... dir) {
		for (int i = 0; i < dir.length; i++) {
			if (dir[i] != 0)
				faces[((dir[i] + 1) / 2) + (i * 2)] = value;
		}
	}
	
	public void set(Cubie copy) {
		for (int i = 0; i < 3; i++) {
			positions[i] = copy.positions[i];
			originalPosition[i] = copy.originalPosition[i];
		}
		for (int i = 0; i < 6; i++)
			faces[i] = copy.faces[i];
		
		getTransform().set(copy.getTransform());
	}
}
