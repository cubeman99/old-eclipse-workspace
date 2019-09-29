package com.base.game.rubiksCube;

import java.util.ArrayList;
import com.base.engine.common.GMath;
import com.base.engine.common.Quaternion;
import com.base.engine.common.Vector3f;
import com.base.engine.core.Keyboard;
import com.base.engine.core.Util;
import com.base.engine.entity.MeshRenderer;
import com.base.engine.entity.Model;
import com.base.engine.entity.SceneObject;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Mesh;
import com.base.engine.rendering.Primitives;
import com.base.engine.rendering.Texture;

public class RubiksCube extends SceneObject {
	public static final boolean[] FLIPPED = new boolean[] {true, true, false};
	public static final int CENTER = 1;
	public static final int EDGE   = 2;
	public static final int CORNER = 3;

	public static final int LEFT     = 1;
	public static final int MIDDLE_X = 2;
	public static final int RIGHT    = 3;
	public static final int DOWN     = 1;
	public static final int MIDDLE_Y = 2;
	public static final int UP       = 3;
	public static final int FRONT    = 1;
	public static final int MIDDLE_Z = 2;
	public static final int BACK     = 3;
	public static final int MIDDLE   = 2;
	
	public static final int X_AXIS = 0;
	public static final int Y_AXIS = 1;
	public static final int Z_AXIS = 2;
	private static final Vector3f[] AXIS_VECTORS = new Vector3f[] {Vector3f.X_AXIS, Vector3f.Y_AXIS, Vector3f.Z_AXIS};
	
	private Mesh cubieMesh;
	private Material cubieMaterial;
	private ArrayList<Cubie> cubies;
//	private int turnAxis;
//	private int turnLayer;
//	private boolean turnClockwise;
	private boolean turning;
	private int algorithmIndex;
	private Move move;
	private Algorithm algorithm;
	private float turnTimer;
	private float turnSpeed;
	
	private int width;
	private int height;
	private int length;
	private int[] size;
	
	
	// ================== CONSTRUCTORS ================== //

	public RubiksCube(int size) {
		this(size, size, size);
	}
	
	public RubiksCube(RubiksCube copy) {
		init(copy.width, copy.height, copy.length);
		init(copy);
	}
	
	public RubiksCube(int width, int height, int length) {
		init(width, height, length);
	}
	
	public void init(RubiksCube copy) {
		for (int i = 0; i < cubies.size(); i++) {
			cubies.get(i).set(copy.cubies.get(i));
		}
	}
	
	public void init(int width, int height, int length) {
		this.width  = width;
		this.height = height;
		this.length = length;
		this.size   = new int[] {width, height, length};
		
		turnSpeed = 3.0f; // Turns per second
		
		cubieMesh = Primitives.createBox(0, 0, 0, 1, 1, 1);
		
		cubieMaterial = new Material();
//    	cubieMaterial.setTexture(new Texture("rubiks_cube.png"));
//    	cubieMaterial.setNormalMap(new Texture("rubiks_cube_normal.png"));
		cubieMaterial.setTexture(new Texture("rubiks_cube_stickerless.png"));
		cubieMaterial.setNormalMap(new Texture("rubiks_cube_stickerless_normal.png"));
		cubieMaterial.setSpecular(2, 16);
		cubieMaterial.setSpecular(3, 128);
		
		cubies = new ArrayList<Cubie>();
		
		for (int x = 0; x < size[0]; x++) {
			for (int y = 0; y < size[1]; y++) {
				for (int z = 0; z < size[2]; z++) {
//					if (x >= 0 && y >= 0 && z >= 0 && x < size[0] && y < size[1] && z < size[2])
					if (x == 0 || y == 0 || z == 0 || x == size[0] - 1 || y == size[1] - 1 || z == size[2] - 1)
						addCubie(x + 1, y + 1, z + 1);
				}
			}
		}
		
		turning        = false;
		move           = null;
		algorithm      = null;
		algorithmIndex = 0;
		turnTimer      = 0;
	}

	
	
	// =================== ACCESSORS =================== //

	public boolean isSolved() {
		for (int axis = 0; axis < 3; axis++) {
			for (int side = 0; side < 2; side++) {
				int[] faceDir = new int[4];
				faceDir[axis] = (side == 0 ? -1 : 1);
				int face = -1;
				
				for (int coord1 = 0; coord1 < 3; coord1++) {
					for (int coord2 = 0; coord2 < 3; coord2++) {
        				int[] pos           = new int[3];
        				pos[axis]           = (side * 2) + 1;
        				pos[(axis + 1) % 3] = coord1 + 1;
        				pos[(axis + 2) % 3] = coord2 + 1;
        				
        				int faceTest = getCubie(pos).getFace(faceDir);
        				if (face < 0)
        					face = faceTest;
        				else if (face != faceTest)
        					return false;
					}
				}
			}
		}
		
		return true;
	}
	
	public Cubie getCubie(int... pos) {
		for (Cubie cubie : cubies) {
			if (cubie.positions[X_AXIS] / 2 == pos[X_AXIS] &&
				cubie.positions[Y_AXIS] / 2 == pos[Y_AXIS] &&
				cubie.positions[Z_AXIS] / 2 == pos[Z_AXIS])
			{
				return cubie;
			}
		}
		return null;
	}
	public Cubie getOriginalCubie(int x, int y, int z) {
		for (Cubie cubie : cubies) {
			if (cubie.originalPosition[X_AXIS] / 2 == x &&
				cubie.originalPosition[Y_AXIS] / 2 == y &&
				cubie.originalPosition[Z_AXIS] / 2 == z)
			{
				return cubie;
			}
		}
		return null;
	}
	
	public Move getMove() {
		return move;
	}
	
	public Material getMaterial() {
		return cubieMaterial;
	}
	
	public int getSize(int axis) {
		return size[axis];
	}
	
	public boolean isTurning() {
		return turning;
	}
	
	

	// ==================== MUTATORS ==================== //

	public void apply(Algorithm algorithm) {
		apply(algorithm, false);
	}
	
	public void apply(Algorithm algorithm, boolean instant) {
		if (!turning && algorithm.getLength() > 0) {
			if (instant) {
				for (int i = 0; i < algorithm.getLength(); i++)
					apply(algorithm.getMove(i), true);
			}
			else {
        		this.algorithm = algorithm;
        		this.algorithmIndex = 0;
        		apply(algorithm.getMove(0), false);
			}
		}
	}
	
	public void apply(Move move) {
		apply(move.getAxis(), move.getLayer(), move.isClockwise(), false);
	}
	
	public void apply(Move move, boolean instant) {
		apply(move.getAxis(), move.getLayer(), move.isClockwise(), instant);
	}

	public void apply(int axis, int layer, boolean clockwise) {
		apply(axis, layer, clockwise, false);
	}
	
	public void apply(int axis, int layer, boolean clockwise, boolean instant) {
		if (turning)
			return;
		
		float angle = GMath.HALF_PI;
		if (FLIPPED[axis])
			angle *= -1;
		if (clockwise)
			angle *= -1;

		// Check for basic, mis-alignment bandaging (between even and odd layers).
		if (layer > 0) {
    		for (Cubie cubie : cubies) {
    			if (cubie.positions[axis] % 2 == 1)
    				return;
    		}
		}
		
		boolean flipped = (clockwise ? FLIPPED[axis] : !FLIPPED[axis]);
		int index1 = (axis + (flipped ? 2 : 1)) % 3;
		int index2 = (axis + (flipped ? 1 : 2)) % 3;
		
		int[][] dirs = new int[4][];
		dirs[0] = new int[] {0, 0, 0};
		dirs[0][index1] = 1;
		
		for (int i = 1; i < 4; i++) {
			dirs[i] = new int[] {0, 0, 0};
			dirs[i][axis]   = dirs[i - 1][axis];
			dirs[i][index1] = dirs[i - 1][index2] * -1;// * (flipped ? -1 : 1);
			dirs[i][index2] = dirs[i - 1][index1];// * (flipped ? 1 : -1);
		}
		
		for (Cubie cubie : cubies) {
			if (cubie.positions[axis] / 2 == layer || layer == 0) {
				if (instant) {
            		cubie.getTransform().setPosition(cubie.getTransform().getPosition().rotate(angle, AXIS_VECTORS[axis]));
            		cubie.getTransform().rotate(AXIS_VECTORS[axis], angle);
				}
				else {
					Quaternion q = new Quaternion(cubie.getTransform().getRotation());
					cubie.setResultRotation(q.rotate(AXIS_VECTORS[axis], angle));
					cubie.setResultPosition(cubie.getTransform().getPosition().rotate(angle, AXIS_VECTORS[axis]));
				}
				
        		int temp = cubie.positions[index1];
        		cubie.positions[index1] = cubie.positions[index2] - ((size[index2] - size[index1]));
        		cubie.positions[index2] = (size[index1] * 2) + 2 - temp - ((size[index1] - size[index2]));
        		
        		// Permutate face indices.
    			int[] tempFaces = new int[6];
    			for (int i = 0; i < 6; i++)
    				tempFaces[i] = cubie.faces[i];
    			for (int i = 0; i < 4; i++) {
        			int[] dir1 = dirs[i];
        			int[] dir2 = dirs[(i + 1) % 4];
        			cubie.setFace(Cubie.getFace(tempFaces, dir2), dir1);
    			}
			}
		}
		
		if (!instant) {
    		move      = new Move(axis, layer, clockwise);;
    		turnTimer = 0;
    		turning   = true;
		}
	}
	
	private void addCubie(int layerX, int layerY, int layerZ) {
		Cubie c = new Cubie(this, layerX, layerY, layerZ);
		Vector3f pos = new Vector3f(layerX, layerY, layerZ);
		pos.x -= 0.5f + (size[0] * 0.5f);
		pos.y -= 0.5f + (size[1] * 0.5f);
		pos.z -= 0.5f + (size[2] * 0.5f);
		c.getTransform().setPosition(pos);
		cubies.add(c);
		addChild(c);
	}
	
	

	// ================ IMPLEMENTATIONS ================ //

	@Override
	public void update(float delta) {
		super.update(delta);
		
		
		if (turning) {
			turnTimer += delta * turnSpeed;
			
			if (turnTimer > 1) {
				turning = false;
				turnTimer = 0;
			}
			
			float angle = GMath.HALF_PI * (delta * turnSpeed);
			if (!FLIPPED[move.getAxis()])
				angle *= -1;
			if (!move.isClockwise())
				angle *= -1;
			for (int i = 0; i < cubies.size(); i++) {
				Cubie cubie = cubies.get(i);
				if (cubie.positions[move.getAxis()] / 2 == move.getLayer() || move.getLayer() == 0) {
	        		cubie.getTransform().rotate(AXIS_VECTORS[move.getAxis()], angle);
	        		cubie.getTransform().setPosition(cubie.getTransform().getPosition().rotate(angle, AXIS_VECTORS[move.getAxis()]));
	        		
	        		if (!turning) {
	        			cubie.applyResultTransform();
	        		}
				}
			}
			
			if (!turning && algorithm != null) {
				algorithmIndex++;
				if (algorithmIndex < algorithm.getLength()) {
					apply(algorithm.getMove(algorithmIndex));
				}
				else
					algorithm = null;
			}
		}
	}
}
